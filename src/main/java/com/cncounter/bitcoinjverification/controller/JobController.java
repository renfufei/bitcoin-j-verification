package com.cncounter.bitcoinjverification.controller;

import com.alibaba.fastjson.JSONObject;
import com.cncounter.bitcoinjverification.components.BitCoinCoreRpcClient;
import com.cncounter.bitcoinjverification.model.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 测试rpc的job
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private BitCoinCoreRpcClient rpcClient;


    @GetMapping("/test/rpc")
    public Result<Object> testRPC() {
        //
        Long genesisHeight = 0L;
        //
        Long latestBlockHeight = rpcClient.getLatestBlockHeight();
        String bestBlockHash = rpcClient.getBestBlockHash();
        String genesisBlockHash = rpcClient.getBlockHashByHeight(genesisHeight);
        JSONObject genesisBlockByHash = rpcClient.getBlockByHash(genesisBlockHash);
        JSONObject bestBlockByHash = rpcClient.getBlockByHash(bestBlockHash);
        JSONObject genesisBlockByHeight = rpcClient.getBlockByHeight(genesisHeight);
        JSONObject latestBlockByHeight = rpcClient.getBlockByHeight(latestBlockHeight);

        JSONObject data = new JSONObject();
        data.put("latestBlockHeight", latestBlockHeight);
        data.put("bestBlockHash", bestBlockHash);
        data.put("genesisBlockHash", genesisBlockHash);
        data.put("genesisBlockByHash", genesisBlockByHash);
        data.put("genesisBlockByHeight", genesisBlockByHeight);
        //data.put("bestBlockByHash", bestBlockByHash);
        // data.put("latestBlockByHeight", latestBlockByHeight);
        return Result.success(data);
    }
}
