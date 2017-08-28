package com.easyder.wrapper.result;

/**
 * @author 刘琛慧
 * @date 2015/8/10.
 */
public class CacheResult {
    public boolean isValid = true; //缓存是否有效
    public String cacheData; //缓存数据解析成功后的JsonObject

    public CacheResult() {
    }

    public CacheResult(boolean isValid, String cacheData) {
        this.isValid = isValid;
        this.cacheData = cacheData;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    public String getCacheData() {
        return cacheData;
    }

    public void setCacheData(String cacheData) {
        this.cacheData = cacheData;
    }
}
