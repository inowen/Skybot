package inowen.pathfinder;

import inowen.SkyBotMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

/**
 * PriorityQueue implementation.
 * Comparator: if a.compareTo(b) < 0, then a<b
 * @param <T>
 * @author inowen
 */
@Mod.EventBusSubscriber(modid= SkyBotMod.MOD_ID, value= Dist.CLIENT)
public class PriorityQueue<T extends Comparable<T>> {
    private final ArrayList<T> heap;

    public PriorityQueue() {
        heap = new ArrayList<>();
    }

    public int size() {
        return heap.size();
    }

    /**
     * Returns the next element in the queue, or null if there isn't one.
     * @return Next element in queue, or null if there is none.
     */
    public T peek() {
        return heap.size()!=0 ? heap.get(0) : null;
    }

    /**
     * Delete and return the first element in the queue.
     * If there are no elements, it returns null.
     * @return The deleted element, or null if there was none.
     */
    public T pop() {
        if (heap.size()==0) {
            return null;
        }
        T returnMe = heap.get(0);
        // Fix the state of the binary heap
        heap.set(0, heap.get(heap.size()-1));
        heap.remove(heap.size()-1);
        int currentIdx = 0;
        int leftChild=1, rightChild=2;
        while (!(leftChild>=heap.size()||rightChild>=heap.size())
                && (heap.get(currentIdx).compareTo(heap.get(leftChild))>0 || heap.get(currentIdx).compareTo(heap.get(rightChild))>0)
        )
        {
            int nextCurrent;
            // Swap with the smallest child
            if (heap.get(leftChild).compareTo(heap.get(rightChild)) < 0) {
                // Left child is smaller
                T currAux = heap.get(currentIdx);
                heap.set(currentIdx, heap.get(leftChild));
                heap.set(leftChild, currAux);
                nextCurrent = leftChild;
            }
            else {
                T currAux = heap.get(currentIdx);
                heap.set(currentIdx, heap.get(rightChild));
                heap.set(rightChild, currAux);
                nextCurrent = rightChild;
            }
            // Get indices of current node's children
            currentIdx = nextCurrent;
            leftChild = 2*currentIdx + 1;
            rightChild = 2*currentIdx + 2;
        }
        // Edge case where the current node has no right child
        if (leftChild < heap.size()) {
            if (heap.get(currentIdx).compareTo(heap.get(leftChild)) > 0) {
                T currTmp = heap.get(currentIdx);
                heap.set(currentIdx, heap.get(leftChild));
                heap.set(leftChild, currTmp);
            }
        }
        return returnMe;
    }


    /**
     * Inserts an item into the queue.
     * @param item
     */
    public void insert(T item) {
        heap.add(item);

        // Bubble up, swapping with its parent while it's smaller than said parent
        int currentIdx = heap.size()-1;
        int parent = currentIdx%2==0 ? ((currentIdx-2)/2) : ((currentIdx-1)/2);
        while (currentIdx!=0 && heap.get(currentIdx).compareTo(heap.get(parent))<=0) {
            // Swap
            T currentTmp = heap.get(currentIdx);
            heap.set(currentIdx, heap.get(parent));
            heap.set(parent, currentTmp);
            // Set current and parent for the next iteration
            currentIdx = parent;
            parent = currentIdx%2==0 ? ((currentIdx-2)/2) : ((currentIdx-1)/2);
        }
    }

}
