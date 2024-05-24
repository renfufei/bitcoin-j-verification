package com.cncounter.bitcoinjverification.kline.rules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

// 1天最低价-规则
@Component
@Slf4j
public class BTCRuleLowerDay1 extends AbstractBTCRule implements RuleApi {

    protected BigDecimal getThreshold() {
        return BigDecimal.valueOf(0.005D);
    }

    protected boolean compare(BigDecimal hasNoticePrice, BigDecimal price) {
        BigDecimal threshold = getThreshold();
        // 1%
        // 已有价格 > 新低价
        // && 差值/已有价格 > 告警变化阈值
        boolean compareResult = (hasNoticePrice.compareTo(price) > 0)
                && hasNoticePrice.subtract(price).divide(hasNoticePrice, RoundingMode.HALF_EVEN).compareTo(threshold) >= 0;
        //
        return compareResult;
    }
}
