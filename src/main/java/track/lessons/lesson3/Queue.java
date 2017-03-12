package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Tehnotrack
 * track.lessons.lesson3
 * <p>
 * Created by ilya on 12.03.17.
 */

interface Queue {

    void enqueue(int value);

    int dequeu() throws NoSuchElementException;

    int size();
}