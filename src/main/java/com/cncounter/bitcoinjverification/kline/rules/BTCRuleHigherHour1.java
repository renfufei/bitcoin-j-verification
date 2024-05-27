package com.cncounter.bitcoinjverification.kline.rules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// 1H最高-规则
@Component
@Slf4j
public class BTCRuleHigherHour1 extends AbstractBTCRule implements RuleApi {

    protected BigDecimal getThreshold() {
        return BigDecimal.valueOf(120);
    }

    @Override
    protected String curDateStrKey() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH", Locale.CHINA);
        return format.format(new Date());
    }

    protected boolean compare(BigDecimal hasNoticePrice, BigDecimal price) {
        BigDecimal threshold = getThreshold();
        // 新高价 - 已有价格 > 告警变化阈值
        boolean compareResult = !(price.subtract(hasNoticePrice).compareTo(threshold) <= 0);
        //
        return compareResult;
    }
}
