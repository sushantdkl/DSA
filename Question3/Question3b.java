// In a mystical world, powerful magical words exist. A magical word is a sequence of letters that reads
// the same forward and backward and always has an odd number of letters.
// A sorcerer has a long ancient manuscript represented as a string M. The sorcerer wants to extract two
// non-overlapping magical words from M in order to maximize their power combination.
// The power of a magical word is equal to its length, and the total power is the product of the lengths of
// the two chosen magical words.
// Task
// Given the manuscript M, determine the maximum possible power combination that can be achieved
// by selecting two non-overlapping magical words.
// Example 1
// Input:
// M = "xyzyxabc"
// Output:5
// Explanation:
// The magical word "xyzyx" (length 5) at [0:4]
// The magical word "a" (length 1) at [5:5]
// The product is 5 × 1 = 5
// Even if we instead choose:
// "xyzyx" (length 5)
// "c" (length 1)
// Max product = 5 × 1 = 5
// So the best possible product is 5.
// Example 2
// Input: M = "levelwowracecar"
// Output: 35
// Explanation:
// "level" (length 5)
// "racecar" (length 7)
// The product is 5 × 7 = 35.


package Question3;
public class Question3b {

    public static int maxMagicalPower(String M) {
        int n = M.length();
        int[] leftMax = new int[n];
        int[] rightMax = new int[n];

        // Step 1: Expand around centers for odd-length palindromes
        for (int center = 0; center < n; center++) {
            int l = center, r = center;
            while (l >= 0 && r < n && M.charAt(l) == M.charAt(r)) {
                int len = r - l + 1;
                if (len % 2 == 1) {
                    leftMax[r] = Math.max(leftMax[r], len);
                    rightMax[l] = Math.max(rightMax[l], len);
                }
                l--;
                r++;
            }
        }

        // Step 2: Build prefix max for leftMax
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i], leftMax[i - 1]);
        }

        // Step 3: Build suffix max for rightMax
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i], rightMax[i + 1]);
        }

        // Step 4: Compute max product
        int maxProduct = 0;
        for (int i = 0; i < n - 1; i++) {
            maxProduct = Math.max(maxProduct, leftMax[i] * rightMax[i + 1]);
        }

        return maxProduct;
    }

    public static void main(String[] args) {
        System.out.println(maxMagicalPower("xyzyxabc")); // Output: 5
        System.out.println(maxMagicalPower("levelwowracecar")); // Output: 35
    }
}
