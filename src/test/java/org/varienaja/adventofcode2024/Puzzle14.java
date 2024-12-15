package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle14 extends PuzzleAbs {
  class Robot {
    Point position;
    Point speed;

    Robot(String line) {
      Pattern p = Pattern.compile("p=(\\d+),(\\d+)\\s+v=(-?\\d+),(-?\\d+)");
      Matcher m = p.matcher(line);
      assert (m.matches());
      position = new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
      speed = new Point(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
    }

    int getQuadrant() {
      int mx = width / 2;
      int my = height / 2;

      if (position.x == mx || position.y == my) {
        return -1;
      }

      if (position.x < mx) {
        if (position.y < my) {
          return 1;
        } else {
          return 2;
        }
      } else {
        if (position.y < my) {
          return 3;
        } else {
          return 4;
        }
      }
    }

    void simulate(long seconds) {
      long newX = position.x + speed.x * seconds;
      long newY = position.y + speed.y * seconds;

      newX %= width;
      newY %= height;

      if (newX < 0) {
        newX += width;
      }
      if (newY < 0) {
        newY += height;
      }
      position = new Point((int)newX, (int)newY);
    }
  }

  private int width = 0;
  private int height = 0;

  @Test
  public void doA() {
    width = 101;
    height = 103;
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(224438715, result);
  }

  @Test
  public void doB() {
    width = 101;
    height = 103;
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(7603, result);
  }

  @Test
  public void testA() {
    width = 11;
    height = 7;
    assertEquals(12, solveA(getStreamingTestInput()));
  }

  private long solveA(Stream<String> input) {
    List<Robot> robots = input.map(Robot::new).collect(Collectors.toList());
    robots.forEach(r -> r.simulate(100));
    Map<Integer, List<Robot>> quadrant2robotCount = robots.stream().collect(Collectors.groupingBy(Robot::getQuadrant));

    long result = 1;
    for (Entry<Integer, List<Robot>> e : quadrant2robotCount.entrySet()) {
      if (e.getKey() >= 0) {
        result *= e.getValue().size();
      }
    }

    return result;
  }

  private long solveB(Stream<String> input) {
    List<Robot> robots = input.map(Robot::new).collect(Collectors.toList());
    robots.forEach(r -> r.simulate(7603));
    // Took a while to see a pattern appear after each 101 seconds, with 28s offset.
    // Then, starting at 28s, with steps of 101s, printing each time, we see a nice tree after 7603s
    for (int i = 0; i < 1 /* 000 */; ++i) {
      Map<Point, Character> world = new HashMap<>();
      robots.forEach(r -> world.put(r.position, '*'));
      System.out.println(i);
      print(world);

      System.out.println();
      System.out.println();
      robots.forEach(r -> r.simulate(101));
    }
    return 7603;
  }

}
