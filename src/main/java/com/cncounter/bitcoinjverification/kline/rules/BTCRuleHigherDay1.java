package com.cncounter.bitcoinjverification.kline.rules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// 1天最高-规则
@Component("BTCRuleHigherDay1")
@Slf4j
public class BTCRuleHigherDay1 extends AbstractBTCRule implements RuleApi {

    protected BigDecimal getThreshold() {
        return BigDecimal.valueOf(50);
    }

    protected boolean compare(BigDecimal hasNoticePrice, BigDecimal price) {
        BigDecimal threshold = getThreshold();
        // 新高价 - 已有价格 > 告警变化阈值
        boolean compareResult = !(price.subtract(hasNoticePrice).compareTo(threshold) <= 0);
        //
        return compareResult;
    }
}
