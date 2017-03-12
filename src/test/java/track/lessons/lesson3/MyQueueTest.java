package track.lessons.lesson3;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Tehnotrack
 * track.lessons.lesson3
 * <p>
 * Created by ilya on 12.03.17.
 */
public class MyQueueTest {

    @Test
    public void testResize1() {
        MyQueue queue = new MyQueue();
        for (int i = 0; i < 1000; i++) {
            queue.enqueue(i);
        }

        Assert.assertTrue(queue.size() == 1000);
    }

    @Test
    public void testResize2() {
        MyQueue queue = new MyQueue();
        for (int i = 0; i < 100; i++) {
            queue.enqueue(i);
        }

        for (int i = 0; i < 100; i++) {
            queue.dequeu();
        }
        Assert.assertTrue(queue.size() == 0);

    }

    @Test(expected = NoSuchElementException.class)
    public void emptyStack() throws Exception {
        MyQueue queue = new MyQueue();
        Assert.assertTrue(queue.size() == 0);
        queue.dequeu();
    }

    @Test
    public void queueAdd() throws Exception {
        MyQueue queue = new MyQueue();
        queue.enqueue(1);

        Assert.assertTrue(queue.size() == 1);
    }

    @Test
    public void queueAddRemove() throws Exception {
        MyQueue queue = new MyQueue();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        Assert.assertEquals(3, queue.size());

        Assert.assertEquals(1, queue.dequeu());
        Assert.assertEquals(2, queue.dequeu());
        Assert.assertEquals(3, queue.dequeu());

        Assert.assertTrue(queue.size() == 0);
    }

    @Test
    public void queueRemove() throws Exception {
        MyQueue queue = new MyQueue();
        queue.enqueue(1);
        queue.dequeu();

        Assert.assertTrue(queue.size() == 0);
    }

    @Test
    public void queueStressTest() {
        final int MAXN = 100 * 1000;
        Queue mlist = new MyQueue();
        java.util.Queue<Integer> testQueue = new ArrayDeque<>();

        Random random = new Random(42);

        for (int i = 0; i < MAXN; i++) {
            mlist.enqueue(i);
            testQueue.add(i);
        }

        while (mlist.size() != 0) {
            Assert.assertEquals(mlist.size(), testQueue.size());
            Assert.assertEquals(mlist.dequeu(), testQueue.poll().intValue());
        }
    }
}
