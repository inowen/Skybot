package inowen.utils.avltree;

public class AvlNode<T> {

    private T value;
    private AvlNode<T> parent = null;
    private AvlNode<T> left = null;
    private AvlNode<T> right = null;

    public AvlNode<T> parent() {
        return parent;
    }

    public AvlNode<T> left() {
        return left;
    }

    public AvlNode<T> right() {
        return right;
    }



}
