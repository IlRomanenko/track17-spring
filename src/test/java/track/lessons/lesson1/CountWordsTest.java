package track.lessons.lesson1;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

/**
 *
 */
@Ignore
public class CountWordsTest {

    static File file;

    @BeforeClass
    public static void init() {
        file = new File("words.txt");
    }


    @Test
    public void countNumbers() throws Exception {
        CountWords countWords = new CountWords();
        Assert.assertEquals(42, countWords.countNumbers(file));
    }

    @Test
    public void concatWords() throws Exception {
        CountWords countWords = new CountWords();
        Assert.assertEquals("hello world !", countWords.concatWords(file));
    }

}