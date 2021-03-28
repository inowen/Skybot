package inowen.utils.avltree;

import com.google.common.primitives.Ints;


/**
 * AVL-Tree.
 * O(log2(n)) insertion, deletion, retrieval.
 *
 * Returns references to contained AvlNodes. Since the Avl-Tree is an ordered structure,
 * those values shouldn't be changed directly on the node inside the tree.
 *
 * In order to change something in the tree, first delete the corresponding node,
 * then change it, after that reinsert it.
 *
 * @author Noah Wenck (inowen)
 */
public class AvlTree<T extends Comparable<T>> {

    private AvlNode<T> root; // NEVER NULL.

    /**
     * Create a tree based on a value for the root.
     */
    public AvlTree(T value) {
        root = new AvlNode<>(value);
    }

    /**
     * Inserts an element into the tree and readjusts the tree.
     * @param value
     */
    public void insert(T value) {
        AvlNode<T> insertMe = new AvlNode<>(value);

        AvlNode<T> current = root;
        while (!current.isNull()) {
            // If lesser or equal, it goes left
            if (lesserEqualThan(value, current.getValue())) {
                if (current.left().isNull()) {
                    current.setLeft(insertMe);
                    insertMe.setParent(current);
                    current = new AvlNode<>(); // Sets current to a null node
                }
                else {
                    current = current.left();
                }
            }
            // If it is greater, it goes to the right
            else {
                if (current.right().isNull()) {
                    current.setRight(insertMe);
                    insertMe.setParent(current);
                    current = new AvlNode<>();
                }
                else {
                    current = current.right();
                }
            }
        }

        // Readjust heights
        recalculateHeightChain(insertMe.parent());

        // Optional rotation: go up from insertMe, find a node with more than 1 height diff btw its children, rotate it and end.
        // -- This should be extracted to a function, I'll have to do the same thing again when I'm deleting.
        int heightDiff = 0;
        current = insertMe;

    }

    // Delete
    public void deleteEqual(AvlNode<T> node) {

    }

    /**
     * Check if the tree contains a node with a value equal to the given one.
     * (Equal means it returns true for item.equals(value))
     * @param value
     * @return Node, or null if none matches the criterion.
     */
    public AvlNode<T> findEqual(T value) {

        AvlNode<T> current = root;
        while(!current.isNull()) {
            if (current.getValue().equals(value)) {
                return current;
            }

            if (value.compareTo(current.getValue()) == 0) {
                return current;
            }
            // a.compareTo(b)<0 <=> a<b
            else if (value.compareTo(current.getValue()) < 0) {
                current = current.left();
            }
            else {
                current = current.right();
            }
        }

        return null;
    }

    /**
     * Check if the tree contains a node with equal value to the given one.
     * @param value Value to compare it to.
     * @return
     */
    public boolean containsEqual(T value) {
        return findEqual(value) != null;
    }

    private void climbUntilRotationNeeded(AvlNode<T> node) {
        int heightDifference = node.left().getHeight() - node.right().getHeight();
        AvlNode<T> current = node;

        while(Math.abs(heightDifference)<=1 && !current.isNull()) {
            current = node.parent();
            heightDifference = current.left().getHeight() - current.right().getHeight();
        }

        // Pick a rotation based on the height difference.
        if (heightDifference > 1) {
            AvlNode<T> leftChild = current.left();
            if (leftChild.left().getHeight() > leftChild.right().getHeight()) {
                simpleRotationRight(current);
            }
            else {
                doubleRotateRight(current);
            }
        }
        else if (heightDifference < -1) { // (remove) Right weights more
            AvlNode<T> rightChild = current.right();
            if (rightChild.right().getHeight() > rightChild.left().getHeight()) {
                simpleRotationLeft(current);
            }
            else {
                doubleRotateLeft(current);
            }
        }
    }



    // ROTATIONS
    // Both simple rotations work with nodes on 3 different levels,
    // the notation: the node to rotate will be A, the relevant node on the level
    // below it is B, and B's relevant child is C.

    private void simpleRotationRight(AvlNode<T> node) {
        AvlNode<T> A = node;
        AvlNode<T> AsParent = A.parent();
        boolean aIsLeftChild = AsParent.left() == A; // Remember: A might be the root.
        AvlNode<T> B = A.left();
        AvlNode<T> C = B.right();

        B.unlinkParent();
        B.setParent(AsParent);
        if (aIsLeftChild) { AsParent.setLeft(B); } else { AsParent.setRight(B); }
        if (AsParent.isNull()) { root = B; }

        B.setRight(A);
        A.setParent(B);

        A.unlinkLeft();
        A.setLeft(C);
        C.setParent(A);

        recalculateHeightChain(A);
    }

    private void simpleRotationLeft(AvlNode<T> node) {
        AvlNode<T> A = node;
        AvlNode<T> B = A.right();
        AvlNode<T> C = B.left();
        AvlNode<T> AsParent = A.parent();
        boolean aIsLeftChild = AsParent.left() == A; // Remember: A might be the root.

        B.unlinkParent();
        B.setParent(AsParent);
        if (aIsLeftChild) { AsParent.setLeft(B); } else { AsParent.setRight(B); }
        if (AsParent.isNull()) { root = B; } // If the rotated node was the root, update root.

        B.setLeft(A);
        A.setParent(B);

        A.unlinkRight();
        A.setRight(C);
        C.setParent(A);

        recalculateHeightChain(A);
    }

    private void doubleRotateRight(AvlNode<T> node) {
        AvlNode<T> leftChild = node.left();
        simpleRotationLeft(leftChild);
        simpleRotationRight(node);
    }

    private void doubleRotateLeft(AvlNode<T> node) {
        AvlNode<T> rightChild = node.right();
        simpleRotationRight(rightChild);
        simpleRotationLeft(node);
    }


    // <=
    private boolean lesserEqualThan(T a, T b) {
        return (a.compareTo(b) <= 0);
    }

    // For testing purposes
    private void printPreorder() {
        root.printPreorder();
    }

    // Helper to recalculate heights after rotations / insertions
    private void recalculateHeightChain(AvlNode<T> node) {
        AvlNode<T> current = node;
        while(!current.isNull()) {
            System.out.println("Recalculation");
            current.setHeight(1 + Ints.max(current.left().getHeight(), current.right().getHeight()));
            current = current.parent();
        }
    }


    // Test
    public static void main(String[] args) {
        System.out.println("This is the AVL test!");

        AvlTree<Integer> testTree = new AvlTree<>(11);
        testTree.insert(99);
        testTree.insert(55);

        System.out.println("Preorder now");
        testTree.printPreorder();

        testTree.doubleRotateLeft(testTree.root);
        System.out.println("After rotating double left");
        testTree.printPreorder();

    }
}
