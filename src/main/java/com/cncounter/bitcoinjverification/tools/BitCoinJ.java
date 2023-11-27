package com.cncounter.bitcoinjverification.tools;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BriefLogFormatter;
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
        startMainNet();
    }

    // 启动主网
    public static void startMainNet() {
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
