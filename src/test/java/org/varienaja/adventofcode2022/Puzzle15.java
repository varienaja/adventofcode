package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle15 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput(), 2000000);
    System.out.println(result);
    assertEquals(5335787L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput(), 4000000, 4000000);
    System.out.println(result);
    assertEquals(13673971349056L, result);
  }

  @Test
  public void testA() {
    assertEquals(26L, solveA(getTestInput(), 10));
  }

  @Test
  public void testB() {
    assertEquals(56000011L, solveB(getTestInput(), 20, 20));
  }

  private List<String> getTestInput() {
    return List.of( //
        "Sensor at x=2, y=18: closest beacon is at x=-2, y=15", //
        "Sensor at x=9, y=16: closest beacon is at x=10, y=16", //
        "Sensor at x=13, y=2: closest beacon is at x=15, y=3", //
        "Sensor at x=12, y=14: closest beacon is at x=10, y=16", //
        "Sensor at x=10, y=20: closest beacon is at x=10, y=16", //
        "Sensor at x=14, y=17: closest beacon is at x=10, y=16", //
        "Sensor at x=8, y=7: closest beacon is at x=2, y=10", //
        "Sensor at x=2, y=0: closest beacon is at x=2, y=10", //
        "Sensor at x=0, y=11: closest beacon is at x=2, y=10", //
        "Sensor at x=20, y=14: closest beacon is at x=25, y=17", //
        "Sensor at x=17, y=20: closest beacon is at x=21, y=22", //
        "Sensor at x=16, y=7: closest beacon is at x=15, y=3", //
        "Sensor at x=14, y=3: closest beacon is at x=15, y=3", //
        "Sensor at x=20, y=1: closest beacon is at x=15, y=3");
  }

  private Map<Point, Point> parse(List<String> lines) {
    Map<Point, Point> sensor2beacon = new HashMap<>();

    Pattern p = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");
    for (String line : lines) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        int sx = Integer.parseInt(m.group(1));
        int sy = Integer.parseInt(m.group(2));

        int bx = Integer.parseInt(m.group(3));
        int by = Integer.parseInt(m.group(4));
        sensor2beacon.put(new Point(sx, sy), new Point(bx, by));
      }
    }
    return sensor2beacon;
  }

  private long solveA(List<String> lines, int y) {
    Map<Point, Point> sensor2beacon = parse(lines);

    Set<Integer> impossible = new HashSet<>();
    for (Entry<Point, Point> e : sensor2beacon.entrySet()) {
      Point sensor = e.getKey();
      int d = sensor.manhattanDistance(e.getValue());

      int currentD = sensor.manhattanDistance(new Point(sensor.x, y));
      if (currentD < d) {
        int delta = (d - currentD);
        IntStream.rangeClosed(sensor.x - delta, sensor.x + delta).forEach(impossible::add);
      }
    }

    Set<Integer> beaconsX = sensor2beacon.values().stream().filter(b -> b.y == y).map(b -> b.x).collect(Collectors.toSet());
    impossible.removeAll(beaconsX);
    return impossible.size();
  }

  private long solveB(List<String> lines, int maxX, int maxY) {
    Map<Point, Integer> sensor2beaconDist = parse(lines).entrySet().stream()
        .collect(Collectors.toMap(Entry::getKey, e -> e.getKey().manhattanDistance(e.getValue())));

    // check circumference of all sensors.
    // Better: check around intersections of diagonal lines that form the border around sensors
    for (Entry<Point, Integer> e : sensor2beaconDist.entrySet()) {
      int d = e.getValue();

      for (int dx = -d - 1; dx <= d + 1; dx++) {
        int dy = d + 1 - dx;
        int x = e.getKey().x + dx;
        int y = e.getKey().y + dy;

        if (x >= 0 && x <= maxX && y >= 0 && y <= maxY) {
          Point candidate = new Point(x, y);
          if (sensor2beaconDist.entrySet().stream() //
              .map(e2 -> e2.getKey().manhattanDistance(candidate) > e2.getValue()) //
              .allMatch(b -> b)) {
            return x * 4000000L + y;
          }
        }
      }
    }

    throw new IllegalStateException();
  }

}
