package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
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
public class Puzzle10 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    System.out.println();
    solveA(getInput(), true); // ZZCBGGCJ
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(10886, sum);
  }

  private long solveA(List<String> input, boolean print) {
    List<Point[]> point2velocity = new LinkedList<>();
    for (String line : input) {
      int ixStart = line.indexOf('<');
      int ixEnd = line.indexOf('>');
      String point = line.substring(ixStart + 1, ixEnd).trim();
      String[] parts = point.split(",\\s+");
      int x = Integer.parseInt(parts[0]);
      int y = Integer.parseInt(parts[1]);
      Point p = new Point(x, y);

      ixStart = line.lastIndexOf('<');
      ixEnd = line.lastIndexOf('>');
      point = line.substring(ixStart + 1, ixEnd).trim();
      parts = point.split(",\\s+");
      x = Integer.parseInt(parts[0]);
      y = Integer.parseInt(parts[1]);
      Point v = new Point(x, y);
      point2velocity.add(new Point[] {
          p, v
      });
    }

    int sizeH = Integer.MAX_VALUE;
    int sizeV = Integer.MAX_VALUE;
    long i = 0;
    while (true) {
      i++;
      int minX = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int minY = Integer.MAX_VALUE;
      int maxY = Integer.MIN_VALUE;
      for (Point[] pv : point2velocity) {
        int newX = pv[0].x + pv[1].x;
        int newY = pv[0].y + pv[1].y;
        pv[0] = new Point(newX, newY);
        minX = Math.min(minX, newX);
        maxX = Math.max(maxX, newX);
        minY = Math.min(minY, newY);
        maxY = Math.max(maxY, newY);
      }
      int newsizeH = maxX - minX;
      int newsizeV = maxY - minY;
      if (newsizeH > sizeH && newsizeV > sizeV) { // We grew! Get previous situation back and print
        i--;
        minX = Integer.MAX_VALUE;
        maxX = Integer.MIN_VALUE;
        minY = Integer.MAX_VALUE;
        maxY = Integer.MIN_VALUE;
        for (Point[] pv : point2velocity) {
          int newX = pv[0].x - pv[1].x;
          int newY = pv[0].y - pv[1].y;
          pv[0] = new Point(newX, newY);
          minX = Math.min(minX, newX);
          maxX = Math.max(maxX, newX);
          minY = Math.min(minY, newY);
          maxY = Math.max(maxY, newY);
        }

        if (print) {
          Set<Point> values = point2velocity.stream().map(pv -> pv[0]).collect(Collectors.toSet());
          for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
              Point p = new Point(x, y);
              System.out.print(values.contains(p) ? '#' : '.');
            }
            System.out.println();
          }
        }
        break;
      }
      sizeH = newsizeH;
      sizeV = newsizeV;
    }

    return i;
  }

  private long solveB(List<String> input) {
    return solveA(input, false);
  }

  @Test
  public void testA() {
    assertEquals(3, solveA(testInput(), false));
  }

  @Test
  public void testB() {
    assertEquals(3, solveB(testInput()));
  }

  private List<String> testInput() {
    return List.of( //
        "position=< 9,  1> velocity=< 0,  2>", //
        "position=< 7,  0> velocity=<-1,  0>", //
        "position=< 3, -2> velocity=<-1,  1>", //
        "position=< 6, 10> velocity=<-2, -1>", //
        "position=< 2, -4> velocity=< 2,  2>", //
        "position=<-6, 10> velocity=< 2, -2>", //
        "position=< 1,  8> velocity=< 1, -1>", //
        "position=< 1,  7> velocity=< 1,  0>", //
        "position=<-3, 11> velocity=< 1, -2>", //
        "position=< 7,  6> velocity=<-1, -1>", //
        "position=<-2,  3> velocity=< 1,  0>", //
        "position=<-4,  3> velocity=< 2,  0>", //
        "position=<10, -3> velocity=<-1,  1>", //
        "position=< 5, 11> velocity=< 1, -2>", //
        "position=< 4,  7> velocity=< 0, -1>", //
        "position=< 8, -2> velocity=< 0,  1>", //
        "position=<15,  0> velocity=<-2,  0>", //
        "position=< 1,  6> velocity=< 1,  0>", //
        "position=< 8,  9> velocity=< 0, -1>", //
        "position=< 3,  3> velocity=<-1,  1>", //
        "position=< 0,  5> velocity=< 0, -1>", //
        "position=<-2,  2> velocity=< 2,  0>", //
        "position=< 5, -2> velocity=< 1,  2>", //
        "position=< 1,  4> velocity=< 2,  1>", //
        "position=<-2,  7> velocity=< 2, -2>", //
        "position=< 3,  6> velocity=<-1, -1>", //
        "position=< 5,  0> velocity=< 1,  0>", //
        "position=<-6,  0> velocity=< 2,  0>", //
        "position=< 5,  9> velocity=< 1, -2>", //
        "position=<14,  7> velocity=<-2,  0>", //
        "position=<-3,  6> velocity=< 2, -1>");
  }

}
