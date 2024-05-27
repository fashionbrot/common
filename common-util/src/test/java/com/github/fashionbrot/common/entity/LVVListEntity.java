package com.github.fashionbrot.common.entity;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper=false)
public class LVVListEntity extends LVVListParentEntity{

    private int id;

    private String name;

}
