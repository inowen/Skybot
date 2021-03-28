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
        current = insertMe.parent();
        while(!current.isNull()) {
            current.setHeight(1 + Ints.max(current.left().getHeight(), current.right().getHeight()));
            current = current.parent();
        }

        // Optional rotation: go up from insertMe, find a node with more than 1 height diff btw its children, rotate it and end.
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



    // ROTATIONS
    private void simpleRotationRight(AvlNode<T> node) {

    }
    private void simpleRotationLeft(AvlNode<T> node) {

    }

    private void doubleRotateRight(AvlNode<T> node) {

    }

    private void doubleRotateLeft(AvlNode<T> node) {

    }



    // <=
    private boolean lesserEqualThan(T a, T b) {
        return (a.compareTo(b) <= 0);
    }

    // For testing purposes
    private void printPreorder() {
        root.printPreorder();
    }


    // Test
    public static void main(String[] args) {
        System.out.println("This is the AVL test!");

        AvlTree<Integer> testTree = new AvlTree<>(10);
        testTree.insert(11);
        testTree.insert(9);
        testTree.insert(8);
        testTree.insert(12);

        System.out.println("Preorder: ");
        testTree.printPreorder();
        System.out.println("Contains a 10? " + (testTree.containsEqual(10) ? "Yes" : "No"));
        System.out.println("Contains a 15? " + (testTree.containsEqual(15) ? "Yes" : "No"));

    }
}
