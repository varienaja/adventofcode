package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle12 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput(), 1000);
    System.out.println(sum);
    assertEquals(12351, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(380635029877596L, sum);
  }

  @Test
  public void testA() {
    assertEquals(179, solveA(testInput1(), 10));
    assertEquals(1940, solveA(testInput2(), 100));
  }

  @Test
  public void testB() {
    assertEquals(2772, solveB(testInput1()));
    assertEquals(4686774924L, solveB(testInput2()));
  }

  long lcm(long a, long b) { // Least common multiple
    long lcm = 0;
    while ((lcm += Math.max(a, b)) % Math.min(a, b) != 0) {
      ;
    }
    return lcm;
  }

  private List<int[]> parse(List<String> input) {
    List<int[] /* Pos(x,y,z) Velocity(x,y,z) */> moons = new ArrayList<>();
    // Parse
    for (String line : input) {
      String[] parts = line.replace("<", "").replace(">", "").split(", ");
      int[] moon = new int[] {
          Integer.parseInt(parts[0].split("=")[1]), //
          Integer.parseInt(parts[1].split("=")[1]), //
          Integer.parseInt(parts[2].split("=")[1]), //
          0, 0, 0
      };
      moons.add(moon);
    }
    return moons;
  }

  private void simulate(List<int[]> moons) {
    // 1 Update velocity by applying gravity
    // Consider every pair
    for (int a = 0; a < moons.size(); a++) {
      for (int b = a + 1; b < moons.size(); b++) {
        int[] moonA = moons.get(a);
        int[] moonB = moons.get(b);

        for (int axis = 0; axis < 3; axis++) {
          if (moonA[axis] > moonB[axis]) {
            moonA[axis + 3] -= 1;
            moonB[axis + 3] += 1;
          } else if (moonA[axis] < moonB[axis]) {
            moonA[axis + 3] += 1;
            moonB[axis + 3] -= 1;
          }
        }
      }
    }

    // 2 Update position by applying velocity
    for (int[] moon : moons) {
      for (int axis = 0; axis < 3; axis++) {
        moon[axis] += moon[axis + 3];
      }
    }

  }

  private long solveA(List<String> input, int steps) {
    List<int[] /* Pos(x,y,z) Velocity(x,y,z) */> moons = parse(input);

    for (int i = 0; i < steps; i++) {
      simulate(moons);
    }

    long energy = 0;
    for (int[] moon : moons) {
      energy += (Math.abs(moon[0]) + Math.abs(moon[1]) + Math.abs(moon[2])) * //
          (Math.abs(moon[3]) + Math.abs(moon[4]) + Math.abs(moon[5]));
    }
    return energy;
  }

  private long solveB(List<String> input) {
    List<int[] /* Pos(x,y,z) Velocity(x,y,z) */> moons = parse(input);

    int cycleCount = 0;
    long[] cycles = new long[] {
        0, 0, 0
    };
    Map<Integer, Set<List<Point>>> dimension2State = new HashMap<>();
    dimension2State.put(0, new HashSet<>());
    dimension2State.put(1, new HashSet<>());
    dimension2State.put(2, new HashSet<>());

    long counter = 0;
    while (cycleCount != 3) {
      List<List<Point>> poss = List.of(new LinkedList<Point>(), new LinkedList<Point>(), new LinkedList<Point>());
      for (int axis = 0; axis < 3; axis++) {
        if (cycles[axis] == 0) {
          for (int[] moon : moons) {
            poss.get(axis).add(new Point(moon[axis], moon[axis + 3]));
          }
          if (!dimension2State.get(axis).add(poss.get(axis))) {
            cycles[axis] = counter;
            cycleCount++;
          }
        }
      }
      simulate(moons);
      counter++;
    }

    return Arrays.stream(cycles).reduce(this::lcm).orElse(-1);
  }

  private List<String> testInput1() {
    return List.of( //
        "<x=-1, y=0, z=2>", //
        "<x=2, y=-10, z=-7>", //
        "<x=4, y=-8, z=8>", //
        "<x=3, y=5, z=-1>");
  }

  private List<String> testInput2() {
    return List.of( //
        "<x=-8, y=-10, z=0>", //
        "<x=5, y=5, z=10>", //
        "<x=2, y=-7, z=3>", //
        "<x=9, y=-8, z=-3>");
  }

}
