package inowen.utils.avltree;

import java.util.function.Predicate;

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

    private AvlNode<T> root = null; // NEVER NULL.

    /**
     * Create a tree based on a value for the root.
     */
    public AvlTree(T value) {
        root = new AvlNode<>(value);
    }

    // Insert
    public void insert(T value) {

    }

    // Delete
    public void delete(AvlNode<T> node) {

    }

    // Retrieve (find)
    public AvlNode<T> find(T value) {
        AvlNode<T> current = root;
        while(!current.isNull()) {
            if (current.getValue() == value) {
                return current;
            }
            AvlNode<T> left = current.left();
            AvlNode<T> right = current.right();

            // How to compare the values?
            if (/* Use the compareTo method. And find out how that would work with BlockPos before continuing here. */) {

            }
        }
    }

    // Check if contained
    public boolean contains(T value) {
        return find(value) != null;
    }





    // Test
    public static void main(String[] args) {
        System.out.println("This is the AVL test!");
    }
}
