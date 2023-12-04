package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle11 extends PuzzleAbs {
  class Monkey {
    Queue<Long> items = new LinkedList<>();
    long tester;
    int monkeyFalse;
    int monkeyTrue;
    Function<Long, Long> operation;
    long inspections;

    Monkey(List<String> lines, int offset) {
      inspections = 0;

      String s = lines.get(offset + 1);
      String[] startItems = s.split(": ")[1].split(", ");
      Stream.of(startItems).mapToLong(Long::parseLong).forEach(items::add);

      s = lines.get(offset + 2);
      if (s.contains("old +")) {
        int p = Integer.parseInt(s.substring(s.indexOf("old + ") + 6));
        operation = o -> o + p;
      } else if (s.contains("old * old")) {
        operation = o -> o * o;
      } else if (s.contains("old *")) {
        int p = Integer.parseInt(s.substring(s.indexOf("old * ") + 6));
        operation = o -> o * p;
      }

      s = lines.get(offset + 3);
      tester = Integer.parseInt(s.substring(s.indexOf("by ") + 3));

      s = lines.get(offset + 4);
      monkeyTrue = Integer.parseInt(s.substring(s.indexOf("monkey ") + 7));

      s = lines.get(offset + 5);
      monkeyFalse = Integer.parseInt(s.substring(s.indexOf("monkey ") + 7));
    }

    void inspect() {
      while (!items.isEmpty()) {
        long item = items.poll();
        item = operation.apply(item);

        if (lcm == 1) {
          item /= 3L;
        } else {
          item %= lcm;
        }
        if (item % tester == 0L) {
          monkeys[monkeyTrue].items.offer(item);
        } else {
          monkeys[monkeyFalse].items.offer(item);
        }
        inspections++;
      }
    }
  }

  long lcm;
  private Monkey[] monkeys;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(54054L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(14314925001L, result);
  }

  @Test
  public void testA() {
    assertEquals(10605L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(2713310158L, solveB(getTestInput()));
  }

  @Override
  protected List<String> getTestInput() {
    return List.of( //
        "Monkey 0:", //
        "  Starting items: 79, 98", //
        "  Operation: new = old * 19", //
        "  Test: divisible by 23", //
        "    If true: throw to monkey 2", //
        "    If false: throw to monkey 3", //
        "", //
        "Monkey 1:", //
        "  Starting items: 54, 65, 75, 74", //
        "  Operation: new = old + 6", //
        "  Test: divisible by 19", //
        "    If true: throw to monkey 2", //
        "    If false: throw to monkey 0", //
        "", //
        "Monkey 2:", //
        "  Starting items: 79, 60, 97", //
        "  Operation: new = old * old", //
        "  Test: divisible by 13", //
        "    If true: throw to monkey 1", //
        "    If false: throw to monkey 3", //
        "", //
        "Monkey 3:", //
        "  Starting items: 74", //
        "  Operation: new = old + 3", //
        "  Test: divisible by 17", //
        "    If true: throw to monkey 0", //
        "    If false: throw to monkey 1" //
    );
  }

  private long solve(List<String> lines, int count, boolean partA) {
    lines = new LinkedList<>(lines);
    lines.add(""); // Make input length divisible by 7
    monkeys = new Monkey[lines.size() / 7];
    for (int m = 0; m < lines.size() / 7; ++m) {
      monkeys[m] = new Monkey(lines, 7 * m);
    }

    lcm = 1;
    if (!partA) {
      // In part b, numbers become too big to handle.
      // but we only care about divisibility by all combined testers.
      // so, if we do everything modulo the product of all testers,
      // we keep that divisibility property, and our result (amount
      // of inspections) remains correct
      for (Monkey m : monkeys) {
        lcm *= m.tester;
      }
    }

    for (int r = 0; r < count; r++) {
      for (Monkey m : monkeys) {
        m.inspect();
      }
    }

    NavigableSet<Long> inspects = Stream.of(monkeys).map(m -> m.inspections).collect(Collectors.toCollection(TreeSet::new));
    return inspects.pollLast() * inspects.pollLast();
  }

  private long solveA(List<String> lines) {
    return solve(lines, 20, true);
  }

  private long solveB(List<String> lines) {
    return solve(lines, 10000, false);
  }

}
