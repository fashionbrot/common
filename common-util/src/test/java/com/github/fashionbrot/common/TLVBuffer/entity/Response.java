package com.github.fashionbrot.common.TLVBuffer.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable{
    private static final long serialVersionUID = -3655390020082644681L;

    private int code;
    private String msg;
    private T data;

}
