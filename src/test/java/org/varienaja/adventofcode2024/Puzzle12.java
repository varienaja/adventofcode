package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.ToLongFunction;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle12 extends PuzzleAbs {

  /** A Garden consists of different points that are adjacent and share the same id. */
  class Garden {
    /** All border Points of the Garden together with 1 or more sides where it has a border. (NSWE). */
    private Map<Point, String> borderPiece2Sides;
    private char id;
    private long area;

    Garden(char id, Point starter) {
      this.id = id;
      area = 0;
      borderPiece2Sides = new HashMap<>();
    }

    /**
     * Cost for fencing method A.
     *
     * @return Garden area * garden perimeter
     */
    long costA() {
      return area * borderPiece2Sides.values().stream().mapToLong(s -> s.length()).sum();
    }

    /**
     * Cost for fencing method B.
     *
     * @return Garden area * number of sides
     */
    long costB() {
      Set<Point> borderPieces = new HashSet<>(borderPiece2Sides.keySet());
      Map<Point, String> q = new HashMap<>();

      long sideCount = 0;
      while (!borderPieces.isEmpty()) { // Refills q, when our border is partitioned (Garden has holes)
        Point starter = borderPieces.iterator().next();
        q.put(starter, "");

        // Go through all border pieces of Garden, taking a random starter, continuing with its immediate neighbours
        while (!q.isEmpty()) {
          Entry<Point, String> e = q.entrySet().iterator().next();
          Point p = e.getKey();
          String previousBorders = e.getValue();
          q.remove(e.getKey());
          borderPieces.remove(p);

          // Number of sides increases with the number of the current border piece
          // minus the sides that the previous piece already had
          String sides = borderPiece2Sides.get(p);
          sideCount += sides.length() - previousBorders.chars().filter(c -> sides.indexOf(c) >= 0).count();

          for (Point nb : p.getNSWENeighbours()) {
            if (borderPieces.contains(nb)) { // Add new unprocessed neighbouring pieces to the queue.
              q.compute(nb, (k, v) -> v == null ? sides //
                  : v + sides // If q already contains the neighbour, append the already counted sides to the filter
              );
            }
          }
        }
      }

      return area * sideCount;
    }

    /**
     * Builds up the entire Garden by growing (floodfill). The coordinates of the Garden will be removed from the given
     * world.
     *
     * @param starter point to begin with
     * @param world the whole world
     */
    void grow(Point starter, Map<Point, Character> world) {
      Set<Point> q = new HashSet<>();
      Set<Point> myPieces = new HashSet<>();
      q.add(starter);

      while (!q.isEmpty()) {
        Point p = q.iterator().next();
        q.remove(p);

        StringBuilder sb = new StringBuilder();
        for (char c : ALLDIRECTIONS.toCharArray()) {
          Point nb = p.add(Point.directionFromChar(c));
          if (world.getOrDefault(nb, '.') == id) {
            if (!myPieces.contains(nb)) {
              q.add(nb);
            }
          } else {
            sb.append(c);
          }
        }
        myPieces.add(p);
        String sides = sb.toString();
        if (!sides.isEmpty()) {
          borderPiece2Sides.put(p, sb.toString());
        }
      }

      area = myPieces.size();
      world.keySet().removeAll(myPieces);
    }
  }

  private static final String ALLDIRECTIONS = "NSWE";

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(1461806, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(887932, result);
  }

  @Test
  public void testA() {
    assertEquals(140, solveA(getTestInput('a')));
    assertEquals(772, solveA(getTestInput('b')));
    assertEquals(1930, solveA(getTestInput('c')));
  }

  @Test
  public void testB() {
    assertEquals(80, solveB(getTestInput('a')));
    assertEquals(436, solveB(getTestInput('b')));
    assertEquals(1206, solveB(getTestInput('c')));
  }

  private long solve(List<String> input, ToLongFunction<Garden> evaluator) {
    Map<Point, Character> world = parseWorld(input);
    List<Garden> gardens = new LinkedList<>();

    while (!world.isEmpty()) {
      Entry<Point, Character> e = world.entrySet().iterator().next();
      Garden g = new Garden(e.getValue(), e.getKey());
      g.grow(e.getKey(), world);
      gardens.add(g);
    }

    return gardens.stream().mapToLong(evaluator).sum();
  }

  private long solveA(List<String> input) {
    return solve(input, Garden::costA);
  }

  private long solveB(List<String> input) {
    return solve(input, Garden::costB);
  }

}
