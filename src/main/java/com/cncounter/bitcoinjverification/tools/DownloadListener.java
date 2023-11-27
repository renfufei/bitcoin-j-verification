package com.cncounter.bitcoinjverification.tools;

import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.*;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.util.List;

@Slf4j
public class DownloadListener extends DownloadProgressTracker {
    public static DownloadListener getInstance() {
        return new DownloadListener();
    }

    @Override
    public void onBlocksDownloaded(Peer peer, Block block, @Nullable FilteredBlock filteredBlock, int blocksLeft) {
        //
        PeerAddress peerAddress = peer.getAddress();
        InetAddress peerAddr = peerAddress.getAddr();
        String peerHostAddress = peerAddr.getHostAddress();
        //
        Sha256Hash blockHash = block.getHash();
        List<Transaction> transactions = block.getTransactions();
        int transactionsSize = transactions.size();
        //
        log.info("有区块下载完成: peer={}; blockHash={}; transactionsSize={};  blocksLeft={};  ", peerHostAddress, blockHash, transactionsSize, blocksLeft);
        super.onBlocksDownloaded(peer, block, filteredBlock, blocksLeft);
    }
}
