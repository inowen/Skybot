package inowen.utils.avltree;

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
public class AvlTree<T> {

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

    }

    // Check if contained
    public boolean contains(T value) {
        
    }





    // Test
    public static void main(String[] args) {
        System.out.println("This is the AVL test!");
    }
}
