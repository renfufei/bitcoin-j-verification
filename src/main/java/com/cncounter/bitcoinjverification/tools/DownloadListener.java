package com.cncounter.bitcoinjverification.tools;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.*;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

@Slf4j
public class DownloadListener extends DownloadProgressTracker {
    public static DownloadListener getInstance() {
        return new DownloadListener();
    }

    @Override
    public void onBlocksDownloaded(Peer peer, Block block, @Nullable FilteredBlock filteredBlock, int blocksLeft) {
        //
        // PeerAddress peerAddress = peer.getAddress();
        // InetAddress peerAddr = peerAddress.getAddr();
        String peerHostAddress = null;// peerAddr.getHostAddress();
        //
        long difficultyTarget = block.getDifficultyTarget();
        Date time = block.getTime();
        // long timeSeconds = block.getTimeSeconds();
        Sha256Hash blockHash = block.getHash();
        Sha256Hash prevBlockHash = block.getPrevBlockHash();
        List<Transaction> transactions = block.getTransactions();
        int transactionsSize = transactions.size();
        //
        log.info("[下载监听]有区块下载完成: time={}; blockHash={}; transactionsSize={};  blocksLeft={};  ",
                time, blockHash, transactionsSize, blocksLeft);
        // log.info("[下载监听]block={};", JSON.toJSON(block));
        super.onBlocksDownloaded(peer, block, filteredBlock, blocksLeft);
    }

    @Override
    public void doneDownload() {
        log.info("[下载监听]blockchain downloaded");
    }
}
