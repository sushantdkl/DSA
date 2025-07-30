// A company's offices are connected by secure communication links, where:
// Each office is represented as a node.
// Each communication link is represented as an edge with a signal strength limit.
// Given a company network with 6 offices (nodes) and 5 communication links (edges), your task is to
// verify if a message can be securely transmitted from one office to another without exceeding the
// maximum signal strength.
// Graph Representation from the Image
// Offices (Nodes): {0, 1, 2, 3, 4, 5}
// Communication Links (Edges with Strengths):
// 0 ↔ 2 (Strength: 4)
// 2 ↔ 3 (Strength: 1)
// 2 ↔ 1 (Strength: 3)
// 4 ↔ 5 (Strength: 5)
// 3 ↔ 2 (Strength: 2)
// Class Implementation
// Implement the SecureTransmission class:
// SecureTransmission(int n, int[][] links)
// Initializes the system with n offices and a list of communication links.
// Each link is represented as [a, b, strength], indicating an undirected connection between office a and
// office b with a signal strength of strength.
// boolean canTransmit(int sender, int receiver, int maxStrength)
// Returns true if there exists a path between sender and receiver, where all links on the path have a
// strength strictly less than maxStrength.
// Otherwise, returns false.
// Example
// Input:
// ["SecureTransmission", "canTransmit", "canTransmit", "canTransmit", "canTransmit"]
// [[6, [[0, 2, 4], [2, 3, 1], [2, 1, 3], [4, 5, 5]]], [2, 3, 2], [1, 3, 3], [2, 0, 3], [0, 5, 6]]
// Output: [null, true, false, true, false]
// Explanation
// SecureTransmission(6, [[0, 2, 4], [2, 3, 1], [2, 1, 3], [4, 5, 5]])
// Initializes a network with 6 offices and 4 communication links.
// canTransmit(2, 3, 2) → true
// A direct link 2 → 3 exists with strength 1, which is less than 2.
// canTransmit(1, 3, 3) → false
// 1 → 2 has strength 3, which is not strictly less than 3, so transmission fails.
// canTransmit(2, 0, 3) → true
// 2 → 3 → 0 is a valid path.
// Links (2 → 3) and (3 → 0) have strengths 1, 2 (both < 3), so successful transmission.
// canTransmit(0, 5, 6) → false
// There is no connection between 0 and 5, so transmission fails.



package Question4;
import java.util.*;

public class Question4a {
    private final Map<Integer, List<int[]>> graph;

    public Question4a(int n, int[][] links) {
        graph = new HashMap<>();
        for (int i = 0; i < n; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (int[] link : links) {
            int a = link[0], b = link[1], strength = link[2];
            graph.get(a).add(new int[]{b, strength});
            graph.get(b).add(new int[]{a, strength}); // undirected
        }
    }

    public boolean canTransmit(int sender, int receiver, int maxStrength) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(sender);
        visited.add(sender);

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            if (curr == receiver) return true;

            for (int[] neighbor : graph.get(curr)) {
                int next = neighbor[0], strength = neighbor[1];
                if (strength < maxStrength && !visited.contains(next)) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int[][] links = {
            {0, 2, 4},
            {2, 3, 1},
            {2, 1, 3},
            {4, 5, 5}
        };

        Question4a st = new Question4a(6, links);

        System.out.println(st.canTransmit(2, 3, 2)); // true
        System.out.println(st.canTransmit(1, 3, 3)); // false
        System.out.println(st.canTransmit(2, 0, 3)); // true
        System.out.println(st.canTransmit(0, 5, 6)); // false
    }
}
