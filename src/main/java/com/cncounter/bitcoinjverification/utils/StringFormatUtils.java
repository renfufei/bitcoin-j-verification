package com.cncounter.bitcoinjverification.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.text.SimpleDateFormat;
import java.util.*;

public class StringFormatUtils {

    // 当前时间
    public static final String str(List<? extends Object> collection) {
        if (Objects.isNull(collection)) {
            return "[]";
        }
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(collection);
        return jsonArray.toString(SerializerFeature.PrettyFormat);
    }

    public static boolean isEmpty(String str) {
        return (null == str) || (str.trim().isEmpty());
    }
}
