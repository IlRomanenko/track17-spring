package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Tehnotrack
 * track.lessons.lesson3
 * <p>
 * Created by ilya on 12.03.17.
 */

interface Stack {
    void push(int value);

    int pop() throws NoSuchElementException;

    int size();
}
