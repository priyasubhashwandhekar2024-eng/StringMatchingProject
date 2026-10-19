
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StringMatching {

    // 1. Naive String Matching Algorithm
    public static List<Integer> naiveSearch(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        if (m == 0 || m > n) {
            return positions;
        }

        for (int i = 0; i <= n - m; i++) {
            int j = 0;

            while (j < m && text.charAt(i + j) == pattern.charAt(j)) {
                j++;
            }

            if (j == m) {
                positions.add(i);
            }
        }

        return positions;
    }

    // 2. Rabin-Karp Algorithm
    public static List<Integer> rabinKarp(String text, String pattern) {
        List<Integer> positions = new ArrayList<>();

        int n = text.length();
        int m = pattern.length();

        if (m == 0 || m > n) {
            return positions;
        }

        int base = 256;
        int prime = 101;

        long patternHash = 0;
        long windowHash = 0;
        long highest = 1;

        for (int i = 0; i < m - 1; i++) {
            highest = (highest * base) % prime;
        }

        for (int i = 0; i < m; i++) {
            patternHash = (base * patternHash + pattern.charAt(i)) % prime;
            windowHash = (base * windowHash + text.charAt(i)) % prime;
        }

        for (int i = 0; i <= n - m; i++) {

            // Compare hashes, then verify actual characters
            if (patternHash == windowHash
                    && text.regionMatches(i, pattern, 0, m)) {
                positions.add(i);
            }

            // Calculate hash for the next text window
            if (i < n - m) {
                windowHash = (base * (windowHash
                        - text.charAt(i) * highest)
                        + text.charAt(i + m)) % prime;

                if (windowHash < 0) {
                    windowHash += prime;
                }
            }
        }

        return positions;
    }

    // 3. Main method
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the text: ");
        String text = sc.nextLine();

        System.out.print("Enter the pattern to search: ");
        String pattern = sc.nextLine();

        long start = System.nanoTime();
        List<Integer> naiveResult = naiveSearch(text, pattern);
        long naiveTime = System.nanoTime() - start;

        start = System.nanoTime();
        List<Integer> rkResult = rabinKarp(text, pattern);
        long rkTime = System.nanoTime() - start;

        System.out.println("\n----- RESULTS -----");
        System.out.println("Naive Matching Positions: " + naiveResult);
        System.out.println("Rabin-Karp Positions: " + rkResult);

        System.out.printf("Naive Execution Time: %d ns%n", naiveTime);
        System.out.printf("Rabin-Karp Execution Time: %d ns%n", rkTime);

        if (naiveResult.equals(rkResult)) {
            System.out.println("Verification: Both algorithms give the same result.");
        } else {
            System.out.println("Verification: Results do not match!");
        }

        sc.close();
    }
}
