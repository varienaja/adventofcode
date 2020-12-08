package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle08 extends PuzzleAbs {

  private int alterLine(List<String> modLines, int lineToChange) {
    for (int i = lineToChange; i < modLines.size(); i++) {
      if (modLines.get(i).startsWith("jmp")) {
        modLines.set(i, modLines.get(i).replace("jmp", "nop"));
        return i + 1;
      } else if (modLines.get(i).startsWith("nop")) {
        modLines.set(i, modLines.get(i).replace("nop", "jmp"));
        return i + 1;
      }
    }
    throw new IndexOutOfBoundsException(lineToChange);
  }

  private SimpleEntry<Long, Boolean> runProgram(List<String> lines) {
    Set<Integer> executedLines = new HashSet<>();
    long acc = 0;
    int ip = 0;

    while (executedLines.add(ip) && ip < lines.size()) {
      String line = lines.get(ip);
      String[] parts = line.split("\\s+");
      int value = Integer.parseInt(parts[1]);
      switch (parts[0]) {
        case "jmp":
          ip += value;
          break;
        case "acc":
          acc += value; // fallthrough
        default:
          ip++;
      }
    }

    return new SimpleEntry<>(acc, ip < lines.size()); // Aargh @ no Tuples in Java
  }

  private long solveA(List<String> lines) {
    return runProgram(lines).getKey();
  }

  private long solveB(List<String> lines) {
    int lineToChange = 0;
    SimpleEntry<Long, Boolean> result;

    do {
      List<String> modLines = new ArrayList<>(lines);
      lineToChange = alterLine(modLines, lineToChange);
      result = runProgram(modLines);
    } while (result.getValue());

    return result.getKey();
  }

  @Test
  public void testDay08() throws IOException, URISyntaxException {
    List<String> testInput = Arrays.asList( //
        "nop +0", //
        "acc +1", //
        "jmp +4", //
        "acc +3", //
        "jmp -3", //
        "acc -99", //
        "acc +1", //
        "jmp -4", //
        "acc +6");
    assertEquals(5, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(1782L, result);
    System.out.println(result);

    assertEquals(8, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    assertEquals(797L, result);
    System.out.println(result);
  }

}
