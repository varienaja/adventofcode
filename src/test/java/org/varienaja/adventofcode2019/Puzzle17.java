package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
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
public class Puzzle17 extends PuzzleAbs {
  private static final boolean DEBUG = false;
  private Map<Point, Character> world;
  private Set<Point> cleaned;
  private Point botPos;

  private int cleanScaffold(int direction) {
    int cleans = 0;
    boolean ok = true;

    do {
      Point toTry = null;
      if (direction == 0) { // n
        toTry = botPos.getNorth();
      } else if (direction == 1) { // w
        toTry = botPos.getWest();
      } else if (direction == 2) { // s
        toTry = botPos.getSouth();
      } else if (direction == 3) { // e
        toTry = botPos.getEast();
      }

      if (world.getOrDefault(toTry, '.') == '#') {
        cleans++;
        cleaned.add(toTry);
        botPos = toTry;
      } else {
        ok = false;
      }
    } while (ok);

    return cleans;
  }

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals(6052, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    solveA(getInputString());
    long sum = solveB(getInputString());
    System.out.println(sum);
    assertEquals(752491, sum);
  }

  private boolean isScaffold(Point myPos, int direction) {
    Point toTry = null;
    if (direction == 0) { // n
      toTry = myPos.getNorth();
    } else if (direction == 1) { // w
      toTry = myPos.getWest();
    } else if (direction == 2) { // s
      toTry = myPos.getSouth();
    } else if (direction == 3) { // e
      toTry = myPos.getEast();
    }

    return world.getOrDefault(toTry, '.') == '#';
  }

  private long solveA(String input) {
    world = new HashMap<>();

    BlockingQueue<Long> in = new LinkedBlockingDeque<>();
    BlockingQueue<Long> out = new LinkedBlockingDeque<>();
    try {
      Intcode.run(input, Map.of(), in, out).get();
    } catch (Exception e) {
      e.printStackTrace();
    }

    int maxX = 0;
    int maxY = 0;

    Point p = new Point(0, 0);

    if (DEBUG) {
      System.out.println();
    }
    for (long l : out) {
      if (l == 35L) {
        world.put(p, '#');
        p = p.getEast();
        maxX = Math.max(maxX, p.x);
        if (DEBUG) {
          System.out.print('#');
        }
      } else if (l == 46L) {
        world.put(p, '.');
        p = p.getEast();
        maxX = Math.max(maxX, p.x);
        if (DEBUG) {
          System.out.print('.');
        }
      } else if (l == 10L) {
        p = new Point(0, p.y + 1);
        maxY = Math.max(maxY, p.y);
        if (DEBUG) {
          System.out.println();
        }
      } else {
        botPos = p;
        world.put(p, (char)(int)l);
        p = p.getEast();
        if (DEBUG) {
          System.out.print((char)(int)l);
        }
      }
    }
    maxY--;
    maxY--;

    // Get intersections
    long result = 0;
    for (int y = 1; y < maxY; y++) {
      for (int x = 1; x < maxX; x++) {
        p = new Point(x, y);
        if (world.get(p) == '#') {
          boolean isIntersection = true;
          for (Point nb : p.getNSWENeighbours()) {
            if (world.get(nb) != '#') {
              isIntersection = false;
              break;
            }
          }
          if (isIntersection) {
            result += p.x * p.y;
          }
        }
      }
    }

    return result;
  }

  private long solveB(String input) {
    String dirs = "^<v>"; // nwse
    boolean turning = true;
    int direction = dirs.indexOf(world.get(botPos));

    List<String> moves = new LinkedList<>();
    // check value of world(botPos)
    cleaned = new HashSet<>();
    Set<Point> scaffolds = world.entrySet().stream().filter(e -> e.getValue() == '#').map(e -> e.getKey()).collect(Collectors.toSet());
    while (!cleaned.equals(scaffolds)) {
      // State = turning; check around bot, turn L or R to point nose to scaffold
      if (turning) {
        // try L
        int dir = (direction + 1) % dirs.length();
        if (isScaffold(botPos, dir)) {
          moves.add("L");
          turning = false;
        } else { // try R
          dir = (direction + 3) % dirs.length();
          moves.add("R");
          turning = false;
        }
        direction = dir;
      }

      // then, state = !turning; move until '.' detected
      int cleans = cleanScaffold(direction);
      moves.add("" + cleans);
      turning = true;
    }

    // String ms = moves.stream().collect(Collectors.joining(",")) + ",";

    // Now I have to bundle stuff in A,B,C such that no one is longer than 20 bytes

    // System.out.println(ms);
    //
    // System.out.println(ms.replace("L,6,R,12,L,4,L,6,", "A"));
    // System.out.println(ms.replace("L,6,R,12,L,4,L,6,", "A").replace("R,6,L,6,R,12,", "B"));
    // System.out.println(ms.replace("L,6,R,12,L,4,L,6,", "A").replace("R,6,L,6,R,12,",
    // "B").replace("L,6,L,10,L,10,R,6,", "C"));

    String main = "A,B,B,C,A,B,C,A,B,C"; // I did this by hand... :-D
    String fA = "L,6,R,12,L,4,L,6";
    String fB = "R,6,L,6,R,12";
    String fC = "L,6,L,10,L,10,R,6";

    BlockingQueue<Long> in = new LinkedBlockingDeque<>();
    for (String s : new String[] {
        main, fA, fB, fC, "n"
    }) {
      for (char c : s.toCharArray()) {
        in.offer((long)c);
      }
      in.offer(10L);
    }

    BlockingQueue<Long> out = new LinkedBlockingDeque<>();
    try {
      Intcode.run(input, Map.of(0, 2L), in, out).get();
    } catch (Exception e) {
      e.printStackTrace();
    }

    long lastL = 0;
    if (DEBUG) {
      System.out.println();
    }
    Point p = new Point(0, 0);
    for (long l : out) {
      lastL = l;
      if (l == 35L) {
        p = p.getEast();
        if (DEBUG) {
          System.out.print('#');
        }
      } else if (l == 46L) {
        p = p.getEast();
        if (DEBUG) {
          System.out.print('.');
        }
      } else if (l == 10L) {
        p = new Point(0, p.y + 1);
        if (DEBUG) {
          System.out.println();
        }
      } else {
        p = p.getEast();
        if (DEBUG) {
          System.out.print((char)(int)l);
        }
      }
    }

    return lastL;
  }

}
