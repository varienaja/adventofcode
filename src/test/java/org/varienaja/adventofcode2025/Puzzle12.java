package org.varienaja.adventofcode2025;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2025.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2025">adventofcode.com</a>
 */
public class Puzzle12 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(526L, result);
  }

  @Test
  public void testA() {
    assertEquals(1L, solveA(getTestInput()));
  }

  private List<Entry<Point, int[]>> parseFields(List<String> lines) {
    List<Entry<Point, int[]>> fields = new LinkedList<>();
    for (String line : lines) {
      if (line.contains("x")) {
        String[] parts = line.split(": ");
        String[] xy = parts[0].split("x");
        Point field = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));

        String[] amountss = parts[1].split(" ");
        int[] amounts = Stream.of(amountss).mapToInt(Integer::parseInt).toArray();

        fields.add(Map.entry(field, amounts));
      }
    }
    return fields;
  }

  private List<Integer> parseFigures(List<String> lines) {
    List<Integer> figures = new ArrayList<>();
    int size = 0;
    for (String line : lines) {
      if (line.matches("(#|\\.)+")) {
        size += line.replace(".", "").length();
      } else if (line.isEmpty()) {
        figures.add(size);
        size = 0;
      }
    }
    return figures;
  }

  private long solveA(List<String> lines) {
    List<Integer> figures = parseFigures(lines);
    List<Entry<Point, int[]>> fields = parseFields(lines);

    long counter = 0;
    for (Entry<Point, int[]> field : fields) {
      int fieldSize = field.getKey().x * field.getKey().y;

      int packagesize = 0;
      for (int i = 0; i < field.getValue().length; ++i) {
        packagesize += field.getValue()[i] * figures.get(i);
      }
      if (packagesize * 1.3d < fieldSize) {
        counter++;
      }
    }

    return counter;
  }

}
