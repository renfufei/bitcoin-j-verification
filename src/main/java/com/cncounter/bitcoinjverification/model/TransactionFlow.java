package com.cncounter.bitcoinjverification.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

// 交易流水
@Data
@SuperBuilder
public class TransactionFlow {
    // 交易Hash
    private String txHash;
    // 来源地址: 付款方
    private String fromAddress;
    // 目标地址: 收款方
    private String toAddress;
    // 交易金额
    private BigDecimal amount;
    // 文本金额
    private String amountDesc;
    // 文本金额
    private Long amountCong;

}
