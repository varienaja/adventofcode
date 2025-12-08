package org.varienaja.adventofcode2025;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point3D;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2025.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2025">adventofcode.com</a>
 */
public class Puzzle08 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solve(getInput(), 1000, true);
    System.out.println(result);
    assertEquals(84968L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solve(getInput(), 0, false);
    System.out.println(result);
    assertEquals(8663467782L, result);
  }

  @Test
  public void testA() {
    assertEquals(40L, solve(getTestInput(), 10, true));
  }

  @Test
  public void testB() {
    assertEquals(25272L, solve(getTestInput(), 0, false));
  }

  private long solve(List<String> lines, int count, boolean partA) {
    Set<Set<Point3D>> circuits = new HashSet<>();

    Set<Point3D> points = new HashSet<>();
    for (String line : lines) {
      String[] parts = line.split(",");
      Point3D p = new Point3D(parts[0], parts[1], parts[2]);
      points.add(p);
      circuits.add(Set.of(p));
    }

    Map<Set<Point3D>, Double> distances = new HashMap<>();
    Iterator<Point3D> pIt = points.iterator();
    while (pIt.hasNext()) {
      Point3D p1 = pIt.next();
      pIt.remove();
      for (Point3D p2 : points) {
        if (!p1.equals(p2)) {
          distances.put(Set.of(p1, p2), p1.euclidianDistance(p2));
        }
      }
    }

    List<Entry<Set<Point3D>, Double>> closestPoints = new LinkedList<>(
        distances.entrySet().stream().sorted((e1, e2) -> Double.compare(e1.getValue(), e2.getValue())).toList());

    int i = 0;
    while (true) {
      Iterator<Entry<Set<Point3D>, Double>> itx = closestPoints.iterator();
      Set<Point3D> closest = itx.next().getKey();
      itx.remove();
      Iterator<Point3D> itc = closest.iterator();
      Point3D c1 = itc.next();
      Point3D c2 = itc.next();

      Set<Point3D> newCircuit = new HashSet<>();
      Iterator<Set<Point3D>> it = circuits.iterator();
      while (it.hasNext()) {
        Set<Point3D> candidate = it.next();
        if (candidate.contains(c1) || candidate.contains(c2)) {
          it.remove();
          newCircuit.addAll(candidate);
        }
      }

      if (circuits.isEmpty()) { // We're connecting the last remaining disjunct circuits
        return (long)c1.x * (long)c2.x;
      }

      circuits.add(newCircuit);
      ++i;
      if (partA && i == count) {
        List<Set<Point3D>> sorted = circuits.stream().sorted((a1, a2) -> Integer.compare(a2.size(), a1.size())).toList();
        return sorted.get(0).size() * sorted.get(1).size() * sorted.get(2).size();
      }
    }
  }

}
