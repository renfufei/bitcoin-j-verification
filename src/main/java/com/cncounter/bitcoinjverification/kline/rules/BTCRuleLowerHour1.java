package com.cncounter.bitcoinjverification.kline.rules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// 1H最低价-规则
@Component
@Slf4j
public class BTCRuleLowerHour1 extends AbstractBTCRule implements RuleApi {

    protected BigDecimal getThreshold() {
        return BigDecimal.valueOf(50);
    }

    @Override
    protected String curDateStrKey() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH", Locale.CHINA);
        return format.format(new Date());
    }

    protected boolean compare(BigDecimal hasNoticePrice, BigDecimal price) {
        BigDecimal threshold = getThreshold();
        // 已有价格 - 新低价 > 告警变化阈值
        boolean compareResult = !(hasNoticePrice.subtract(price).compareTo(threshold) <= 0);
        //
        return compareResult;
    }
}
