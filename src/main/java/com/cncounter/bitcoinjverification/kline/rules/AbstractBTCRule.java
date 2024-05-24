package com.cncounter.bitcoinjverification.kline.rules;

import com.alibaba.fastjson.JSONObject;
import com.cncounter.bitcoinjverification.model.TickerPrice;
import com.cncounter.bitcoinjverification.tools.HttpUtilsOK;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public abstract class AbstractBTCRule implements RuleApi {
    public static final String BTC_USDT = "BTC" + "USDT";

    // 缓存记录; key=日期; value=交易价;
    protected Map<String, BigDecimal> noticedPriceMap = new HashMap<>();

    // 对比
    protected abstract boolean compare(BigDecimal hasNoticePrice, BigDecimal price);


    @Override
    public boolean accept(TickerPrice tickerPrice) {
        // 只接受BTCUSDT交易对;
        return BTC_USDT.equalsIgnoreCase(tickerPrice.getSymbol());
    }

    @Override
    public String execute(TickerPrice tickerPrice) {
        if (!accept(tickerPrice)) {
            return "";
        }
        boolean needNoticeFlag = needNotice(tickerPrice);
        if (needNoticeFlag) {
            // 保存通知价格;
            setNoticedPrice(tickerPrice);
            // 获取通知消息
            String msg = getNoticeMessage(tickerPrice);
            // 执行通知
            doNotice(msg);
            return msg;
        }
        return "";
    }


    protected boolean needNotice(TickerPrice tickerPrice) {
        // 已通知价格
        BigDecimal noticedPrice = getNoticedPrice();
        if (Objects.isNull(noticedPrice)) {
            return true; // 当日没有通知; 需要通知;
        }
        BigDecimal price = tickerPrice.getPrice();
        // 比较价格;
        boolean needNoticeFlag = (compare(noticedPrice, price));

        if (!needNoticeFlag) {
            log.info("[规则][{}]交易对通知价格变化不明显; symbol={}; hasNoticePrice={}; price={}",
                    getClass().getSimpleName(),
                    tickerPrice.getSymbol(), noticedPrice, price);
        }

        return needNoticeFlag;
    }

    // 获取已通知的数据
    protected BigDecimal getNoticedPrice() {
        String dateStrKey = curDateStrKey();
        // 1.1 顺便先清理过期的数据: 不是当前的都删除;
        Set<String> keySet = noticedPriceMap.keySet();
        for (String outerKey : keySet) {
            if (!dateStrKey.equals(outerKey)) {
                noticedPriceMap.remove(outerKey);
            }
        }
        // 1.2 获取当前的数据
        BigDecimal noticedPrice = noticedPriceMap.get(dateStrKey);
        return noticedPrice;
    }

    protected void setNoticedPrice(TickerPrice tickerPrice) {
        String dateStrKey = curDateStrKey();
        BigDecimal price = tickerPrice.getPrice();
        //
        noticedPriceMap.put(dateStrKey, price);
    }

    // 获取通知消息
    protected String getNoticeMessage(TickerPrice tickerPrice) {
        //
        StringBuilder builder = new StringBuilder();
        builder.append("【通知】K线价格告警").append("\n");
        builder.append("by: ").append(getClass().getSimpleName()).append("\n");
        builder.append("告警时间:").append(curDateTimeStrKey()).append("\n");
        builder.append("交易对:").append(tickerPrice.getSymbol()).append("\n");
        builder.append("已通知价格:").append(getNoticedPrice()).append("\n");
        builder.append("当前价格:").append(tickerPrice.getPrice().toPlainString()).append("\n");
        String message = builder.toString();
        return message;
    }

    // 通知到哪里;
    protected void doNotice(String message) {
        //
        log.info("准备发送通知消息:\n {}", message);
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
        log.info("钉钉通知返回结果: {}", respStr);
    }


    protected String curDateStrKey() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(new Date());
    }

    protected String curDateTimeStrKey() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(new Date());
    }
}
