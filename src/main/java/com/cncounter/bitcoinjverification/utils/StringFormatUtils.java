package com.cncounter.bitcoinjverification.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class StringFormatUtils {

    // 当前时间
    public static final String str(JSONArray jsonArray) {
        if (Objects.isNull(jsonArray)){
            return "[]";
        }
        return jsonArray.toString(SerializerFeature.PrettyFormat);

    }
}
