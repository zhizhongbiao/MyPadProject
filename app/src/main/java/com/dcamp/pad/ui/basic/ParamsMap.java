package com.dcamp.pad.ui.basic;

import android.support.v4.util.ArrayMap;

import java.io.Serializable;

/**
 * Auther:  winds
 * Data:    2017/5/8
 * Desc:
 */

public class ParamsMap {
    ArrayMap<String, Serializable> params;

    public ParamsMap() {
        this.params = new ArrayMap<>();
    }

    public ParamsMap put(String key, Serializable value) {
        params.put(key, value);
        return this;
    }

    /**
     * 去除null 空字符串 -1 加入集合
     *
     * @param key
     * @param value
     * @return
     */
    public ParamsMap putWithoutEmpty(String key, Serializable value) {
        if (value != null) {
            if (value instanceof String) {
                if ((((String) value).trim().length() == 0)) {
                    return this;
                }
            }

            if (value instanceof Integer) {
                if (((Integer) value) == -1) {
                    return this;
                }
            }
            params.put(key, value);
        }
        return this;
    }

    public Serializable get(String key) {
        return params.get(key);
    }

    public ArrayMap<String, Serializable> get() {
        return params;
    }
}
