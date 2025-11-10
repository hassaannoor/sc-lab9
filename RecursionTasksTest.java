import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

public class RecursionTasksTest {

    /******************************************************
     * TASK 1: Integer-to-String Conversion using Recursion
     ******************************************************/
    @Test
    public void testStringValueBase16() {
        assertEquals("FF", RecursionTasks.Task1_IntegerToString.stringValue(255, 16));
        assertEquals("-101010", RecursionTasks.Task1_IntegerToString.stringValue(-42, 2));
    }

    @Test
    public void testStringValueBase36() {
        assertEquals("9IX", RecursionTasks.Task1_IntegerToString.stringValue(12345, 36));
    }

    @Test
    public void testInvalidBase() {
        assertThrows(IllegalArgumentException.class, () -> {
            RecursionTasks.Task1_IntegerToString.stringValue(10, 40);
        });
    }

    @Test
    public void testRecursiveAndIterativeEqual() {
        int n = 9876;
        int base = 8;
        String recursive = RecursionTasks.Task1_IntegerToString.stringValue(n, base);
        String iterative = RecursionTasks.Task1_IntegerToString.stringValueIterative(n, base);
        assertEquals(recursive, iterative);
    }

    /*******************************************
     * TASK 2: Recursive File Size Calculator
     *******************************************/
    @Test
    public void testGetDirectorySize() throws IOException {
        // Create a temporary directory with files
        File tempDir = new File("tempTestDir");
        tempDir.mkdir();

        File file1 = new File(tempDir, "file1.txt");
        File file2 = new File(tempDir, "file2.tmp"); // should be excluded
        java.nio.file.Files.writeString(file1.toPath(), "HelloWorld");
        java.nio.file.Files.writeString(file2.toPath(), "123456");

        long expected = file1.length(); // only file1 should count
        long result = RecursionTasks.Task2_DirectorySize.getDirectorySize(tempDir);

        assertEquals(expected, result);
        
        // Cleanup
        file1.delete();
        file2.delete();
        tempDir.delete();
    }

    @Test
    public void testFormatSize() {
        String formatted = RecursionTasks.Task2_DirectorySize.formatSize(10240);
        assertTrue(formatted.contains("KB"));
    }

    /***********************************************
     * TASK 3: Mutual Recursion – Even / Odd Checker
     ***********************************************/
    @Test
    public void testEvenOddPositiveNumbers() {
        assertTrue(RecursionTasks.Task3_MutualRecursion.isEven(4));
        assertTrue(RecursionTasks.Task3_MutualRecursion.isOdd(5));
    }

    @Test
    public void testEvenOddNegativeNumbers() {
        assertTrue(RecursionTasks.Task3_MutualRecursion.isEven(-6));
        assertTrue(RecursionTasks.Task3_MutualRecursion.isOdd(-3));
    }

    @Test
    public void testEvenOddEdgeCases() {
        assertTrue(RecursionTasks.Task3_MutualRecursion.isEven(0));
        assertFalse(RecursionTasks.Task3_MutualRecursion.isOdd(0));
    }

    /********************************************
     * TASK 4: Reentrant Recursive Array Sum
     ********************************************/
    @Test
    public void testSumArray() {
        int[] arr = {1, 2, 3, 4, 5};
        int sum = RecursionTasks.Task4_ReentrantSum.sumArray(arr, 0);
        assertEquals(15, sum);
    }

    @Test
    public void testSumArrayEmpty() {
        int[] arr = {};
        assertEquals(0, RecursionTasks.Task4_ReentrantSum.sumArray(arr, 0));
    }

    @Test
    public void testConcurrentReentrantSafety() throws InterruptedException {
        int[] arr = {1, 2, 3, 4, 5};
        int expected = 15;

        Runnable task = () -> {
            int sum = RecursionTasks.Task4_ReentrantSum.sumArray(arr, 0);
            assertEquals(expected, sum);
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    @Test
    public void testNonReentrantUnsafe() {
        int[] arr = {1, 2, 3};
        RecursionTasks.Task4_ReentrantSum.sharedIndex = 0;
        int result = RecursionTasks.Task4_ReentrantSum.sumArrayNonReentrant(arr);
        assertEquals(6, result);
    }
}
