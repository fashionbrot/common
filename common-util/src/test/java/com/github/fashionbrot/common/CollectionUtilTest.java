package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.CollectionUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class CollectionUtilTest {

    @Test
    public void testMergeValidInput() {
        // 测试正常情况下的合并嵌套集合
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(4, 5, 6);
        List<Integer> list3 = Arrays.asList(7, 8, 9);
        Collection<List<Integer>> nestedCollections = Arrays.asList(list1, list2, list3);

        Collection<Integer> mergedCollection = CollectionUtil.marge(nestedCollections);
        assertNotNull(mergedCollection);

        // 检查合并后的集合是否包含所有元素
        List<Integer> expectedList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertTrue(mergedCollection.containsAll(expectedList));
    }

    @Test
    public void testMergeEmptyInput() {
        // 测试空输入
        Collection<Integer> mergedCollection = CollectionUtil.marge(new ArrayList<>());
        assertNull(mergedCollection);
    }

    @Test
    public void testMergeNestedEmptyCollections() {
        // 测试嵌套的空集合
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        List<Integer> list3 = new ArrayList<>();
        Collection<List<Integer>> nestedCollections = Arrays.asList(list1, list2, list3);

        Collection<Integer> mergedCollection = CollectionUtil.marge(nestedCollections);
        assertNotNull(mergedCollection);

        // 检查合并后的集合是否为空
        assertTrue(mergedCollection.isEmpty());
    }


    @Test
    public void testFilterValidInput() {
        // 测试正常情况下的过滤
        List<String> stringList = Arrays.asList("apple", "banana", "cherry", "date");

        // 过滤出长度为 5 的字符串
        Predicate<String> filterPredicate = s -> s.length() == 5;

        String result = CollectionUtil.filter(stringList, filterPredicate);
        assertNotNull(result);
        assertEquals("apple", result);
    }

    @Test
    public void testFilterEmptyList() {
        // 测试空列表输入
        List<String> emptyList = Arrays.asList();

        Predicate<String> filterPredicate = s -> s.length() == 5;

        String result = CollectionUtil.filter(emptyList, filterPredicate);
        assertNull(result);
    }

    @Test
    public void testFilterNoMatchingElement() {
        // 测试没有符合条件的元素
        List<String> stringList = Arrays.asList("apple", "banana", "cherry", "date");

        // 过滤出长度为 6 的字符串，但列表中没有符合条件的元素
        Predicate<String> filterPredicate = s -> s.length() == 10;

        String result = CollectionUtil.filter(stringList, filterPredicate);
        System.out.println(result);
        assertNull(result);
    }

}
