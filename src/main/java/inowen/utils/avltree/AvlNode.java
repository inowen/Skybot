package inowen.utils.avltree;

public class AvlNode<T> {

    private T value = null;
    private AvlNode<T> parent = null;
    private AvlNode<T> left = null;
    private AvlNode<T> right = null;

    /**
     * Returns the parent of this node.
     * @return Parent
     */
    public AvlNode<T> parent() {
        return parent;
    }

    /**
     * Returns the left child of this node.
     * @return Left child
     */
    public AvlNode<T> left() {
        return left;
    }

    /**
     * Returns the right child of this node.
     * @return Right child
     */
    public AvlNode<T> right() {
        return right;
    }

    /**
     * Returns the value that this node contains.
     * @return
     */
    public T getValue() {
        return value;
    }

    /**
     * Whether this node is a null node (contains nothing, empty, not a node).
     * @return Boolean
     */
    public boolean isNull() {
        return value == null;
    }


    /**
     * Height of this node from the bottom of the tree.
     * Length of the longest path to a leaf.
     * Leaf heights are 0, null nodes would have -1 height.
     * @return
     */
    private int height() {

    }

}
