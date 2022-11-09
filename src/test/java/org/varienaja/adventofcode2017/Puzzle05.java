package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle05 extends PuzzleAbs {

  private int solveDay5a(String inInput) {
    Pattern pattern = Pattern.compile("\\\\n");
    Integer[] code = pattern.splitAsStream(inInput).map(Integer::parseInt).toArray(Integer[]::new);

    int exPointer = 0;
    int steps = 0;
    while (exPointer >= 0 && exPointer < code.length) {
      int jump = code[exPointer]++;
      exPointer += jump;
      steps++;
    }

    return steps;
  }

  private int solveDay5b(String inInput) {
    Pattern pattern = Pattern.compile("\\\\n");
    Integer[] code = pattern.splitAsStream(inInput).map(Integer::parseInt).toArray(Integer[]::new);

    int exPointer = 0;
    int steps = 0;
    while (exPointer >= 0 && exPointer < code.length) {
      int jump = code[exPointer];
      code[exPointer] += jump >= 3 ? -1 : 1;
      exPointer += jump;
      steps++;
    }

    return steps;
  }

  @Test
  public void testDay5() throws Exception {
    assertEquals(5, solveDay5a("0\\n3\\n0\\n1\\n-3"));

    System.out.print("Solution 5a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2017/day05.txt").toURI()));
    String line = lines.stream().collect(Collectors.joining("\\n"));
    System.out.println(solveDay5a(line));

    assertEquals(10, solveDay5b("0\\n3\\n0\\n1\\n-3"));
    System.out.print("Solution 5b: ");
    System.out.println(solveDay5b(line));
  }

}
