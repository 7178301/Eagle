package eagle.logging;

import java.util.LinkedList;

/**
 * Created by cameron on 10/8/15.
 */
public class LogBufferCircle<E> extends LinkedList<E> {
    int maxsize = 0;

    public LogBufferCircle(int size) {
        this.maxsize = size;
    }

    @Override
    public boolean add(E e) {
        super.add(e);
        while (size() > maxsize) {
            pop();
        }
        return true;
    }


}
