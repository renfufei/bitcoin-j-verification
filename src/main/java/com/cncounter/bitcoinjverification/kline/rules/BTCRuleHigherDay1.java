package com.cncounter.bitcoinjverification.kline.rules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

// 1天最高-规则
@Component
@Slf4j
public class BTCRuleHigherDay1 extends AbstractBTCRule implements RuleApi {

    protected BigDecimal getThreshold() {
        return BigDecimal.valueOf(0.005D);
    }

    protected boolean compare(BigDecimal hasNoticePrice, BigDecimal price) {
        BigDecimal threshold = getThreshold();
        // 1%
        // 新高价 - 已有价格 / 已有价格 > 告警变化阈值
        boolean compareResult = (hasNoticePrice.compareTo(price) < 0)
                && price.subtract(hasNoticePrice).divide(hasNoticePrice, RoundingMode.HALF_EVEN).compareTo(threshold) >= 0;
        //
        return compareResult;
    }
}
