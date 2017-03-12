package track.lessons.lesson3;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 *
 */
public class MyArrayListTest {

    @Test
    public void testResize1() {
        MyArrayList list = new MyArrayList(0);
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }

        Assert.assertTrue(list.size() == 1000);
    }

    @Test
    public void testResize2() {
        MyArrayList list = new MyArrayList(0);
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }

        for (int i = 0; i < 100; i++) {
            list.remove(0);
        }
        Assert.assertTrue(list.size() == 0);

    }

    @Test(expected = NoSuchElementException.class)
    public void emptyList() throws Exception {
        List list = new MyArrayList();
        Assert.assertTrue(list.size() == 0);
        list.get(0);
    }

    @Test
    public void listAdd() throws Exception {
        List list = new MyArrayList();
        list.add(1);

        Assert.assertTrue(list.size() == 1);
    }

    @Test
    public void listAddRemove() throws Exception {
        List list = new MyArrayList();
        list.add(1);
        list.add(2);
        list.add(3);

        Assert.assertEquals(3, list.size());

        Assert.assertEquals(1, list.get(0));
        Assert.assertEquals(2, list.get(1));
        Assert.assertEquals(3, list.get(2));

        list.remove(1);
        Assert.assertEquals(3, list.get(1));
        Assert.assertEquals(1, list.get(0));

        list.remove(1);
        list.remove(0);

        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void listRemove() throws Exception {
        List list = new MyArrayList();
        list.add(1);
        list.remove(0);

        Assert.assertTrue(list.size() == 0);
    }

    @Test
    public void listStressTest() {
        final int MAXN = 100 * 1000;
        List mlist = new MyArrayList();
        java.util.ArrayList<Integer> testList = new ArrayList<>();

        Random random = new Random(42);

        for (int i = 0; i < MAXN; i++) {
            mlist.add(i);
            testList.add(i);
        }

        while (mlist.size() != 0) {
            Assert.assertEquals(mlist.size(), testList.size());
            int position = random.nextInt(mlist.size());
            Assert.assertEquals(mlist.remove(position), testList.remove(position).intValue());
        }
    }
}
