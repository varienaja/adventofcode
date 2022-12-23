package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.Point.Direction;
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

  private List<String> getTestInput() {
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

  private Direction mapDirection(int facing) {
    if (facing == 0) {
      return Direction.east;
    }
    if (facing == 1) {
      return Direction.south;
    }
    if (facing == 2) {
      return Direction.west;
    }
    return Direction.north;
  }

  private int mapFace(Direction d) {
    if (d == Direction.east) {
      return 0;
    }
    if (d == Direction.west) {
      return 2;
    }
    if (d == Direction.north) {
      return 3;
    }
    return 1;
  }

  private Direction opposite(Direction d) {
    if (d == Direction.north) {
      return Direction.south;
    } else if (d == Direction.south) {
      return Direction.north;
    } else if (d == Direction.west) {
      return Direction.east;
    }
    return Direction.west;
  }

  private long solveA(List<String> lines) {
    // Facing is 0 for right (>), 1 for down (v), 2 for left (<), and 3 for up (^).
    Map<Point, Character> maze = new HashMap<>();

    boolean firstPoint = true;

    Point myPos = new Point(1, 1);
    int myFacing = 0;
    Direction d = Direction.east;

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
          Direction op = opposite(d);
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
    Direction d = Direction.east;

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
            d = Direction.west;
            wrapPos = new Point(99, 149 - myPos.y);
          } else if (nextPos.y < 0) { // D or E
            if (nextPos.x >= 100) { // Eup
              wrapPos = new Point(myPos.x - 100, 199);
            } else { // Dup
              wrapPos = new Point(0, myPos.x + 100);
              d = Direction.east;
            }
          } else if (nextPos.x < 0) { // D or G
            if (nextPos.y >= 150) { // Dwest
              wrapPos = new Point(myPos.y - 100, 0);
              d = Direction.south;
            } else { // Gwest
              wrapPos = new Point(50, 149 - myPos.y);
              d = Direction.east;
            }
          } else if (nextPos.y < 100 && nextPos.x < 50) { // J or G
            if (myPos.y < 50) { // Gwest2
              wrapPos = new Point(0, 149 - myPos.y);
              d = Direction.east;
            } else { // Jwest
              if (d == Direction.west) {
                wrapPos = new Point(myPos.y - 50, 100);
                d = Direction.south;
              } else { // Jnorth
                wrapPos = new Point(50, myPos.x + 50);
                d = Direction.east;
              }
            }
          } else if (nextPos.y >= 150 && nextPos.x >= 50) { // Csouth / Ceast
            if (d == Direction.south) { // Csouth
              wrapPos = new Point(49, myPos.x + 100);
              d = Direction.west;
            } else { // Ceast
              wrapPos = new Point(myPos.y - 100, 149);
              d = Direction.north;
            }
          } else if (nextPos.y >= 200) { // Esouth
            wrapPos = new Point(myPos.x + 100, 0);
          } else if (nextPos.x >= 100 && nextPos.y >= 50) { // Asouth, Aeast, beast
            if (nextPos.y >= 100) { // Beast2
              d = Direction.west;
              wrapPos = new Point(149, 149 - myPos.y);
            } else if (d == Direction.east) { // Aeast
              d = Direction.north;
              wrapPos = new Point(myPos.y + 50, 49);
            } else { // aSouth
              d = Direction.west;
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
