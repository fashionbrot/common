package com.github.fashionbrot.common.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fashionbrot
 */
public class HttpHeader {

    //是否允许覆盖
    private Boolean override;

    private  Map<String,String> header = new HashMap<>();

    public HttpHeader override(Boolean override){
        this.override = override;
        return this;
    }

    public HttpHeader add(String key, String value){
        header.put(key,value);
        return this;
    }

    public HttpHeader add(Map<String,String> header){
        header.putAll(header);
        return this;
    }

    public HttpHeader set(Map<String,String> header){
        this.header = header;
        return this;
    }

    public Map<String,String> getHeader(){
        return header;
    }

    public Boolean getOverride() {
        return override;
    }
}
