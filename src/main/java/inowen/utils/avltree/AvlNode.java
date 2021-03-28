package inowen.utils.avltree;

import com.google.common.primitives.Ints;

public class AvlNode<T> {

    private NodeWrapper<T> wrapper;

    /**
     * Initializes the node as a null node.
     */
    public AvlNode() {
        wrapper = null;
    }

    /**
     * Initialize the node with something to contain.
     * Parent and children will be null nodes.
     * @param value
     */
    public AvlNode(T value) {
        wrapper = new NodeWrapper<>(value);
    }

    /**
     * Returns the parent of this node.
     * @return Parent
     */
    public AvlNode<T> parent() {
        return isNull() ? new AvlNode<>() : wrapper.parent;
    }

    /**
     * Returns the left child of this node.
     * @return Left child
     */
    public AvlNode<T> left() {
        return isNull() ? new AvlNode<>() : wrapper.left;
    }

    /**
     * Returns the right child of this node.
     * @return Right child
     */
    public AvlNode<T> right() {
        return isNull() ? new AvlNode<>() : wrapper.right;
    }

    /**
     * Returns the stored height of this node in the tree
     * @return Height (-1 if null node).
     */
    public int getHeight() {
        return isNull() ? -1 : wrapper.height;
    }

    /**
     * Set value for the height of this node.
     * Null is protected: it will always have -1 height.
     * @param h
     */
    public void setHeight(int h) {
        if (!isNull()) {
            wrapper.height = h;
        }
    }

    /**
     * Returns the value that this node contains.
     * @return
     */
    public T getValue() {
        return isNull() ? null : wrapper.value;
    }

    /**
     * Whether this node is a null node (contains nothing, empty, not a node).
     * @return Boolean
     */
    public boolean isNull() {
        return wrapper == null;
    }


    /**
     * Make the given node the current node's left child.
     * Won't link if given a null node.
     * (Doesn't change any parental links)
     * @param node
     */
    public void setLeft(AvlNode<T> node) {
        if (!node.isNull())
            wrapper.left = node;
    }

    /**
     * Make the given node the current node's left child.
     * Won't link if given a null node.
     * (Doesn't change any parental links)
     * @param node
     */
    public void setRight(AvlNode<T> node) {
        if (!node.isNull())
            wrapper.right = node;
    }

    /**
     * Make the given node the current node's parent.
     * Won't link if given a null node.
     * @param node
     */
    public void setParent(AvlNode<T> node) {
        if (!node.isNull())
            wrapper.parent = node;
    }

    // Unlink
    public void unlinkLeft() {
        if (!isNull())
            wrapper.left = null;
    }
    public void unlinkRight() {
        if (!isNull())
            wrapper.right = null;
    }
    public void unlinkParent() {
        if (!isNull())
            wrapper.parent = null;
    }

    /**
     * Print the subtree whose root is this node.
     */
    void printPreorder() {
        if (!isNull()) {
            wrapper.left.printPreorder();
            System.out.println("Value=" + wrapper.value.toString() + ", height=" + wrapper.height);
            wrapper.right.printPreorder();
        }
    }


    /**
     * Height of this node from the bottom of the tree.
     * Length of the longest path to a leaf.
     * Leaf heights are 0, null nodes would have -1 height.
     * @return
     */
    private int recursiveHeight() {
        if (isNull())
            return -1;
        return (1+ Ints.max(wrapper.left.recursiveHeight(), wrapper.right.recursiveHeight()));
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

        /**
         * Initialize with a value.
         * @param value What the node should contain
         */
        NodeWrapper(T value) {
            this.value = value;
            parent = new AvlNode<>();
            left = new AvlNode<>();
            right = new AvlNode<>();
            height = 0; // Initially it's a leaf, it it is given children it has to be manually readjusted
        }

        private T value;
        private int height;
        private AvlNode<T> parent;
        private AvlNode<T> left;
        private AvlNode<T> right;
    }

}
