package com.github.fashionbrot.common.http;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fashionbrot
 */
public class HttpCookie {

    private List<String> cookieList = new ArrayList<>();

    public HttpCookie add(String cookie){
        cookieList.add(cookie);
        return this;
    }

    public List<String> getCookieList(){
        return cookieList;
    }
}
