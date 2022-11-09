
package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle20 extends PuzzleAbs {

  class PVA {
    int i;
    Triple p;
    Triple v;
    Triple a;

    PVA(int i, String line) {
      this.i = i;
      String[] parts = line.split(", ");
      p = new Triple(parts[0].substring(2));
      v = new Triple(parts[1].substring(2));
      a = new Triple(parts[2].substring(2));
    }

    @Override
    public String toString() {
      return "Particle" + i;
    }
  }

  static class Triple {
    public static Triple add(Triple p, Triple v) {
      return new Triple(p.x + v.x, p.y + v.y, p.z + v.z);
    }

    int x;
    int y;
    int z;

    Triple(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    Triple(String input) {
      String[] parts = input.replace("<", "").replace(">", "").split(",");
      x = Integer.parseInt(parts[0]);
      y = Integer.parseInt(parts[1]);
      z = Integer.parseInt(parts[2]);
    }

    int dist() {
      return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Triple) {
        Triple t = (Triple)o;
        return t.x == x && t.y == y && t.z == z;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
      return "(" + x + "," + y + "," + z + ")";
    }
  }

  private long solveA(List<String> input) {
    PVA[] particles = new PVA[input.size()];
    for (int i = 0; i < input.size(); i++) {
      particles[i] = new PVA(i, input.get(i));
    }

    int minV = Integer.MAX_VALUE;
    int ixSlowest = -1;

    for (int i = 0; i < particles.length; i++) {
      PVA p = particles[i];

      p.v.x += 10000 * p.a.x;
      p.v.y += 10000 * p.a.y;
      p.v.z += 10000 * p.a.z;

      if (p.v.dist() < minV) {
        minV = p.v.dist();
        ixSlowest = i;
      }
    }

    return ixSlowest;
  }

  private long solveB(List<String> input) {
    Set<PVA> particles = new HashSet<>();
    for (int i = 0; i < input.size(); i++) {
      particles.add(new PVA(i, input.get(i)));
    }

    Map<Triple, List<PVA>> pos2Count = new HashMap<>();
    for (PVA p : particles) {
      List<PVA> l = new LinkedList<>();
      l.add(p);
      pos2Count.put(p.p, l);
    }

    for (int t = 0; t < 100; t++) {
      for (PVA p : particles) {
        p.v = Triple.add(p.v, p.a);
        pos2Count.getOrDefault(p.p, new LinkedList<>()).remove(p);

        p.p = Triple.add(p.p, p.v);
        List<PVA> l = pos2Count.get(p.p);
        if (l == null) {
          l = new LinkedList<>();
          pos2Count.put(p.p, l);
        }
        l.add(p);
      }

      Iterator<Entry<Triple, List<PVA>>> it = pos2Count.entrySet().iterator();
      while (it.hasNext()) {
        Entry<Triple, List<PVA>> e = it.next();
        int sz = e.getValue().size();
        if (sz > 1) {
          particles.removeAll(e.getValue());
          it.remove();
        } else if (sz == 0) {
          it.remove();
        }
      }

      // System.out.println("t: " + t + " particles: " + particles.size());
    }

    return particles.size();
  }

  @Test
  public void testDay19() {
    List<String> testInput = List.of( //
        "p=<3,0,0>, v=<2,0,0>, a=<-1,0,0>", //
        "p=<4,0,0>, v=<0,0,0>, a=<-2,0,0>");
    assertEquals(0, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(243, result);

    testInput = List.of( //
        "p=<-6,0,0>, v=<3,0,0>, a=<0,0,0>", //
        "p=<-4,0,0>, v=<2,0,0>, a=<0,0,0>", //
        "p=<-2,0,0>, v=<1,0,0>, a=<0,0,0>", //
        "p=<3,0,0>, v=<-1,0,0>, a=<0,0,0>");
    assertEquals(1, solveB(testInput));
    announceResultB();
    long stepCount = solveB(lines);
    System.out.println(stepCount);
    assertEquals(648, stepCount);
  }

}
