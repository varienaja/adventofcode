package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle22 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(3590L, result); // not 3589
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(86382L, result); // 36434 too low. 90238 too high
  }

  @Test
  public void testA() {
    assertEquals(6032L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
  }

  @Override
  protected List<String> getTestInput() {
    return List.of( //
        "        ...#", //
        "        .#..", //
        "        #...", //
        "        ....", //
        "...#.......#", //
        "........#...", //
        "..#....#....", //
        "..........#.", //
        "        ...#....", //
        "        .....#..", //
        "        .#......", //
        "        ......#.", //
        "", //
        "10R5L5R10L4R5L5");
  }

  private Point mapDirection(int facing) {
    if (facing == 0) {
      return Point.dEast;
    }
    if (facing == 1) {
      return Point.dSouth;
    }
    if (facing == 2) {
      return Point.dWest;
    }
    return Point.dNorth;
  }

  private int mapFace(Point d) {
    if (Point.dEast.equals(d)) {
      return 0;
    }
    if (Point.dWest.equals(d)) {
      return 2;
    }
    if (Point.dNorth.equals(d)) {
      return 3;
    }
    return 1;
  }

  private Point opposite(Point d) {
    if (Point.dNorth.equals(d)) {
      return Point.dSouth;
    } else if (Point.dSouth.equals(d)) {
      return Point.dNorth;
    } else if (Point.dWest.equals(d)) {
      return Point.dEast;
    }
    return Point.dWest;
  }

  private long solveA(List<String> lines) {
    // Facing is 0 for right (>), 1 for down (v), 2 for left (<), and 3 for up (^).
    Map<Point, Character> maze = new HashMap<>();

    boolean firstPoint = true;

    Point myPos = new Point(1, 1);
    int myFacing = 0;
    Point d = Point.dEast;

    int y = 0;
    boolean parsingMaze = true;
    Iterator<String> it = lines.iterator();
    while (it.hasNext() && parsingMaze) {
      String line = it.next();
      parsingMaze = !line.isEmpty();
      if (parsingMaze) {
        for (int x = 0; x < line.length(); x++) {
          char c = line.charAt(x);
          if (c == '.') {
            maze.put(new Point(x, y), c);
            if (firstPoint) {
              myPos = new Point(x, y);
              firstPoint = false;
            }
          } else if (c == '#') {
            maze.put(new Point(x, y), c);
          }
        }
      }
      ++y;
    }

    String route = it.next();

    int ix = 0;
    while (ix < route.length()) {
      char c;
      int steps = 0;
      // parse number
      do {
        c = route.charAt(ix++);
        if (Character.isDigit(c)) {
          steps = steps * 10 + Integer.parseInt("" + c);
        }
      } while (ix < route.length() && Character.isDigit(c));

      // move 'steps' in the current facing
      boolean ok = true;
      while (ok && steps > 0) {
        Point nextPos = myPos.add(d);
        Character m = maze.get(nextPos);
        if (m == null) { // wrap around..
          // opposite direction...
          // find until no point found, if stone: don't move
          Point wrapPos = new Point(myPos.x, myPos.y);
          Point op = opposite(d);
          char wrap = ' ';
          do {
            wrap = maze.get(wrapPos);
            wrapPos = wrapPos.add(op);
          } while (maze.containsKey(wrapPos));

          if (wrap == '.') { // DOit
            myPos = wrapPos.add(opposite(op));
          } else { // BLocked: don't wrap
            ok = false;
          }
        } else if (m == '.') {
          myPos = myPos.add(d);
        } else if (m == '#') {
          ok = false;
        }
        --steps;
      }
      if (ix < route.length()) {
        if (c == 'R') {
          myFacing++;
          if (myFacing > 3) {
            myFacing = 0;
          }
        } else { // L
          myFacing--;
          if (myFacing < 0) {
            myFacing = 3;
          }
        }
        d = mapDirection(myFacing);
      }
    }

    // The final password is the sum of
    // 1000 times the row, 4 times the column, and the facing.
    // Travel though maze
    return 1000L * (1 + myPos.y) + 4 * (1 + myPos.x) + myFacing;
  }

  private long solveB(List<String> lines) {

    // Facing is 0 for right (>), 1 for down (v), 2 for left (<), and 3 for up (^).
    Map<Point, Character> maze = new HashMap<>();

    boolean firstPoint = true;

    Point myPos = new Point(1, 1);
    int myFacing = 0;
    Point d = Point.dEast;

    int y = 0;
    boolean parsingMaze = true;
    Iterator<String> it = lines.iterator();
    while (it.hasNext() && parsingMaze) {
      String line = it.next();
      parsingMaze = !line.isEmpty();
      if (parsingMaze) {
        for (int x = 0; x < line.length(); x++) {
          char c = line.charAt(x);
          if (c == '.') {
            maze.put(new Point(x, y), c);
            if (firstPoint) {
              myPos = new Point(x, y);
              firstPoint = false;
            }
          } else if (c == '#') {
            maze.put(new Point(x, y), c);
          }
        }
      }
      ++y;
    }

    String route = it.next();

    int ix = 0;
    while (ix < route.length()) {
      char c;
      int steps = 0;
      // parse number
      do {
        c = route.charAt(ix++);
        if (Character.isDigit(c)) {
          steps = steps * 10 + Integer.parseInt("" + c);
        }
      } while (ix < route.length() && Character.isDigit(c));

      // move 'steps' in the current facing
      boolean ok = true;
      while (ok && steps > 0) {
        Point nextPos = myPos.add(d);
        Character m = maze.get(nextPos);
        if (m == null) { // wrap around.. TYPE B
          Point wrapPos = null;
          if (nextPos.x >= 150) { // Beast
            d = Point.dWest;
            wrapPos = new Point(99, 149 - myPos.y);
          } else if (nextPos.y < 0) { // D or E
            if (nextPos.x >= 100) { // Eup
              wrapPos = new Point(myPos.x - 100, 199);
            } else { // Dup
              wrapPos = new Point(0, myPos.x + 100);
              d = Point.dEast;
            }
          } else if (nextPos.x < 0) { // D or G
            if (nextPos.y >= 150) { // Dwest
              wrapPos = new Point(myPos.y - 100, 0);
              d = Point.dSouth;
            } else { // Gwest
              wrapPos = new Point(50, 149 - myPos.y);
              d = Point.dEast;
            }
          } else if (nextPos.y < 100 && nextPos.x < 50) { // J or G
            if (myPos.y < 50) { // Gwest2
              wrapPos = new Point(0, 149 - myPos.y);
              d = Point.dEast;
            } else { // Jwest
              if (d == Point.dWest) {
                wrapPos = new Point(myPos.y - 50, 100);
                d = Point.dSouth;
              } else { // Jnorth
                wrapPos = new Point(50, myPos.x + 50);
                d = Point.dEast;
              }
            }
          } else if (nextPos.y >= 150 && nextPos.x >= 50) { // Csouth / Ceast
            if (d == Point.dSouth) { // Csouth
              wrapPos = new Point(49, myPos.x + 100);
              d = Point.dWest;
            } else { // Ceast
              wrapPos = new Point(myPos.y - 100, 149);
              d = Point.dNorth;
            }
          } else if (nextPos.y >= 200) { // Esouth
            wrapPos = new Point(myPos.x + 100, 0);
          } else if (nextPos.x >= 100 && nextPos.y >= 50) { // Asouth, Aeast, beast
            if (nextPos.y >= 100) { // Beast2
              d = Point.dWest;
              wrapPos = new Point(149, 149 - myPos.y);
            } else if (d == Point.dEast) { // Aeast
              d = Point.dNorth;
              wrapPos = new Point(myPos.y + 50, 49);
            } else { // aSouth
              d = Point.dWest;
              wrapPos = new Point(99, myPos.x - 50);
            }
          }

          // if stone: don't move
          Character wrap = maze.get(wrapPos);
          if (wrap == null) {
            System.out.println("panic" + wrapPos);
          }

          if (wrap == '.') { // DOit
            myPos = wrapPos;
            myFacing = mapFace(d);
          } else { // BLocked: don't wrap
            ok = false;
            d = mapDirection(myFacing);
          }

        } else if (m == '.') {
          myPos = myPos.add(d);
        } else if (m == '#') {
          ok = false;
        }
        --steps;
      }
      if (ix < route.length()) {
        if (c == 'R') {
          myFacing++;
          if (myFacing > 3) {
            myFacing = 0;
          }
        } else { // L
          myFacing--;
          if (myFacing < 0) {
            myFacing = 3;
          }
        }
        d = mapDirection(myFacing);
      }
    }

    // The final password is the sum of
    // 1000 times the row, 4 times the column, and the facing.
    // Travel though maze
    return 1000L * (1 + myPos.y) + 4 * (1 + myPos.x) + myFacing;
  }

}
