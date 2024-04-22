package com.cncounter.kline;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cncounter.bitcoinjverification.tools.HttpUtilsOK;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

// 测试币安的行情接口-与通知;
public class TestBinanceTicker {

    public static void main(String[] args) throws Exception {
        // 循环
        while (Thread.currentThread().isAlive()) {
            // 判断退出条件: TO_DO
            try {
                // 监控币安行情;
                monitorBinanceTicker();
            } catch (Throwable ignore) {
                // 是否打印错误信息;
                ignore.printStackTrace();
            }
            // 暂停 X 秒
            TimeUnit.SECONDS.sleep(15L);
        }
    }

    // 监控币安行情;
    public static void monitorBinanceTicker() {
        // 行情URL
        final String BINANCE_TICKER_URL = "https://api.binance.com/api/v3/ticker/price";
        // 获取交易
        String resp = HttpUtilsOK.get(BINANCE_TICKER_URL);
        // 转换为JSON数组
        JSONArray respArray = JSONArray.parseArray(resp);
        // 转换为Java对象
        List<BinanceTickerPrice> priceList = respArray.toJavaList(BinanceTickerPrice.class);
        // 遍历
        for (BinanceTickerPrice tickerPrice : priceList) {
            // 检查交易对价格并通知;
            try {
                // 检查交易对价格并通知;
                toCheckAndNotice(tickerPrice);
            } catch (Exception ignore) {
                // 吃掉异常; 避免干扰到其他交易对;
                ignore.printStackTrace();
            }
        }
        // 日志; TO_DO
    }

    private static void toCheckAndNotice(BinanceTickerPrice tickerPrice) {
        // 后期可以使用 aviator, 进行条件表达式判断(解析配置的字符串规则);
        // 例如: symbol=="BTCUSDT" && price < 50000

        // 规则1
        {
            // 最低价规则;
            BigDecimal lowPrice = new BigDecimal(66000);
            if (
                    "BTCUSDT".equals(tickerPrice.symbol)
                            && tickerPrice.price.compareTo(lowPrice) < 0
            ) {
                // 此最低价, 每天只通知一次;
                noticeLowPriceOncePerDay(tickerPrice, lowPrice);
            }
        }
    }


    // 最低价缓存记录; key=日期; value={k=交易对; v=最低价;}
    private static Map<String, Map<String, BigDecimal>> lowPriceNoticeMap = new HashMap<>();

    private static void noticeLowPriceOncePerDay(BinanceTickerPrice tickerPrice, BigDecimal lowPrice) {
        String symbol = tickerPrice.getSymbol();
        BigDecimal price = tickerPrice.getPrice();
        //
        String dateStrKey = curDateStrKey();
        // 顺便先清理过期的数据: 不是当日的都删除;
        Set<String> keySet = lowPriceNoticeMap.keySet();
        for (String outerKey : keySet) {
            if (!dateStrKey.equals(outerKey)) {
                lowPriceNoticeMap.remove(outerKey);
            }
        }
        boolean needNotice = true;
        // 获取当日的数据
        Map<String, BigDecimal> curDateMap = lowPriceNoticeMap.get(dateStrKey);
        if (Objects.isNull(curDateMap)) {
            curDateMap = new HashMap<>();
            lowPriceNoticeMap.put(dateStrKey, curDateMap);
        }
        // 获取当前交易对的已通知价格;
        BigDecimal hasNoticePrice = curDateMap.get(symbol);
        if (Objects.nonNull(hasNoticePrice)) {
            // 已有价格 <= 低价阈值
            if (hasNoticePrice.compareTo(lowPrice) <= 0) {
                // log -
                return; // 不再通知
            }
        }
        // 设置 低价阈值 到Map
        curDateMap.put(symbol, lowPrice);
        // 执行通知; 不管成功失败
        noticeDingDing(symbol, price, lowPrice);
    }

    private static void noticeDingDing(String symbol, BigDecimal price, BigDecimal lowPrice) {
        StringBuilder builder = new StringBuilder();
        builder.append("【通知】K线价格告警").append("\n");
        builder.append("告警日期:").append(curDateStrKey()).append("\n");
        builder.append("交易对:").append(symbol).append("\n");
        builder.append("告警价格:").append(lowPrice.toPlainString()).append("\n");
        builder.append("当前价格:").append(price.toPlainString()).append("\n");
        String message = builder.toString();
        //
        System.out.println(message);
        // 钉钉通知;
        final String dingTokenUrl = "https://oapi.dingtalk.com/robot/send?access_token=b5e2d6bfd13de1e82a571cb768c389529c0b50507b10c01c1c14fbe66b2e9169";
        //
        JSONObject data = new JSONObject();
        JSONObject content = new JSONObject();
        JSONObject at = new JSONObject();
        content.put("content", message);
        at.put("isAtAll", false);
        data.put("msgtype", "text");
        data.put("text", content);
        data.put("at", at);

        //
        String respStr = HttpUtilsOK.post(dingTokenUrl, data);
        // 暂时不处理返回结果;

        //
        System.out.println("===========");
        System.out.println(respStr);
        System.out.println("===========");
    }


    private static String curDateStrKey() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(new Date());
    }

    public static class BinanceTickerPrice {
        private String symbol;
        private BigDecimal price;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}
