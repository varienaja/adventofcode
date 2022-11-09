package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle17 extends PuzzleAbs {
  private static final boolean DEBUG = false;
  private static final Character WALL = Character.valueOf('#');

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(31934, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(24790, sum);
  }

  private Map<Point, Character> solve(List<String> input) {
    Character wall = Character.valueOf('#');
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    int maxX = Integer.MIN_VALUE;
    int minX = Integer.MAX_VALUE;
    Map<Point, Character> grid = new HashMap<>();
    Pattern pattern = Pattern.compile("\\w=(\\d+),\\s\\w=(\\d+)..(\\d+)");
    for (String line : input) {
      Matcher m = pattern.matcher(line);
      if (m.matches()) {
        if (line.startsWith("x")) {
          int x = Integer.parseInt(m.group(1));
          for (int y = Integer.parseInt(m.group(2)); y <= Integer.parseInt(m.group(3)); y++) {
            Point p = new Point(x, y);
            grid.put(p, '#');
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
          }
        } else {
          int y = Integer.parseInt(m.group(1));
          for (int x = Integer.parseInt(m.group(2)); x <= Integer.parseInt(m.group(3)); x++) {
            Point p = new Point(x, y);
            grid.put(p, '#');
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
          }
        }
      }
    }
    // Allow for water to flow left and right of the left- and rightmost buckets
    minX--;
    maxX++;

    // Now fill water from (500,minY), until we detect water at y==maxY
    int depthreached = 0;
    Set<Point> newWaterAt = new TreeSet<>((p1, p2) -> {
      int result = Integer.compare(p1.y, p2.y);
      if (result == 0) {
        result = Integer.compare(p2.x, p1.x);
      }
      return result;
    });
    newWaterAt.add(new Point(500, minY));
    grid.put(new Point(500, minY), 'w');
    boolean waterAtBottom = false;
    while (!waterAtBottom) {
      Iterator<Point> it = newWaterAt.iterator();
      while (it.hasNext()) {
        Point p = it.next();
        if (minX <= p.x && p.x <= maxX && minY <= p.y && p.y <= maxY) {
          grid.put(p, 'w');
        } else {
          it.remove();
        }
        depthreached = Math.max(depthreached, p.y);
      }

      if (!newWaterAt.isEmpty()) {
        Point p = newWaterAt.iterator().next();
        if (grid.get(p.getSouth()) == null) { // trickle down
          Point s = p.getSouth();
          newWaterAt.add(s);
        } else { // trickle sideways where possible
          Set<Point> addedWater = new HashSet<>();
          addedWater.add(p);

          boolean bucketOverflow = false;
          Point s = p;
          while (grid.get(s.getEast()) == null || grid.get(s.getEast()) == 'w') {
            s = s.getEast();
            Character south = grid.get(s.getSouth());
            if (south == null) {
              Point w = s.getWest();
              Character prevS = grid.get(w.getSouth());
              if (wall.equals(prevS)) { // if previous south was '#': overflow, but only not already overflown
                if (grid.get(s) == null) {
                  newWaterAt.add(s);
                  bucketOverflow = true;
                }
              } else {
                bucketOverflow = true;
              }
              break;
            }
            grid.put(s, 'w');
            addedWater.add(s);
          }
          s = p;
          while (grid.get(s.getWest()) == null || grid.get(s.getWest()) == 'w') {
            s = s.getWest();
            Character south = grid.get(s.getSouth());
            if (south == null) {
              Point w = s.getEast();
              Character prevS = grid.get(w.getSouth());
              if (wall.equals(prevS)) { // if previous south was '#': overflow, but only not already overflown
                if (grid.get(s) == null) {
                  newWaterAt.add(s);
                  bucketOverflow = true;
                }
              } else {
                bucketOverflow = true;
              }
              break;
            }
            grid.put(s, 'w');
            addedWater.add(s);
          }
          if (!bucketOverflow) {
            newWaterAt.add(p.getNorth());
            addedWater.stream().forEach(still -> grid.put(still, '~'));
          }
        }

        newWaterAt.remove(p);
      }

      waterAtBottom = depthreached > maxY && newWaterAt.isEmpty();
    }

    if (DEBUG) {
      try {
        File f = new File("/home/varienaja/test.txt");
        PrintStream fout = new PrintStream(new FileOutputStream(f));
        for (int y = minY; y <= maxY; y++) {
          for (int x = minX; x <= maxX; x++) {
            Character c = grid.get(new Point(x, y));
            fout.print(c == null ? '.' : c);
          }
          fout.println();
        }
        fout.flush();
        fout.close();
      } catch (Exception e) { // Ignore
      }
    }

    return grid;
  }

  private long solveA(List<String> input) {
    Map<Point, Character> grid = solve(input);
    return grid.values().stream().filter(v -> !WALL.equals(v)).count();
  }

  private long solveB(List<String> input) {
    Map<Point, Character> grid = solve(input);
    Character tilde = Character.valueOf('~');
    return grid.values().stream().filter(v -> tilde.equals(v)).count();
  }

  @Test
  public void testA() {
    assertEquals(57, solveA(testInput()));
  }

  @Test
  public void testB() {
    assertEquals(29, solveB(testInput()));
  }

  private List<String> testInput() {
    return List.of( //
        "x=495, y=2..7", //
        "y=7, x=495..501", //
        "x=501, y=3..7", //
        "x=498, y=2..4", //
        "x=506, y=1..2", //
        "x=498, y=10..13", //
        "x=504, y=10..13", //
        "y=13, x=498..504");
  }
}
