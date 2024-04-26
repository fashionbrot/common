package com.github.fashionbrot.exception;

import com.github.fashionbrot.function.ThrowMsgFunction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonException extends RuntimeException {

    private int code;
    private String msg;


    public CommonException(String msg){
        super(msg);
        this.code = 1;
        this.msg = msg;
    }

    public CommonException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }



    public static void throwMsg(String msg){
        throw new CommonException(msg);
    }

    public static void throwMsg(int code,String msg){
        throw new CommonException(code,msg);
    }


    public static ThrowMsgFunction isTrue(boolean condition){
        return (code,msg) -> {
            if (condition) {
                CommonException.throwMsg(code,msg);
            }
        };
    }



}
