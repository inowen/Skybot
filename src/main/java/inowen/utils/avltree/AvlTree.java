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

    /**
     * Inserts an element into the tree and readjusts the tree.
     * @param value
     */
    public void insert(T value) {

        AvlNode<T> current = root;
        while (!current.isNull()) {
            // If lesser
                // If left node is null put it there

                // Else, 
        }

        // Readjust heights

        // Optional rotation
    }

    // Delete
    public void delete(AvlNode<T> node) {

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
            AvlNode<T> left = current.left();
            AvlNode<T> right = current.right();

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



    // Rotations: Simple right, simple left, double right, double left



    // <=
    private boolean lesserThan(T a, T b) {
        return (a.compareTo(b) < 0);
    }


    // Test
    public static void main(String[] args) {
        System.out.println("This is the AVL test!");
    }
}
