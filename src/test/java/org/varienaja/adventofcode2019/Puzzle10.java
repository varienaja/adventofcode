package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle10 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(280, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(706, sum);
  }

  private int gcd(int n1, int n2) {
    if (n2 == 0) {
      return n1;
    }
    return gcd(n2, n1 % n2);
  }

  private Map<Point, Integer> solve(List<String> input) {
    int h = input.size();
    int w = input.get(0).length();
    Map<Point, Integer> asteroid2visible = new HashMap<>();

    for (int y = 0; y < h; y++) {
      String line = input.get(y);
      for (int x = 0; x < w; x++) {
        char c = line.charAt(x);
        if (c == '#') { // Count visible asteroids from here
          int visible = 0;
          Set<Point> dirSeen = new HashSet<>();
          // so we decide which directions are possible. these are:
          // (dx,dy) for dx in -x ... w-x, dy in -y..h+y

          // System.out.print(new Point(x, y) + ": ");

          for (int dy = -y; dy < h - y; dy++) {
            for (int dx = -x; dx < w - x; dx++) {
              if (dy == 0 && dx == 0) {
                continue;
              }

              Point dir = new Point(dx, dy);
              if (dy == 0) {
                dir = new Point(Integer.signum(dx), 0);
              } else if (dx == 0) {
                dir = new Point(0, Integer.signum(dy));
              } else {
                int gcd = gcd(dx, dy);
                dir = new Point(Integer.signum(dx) * Math.abs(dx / gcd), Integer.signum(dy) * Math.abs(dy / gcd));
              }
              if (dirSeen.add(dir)) {
                // System.out.print(dir);
                int d = 1;
                int xx;
                int yy;
                boolean asteroidFound = false;
                do {
                  xx = x + d * dir.x;
                  yy = y + d * dir.y;
                  if (0 <= xx && xx < w && 0 <= yy && yy < h) {
                    asteroidFound = input.get(yy).charAt(xx) == '#';
                  } else {
                    break;
                  }
                  d++;
                } while (0 <= xx && xx < w && 0 <= yy && yy < h && !asteroidFound);
                if (asteroidFound) {
                  visible++;
                  // System.out.print("* ");
                } else {
                  // System.out.print(" ");
                }
              }
            }
          }

          asteroid2visible.put(new Point(x, y), visible);
          // System.out.println();
        }
      }
    }

    // System.out.println(asteroid2visible);
    return asteroid2visible;
  }

  private long solveA(List<String> input) {
    Map<Point, Integer> asteroid2visible = solve(input);
    return asteroid2visible.values().stream().mapToInt(i -> i).max().orElse(-1);
  }

  private long solveB(List<String> input) {
    int h = input.size();
    int w = input.get(0).length();

    Map<Point, Integer> asteroid2visible = solve(input);
    Point laser = asteroid2visible.entrySet().stream().max(Comparator.comparing(e -> e.getValue())).get().getKey();

    // Generate all direction clockwise
    NavigableSet<Point> dirSeen = new TreeSet<>(Comparator.comparing(Point::angle));
    for (int dy = -laser.y; dy < h - laser.y; dy++) {
      for (int dx = -laser.x; dx < w - laser.x; dx++) {
        if (dy == 0 && dx == 0) {
          continue;
        }

        Point dir = new Point(dx, dy);
        if (dy == 0) {
          dir = new Point(Integer.signum(dx), 0);
        } else if (dx == 0) {
          dir = new Point(0, Integer.signum(dy));
        } else {
          int gcd = gcd(dx, dy);
          dir = new Point(Integer.signum(dx) * Math.abs(dx / gcd), Integer.signum(dy) * Math.abs(dy / gcd));
        }
        dirSeen.add(dir);
      }
    }

    Set<Point> asteroidCoords = new HashSet<>();
    for (int y = 0; y < h; y++) {
      String line = input.get(y);
      for (int x = 0; x < w; x++) {
        if (line.charAt(x) == '#') {
          asteroidCoords.add(new Point(x, y));
        }
      }
    }

    // Repeatedly vaporize asteroids clockwise, until we've hit the 200th asteroid
    Point asteroid = null;
    int vaporizedAsteroids = 0;
    Iterator<Point> it = dirSeen.iterator();
    while (vaporizedAsteroids < 200) {
      if (!it.hasNext()) {
        it = dirSeen.iterator();
      }
      Point dir = it.next();

      int d = 1;
      int xx;
      int yy;
      asteroid = null;
      do {
        xx = laser.x + d * dir.x;
        yy = laser.y + d * dir.y;
        if (0 <= xx && xx < w && 0 <= yy && yy < h) {
          asteroid = new Point(xx, yy);
          if (!asteroidCoords.contains(asteroid)) {
            asteroid = null;
          }
        } else {
          break;
        }
        d++;
      } while (0 <= xx && xx < w && 0 <= yy && yy < h && asteroid == null);
      if (asteroid != null) {
        vaporizedAsteroids++;
        asteroidCoords.remove(asteroid);
        // System.out.println(vaporizedAsteroids + " vaporized at: " + asteroid);
      }
    }

    return asteroid.x * 100 + asteroid.y;
  }

  @Test
  public void testA() {
    assertEquals(8, solveA(testInput1()));
    assertEquals(33, solveA(testInput2()));
    assertEquals(210, solveA(testInput3()));
  }

  @Test
  public void testAngles() {
    assertEquals(-1.57, new Point(0, -3).angle(), 0.01D);
    assertEquals(-.785, new Point(3, -3).angle(), 0.01D);
    assertEquals(-.643, new Point(4, -3).angle(), 0.01D);
    assertEquals(0D, new Point(5, 0).angle(), 0.01D);
    assertEquals(.643, new Point(4, 3).angle(), 0.01D);
    assertEquals(.785, new Point(3, 3).angle(), 0.01D);
    assertEquals(1.47, new Point(1, 10).angle(), 0.01D);
    assertEquals(1.56, new Point(1, 100).angle(), 0.01D);
    assertEquals(1.57, new Point(0, 100).angle(), 0.01D);

    assertEquals(1.58, new Point(-1, 100).angle(), 0.01D);
    assertEquals(1.67, new Point(-1, 10).angle(), 0.01D);
    assertEquals(3.14, new Point(-3, 0).angle(), 0.01D);
    assertEquals(3.92, new Point(-3, -3).angle(), 0.01D);
  }

  @Test
  public void testB() {
    assertEquals(802, solveB(testInput3()));
  }

  private List<String> testInput1() {
    return List.of( //
        ".#..#", //
        ".....", //
        "#####", //
        "....#", //
        "...##");
  }

  private List<String> testInput2() {
    return List.of( //
        "......#.#.", //
        "#..#.#....", //
        "..#######.", //
        ".#.#.###..", //
        ".#..#.....", //
        "..#....#.#", //
        "#..#....#.", //
        ".##.#..###", //
        "##...#..#.", //
        ".#....####");
  }

  private List<String> testInput3() {
    return List.of( //
        ".#..##.###...#######", //
        "##.############..##.", //
        ".#.######.########.#", //
        ".###.#######.####.#.", //
        "#####.##.#.##.###.##", //
        "..#####..#.#########", //
        "####################", //
        "#.####....###.#.#.##", //
        "##.#################", //
        "#####.##.###..####..", //
        "..######..##.#######", //
        "####.##.####...##..#", //
        ".#####..#.######.###", //
        "##...#.##########...", //
        "#.##########.#######", //
        ".####.#.###.###.#.##", //
        "....##.##.###..#####", //
        ".#.#.###########.###", //
        "#.#.#.#####.####.###", //
        "###.##.####.##.#..##");
  }

}
