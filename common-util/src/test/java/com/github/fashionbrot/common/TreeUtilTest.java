package com.github.fashionbrot.common;

import com.github.fashionbrot.common.entity.TreeEntity;
import com.github.fashionbrot.common.util.TreeUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author fashionbrot
 */
public class TreeUtilTest {


    @Test
    public void test1() {
        List<TreeEntity> treeList = Arrays.asList(
                TreeEntity.builder().id(1L).parentId(0L).name("软件").build(),
                TreeEntity.builder().id(2L).parentId(1L).name("java").build(),
                TreeEntity.builder().id(3L).parentId(1L).name("python").build(),
                TreeEntity.builder().id(4L).parentId(2L).name("springcloud").build(),
                TreeEntity.builder().id(5L).parentId(2L).name("springboot").build()
        );

        BiConsumer<TreeEntity, Boolean> objectBiConsumer = (treeEntity, b) -> {
            if (!b) {
                treeEntity.setChild(null);
            }
        };

        List<TreeEntity> ts = TreeUtil.buildTree(treeList, TreeEntity::getId, TreeEntity::getParentId, TreeEntity::setChild, (treeEntity, b) -> {
            if (!(Boolean) b) {
                treeEntity.setChild(null);
            }
        }, tree -> tree.getParentId() == 0L);

        System.out.println(ts);

    }
}
