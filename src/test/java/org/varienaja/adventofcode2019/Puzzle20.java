package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle20 extends PuzzleAbs {
  private static final boolean DEBUG = false;
  private Map<Point, Character> world;
  private Map<Point, Point> passages;
  private Map<String, Point> passage2dot;
  private Set<Point> inner;
  private Set<Point> outer;

  private int minX = 0;
  private int maxX = 0;
  private int minY = 0;
  private int maxY = 0;
  private int maxLevel = 0;

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(548, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(6452, sum);
  }

  private long flood(Point start, Point target, Map<Point, Point> gateways) {
    long steps = 0;

    Set<Point> visited = new HashSet<>();
    Queue<Set<Point>> queue = new LinkedList<>();
    queue.add(Collections.singleton(start));
    while (!queue.isEmpty()) {
      Set<Point> pts = queue.poll();
      if (pts.isEmpty()) {
        break;
      }
      if (pts.contains(target)) {
        break;
      }

      Set<Point> nxt = new HashSet<>();
      for (Point p : pts) {
        Set<Point> next = p.getNSWENeighbours().stream() //
            .filter(nb -> !visited.contains(nb)) //
            .filter(nb -> world.get(nb) == '.') //
            .collect(Collectors.toSet());
        for (Point possible : next) {
          visited.add(possible);
          nxt.add(possible);
        }
        Point teleport = gateways.get(p);
        if (teleport != null && !visited.contains(teleport)) {
          visited.add(teleport);
          nxt.add(teleport);
        }
      }
      if (!nxt.isEmpty()) {
        queue.add(nxt);
        steps++;
      }

      if (DEBUG) {
        for (int y = minY; y <= maxY; y++) {
          for (int x = minX; x <= maxX; x++) {
            Point xy = new Point(x, y);
            if (visited.contains(xy)) {
              System.out.print("o");
            } else {
              System.out.print(world.getOrDefault(xy, ' '));
            }
          }
          System.out.println();
        }
        System.out.println();
      }

    }

    return steps;
  }

  private long floodB(Point start, Point target, Map<Point, Point> gateways) {
    // if level == 0, only outside points AA and ZZ work.
    // otherwise: inner point goes a level deeper
    // outer point go a level higher
    // only ZZ in level 0 is an exit
    // So: visited contains a level for each point. and the queue too --> create small class for level,x,y?
    long steps = 0;

    class Pt {
      Point p;
      int level;

      Pt(Point start, int level) {
        p = start;
        this.level = level;
        maxLevel = Math.max(maxLevel, level);
      }

      @Override
      public boolean equals(Object o) {
        if (o instanceof Pt) {
          Pt other = (Pt)o;
          return p.equals(other.p) && level == other.level;
        }
        return false;
      }

      @Override
      public int hashCode() {
        return Objects.hash(p, level);
      }
    }

    Set<Pt> visited = new HashSet<>();
    Queue<Set<Pt>> queue = new LinkedList<>();
    queue.add(Collections.singleton(new Pt(start, 0)));
    while (!queue.isEmpty()) {
      Set<Pt> pts = queue.poll();
      if (pts.isEmpty()) {
        break;
      }
      if (pts.contains(new Pt(target, 0))) {
        break;
      }

      Set<Pt> nxt = new HashSet<>();
      for (Pt p : pts) {
        Set<Point> next = p.p.getNSWENeighbours().stream() //
            .filter(nb -> !visited.contains(new Pt(nb, p.level))) //
            .filter(nb -> world.get(nb) == '.') //
            .collect(Collectors.toSet());
        for (Point possible : next) {
          visited.add(new Pt(possible, p.level));
          nxt.add(new Pt(possible, p.level));
        }

        if (gateways.get(p.p) != null) {
          Pt teleportOut = null;
          if (p.level == 0 && inner.contains(p.p)) {
            teleportOut = new Pt(gateways.get(p.p), p.level + 1);
          } else if (p.level > 0) {
            if (inner.contains(p.p)) {
              teleportOut = new Pt(gateways.get(p.p), p.level + 1);
            } else {
              teleportOut = new Pt(gateways.get(p.p), p.level - 1);
            }
          }
          if (teleportOut != null && !visited.contains(teleportOut)) {
            visited.add(teleportOut);
            nxt.add(teleportOut);
          }
        }
      }
      if (!nxt.isEmpty()) {
        queue.add(nxt);
        steps++;
      }

      if (DEBUG) {
        for (int l = 0; l <= maxLevel; l++) {
          System.out.println(l);
          for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
              Point xy = new Point(x, y);
              if (visited.contains(new Pt(xy, l))) {
                System.out.print("o");
              } else {
                System.out.print(world.getOrDefault(xy, ' '));
              }
            }
            System.out.println();
          }
          System.out.println();
        }
        System.out.println();
      }

    }

    return steps;
  }

  private void parse(List<String> input) {
    passages = new HashMap<>();
    passage2dot = new HashMap<>();
    world = new HashMap<>();

    for (int y = 0; y < input.size(); y++) {
      String line = input.get(y);
      for (int x = 0; x < line.length(); x++) {
        minX = Math.min(x, minX);
        maxX = Math.max(x, maxX);
        minY = Math.min(y, minY);
        maxY = Math.max(y, maxY);

        world.put(new Point(x, y), line.charAt(x));
      }
    }

    Set<Point> toSkip = new HashSet<>();

    for (int y = 0; y < input.size(); y++) {
      String line = input.get(y);
      for (int x = 0; x < line.length(); x++) {
        if (!toSkip.contains(new Point(x, y))) {
          char c = line.charAt(x);
          if (c >= 'A' && c <= 'Z') {
            Point nb = new Point(x + 1, y);
            Point dot = null;
            char cc = world.getOrDefault(nb, ' ');
            if (cc >= 'A' && cc <= 'Z') { // We found two letters horizontally
              toSkip.add(new Point(x, y));
              toSkip.add(new Point(x + 1, y));
              // Is the dot to the left or to the right?
              if (world.getOrDefault(new Point(x + 2, y), ' ') == '.') { // dot to the right
                dot = new Point(x + 2, y);
              } else { // dot to the left
                dot = new Point(x - 1, y);
              }
            } else { // Must be vertically
              cc = world.getOrDefault(new Point(x, y + 1), ' ');
              toSkip.add(new Point(x, y));
              toSkip.add(new Point(x, y + 1));
              if (world.getOrDefault(new Point(x, y + 2), ' ') == '.') { // dot to the bottom
                dot = new Point(x, y + 2);
              } else { // dot to top
                dot = new Point(x, y - 1);
              }
            }
            String key = "" + c + cc;
            Point entry = passage2dot.remove(key);
            if (entry == null) {
              passage2dot.put("" + c + cc, dot);
            } else {
              passages.put(dot, entry);
              passages.put(entry, dot);
            }
          }
        }
      }
    }
  }

  private long solveA(List<String> input) {
    // Parse world
    // mark certain point with their labels (make pairs of Points)
    // floodfill from AA until we reach ZZ
    // So.. detecting the two letter-pairs is the most difficult task :-D

    parse(input);

    // AA and ZZ are in passage2Dot; all other gateways are in 'passages'.
    // We now start flooding the maze, starting at AA. We want to know the amount of steps until ZZ
    return flood(passage2dot.get("AA"), passage2dot.get("ZZ"), passages);
  }

  private long solveB(List<String> input) {
    parse(input);

    inner = new HashSet<>();
    outer = new HashSet<>();
    for (Entry<Point, Point> e : passages.entrySet()) {
      Point p = e.getKey();
      if (Math.abs(p.x - minX) <= 2 || Math.abs(p.x - maxX) <= 2 || //
          Math.abs(p.y - minY) <= 2 || Math.abs(p.y - maxY) <= 2) {
        outer.add(p);
      } else {
        inner.add(p);
      }
    }
    // Divide gateway points by inner- and outer points.
    // keep level next to visited, rest is all the same

    // if point is less that 2 distance from minx,miny or maxy, maxy we have an outer point
    // everything else is an inner point
    return floodB(passage2dot.get("AA"), passage2dot.get("ZZ"), passages);
  }

  @Test
  public void testA() {
    assertEquals(23, solveA(testInput1()));
    assertEquals(58, solveA(testInput2()));
  }

  @Test
  public void testB() {
    assertEquals(26, solveB(testInput1()));
  }

  private List<String> testInput1() {
    return List.of( //
        "         A           ", //
        "         A           ", //
        "  #######.#########  ", //
        "  #######.........#  ", //
        "  #######.#######.#  ", //
        "  #######.#######.#  ", //
        "  #######.#######.#  ", //
        "  #####  B    ###.#  ", //
        "BC...##  C    ###.#  ", //
        "  ##.##       ###.#  ", //
        "  ##...DE  F  ###.#  ", //
        "  #####    G  ###.#  ", //
        "  #########.#####.#  ", //
        "DE..#######...###.#  ", //
        "  #.#########.###.#  ", //
        "FG..#########.....#  ", //
        "  ###########.#####  ", //
        "             Z       ", //
        "             Z       ");
  }

  private List<String> testInput2() {
    return List.of( //
        "                   A               ", //
        "                   A               ", //
        "  #################.#############  ", //
        "  #.#...#...................#.#.#  ", //
        "  #.#.#.###.###.###.#########.#.#  ", //
        "  #.#.#.......#...#.....#.#.#...#  ", //
        "  #.#########.###.#####.#.#.###.#  ", //
        "  #.............#.#.....#.......#  ", //
        "  ###.###########.###.#####.#.#.#  ", //
        "  #.....#        A   C    #.#.#.#  ", //
        "  #######        S   P    #####.#  ", //
        "  #.#...#                 #......VT", //
        "  #.#.#.#                 #.#####  ", //
        "  #...#.#               YN....#.#  ", //
        "  #.###.#                 #####.#  ", //
        "DI....#.#                 #.....#  ", //
        "  #####.#                 #.###.#  ", //
        "ZZ......#               QG....#..AS", //
        "  ###.###                 #######  ", //
        "JO..#.#.#                 #.....#  ", //
        "  #.#.#.#                 ###.#.#  ", //
        "  #...#..DI             BU....#..LF", //
        "  #####.#                 #.#####  ", //
        "YN......#               VT..#....QG", //
        "  #.###.#                 #.###.#  ", //
        "  #.#...#                 #.....#  ", //
        "  ###.###    J L     J    #.#.###  ", //
        "  #.....#    O F     P    #.#...#  ", //
        "  #.###.#####.#.#####.#####.###.#  ", //
        "  #...#.#.#...#.....#.....#.#...#  ", //
        "  #.#####.###.###.#.#.#########.#  ", //
        "  #...#.#.....#...#.#.#.#.....#.#  ", //
        "  #.###.#####.###.###.#.#.#######  ", //
        "  #.#.........#...#.............#  ", //
        "  #########.###.###.#############  ", //
        "           B   J   C               ", //
        "           U   P   P               ");
  }

}
