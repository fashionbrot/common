package com.github.fashionbrot.common;

import com.github.fashionbrot.common.entity.TreeEntity;
import com.github.fashionbrot.common.util.TreeUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

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

        List<TreeEntity> ts2 = TreeUtil.buildAscTree(treeList, TreeEntity::getId, TreeEntity::getParentId, TreeEntity::setChild, (treeEntity, b) -> {
            if (!(Boolean) b) {
                treeEntity.setChild(null);
            }
        }, tree -> tree.getParentId() == 0L, Comparator.comparing(TreeEntity::getName));

        System.out.println(ts2);
    }




    @Test
    public void testBuildTree() {
        // Create some sample data
        List<Node> nodes = createSampleNodes();

        // Define functions and predicates
        Function<Node, Integer> idFunc = Node::getId;
        Function<Node, Integer> pIdFunc = Node::getParentId;
        BiConsumer<Node, List<Node>> setChildListFunc = Node::setChildren;
        BiConsumer<Node, Boolean> setHasChildFunc = Node::setHasChildren;
        Predicate<Node> rootPIdValue = node -> node.getParentId() == 0;

        // Build the tree
        List<Node> tree = TreeUtil.buildTree(nodes, idFunc, pIdFunc, setChildListFunc, setHasChildFunc, rootPIdValue);

        // Assert the expected tree structure
        assertEquals(1, tree.size());
        assertEquals(2, tree.get(0).getChildren().size());
        assertEquals(2, tree.get(0).getChildren().get(0).getChildren().size());
    }


    private List<Node> createSampleNodes() {
        List<Node> nodes = new ArrayList<>();

        nodes.add(new Node(1, 0));  // Root node
        nodes.add(new Node(2, 1));
        nodes.add(new Node(3, 1));
        nodes.add(new Node(4, 2));
        nodes.add(new Node(5, 2));
        nodes.add(new Node(6, 3));

        return nodes;
    }


    // Define the Node class (for testing purposes)
    static class Node {
        private final int id;
        private final Integer parentId;
        private List<Node> children;
        private boolean hasChildren;

        public Node(int id, Integer parentId) {
            this.id = id;
            this.parentId = parentId;
        }

        public int getId() {
            return id;
        }

        public Integer getParentId() {
            return parentId;
        }

        public List<Node> getChildren() {
            return children;
        }

        public void setChildren(List<Node> children) {
            this.children = children;
        }

        public boolean hasChildren() {
            return hasChildren;
        }

        public void setHasChildren(boolean hasChildren) {
            this.hasChildren = hasChildren;
        }
    }

}
