package org.varienaja.adventofcode2023;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2023.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2023">adventofcode.com</a>
 */
public class Puzzle19 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(398527L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(133973513090020L, result);
  }

  @Test
  public void testA() {
    assertEquals(19114L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(167409079868000L, solveB(getTestInput()));
  }

  long execute(Map<String, List<String>> rules, Map<String, Integer> vals, String instr) {
    Iterator<String> it = rules.get(instr).iterator();
    while (it.hasNext()) {
      String instruction = it.next();
      String next = executeInstruction(rules, vals, instruction);
      if ("A".equals(next)) {
        return vals.values().stream().mapToInt(Integer::intValue).sum();
      }
      if ("R".equals(next)) {
        return 0L;
      }
      if (next != null) {
        return execute(rules, vals, next);
      }
    }
    throw new IllegalStateException("??");
  }

  void executeB(Map<String, List<String>> rules, Map<String, List<Map<String, Point>>> values, String instr) {
    Iterator<String> it = rules.get(instr).iterator();
    while (it.hasNext()) {
      String instruction = it.next();
      String next = executeInstructionB(rules, instr, values, instruction);
      if ("A".equals(next)) {
        continue;
      }
      if ("R".equals(next)) {
        continue;
      }
      if (next != null) {
        executeB(rules, values, next);
      }
    }
  }

  String executeInstruction(Map<String, List<String>> rules, Map<String, Integer> vals, String instruction) {
    if (!instruction.contains(":")) {
      return instruction;
    }

    String[] parts = instruction.split(":");
    String[] pts = parts[0].split("\\W");
    if (parts[0].contains(">")) {
      return (vals.get(pts[0]) > Integer.parseInt(pts[1])) ? parts[1] : null;
    }
    if (parts[0].contains("<")) {
      return (vals.get(pts[0]) < Integer.parseInt(pts[1])) ? parts[1] : null;
    }
    return parts[1];
  }

  String executeInstructionB(Map<String, List<String>> rules, String currentInst, Map<String, List<Map<String, Point>>> values, String instruction) {
    List<Map<String, Point>> l = values.get(currentInst);

    if (!instruction.contains(":")) {
      values.computeIfAbsent(instruction, v -> new LinkedList<>()).addAll(l);
      return instruction;
    }

    String[] parts = instruction.split(":");
    String[] pts = parts[0].split("\\W");
    int xx = Integer.parseInt(pts[1]);
    if (parts[0].contains(">")) {
      for (Map<String, Point> m : l) {
        Map<String, Point> mm = new HashMap<>();
        for (Entry<String, Point> e : m.entrySet()) {
          if (pts[0].equals(e.getKey())) { // split
            mm.put(e.getKey(), new Point(xx + 1, e.getValue().y));
            e.setValue(new Point(e.getValue().x, xx));
          } else {
            mm.put(e.getKey(), e.getValue());
          }
        }
        values.computeIfAbsent(parts[1], v -> new LinkedList<>()).add(mm);
      }
    } else if (parts[0].contains("<")) {
      for (Map<String, Point> m : l) {
        Map<String, Point> mm = new HashMap<>();
        for (Entry<String, Point> e : m.entrySet()) {
          if (pts[0].equals(e.getKey())) { // split
            mm.put(e.getKey(), new Point(e.getValue().x, xx - 1));
            e.setValue(new Point(xx, e.getValue().y));
          } else {
            mm.put(e.getKey(), e.getValue());
          }
        }
        values.computeIfAbsent(parts[1], v -> new LinkedList<>()).add(mm);
      }
    }

    return parts[1];
  }

  private long solveA(List<String> lines) {
    boolean parseRule = true;
    long sum = 0;

    Map<String, List<String>> rules = new HashMap<>();
    for (String line : lines) {
      if (line.isEmpty()) {
        parseRule = false;
        continue;
      }

      if (parseRule) {
        String[] parts = line.split("\\{");
        String[] ruless = parts[1].substring(0, parts[1].length() - 1).split(",");
        rules.put(parts[0], List.of(ruless));
      } else {
        Map<String, Integer> vals = new HashMap<>();

        String[] parts = line.substring(1, line.length() - 1).split(",");
        for (String part : parts) {
          String[] pts = part.split("=");
          vals.put(pts[0], Integer.parseInt(pts[1]));
        }

        sum += execute(rules, vals, "in");
      }
    }

    return sum;
  }

  private long solveB(List<String> lines) {
    Map<String, List<String>> rules = new HashMap<>();
    for (String line : lines) {
      if (line.isEmpty()) {
        break;
      }

      String[] parts = line.split("\\{");
      String[] ruless = parts[1].substring(0, parts[1].length() - 1).split(",");
      rules.put(parts[0], List.of(ruless));
    }

    Map<String, List<Map<String, Point>>> values = new HashMap<>();
    values.put("in",
        new LinkedList<>(List.of(new HashMap<>(Map.of("x", new Point(1, 4000), "m", new Point(1, 4000), "a", new Point(1, 4000), "s", new Point(1, 4000))))));
    // Point class misused as a Range
    executeB(rules, values, "in");

    long sum = 0;
    for (Map<String, Point> m : values.get("A")) {
      sum += m.values().stream().mapToLong(p -> 1 + p.y - p.x).reduce((a, b) -> a * b).orElseThrow();
    }

    return sum;
  }

}
