package inowen.utils.avltree;

import com.google.common.primitives.Ints;

public class AvlNode<T> {

    /**
     * Initializes the node as a null node, as well as its parent and children.
     */
    public AvlNode() {
        value = null;
        parent = new AvlNode<>(); // ALERT! This is an infinite loop.
        left = new AvlNode<>();   // INFINITE LOOP ALERT!!!
        right = new AvlNode<>(); // TODO: For real, replace this somehow!
    }

    /**
     * Initialize the node with something to contain.
     * @param value
     */
    public AvlNode(T value) {
        this();
        this.value = value;
    }

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
        if (isNull())
            return -1;
        return (1+ Ints.max(left.height(), right.height()));
    }


    /**
     * Contains everything that makes a node.
     * The reason for this class is to avoid returning null when asking
     * for a node that doesn't exist (for example, children of a leaf),
     * and instead return a node with a null value for the wrapper.
     *
     * That way, even on null (empty) nodes the isNull() function works.
     *
     * @param <T> Type of the value contained in the node.
     */
    private class NodeWrapper<T> {
        T value;
        AvlNode<T> parent;
        AvlNode<T> left;
        AvlNode<T> right;
    }

}
