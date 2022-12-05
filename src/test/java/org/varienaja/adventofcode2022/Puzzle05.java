package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle05 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    String result = solveA(getInput());
    assertEquals("VJSFHWGFT", result);
    System.out.println(result);
  }

  @Test
  public void doB() {
    announceResultB();
    String result = solveB(getInput());
    assertEquals("LCTQFBVZV", result);
    System.out.println(result);
  }

  private List<String> getTestInput() {
    return List.of( //
        "    [D]    ", //
        "[N] [C]    ", //
        "[Z] [M] [P]", //
        " 1   2   3 ", //
        "", //
        "move 1 from 2 to 1", //
        "move 3 from 1 to 3", //
        "move 2 from 2 to 1", //
        "move 1 from 1 to 2");
  }

  private String solve(List<String> lines, boolean optionA) {
    LinkedList<String> initialState = new LinkedList<>();

    Iterator<String> it = lines.iterator();
    String line = null;
    while (!(line = it.next()).isEmpty()) {
      initialState.add(line);
    }

    Map<String, StringBuffer> state = new LinkedHashMap<>();
    line = initialState.pollLast();
    for (String part : line.trim().split("\\s+")) {
      state.put(part, new StringBuffer());
    }

    while ((line = initialState.pollLast()) != null) {
      for (int ix = 0; ix < line.length(); ix++) {
        char c = line.charAt(ix);
        if (Character.isLetter(c)) {
          state.get(Integer.toString(1 + (ix / 4))).append(c); // Map 1,5,9... to 1,2,3...
        }
      }
    }

    // Do the moving...
    Pattern p = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
    while (it.hasNext()) {
      String move = it.next();
      Matcher m = p.matcher(move);
      if (m.matches()) {
        int amount = Integer.parseInt(m.group(1));
        String from = m.group(2);
        String to = m.group(3);

        StringBuffer stack = state.get(from);
        StringBuilder toMove = new StringBuilder(stack.substring(stack.length() - amount, stack.length()));
        stack.delete(stack.length() - amount, stack.length());
        if (optionA) {
          toMove.reverse();
        }
        stack = state.get(to);
        stack.append(toMove);
      }
    }

    return state.values().stream().map(sb -> sb.substring(sb.length() - 1)).collect(Collectors.joining());
  }

  private String solveA(List<String> lines) {
    return solve(lines, true);
  }

  private String solveB(List<String> lines) {
    return solve(lines, false);
  }

  @Test
  public void testA() {
    assertEquals("CMZ", solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals("MCD", solveB(getTestInput()));
  }

}
