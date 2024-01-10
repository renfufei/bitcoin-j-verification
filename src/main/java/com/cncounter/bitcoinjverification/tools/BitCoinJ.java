package com.cncounter.bitcoinjverification.tools;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.*;
import org.bitcoinj.core.listeners.DownloadProgressTracker;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.Wallet;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class BitCoinJ {

    public static AtomicBoolean autoStart = new AtomicBoolean(true);
    private static Address forwardingAddress;
    // 工具
    private static WalletAppKit kit;

    public static final String MAIN_NET_FILE_PREFIX = "verification-service";

    @PostConstruct
    public void init() {
        log.info("[系统启动]BitCoinJ: 开始初始化");
        try {
            startMainNet();
        } catch (Exception e) {
            log.warn("[系统启动]启动主网失败! error:{}", e.getMessage(), e);
        }
    }

    // 启动主网
    public static void startMainNet() throws Exception {
        //
        BriefLogFormatter.init();
        // 文件前缀
        String filePrefix = MAIN_NET_FILE_PREFIX;
        // 主网配置参数
        NetworkParameters params = MainNetParams.get();
        log.info("当前连接的Network: " + params.getId());

        File chainFile = new File(filePrefix + ".spvchain");
        if (chainFile.exists()) {
            chainFile.delete();
        }

        // Setting up the BlochChain, the BlocksStore and connecting to the network.
        SPVBlockStore chainStore = new SPVBlockStore(params, chainFile);
        BlockChain chain = new BlockChain(params, chainStore);
        PeerGroup peerGroup = new PeerGroup(params, chain);
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));

//        Wallet wallet = Wallet.createBasic(params);
//        // Now we need to hook the wallet up to the blockchain and the peers. This registers event listeners that notify our wallet about new transactions.
//        chain.addWallet(wallet);
//        peerGroup.addWallet(wallet);

        DownloadProgressTracker bListener = DownloadListener.getInstance();

        // Now we re-download the blockchain. This replays the chain into the wallet. Once this is completed our wallet should know of all its transactions and print the correct balance.
        peerGroup.start();
        peerGroup.startBlockChainDownload(bListener);
        // 阻塞直到下载完成;
        bListener.await();

        // Print a debug message with the details about the wallet. The correct balance should now be displayed.
        // System.out.println(wallet.toString());

        // shutting down again
        peerGroup.stop();
    }

    public static void startMainNet_BAD() {
        //
        BriefLogFormatter.init();
        // 文件前缀
        String filePrefix = MAIN_NET_FILE_PREFIX;
        // 主网配置参数
        NetworkParameters params = MainNetParams.get();
        log.info("当前连接的Network: " + params.getId());
        // 转账地址?
        // forwardingAddress = LegacyAddress.fromBase58(params, args[0]);
        // System.out.println("Forwarding address: " + forwardingAddress);

        // Start up a basic app using a class that automates some boilerplate.
        kit = new WalletAppKit(params, new File("."), filePrefix);

        //
        kit.setDownloadListener(DownloadListener.getInstance());

        // 异步线程-开启钱包工具
        if (autoStart.get()) {
            kit.startAsync();
        }
        // 等待: 到达运行状态
        // kit.awaitRunning();

    }
}
