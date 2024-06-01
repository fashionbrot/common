package com.github.fashionbrot.common.TVLJS.js.entity;

import lombok.Data;

@Data
public class JsEntity1 {

    private short shortMin;
    private short shortMax;
    private Short shortNull;

    private int intMin;
    private int intMax;
    private Integer intNull;


    private long longMin;
    private long longMax;
    private Long longNull;

    private float floatMin;
    private float floatMax;
    private Float floatNull;

    private double doubleMin;
    private double doubleMax;
    private Double doubleNull;

    private boolean booleanTrue;
    private boolean booleanFalse;
    private Boolean booleanNull;
}
