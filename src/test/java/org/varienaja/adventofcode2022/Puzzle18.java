package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.varienaja.Point3D;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle18 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(3466L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(2012L, result);
  }

  @Test
  public void testA() {
    assertEquals(64, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(58, solveB(getTestInput()));
  }

  private long countNeighbours(Set<Point3D> points, Predicate<Point3D> filter) {
    return points.stream() //
        .flatMap(p -> p.getNSWEUDNeighbours().stream()) //
        .filter(filter) //
        .count();
  }

  private List<String> getTestInput() {
    return List.of( //
        "2,2,2", //
        "1,2,2", //
        "3,2,2", //
        "2,1,2", //
        "2,3,2", //
        "2,2,1", //
        "2,2,3", //
        "2,2,4", //
        "2,2,6", //
        "1,2,5", //
        "3,2,5", //
        "2,1,5", //
        "2,3,5");
  }

  private Set<Point3D> parse(List<String> lines) {
    return lines.stream() //
        .map(line -> line.split(",")) //
        .map(arr -> new Point3D(arr[0], arr[1], arr[2])) //
        .collect(Collectors.toSet());
  }

  private long solveA(List<String> lines) {
    Set<Point3D> points = parse(lines);

    return countNeighbours(points, Predicate.not(points::contains));
  }

  private long solveB(List<String> lines) {
    Set<Point3D> points = parse(lines);
    IntSummaryStatistics stats = points.stream().flatMapToInt(p -> IntStream.of(p.x, p.y, p.z)).summaryStatistics();
    int min = stats.getMin() - 1;
    int max = stats.getMax() + 1;

    // Sweep cube from (min,min,min) to (max,max,max), collect everything that is 'outside' lava (not in points)
    Set<Point3D> outside = new HashSet<>();

    Set<Point3D> toCheck = new HashSet<>();
    toCheck.add(new Point3D(min, min, min));
    while (!toCheck.isEmpty()) {
      Iterator<Point3D> it = toCheck.iterator();
      Point3D oPoint = it.next();
      it.remove();
      outside.add(oPoint);

      oPoint.getNSWEUDNeighbours().stream() //
          .filter(Predicate.not(points::contains)) //
          .filter(nb -> nb.x >= min && nb.x <= max && //
              nb.y >= min && nb.y <= max && //
              nb.z >= min && nb.z <= max) //
          .filter(Predicate.not(outside::contains)) //
          .forEach(toCheck::add);
    }

    return countNeighbours(points, outside::contains);
  }

}
