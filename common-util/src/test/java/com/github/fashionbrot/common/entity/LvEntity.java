package com.github.fashionbrot.common.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class LvEntity extends LvParent1Entity {

    private String b;
    private Byte b1;
    private Short b2;
    private Integer b3;
    private Long b4;
    private Float b5;
    private Double b6;
    private BigDecimal b7;
    private Date b8;
    private LocalTime b9;
    private LocalDate b10;
    private LocalDateTime b11;
    private Boolean b12;

    private List<LVVListEntity> list1;
}
