package com.github.fashionbrot.common.ribbon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Server {

    private String scheme;

    private String host;
    /**
     * 权重
     */
    private int weight;

    public String getServer(){
        return scheme+host;
    }


}
