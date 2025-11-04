package com.cncounter.bitcoinjverification.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cncounter.bitcoinjverification.model.TransactionFlow;
import com.cncounter.bitcoinjverification.utils.StringFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.*;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptPattern;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.cncounter.bitcoinjverification.utils.CommonDateUtils.dateToStr;

@Slf4j
public class DownloadListener extends DownloadProgressTracker {
    public static DownloadListener getInstance() {
        return new DownloadListener();
    }

    @Override
    public void onBlocksDownloaded(Peer peer, Block block, @Nullable FilteredBlock filteredBlock, int blocksLeft) {
        //
        Date time = block.getTime();
        //
        JSONObject blockInfo = briefBlockInfo(block);
        //
        log.info("[下载监听]有区块下载完成: 区块时间=[{}];  blocksLeft={}; blockInfo={}; ",
                dateToStr(time), blocksLeft, blockInfo);
        // 回调
        toProcessBlock(block);
        //
        super.onBlocksDownloaded(peer, block, filteredBlock, blocksLeft);
    }


    private void toProcessBlock(Block block) {
        NetworkParameters networkParams = block.getParams();
        // 获取交易
        List<Transaction> transactions = block.getTransactions();
        int transactionsSize = transactions.size();
        for (int i = 0; i < transactionsSize; i++) {
            Transaction tx = transactions.get(i);
            //
            processTx(tx, networkParams);
        }

        // 临时的阻塞
        try {
            TimeUnit.SECONDS.sleep(5L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void processTx(Transaction tx, NetworkParameters networkParams) {
        // 交易ID
        Sha256Hash txId = tx.getTxId();
        //  交易Hash?
        String txHash = txId.toString();
        // 手续费: 给矿工
        Coin fee = tx.getFee();
        // 输入: 付钱方
        List<TransactionInput> inputs = tx.getInputs();
        List<TransactionFlow> txInArray = parseTxInput(inputs, networkParams);
        log.info("[下载监听]解析到交易付款方信息:\n{}", StringFormatUtils.str(txInArray));

        // 输出: 收款方, + 找零付款方
        List<TransactionOutput> outputs = tx.getOutputs();
        List<TransactionFlow> txOutArray = parseTxOut(outputs, networkParams);
        log.info("[下载监听]解析到交易收款方信息:\n{}", StringFormatUtils.str(txOutArray));
    }

    private List<TransactionFlow> parseTxInput(List<TransactionInput> inputs, NetworkParameters networkParams) {
        List<TransactionFlow> resultArray = new ArrayList<>();
        for (TransactionInput input : inputs) {
            int index = input.getIndex();
            //
            TransactionOutPoint outpoint = input.getOutpoint();
            Sha256Hash outpointHash = outpoint.getHash();
            String parentTxHash = outpointHash.toString();
            //
            Coin value = Coin.ZERO;
            String fromAddress = "";
            TransactionOutput connectedOutput = input.getConnectedOutput();
            if (Objects.nonNull(connectedOutput)) {
                Script scriptPubKey = connectedOutput.getScriptPubKey();
                // 资金来源地址
                fromAddress = parsePublicAddress(scriptPubKey, networkParams);
                //
                value = connectedOutput.getValue();
            }

            long amountCong = value.longValue();
            // 文本金额
            String amountDesc = value.toPlainString();
            // 金额
            BigDecimal amount = new BigDecimal(amountDesc);
            //
            TransactionFlow flow = TransactionFlow.builder()
                    .toAddress(null)
                    .fromAddress(fromAddress)
                    .amount(amount)
                    .amountDesc(amountDesc)
                    .amountCong(amountCong)
                    .build();
            resultArray.add(flow);
        }
        return resultArray;
    }

    private List<TransactionFlow> parseTxOut(List<TransactionOutput> outputs, NetworkParameters networkParams) {
        List<TransactionFlow> resultArray = new ArrayList<>();
        // 分析输出
        for (TransactionOutput out : outputs) {
            // int outputIndex = out.getIndex();
            // 金额-聪
            long amountCong = out.getValue().longValue();
            // 文本金额
            String amountDesc = out.getValue().toPlainString();
            BigDecimal amount = new BigDecimal(amountDesc);
            //
            Script scriptPubKey = out.getScriptPubKey();
            // 转到的目标地址
            String toAddress = parsePublicAddress(scriptPubKey, networkParams);
            //
            TransactionFlow flow = TransactionFlow.builder()
                    .toAddress(toAddress)
                    .amount(amount)
                    .amountDesc(amountDesc)
                    .amountCong(amountCong)
                    .build();
            resultArray.add(flow);
        }

        return resultArray;
    }

    private String parsePublicAddress(Script pubKey, NetworkParameters params) {
        if (ScriptPattern.isP2PKH(pubKey))
            return LegacyAddress.fromPubKeyHash(params, ScriptPattern.extractHashFromP2PKH(pubKey)).toString();
        else if (ScriptPattern.isP2SH(pubKey))
            return LegacyAddress.fromScriptHash(params, ScriptPattern.extractHashFromP2SH(pubKey)).toString();
        else if (ScriptPattern.isP2PK(pubKey))
            return LegacyAddress.fromKey(params, ECKey.fromPublicOnly(ScriptPattern.extractKeyFromP2PK(pubKey))).toString();
        else if (ScriptPattern.isP2WH(pubKey))
            return SegwitAddress.fromHash(params, ScriptPattern.extractHashFromP2WH(pubKey)).toString();
        else if (ScriptPattern.isP2TR(pubKey))
            return SegwitAddress.fromProgram(params, 1, ScriptPattern.extractOutputKeyFromP2TR(pubKey)).toString();
        return null;
    }

    // 区块信息
    private JSONObject briefBlockInfo(Block block) {
        // 版本号
        long version = block.getVersion();
        // 难度信息
        long difficultyTarget = block.getDifficultyTarget();
        // 区块时间
        Date blockTime = block.getTime();
        // 区块hash
        Sha256Hash blockHash = block.getHash();
        // Sha256Hash prevBlockHash = block.getPrevBlockHash();
        List<Transaction> transactions = block.getTransactions();
        // 交易数量
        int transactionsSize = transactions.size();

        //
        JSONObject briefInfo = new JSONObject();
        //briefInfo.put("version", version);
        briefInfo.put("difficulty", difficultyTarget);
        briefInfo.put("blockTime", blockTime);
        briefInfo.put("blockHash", blockHash);
        briefInfo.put("transactionsSize", transactionsSize);

        return briefInfo;
    }

    @Override
    public void doneDownload() {
        log.info("[下载监听]blockchain downloaded");
    }
}
