package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * <p>
 * Должен иметь 2 конструктора
 * - без аргументов - создает внутренний массив дефолтного размера на ваш выбор
 * - с аргументом - начальный размер массива
 */
public class MyArrayList extends List {

    private static final int defaultSize = 16;

    private int[] array;
    private int arrayCapacity = 0;

    public MyArrayList() {
        array = new int[defaultSize];
        arrayCapacity = defaultSize;
    }

    public MyArrayList(int capacity) {
        if (capacity < defaultSize) {
            capacity = defaultSize;
        }
        array = new int[capacity];
        arrayCapacity = capacity;
    }

    private void expand() {
        arrayCapacity *= 2;
        int[] buffer = new int[arrayCapacity];
        System.arraycopy(array, 0, buffer, 0, currentSize);
        array = buffer;
    }

    private void collapse() {
        if (arrayCapacity < defaultSize) {
            return;
        }
        int[] buffer = new int[arrayCapacity / 2];
        System.arraycopy(array, 0, buffer, 0, currentSize);
        array = buffer;
    }

    @Override
    public void add(int item) {
        if (currentSize == arrayCapacity) {
            expand();
        }
        array[currentSize] = item;
        currentSize += 1;
    }

    @Override
    public int remove(int idx) throws NoSuchElementException {
        checkIdx(idx);
        currentSize -= 1;

        int value = array[idx];
        System.arraycopy(array, idx + 1, array, idx, currentSize - idx);
        if (currentSize < arrayCapacity / 4) {
            collapse();
        }

        return value;
    }

    @Override
    public int get(int idx) throws NoSuchElementException {
        checkIdx(idx);
        return array[idx];
    }
}
