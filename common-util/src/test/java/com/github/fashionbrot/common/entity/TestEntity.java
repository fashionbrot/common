package com.github.fashionbrot.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fashionbrot
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {

    private Long id;

    private Long parentId;

    private String name;

    private String parentName;

    private Long test5;

    private Long test6;

    private String test7;

    private Long test8;

    private Long test9;

    private String test10;
}
