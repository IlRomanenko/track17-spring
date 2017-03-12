package track.lessons.lesson3;

import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Tehnotrack
 * track.lessons.lesson3
 * <p>
 * Created by ilya on 12.03.17.
 */
public class MyStackTest {

    @Test
    public void testResize1() {
        MyStack stack = new MyStack();
        for (int i = 0; i < 1000; i++) {
            stack.push(i);
        }

        Assert.assertTrue(stack.size() == 1000);
    }

    @Test
    public void testResize2() {
        MyStack stack = new MyStack();
        for (int i = 0; i < 100; i++) {
            stack.push(i);
        }

        for (int i = 0; i < 100; i++) {
            stack.pop();
        }
        Assert.assertTrue(stack.size() == 0);

    }

    @Test(expected = NoSuchElementException.class)
    public void emptyStack() throws Exception {
        MyStack stack = new MyStack();
        Assert.assertTrue(stack.size() == 0);
        stack.pop();
    }

    @Test
    public void stackAdd() throws Exception {
        MyStack stack = new MyStack();
        stack.push(1);

        Assert.assertTrue(stack.size() == 1);
    }

    @Test
    public void stackAddRemove() throws Exception {
        MyStack stack = new MyStack();
        stack.push(1);
        stack.push(2);
        stack.push(3);

        Assert.assertEquals(3, stack.size());

        Assert.assertEquals(3, stack.pop());
        Assert.assertEquals(2, stack.pop());
        Assert.assertEquals(1, stack.pop());

        Assert.assertTrue(stack.size() == 0);
    }

    @Test
    public void stackRemove() throws Exception {
        MyStack stack = new MyStack();
        stack.push(1);
        stack.pop();

        Assert.assertTrue(stack.size() == 0);
    }

    @Test
    public void listStressTest() {
        final int MAXN = 100 * 1000;
        Stack mlist = new MyStack();
        java.util.Stack<Integer> testStack = new java.util.Stack<>();

        Random random = new Random(42);

        for (int i = 0; i < MAXN; i++) {
            mlist.push(i);
            testStack.push(i);
        }

        while (mlist.size() != 0) {
            Assert.assertEquals(mlist.size(), testStack.size());
            Assert.assertEquals(mlist.pop(), testStack.pop().intValue());
        }
    }
}
