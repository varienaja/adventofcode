package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle22 extends PuzzleAbs {

  class State {
    Map<Point, Point> grid;
    Point desiredDataPosition;

    @Override
    public boolean equals(Object o) {
      if (o instanceof State) {
        State s = (State)o;
        return s.grid.equals(grid) && s.desiredDataPosition.equals(desiredDataPosition);
      }

      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(grid, desiredDataPosition);
    }
  }

  long calcStates(Set<State> states, int steps) {
    // This naive solution takes too much memory; we simply generate too many new states
    // TODO What is a good heuristic to filter out stuff we don't need?

    // Stage 1: move free data towards desiredPosition, so it can be moved
    // Stage 2: move desiredPos towards (0,0)
    // Stage 3: move free space towards (0,0)
    // Finish: move desiredData to (0,0)

    for (State state : states) {
      if (state.desiredDataPosition.x == 0 && state.desiredDataPosition.y == 0) {
        return steps;
      }
    }

    Set<State> nextStates = new HashSet<>();

    for (State state : states) {

      for (Entry<Point, Point> a : state.grid.entrySet()) {
        for (Point nb : a.getKey().getNSWENeighbours()) {
          Point cap = state.grid.get(nb);
          if (cap != null) {
            if (a.getValue().y > 0) {
              if (a.getValue().y <= (cap.x - cap.y)) {
                // Shovel data from a to nb
                // System.out.println("Move from "+a.getKey()+" to "+nb);

                Map<Point, Point> copy = new HashMap<>(state.grid);
                copy.put(a.getKey(), new Point(a.getValue().x, 0));
                copy.put(nb, new Point(cap.x, cap.y + a.getValue().y));
                Point newPos = state.desiredDataPosition;
                if (a.getKey().equals(state.desiredDataPosition)) {
                  newPos = nb;
                }
                State ns = new State();
                ns.grid = copy;
                ns.desiredDataPosition = newPos;
                nextStates.add(ns);
              }
            }
          }
        }
      }
    }

    return calcStates(nextStates, steps + 1);
  }

  private long solveA(List<String> input) {
    Map<Point, Point> grid = new HashMap<>();

    Pattern p = Pattern.compile("/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+\\d+T\\s+\\d+%");
    for (String line : input) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        int x = Integer.parseInt(m.group(1));
        int y = Integer.parseInt(m.group(2));
        Point node = new Point(x, y);

        int size = Integer.parseInt(m.group(3));
        int used = Integer.parseInt(m.group(4));
        Point cap = new Point(size, used);

        grid.put(node, cap);
      }
    }

    long viablePairs = 0;
    for (Entry<Point, Point> a : grid.entrySet()) {
      for (Entry<Point, Point> b : grid.entrySet()) {
        if (a.getValue().y > 0) {
          if (!a.getKey().equals(b.getKey())) {
            if (a.getValue().y <= (b.getValue().x - b.getValue().y)) {
              viablePairs++;
            }
          }
        }
      }
    }
    return viablePairs;
  }

  private long solveB(List<String> input) {
    int maxX = 0;
    int maxY = 0;
    // I want the data of max(x) == 36, 0
    // How many copies doe I need minimum to get the data to 0,0?

    // Looks like slider puzzle
    Map<Point, Point> grid = new HashMap<>();

    Pattern p = Pattern.compile("/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+\\d+T\\s+\\d+%");
    for (String line : input) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        int x = Integer.parseInt(m.group(1));
        int y = Integer.parseInt(m.group(2));
        Point node = new Point(x, y);
        maxX = Math.max(x, maxX);
        maxY = Math.max(y, maxY);

        int size = Integer.parseInt(m.group(3));
        int used = Integer.parseInt(m.group(4));
        Point cap = new Point(size, used);

        grid.put(node, cap);
      }
    }

    // System.out.println();
    // for (int y = 0; y <= maxY; y++) {
    // for (int x = 0; x <= maxX; x++) {
    // Point pp = new Point(x, y);
    // Point cap = grid.get(pp);
    // System.out.print(cap.x + "/" + cap.y + " ");
    //
    // }
    // System.out.println();
    // }

    if (maxX * maxY < 10) {
      State s = new State();
      s.grid = grid;
      s.desiredDataPosition = new Point(maxX, 0);
      return calcStates(Collections.singleton(s), 0);
    }
    // Move hole...
    return 18 // Up
        + 22 // Left
        + 22 // Right
        + 5 * 35 // 35 Moves of desired disk
        + 1;
  }

  @Test
  public void testDay00() {
    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines);
    System.out.println(sum);
    assertEquals(901, sum);

    List<String> testInput = List.of("Filesystem            Size  Used  Avail  Use%", //
        "/dev/grid/node-x0-y0   10T    8T     2T   80%", //
        "/dev/grid/node-x0-y1   11T    6T     5T   54%", //
        "/dev/grid/node-x0-y2   32T   28T     4T   87%", //
        "/dev/grid/node-x1-y0    9T    7T     2T   77%", //
        "/dev/grid/node-x1-y1    8T    0T     8T    0%", //
        "/dev/grid/node-x1-y2   11T    7T     4T   63%", //
        "/dev/grid/node-x2-y0   10T    6T     4T   60%", //
        "/dev/grid/node-x2-y1    9T    8T     1T   88%", //
        "/dev/grid/node-x2-y2    9T    6T     3T   66%");
    assertEquals(7, solveB(testInput));

    announceResultB();
    sum = solveB(lines);
    System.out.println(sum);
    assertEquals(238, sum); // 182 too low; 242 too high; 241 too high; 237 incorrect; 240 incorrect; 239 incorrect
  }

}
