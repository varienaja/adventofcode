package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle10 extends PuzzleAbs {

  private String autoComplete(String line) {
    String push = "{(<[";
    String pop = "})>]";
    Stack<Character> st = new Stack<>();
    for (char c : line.toCharArray()) {
      int ix = push.indexOf(c);
      if (ix >= 0) {
        st.push(pop.charAt(ix));
      } else {
        if (c != st.pop()) {
          return "!" + c;
        }
      }
    }

    StringBuilder sb = new StringBuilder();
    while (!st.isEmpty()) {
      sb.append(st.pop());
    }
    return sb.toString();
  }

  private long solveA(List<String> lines) {
    long sum = 0;
    for (String line : lines) {
      String c = autoComplete(line);
      if (c.equals("!)")) {
        sum += 3;
      } else if (c.equals("!]")) {
        sum += 57;
      } else if (c.equals("!}")) {
        sum += 1197;
      } else if (c.equals("!>")) {
        sum += 25137;
      }
    }
    return sum;
  }

  private long solveB(List<String> lines) {
    List<String> correctedLines = lines.stream().filter(l -> !autoComplete(l).startsWith("!")).collect(Collectors.toList());
    long[] scores = correctedLines.stream() //
        .map(line -> autoComplete(line)) //
        .mapToLong(line -> line.chars().mapToLong(" )]}>"::indexOf).reduce((a, b) -> 5 * a + b).getAsLong()) //
        .sorted().toArray();
    return scores[scores.length / 2];
  }

  @Test
  public void testDay10() {
    List<String> testInput = Arrays.asList( //
        "[({(<(())[]>[[{[]{<()<>>", //
        "[(()[<>])]({[<{<<[]>>(", //
        "{([(<{}[<>[]}>{[]{[(<()>", //
        "(((({<>}<{<{<>}{[]{[]{}", //
        "[[<[([]))<([[{}[[()]]]", //
        "[{[{({}]{}}([{[{{{}}([]", //
        "{<[[]]>}<{[{[{[]{()[[[]", //
        "[(<(<(<{}))><([]([]()", //
        "<{([([[(<>()){}]>(<<{{", //
        "<{([{{}}[<[[[<>{}]]]>[]]");
    assertEquals(26397, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(271245, result);

    assertEquals(288957, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(1685293086, result);
  }

}
