package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.Point;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle15 extends PuzzleAbs {

  private void findShortestPath(Map<Point, Long> p2dist, Point start, List<String> lines, Point dest) {
    Set<Point> todoSet = new HashSet<>();
    Set<Point> unvisited = new HashSet<>(p2dist.keySet());

    Queue<Point> todo = new PriorityQueue<>((p1, p2) -> Long.compare(p2dist.get(p1), p2dist.get(p2)));
    todo.add(start);

    while (!todo.isEmpty()) {
      Point current = todo.poll();
      todoSet.remove(current);

      long mycost = p2dist.get(current);

      Set<Point> nbs = current.getNSWENeighbours().stream().filter(unvisited::contains).collect(Collectors.toSet());
      for (Point nb : nbs) {
        long curdist = p2dist.get(nb);
        long newdist = mycost + Long.valueOf("" + lines.get(nb.y).charAt(nb.x));

        if (newdist < curdist) {
          p2dist.put(nb, newdist);
        }
      }
      nbs.stream().filter(todoSet::add).forEach(todo::offer);
      unvisited.remove(current);
    }
  }

  private void printGrid(Map<Point, Long> p2dist, List<String> lines) {
    int maxY = p2dist.keySet().stream().mapToInt(p -> p.y).max().orElse(0);
    int maxX = p2dist.keySet().stream().mapToInt(p -> p.x).max().orElse(0);
    System.out.println();
    for (int y = 0; y <= maxY; y++) {
      for (int x = 0; x <= maxX; x++) {
        System.out.print(String.format("%4d", p2dist.get(new Point(x, y))));
        System.out.print("(");
        System.out.print("" + lines.get(y).charAt(x));
        System.out.print(")");
        System.out.print(",");
      }
      System.out.println();
    }

    System.out.println();
  }

  private long solveA(List<String> lines, boolean showGrid) {
    Map<Point, Long> p2dist = new HashMap<>();
    for (int y = 0; y < lines.size(); y++) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); x++) {
        p2dist.put(new Point(x, y), Long.MAX_VALUE);
      }
    }

    Point start = new Point(0, 0);
    p2dist.put(start, 0L);
    Point dest = new Point(lines.get(0).length() - 1, lines.size() - 1);

    findShortestPath(p2dist, start, lines, dest);

    if (showGrid) {
      printGrid(p2dist, lines);
    }

    return p2dist.get(dest);
  }

  private long solveB(List<String> lines, boolean showGrid) {
    // Stretch vertically
    List<String> newLinesA = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      final int add = i;
      for (String line : lines) {
        newLinesA.add(line.chars().map(c -> {
          int r = c + add;
          if (r > '9') {
            r -= 9;
          }
          return r;
        }).mapToObj(Character::toString).collect(Collectors.joining()));
      }
    }

    // Stretch horizontally
    List<String> newLines = new ArrayList<>();
    for (String line : newLinesA) {
      StringBuilder sb = new StringBuilder();

      for (int i = 0; i < 5; i++) {
        final int add = i;
        sb.append(line.chars().map(c -> {
          int r = c + add;
          if (r > '9') {
            r -= 9;
          }
          return r;
        }).mapToObj(Character::toString).collect(Collectors.joining()));
      }
      newLines.add(sb.toString());
    }

    return solveA(newLines, showGrid);

  }

  @Test
  public void testDay15() {
    List<String> testInput = Arrays.asList(//
        "1163751742", //
        "1381373672", //
        "2136511328", //
        "3694931569", //
        "7463417111", //
        "1319128137", //
        "1359912421", //
        "3125421639", //
        "1293138521", //
        "2311944581");
    assertEquals(40, solveA(testInput, false));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines, false);
    System.out.println(result);
    assertEquals(745, result);

    assertEquals(315, solveB(testInput, false));
    announceResultB();
    result = solveB(lines, false);
    System.out.println(result);
    assertEquals(3002, result);
  }

}
