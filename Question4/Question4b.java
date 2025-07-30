//  You are provided with the chemical formula, return maximum number of atoms represented in
// chemical formula.
// A Treasure Hunt Game is played on an undirected graph by two players.
// Player 1 starts at node 1 and moves first.
// Player 2 starts at node 2 and moves second.
// There is a Treasure at node 0 🎯.
// Game Rules:
// The graph[a] represents an undirected graph where graph[a] contains a list of all nodes connected to
// node a.
// Player 1 moves first, followed by Player 2.
// Player 2 cannot move to node 0 (Treasure).
// The game ends in three ways:
// Player 1 reaches node 0 → Player 1 Wins (Return 1).
// Player 2 catches Player 1 (both at the same node) → Player 2 Wins (Return 2).
// A position repeats (same player at the same node) → Game is a Draw (Return 0).
// Given Graph Representation:
// Graph = [
//  [2, 5], # Node 0 is connected to nodes 2 and 5 (Hole)
//  [3], # Node 1 is connected to node 3 (Mouse starts here)
//  [0, 4, 5],# Node 2 is connected to nodes 0, 4, and 5 (Cat starts here)
//  [1, 4, 5],# Node 3 is connected to nodes 1, 4, and 5
//  [2, 3], # Node 4 is connected to nodes 2 and 3
//  [0, 2, 3] # Node 5 is connected to nodes 0, 2, and 3]
// Game Rules Recap
// Player 1 starts at node 1 and moves first.
// Player 2 starts at node 2 and moves second.
// Player 2 cannot move to node 0.
// The game ends in three ways:
// Player 1 reaches node 0 → Player 1 Wins (1)
// Player 2 catches Player 1 → Player 2 Wins (2)
// A repeated position occurs → Game is a Draw (0)
// Step-by-Step Simulation
// Turn 1: Player 1 Moves (Starts at Node 1)
// The only move available is to node 3.
// Player 1 moves to node 3.
// Turn 2: Player 2 Moves (Starts at Node 2)
// Player 2 has three options: node 0 (forbidden), node 4, and node 5.
// Player 2 moves to node 4.
// Turn 3: Player 1 Moves (At Node 3)
// Player 1 can move to node 1, node 4, or node 5.
// To move toward node 0, Player 1 moves to node 5.
// Turn 4: Player 2 Moves (At Node 4)
// The only move available is to node 2.
// Player 2 moves back to node 2.
// Turn 5: Player 1 Moves (At Node 5)
// Player 1 can move to node 0 (winning move), node 2, or node 3.
// Player 1 moves to node 0 and wins, but...
// Turn 6: Player 2 Moves (At Node 2)
// Player 2 can move to node 0 (forbidden), node 4, or node 5.
// Player 2 moves to node 5.
// Cycle Detection and Draw Condition
// The same positions start repeating (nodes 3, 5, 4, and 2).
// The game enters a loop.
// Since no player forces a win, the game results in a draw (0).
// Final Output:
// Output: 0 (Draw)




package Question4;
import java.util.*;

public class Question4b {

    static final int DRAW = 0;
    static final int PLAYER1_WIN = 1;
    static final int PLAYER2_WIN = 2;

    public int treasureGame(int[][] graph) {
        int n = graph.length;
        int[][][] memo = new int[n][n][2]; // [p1Pos][p2Pos][turn]
        return dfs(graph, 1, 2, 0, memo, new HashSet<>());
    }

    private int dfs(int[][] graph, int p1, int p2, int turn, int[][][] memo, Set<String> visited) {
        if (p1 == 0) return PLAYER1_WIN;
        if (p1 == p2) return PLAYER2_WIN;

        String stateKey = p1 + "," + p2 + "," + turn;
        if (visited.contains(stateKey)) return DRAW;

        if (memo[p1][p2][turn] != 0) return memo[p1][p2][turn];

        visited.add(stateKey);
        int result = turn == 0 ? PLAYER2_WIN : PLAYER1_WIN; // Default fallback

        if (turn == 0) {
            for (int next : graph[p1]) {
                int nextResult = dfs(graph, next, p2, 1, memo, visited);
                if (nextResult == PLAYER1_WIN) {
                    result = PLAYER1_WIN;
                    break;
                } else if (nextResult == DRAW) {
                    result = DRAW;
                }
            }
        } else {
            for (int next : graph[p2]) {
                if (next == 0) continue; // forbidden node
                int nextResult = dfs(graph, p1, next, 0, memo, visited);
                if (nextResult == PLAYER2_WIN) {
                    result = PLAYER2_WIN;
                    break;
                } else if (nextResult == DRAW) {
                    result = DRAW;
                }
            }
        }

        visited.remove(stateKey);
        memo[p1][p2][turn] = result;
        return result;
    }

    public static void main(String[] args) {
        Question4b game = new Question4b();
        int[][] graph = {
            {2, 5},
            {3},
            {0, 4, 5},
            {1, 4, 5},
            {2, 3},
            {0, 2, 3}
        };

        int result = game.treasureGame(graph);
        System.out.println("Game Result: " + result); // Expected output: 0 (Draw)
    }
}
