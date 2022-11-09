package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle13 extends PuzzleAbs {
  class Cart implements Comparable<Cart> {
    int ix;
    Point position;
    int direction;
    int turnTo = 0;

    Cart(int ix, int x, int y, char c) {
      this.ix = ix;
      position = new Point(x, y);
      if (c == '>') {
        direction = 1;
      } else if (c == '<') {
        direction = 3;
      } else if (c == '^') {
        direction = 0;
      } else if (c == 'v') {
        direction = 2;
      }
    }

    @Override
    public int compareTo(Cart o) {
      int result = Integer.compare(position.y, o.position.y);
      if (result == 0) {
        result = Integer.compare(position.x, o.position.x);
      }
      return result;
    }

    Point move(Map<Point, Character> grid) {
      // move one in 'direction'. If grid has a / \; turn, | go further n or s; - go further e or w
      // on +, do turnOnIntersection
      if (direction == 0) { // n
        position = position.getNorth();
      } else if (direction == 1) { // e
        position = position.getEast();
      } else if (direction == 2) { // s
        position = position.getSouth();
      } else if (direction == 3) { // w
        position = position.getWest();
      }
      char road = grid.get(position);
      if (road == '+') {
        turnOnIntersection();
      } else if (road == '\\') {
        if (direction == 0) { // n
          direction = 3;
        } else if (direction == 1) { // e
          direction = 2;
        } else if (direction == 2) { // s
          direction = 1;
        } else if (direction == 3) { // w
          direction = 0;
        }
      } else if (road == '/') {
        if (direction == 0) { // n
          direction = 1;
        } else if (direction == 1) { // e
          direction = 0;
        } else if (direction == 2) { // s
          direction = 3;
        } else if (direction == 3) { // w
          direction = 2;
        }
      }
      return position;
    }

    void turnOnIntersection() {
      if (turnTo == 0) { // left
        direction--;
      } else if (turnTo == 2) { // right
        direction++;
      }
      if (direction < 0) {
        direction += 4;
      } else if (direction >= 4) {
        direction -= 4;
      }
      turnTo++;
      if (turnTo > 2) {
        turnTo -= 3;
      }
    }

  }

  @Test
  public void doA() {
    announceResultA();
    Point p = solveA(getInput());
    System.out.println(p);
    assertEquals(new Point(83, 49), p);
  }

  @Test
  public void doB() {
    announceResultB();
    Point p = solveB(getInput());
    System.out.println(p);
    assertEquals(new Point(73, 36), p);
  }

  private Point solve(List<String> input, boolean puzzleA) {
    int cIx = 1;
    List<Cart> carts = new LinkedList<>();
    Map<Point, Character> grid = new HashMap<>();
    for (int y = 0; y < input.size(); y++) {
      String line = input.get(y);
      for (int x = 0; x < line.length(); x++) {
        char c = line.charAt(x);
        if (c != ' ') {
          if ("<>^v".indexOf(c) != -1) {
            carts.add(new Cart(cIx++, x, y, c));
            if ("<>".indexOf(c) != -1) {
              grid.put(new Point(x, y), '-');
            } else {
              grid.put(new Point(x, y), '|');
            }
          } else {
            grid.put(new Point(x, y), c);
          }
        }
      }
    }

    List<Cart> toRemove = new LinkedList<>();
    while (true) {
      Set<Point> cartPositions = carts.stream().map(c -> c.position).collect(Collectors.toSet());
      Collections.sort(carts);

      toRemove.clear();
      for (Cart cart : carts) {
        cartPositions.remove(cart.position);
        Point newPos = cart.move(grid);
        if (!cartPositions.add(newPos)) {
          if (puzzleA) {
            return newPos;
          } else {
            for (Cart candidate : carts) {
              if (candidate.position.equals(newPos)) {
                toRemove.add(candidate);
              }
            }
          }
        }
      }

      carts.removeAll(toRemove);
      if (carts.size() == 1) {
        return carts.iterator().next().position;
      }
    }
  }

  private Point solveA(List<String> input) {
    return solve(input, true);
  }

  private Point solveB(List<String> input) {
    return solve(input, false);
  }

  @Test
  public void testA() {
    assertEquals(new Point(7, 3), solveA(testInputA()));
  }

  @Test
  public void testB() {
    assertEquals(new Point(6, 4), solveB(testInputB()));
  }

  private List<String> testInputA() {
    return List.of( //
        "/->-\\        ", //
        "|   |  /----\\", //
        "| /-+--+-\\  |", //
        "| | |  | v  |", //
        "\\-+-/  \\-+--/", //
        "  \\------/   ");
  }

  private List<String> testInputB() {
    return List.of( //
        "/>-<\\  ", //
        "|   |  ", //
        "| /<+-\\", //
        "| | | v", //
        "\\>+</ |", //
        "  |   ^", //
        "  \\<->/");
  }

}
