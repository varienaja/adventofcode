package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle15 extends PuzzleAbs {
  private static final boolean DEBUG = false;

  @Test
  public void doA() {
    announceResultA();
    long result = solve(getInput(), true);
    System.out.println(result);
    assertEquals(1465152, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solve(getInput(), false);
    System.out.println(result);
    assertEquals(1511259, result);
  }

  @Test
  public void testA() {
    assertEquals(2028, solve(getTestInput('a'), true));
    assertEquals(10092, solve(getTestInput('b'), true));
  }

  @Test
  public void testB() {
    assertEquals(618, solve(getTestInput('c'), false));
    assertEquals(9021, solve(getTestInput('b'), false));
  }

  boolean tryMove(Map<Point, Character> world, Point where, Point direction) {
    Point dest = where.add(direction);
    char what = world.get(dest);
    if (what == '#') {
      return false;
    } else if (what == 'O' || (direction.y == 0 && (what == '[' || what == ']'))) {
      if (!tryMove(world, dest, direction)) {
        return false;
      }
    } else if (direction.x == 0 && (what == '[' || what == ']')) {
      return tryMoveBigBlock(world, where, direction);
    }

    // what == '.': we can move
    world.put(dest, world.get(where));
    world.put(where, '.');
    return true;
  }

  /**
   * Tries to move big blocks up or down.
   *
   * @param world our world
   * @param where where top push
   * @param direction in what direction to push
   * @return whether the push succeeded. Not that the world is changed, even if the push failed. This dfs search should
   *         better be a bfs.
   */
  boolean tryMoveBigBlock(Map<Point, Character> world, Point where, Point direction) {
    Point dest = where.add(direction);
    char what1 = world.get(dest);
    if (what1 == '#') {
      return false;
    } else if (what1 == '[') { // Try to move right (east) ']' too
      boolean couldMove = tryMoveBigBlock(world, dest, direction) //
          && tryMoveBigBlock(world, dest.add(Point.dEast), direction);
      if (!couldMove) {
        return false;
      }
    } else if (what1 == ']') { // Try to move left (west) '[' too
      boolean couldMove = tryMoveBigBlock(world, dest.add(Point.dWest), direction) //
          && tryMoveBigBlock(world, dest, direction);
      if (!couldMove) {
        return false;
      }
    }

    // what == '.': we can move!
    world.put(dest, world.get(where));
    world.put(where, '.');

    return true;
  }

  private Point find(Map<Point, Character> world, char c) {
    for (Entry<Point, Character> e : world.entrySet()) {
      if (e.getValue() == c) { // Box
        return e.getKey();
      }
    }
    throw new IllegalStateException("No " + c + " in world");
  }

  private long solve(List<String> input, boolean partA) {
    String line = "";
    int i = 0;
    List<String> maze = new LinkedList<>();
    while (!(line = input.get(i++)).isEmpty()) {
      if (partA) {
        maze.add(line);
      } else { // Duplicate stuff horizontally
        StringBuilder sbb = new StringBuilder();
        for (char c : line.toCharArray()) {
          if (c == 'O') {
            sbb.append("[]");
          } else if (c == '@') {
            sbb.append("@.");
          } else {
            sbb.append(c).append(c);
          }
        }
        maze.add(sbb.toString());
      }
    }
    StringBuilder sb = new StringBuilder();
    while (i < input.size()) {
      sb.append(input.get(i++));
    }
    String instructions = sb.toString();
    Map<Point, Character> world = parseWorld(maze);
    Point me = find(world, '@');

    // execute instructions according to rules
    for (char c : instructions.toCharArray()) {
      if (DEBUG) {
        System.out.println(c);
        print(world);
      }
      Point direction = Point.directionFromChar(c);
      Point newMe = me.add(direction);

      Map<Point, Character> old = new HashMap<>(world);
      boolean couldMove = tryMove(world, me, direction);
      if (!couldMove) {
        world = old; // This is only for Part B: We have to undo the changes if moving a big block north or south failed
        continue;
      }

      // empty space: no problem
      world.put(me, '.'); // remove old me
      world.put(newMe, '@'); // place new
      me = newMe;
    }

    return world.entrySet().stream() //
        .filter(e -> "[O".indexOf(e.getValue()) >= 0) //
        .mapToLong(e -> e.getKey().x + 100 * e.getKey().y) //
        .sum();
  }

}
