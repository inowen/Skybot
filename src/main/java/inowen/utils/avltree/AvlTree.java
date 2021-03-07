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

    private AvlNode<T> root = null;

    public AvlTree() {
        root = new AvlNode<>();
    }

    // Insert

    // Delete

    // Retrieve (find)





    // Test
    public static void main(String[] args) {
        System.out.println("This is the AVL test!");
    }
}
