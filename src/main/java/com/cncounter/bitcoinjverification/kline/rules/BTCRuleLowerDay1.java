package com.cncounter.bitcoinjverification.kline.rules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// 1天最低价-规则
@Component("BTCRuleLowerDay1")
@Slf4j
public class BTCRuleLowerDay1 extends AbstractBTCRule implements RuleApi {

    protected BigDecimal getThreshold() {
        return BigDecimal.valueOf(50);
    }

    protected boolean compare(BigDecimal hasNoticePrice, BigDecimal price) {
        BigDecimal threshold = getThreshold();
        // 已有价格 - 新低价 > 告警变化阈值
        boolean compareResult = !(hasNoticePrice.subtract(price).compareTo(threshold) <= 0);
        //
        return compareResult;
    }
}
