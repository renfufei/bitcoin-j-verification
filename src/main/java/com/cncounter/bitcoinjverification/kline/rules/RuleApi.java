package com.cncounter.bitcoinjverification.kline.rules;

import com.cncounter.bitcoinjverification.model.TickerPrice;

// 规则
public interface RuleApi {

    // 是否处理
    public boolean accept(TickerPrice tickerPrice);

    // 执行规则
    public String execute(TickerPrice tickerPrice);
}
