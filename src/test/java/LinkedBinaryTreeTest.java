import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedBinaryTreeTest {

    private LinkedBinaryTree<String> sampleTree() {
        // Builds:
        //        A
        //      /   \
        //     B     C
        //    / \   / \
        //   D  E  F  G
        LinkedBinaryTree<String> t = new LinkedBinaryTree<>();
        Position<String> a = t.addRoot("A");
        Position<String> b = t.addLeft(a, "B");
        Position<String> c = t.addRight(a, "C");
        t.addLeft(b, "D");
        t.addRight(b, "E");
        t.addLeft(c, "F");
        t.addRight(c, "G");
        return t;
    }

    @Test
    void addRootOnEmptyTree_setsRootAndSize() {
        LinkedBinaryTree<Integer> t = new LinkedBinaryTree<>();
        assertTrue(t.isEmpty());

        Position<Integer> r = t.addRoot(10);

        assertEquals(1, t.size());
        assertEquals(r, t.root());
        assertEquals(10, r.getElement());
        assertFalse(t.isEmpty());
    }

    @Test
    void addRootOnNonEmptyTree_throws() {
        LinkedBinaryTree<String> t = new LinkedBinaryTree<>();
        t.addRoot("A");
        assertThrows(IllegalStateException.class, () -> t.addRoot("B"));
    }

    @Test
    void addLeft_whenLeftAlreadyExists_throws() {
        LinkedBinaryTree<String> t = new LinkedBinaryTree<>();
        Position<String> r = t.addRoot("A");
        t.addLeft(r, "B");
        assertThrows(IllegalArgumentException.class, () -> t.addLeft(r, "X"));
    }

    @Test
    void addRight_whenRightAlreadyExists_throws() {
        LinkedBinaryTree<String> t = new LinkedBinaryTree<>();
        Position<String> r = t.addRoot("A");
        t.addRight(r, "C");
        assertThrows(IllegalArgumentException.class, () -> t.addRight(r, "X"));
    }

    @Test
    void parentLeftRight_accessorsWork() {
        LinkedBinaryTree<String> t = new LinkedBinaryTree<>();
        Position<String> a = t.addRoot("A");
        Position<String> b = t.addLeft(a, "B");
        Position<String> c = t.addRight(a, "C");

        assertEquals(a, t.parent(b));
        assertEquals(a, t.parent(c));
        assertEquals(b, t.left(a));
        assertEquals(c, t.right(a));
    }

    @Test
    void positions_inorder_returnsExpectedOrder() {
        LinkedBinaryTree<String> t = sampleTree();

        List<String> actual = new ArrayList<>();
        for (Position<String> p : t.positions()) {
            actual.add(p.getElement());
        }

        List<String> expected = List.of("D", "B", "E", "A", "F", "C", "G");
        assertEquals(expected, actual);
    }

    @Test
    void iterator_inorder_returnsExpectedOrder() {
        LinkedBinaryTree<String> t = sampleTree();

        List<String> actual = new ArrayList<>();
        for (String s : t) {           // uses iterator()
            actual.add(s);
        }

        List<String> expected = List.of("D", "B", "E", "A", "F", "C", "G");
        assertEquals(expected, actual);
    }

    @Test
    void remove_leaf_decrementsSizeAndDetaches() {
        LinkedBinaryTree<String> t = new LinkedBinaryTree<>();
        Position<String> a = t.addRoot("A");
        Position<String> b = t.addLeft(a, "B");
        Position<String> c = t.addRight(a, "C");

        assertEquals(3, t.size());
        assertEquals("C", t.remove(c));
        assertEquals(2, t.size());
        assertNull(t.right(a)); // right child removed
        assertEquals(b, t.left(a));
    }

    @Test
    void remove_nodeWithOneChild_splicesChildUp() {
        // A
        //  \
        //   C
        //  /
        // F
        LinkedBinaryTree<String> t = new LinkedBinaryTree<>();
        Position<String> a = t.addRoot("A");
        Position<String> c = t.addRight(a, "C");
        Position<String> f = t.addLeft(c, "F");

        assertEquals(3, t.size());
        assertEquals("C", t.remove(c));     // should splice F up to be A's right
        assertEquals(2, t.size());

        assertNotNull(t.right(a));
        assertEquals("F", t.right(a).getElement());
        assertEquals(a, t.parent(t.right(a)));
    }

    @Test
    void remove_nodeWithTwoChildren_throws() {
        LinkedBinaryTree<String> t = sampleTree();

        // In sample tree, root "A" has two children, so remove should throw
        assertThrows(IllegalArgumentException.class, () -> t.remove(t.root()));
    }

    @Test
    void attach_onInternalNode_throws() {
        LinkedBinaryTree<String> t = sampleTree(); // root is internal

        LinkedBinaryTree<String> t1 = new LinkedBinaryTree<>();
        t1.addRoot("X");

        LinkedBinaryTree<String> t2 = new LinkedBinaryTree<>();
        t2.addRoot("Y");

        assertThrows(IllegalArgumentException.class, () -> t.attach(t.root(), t1, t2));
    }

    @Test
    void attach_onLeaf_increasesSizeAndLinksSubtrees() {
        LinkedBinaryTree<String> t = new LinkedBinaryTree<>();
        Position<String> a = t.addRoot("A");
        Position<String> b = t.addLeft(a, "B"); // leaf to attach to

        LinkedBinaryTree<String> t1 = new LinkedBinaryTree<>();
        t1.addRoot("X");

        LinkedBinaryTree<String> t2 = new LinkedBinaryTree<>();
        t2.addRoot("Y");

        t.attach(b, t1, t2);

        assertEquals(4, t.size());            // A, B, X, Y
        assertEquals("X", t.left(b).getElement());
        assertEquals("Y", t.right(b).getElement());
        assertEquals(b, t.parent(t.left(b)));
        assertEquals(b, t.parent(t.right(b)));
    }
}

