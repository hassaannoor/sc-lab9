import java.io.File;
import java.util.Scanner;

/**
 * RecursionTasks.java
 * 
 * Contains 4 recursion-based exercises:
 *  1. Integer-to-String Conversion (base 2–36)
 *  2. Recursive File Size Calculator
 *  3. Mutual Recursion (Even/Odd)
 *  4. Reentrant Recursive Array Sum
 */
public class RecursionTasks {

    /******************************************************
     * TASK 1: Integer-to-String Conversion using Recursion
     ******************************************************/
    public static class Task1_IntegerToString {
        private static final String DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public static String stringValue(int n, int base) {
            if (base < 2 || base > 36)
                throw new IllegalArgumentException("Base must be between 2 and 36.");

            if (n < 0)
                return "-" + stringValue(-n, base);

            if (n < base)
                return String.valueOf(DIGITS.charAt(n));

            return stringValue(n / base, base) + DIGITS.charAt(n % base);
        }

        // Iterative version for comparison
        public static String stringValueIterative(int n, int base) {
            if (base < 2 || base > 36)
                throw new IllegalArgumentException("Base must be between 2 and 36.");

            if (n == 0)
                return "0";

            boolean negative = n < 0;
            n = Math.abs(n);
            StringBuilder sb = new StringBuilder();

            while (n > 0) {
                sb.append(DIGITS.charAt(n % base));
                n /= base;
            }

            if (negative) sb.append('-');
            return sb.reverse().toString();
        }
    }

    /*******************************************
     * TASK 2: Recursive File Size Calculator
     *******************************************/
    public static class Task2_DirectorySize {
        private static final String[] EXCLUDED_EXTENSIONS = { ".tmp", ".log" };

        public static long getDirectorySize(File folder) {
            if (!folder.exists()) return 0;
            return traverseFolder(folder);
        }

        private static long traverseFolder(File folder) {
            long total = 0;
            File[] files = folder.listFiles();
            if (files == null) return 0;

            for (File f : files)
                total += processFile(f);

            return total;
        }

        private static long processFile(File f) {
            if (f.isFile()) {
                for (String ext : EXCLUDED_EXTENSIONS)
                    if (f.getName().endsWith(ext))
                        return 0;
                return f.length();
            } else if (f.isDirectory()) {
                return traverseFolder(f);
            }
            return 0;
        }

        // Convert bytes → human-readable
        public static String formatSize(long bytes) {
            if (bytes < 1024) return bytes + " B";
            int exp = (int) (Math.log(bytes) / Math.log(1024));
            return String.format("%.2f %sB", bytes / Math.pow(1024, exp), "KMGTPE".charAt(exp - 1));
        }
    }

    /***********************************************
     * TASK 3: Mutual Recursion – Even / Odd Checker
     ***********************************************/
    public static class Task3_MutualRecursion {
        public static boolean isEven(int n) {
            if (n == 0) return true;
            if (n < 0) return isEven(-n);
            return isOdd(n - 1);
        }

        public static boolean isOdd(int n) {
            if (n == 0) return false;
            if (n < 0) return isOdd(-n);
            return isEven(n - 1);
        }
    }

    /********************************************
     * TASK 4: Reentrant Recursive Array Sum
     ********************************************/
    public static class Task4_ReentrantSum {
        // Reentrant-safe recursion
        public static int sumArray(int[] arr, int index) {
            if (arr == null || index >= arr.length)
                return 0;
            return arr[index] + sumArray(arr, index + 1);
        }

        // Non-reentrant version (for demonstration)
        private static int sharedIndex = 0;
        public static int sumArrayNonReentrant(int[] arr) {
            if (sharedIndex >= arr.length) return 0;
            return arr[sharedIndex++] + sumArrayNonReentrant(arr);
        }
    }

    /*******************************************
     * MAIN MENU
     *******************************************/
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========= RECURSION TASKS =========");
            System.out.println("1. Integer to String (Base Conversion)");
            System.out.println("2. Recursive Directory Size Calculator");
            System.out.println("3. Mutual Recursion (Even/Odd)");
            System.out.println("4. Reentrant Recursive Sum (Multi-thread demo)");
            System.out.println("0. Exit");
            System.out.print("Select task: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter integer: ");
                    int n = sc.nextInt();
                    System.out.print("Enter base (2–36): ");
                    int base = sc.nextInt();
                    System.out.println("Recursive: " + Task1_IntegerToString.stringValue(n, base));
                    System.out.println("Iterative: " + Task1_IntegerToString.stringValueIterative(n, base));
                }

                case 2 -> {
                    System.out.print("Enter directory path: ");
                    String path = sc.nextLine();
                    File folder = new File(path);
                    long size = Task2_DirectorySize.getDirectorySize(folder);
                    System.out.println("Total size: " + Task2_DirectorySize.formatSize(size));
                }

                case 3 -> {
                    System.out.print("Enter integer: ");
                    int num = sc.nextInt();
                    System.out.println("isEven(" + num + ") = " + Task3_MutualRecursion.isEven(num));
                    System.out.println("isOdd(" + num + ") = " + Task3_MutualRecursion.isOdd(num));
                }

                case 4 -> {
                    int[] arr = {1, 2, 3, 4, 5};
                    System.out.println("Array sum (reentrant): " + Task4_ReentrantSum.sumArray(arr, 0));

                    // Multi-threaded demo
                    Runnable task = () -> {
                        int result = Task4_ReentrantSum.sumArray(arr, 0);
                        System.out.println(Thread.currentThread().getName() + " -> sum: " + result);
                    };
                    Thread t1 = new Thread(task, "Thread-1");
                    Thread t2 = new Thread(task, "Thread-2");
                    t1.start();
                    t2.start();
                }

                case 0 -> {
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                }

                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
