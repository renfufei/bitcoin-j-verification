package com.cncounter.bitcoinjverification.components;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cncounter.bitcoinjverification.tools.HttpUtilsOK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

// 连接 BitCoinCore 的客户端
// rcp文档: https://developer.bitcoin.org/reference/rpc/

@Slf4j
@Component
public class BitCoinCoreRpcClient {

    @Value("${bitcoin.core.rpc.host:localhost}")
    private String rpcHost;
    @Value("${bitcoin.core.rpc.port:8332}")
    private String rpcPort;
    @Value("${bitcoin.core.rpc.user:admin}")
    private String rpcUser;
    @Value("${bitcoin.core.rpc.password:admin}")
    private String rpcPassword;

    // 获取最新的区块高度
    public Long getLatestBlockHeight() {
        JSONObject request = baseRpcReq("getblockcount");
        JSONObject resp = queryRpc(request);
        // 检索结果
        Long height = resp.getLong("result");
        log.info("[RPC客户端]获取到最新的区块高度: {}", height);
        return height;
    }

    // 获取最新的可信任区块Hash
    public String getBestBlockHash() {
        JSONObject request = baseRpcReq("getbestblockhash");
        JSONObject resp = queryRpc(request);
        // 检索结果
        String hash = resp.getString("result");
        log.info("[RPC客户端]获取最新的可信任区块Hash: {}", hash);
        return hash;
    }

    // 获取指定高度的区块, 从0开始
    public JSONObject getBlockByHeight(long height) {
        String blockHash = getBlockHashByHeight(height);
        return getBlockByHash(blockHash);
    }

    public JSONObject getBlockByHash(String blockHash) {
        if (null == blockHash || blockHash.trim().isEmpty()) {
            return null;
        }
        JSONObject request = baseRpcReq("getblock", blockHash);

        JSONObject resp = queryRpc(request);
        JSONObject block = resp.getJSONObject("result");
        return block;
    }


    public String getBlockHashByHeight(Long height) {
        JSONObject request = baseRpcReq("getblockhash", height);
        //request.put("params", Arrays.asList(height));

        JSONObject resp = queryRpc(request);
        // 检索结果
        String hash = resp.getString("result");
        log.info("[RPC客户端]根据高度[{}]获取到区块Hash: {}", height, hash);
        return hash;
    }


    private static JSONObject baseRpcReq(String method, Object... paramItems) {
        JSONObject request = new JSONObject();
        request.put("id", "BitCoinCoreRpcClient");
        request.put("jsonrpc", "1.0");
        request.put("method", method);

        JSONArray params = new JSONArray();
        if (Objects.nonNull(paramItems)) {
            for (Object item : paramItems) {
                if (Objects.nonNull(item)) {
                    params.add(item);
                }
            }
        }
        request.put("params", params);

        return request;
    }


    private JSONObject queryRpc(JSONObject request) {
        String baseUrl = rpcBaseUrl();
        String respString = HttpUtilsOK.post(baseUrl, request);
        JSONObject resp = JSONObject.parseObject(respString);
        return resp;
    }


    private String rpcBaseUrl() {
        String baseUrl = "http://" + rpcUser + ':' + rpcPassword + "@" + rpcHost + ":" + rpcPort + "/";
        return baseUrl;
    }
}
