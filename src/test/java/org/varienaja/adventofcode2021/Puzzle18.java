package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 * <p>I don't want to mess around with Strings too much, so I change all
 * non-digits to values below 0. Therefore [] becomes ()
 * Now I can add and subtract to the individual characters directly.
 * So I don't have <tt>[10,0]</tt> but rather <tt>[:,0]</tt></p>
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle18 extends PuzzleAbs {

  private String add(String s, String t) {
    return "(" + s + "," + t + ")";
  }

  public long calcMagnitude(String s) {
    char[] ss = s.toCharArray();

    for (int i = 0; i < ss.length; i++) {
      char c = ss[i];
      if (c == '[') {
        int nextCloseBracket = s.indexOf(']', i + 1);
        int nextOpenBracket = s.indexOf('[', i + 1);
        if (nextOpenBracket == -1 || nextCloseBracket < nextOpenBracket) {
          // find a pair that is inside 4 pairs
          int closeBracket = s.indexOf(']', i);
          String pair = s.substring(i + 1, closeBracket);

          String[] ab = pair.split(",");
          long a = Long.parseLong(ab[0]);
          long b = Long.parseLong(ab[1]);

          long nv = 3 * a + 2 * b;

          String before = s.substring(0, i);
          String after = s.substring(closeBracket + 1);
          String result = before + Long.toString(nv) + after;
          return calcMagnitude(result);

        }
      }
    }
    return Long.parseLong(s);
  }

  private String explode(String s) {
    // If any pair is nested inside four pairs, the leftmost such pair explodes.

    // To explode a pair, the pair's left value is added to the first regular number to the left of the exploding pair
    // (if any), and the pair's right value is added to the first regular number to the right of the exploding pair (if
    // any). Exploding pairs will always consist of two regular numbers. Then, the entire exploding pair is replaced
    // with the regular number 0.
    int bracketDepth = 0;
    char[] ss = s.toCharArray();
    for (int i = 0; i < ss.length; i++) {
      char c = ss[i];
      if (c == '(') {
        bracketDepth++;

        if (bracketDepth >= 5 && s.charAt(i + 4) == ')') {
          String pair = s.substring(i + 1, i + 4);
          String[] ab = pair.split(",");
          int a = ab[0].charAt(0) - '0';
          int b = ab[1].charAt(0) - '0';

          for (int as = i; as >= 0; as--) {
            if (ss[as] >= '0') {
              ss[as] += a;
              break;
            }
          }

          for (int bs = i + 4; bs < ss.length; bs++) {
            if (ss[bs] >= '0') {
              ss[bs] += b;
              break;
            }
          }

          String r = String.valueOf(ss);
          return r.substring(0, i) + "0" + r.substring(i + 5);
        }
      } else if (c == ')') {
        bracketDepth--;
      }
    }

    return s;
  }

  private String solveA(List<String> lines) {
    lines = lines.stream().map(line -> line.replace('[', '(').replace(']', ')')).collect(Collectors.toList());
    String sum = lines.get(0);

    for (int i = 1; i < lines.size(); i++) {
      sum = add(sum, lines.get(i));

      boolean changed = true;
      while (changed) {
        changed = false;

        boolean exploded = true;
        while (exploded) {
          String result = explode(sum);
          exploded = !result.equals(sum);
          sum = result;
          if (exploded) {
            changed = true;
          }
        }

        boolean split = true;
        String result = split(sum);
        split = !result.equals(sum);
        sum = result;
        if (split) {
          changed = true;
        }
      }
    }

    return sum.replace('(', '[').replace(')', ']');
  }

  private long solveB(List<String> lines) {
    long max = Long.MIN_VALUE;
    for (String a : lines) {
      for (String b : lines) {
        if (!a.equals(b)) {
          String sum = solveA(Arrays.asList(a, b));
          max = Math.max(max, calcMagnitude(sum));
        }
      }
    }

    return max;
  }

  private String split(String s) {
    // If any regular number is 10 or greater, the leftmost such regular number splits.

    // To split a regular number, replace it with a pair; the left element of the pair should be the regular number
    // divided by two and rounded down, while the right element of the pair should be the regular number divided by two
    // and rounded up. For example, 10 becomes [5,5], 11 becomes [5,6], 12 becomes [6,6], and so on.
    char[] ss = s.toCharArray();

    for (int i = 0; i < ss.length; i++) {
      if (ss[i] > '9') {
        int nr = ss[i] - '0';
        int a = nr / 2;
        int b = nr - a;

        char aa = (char)('0' + a);
        char bb = (char)('0' + b);
        String newPair = "(" + aa + "," + bb + ")";
        return s.substring(0, i) + newPair + s.substring(i + 1);
      }
    }

    return s;
  }

  @Test
  public void testAssumption() {

    assertTrue('[' > '9');
    assertTrue(']' > '9');

    assertTrue('(' < '0');
    assertTrue(',' < '0');
    assertTrue(')' < '0');
  }

  @Test
  public void testDay18() {
    List<String> testInput = Arrays.asList( //
        "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]", //
        "[[[5,[2,8]],4],[5,[[9,9],0]]]", //
        "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]", //
        "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]", //
        "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]", //
        "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]", //
        "[[[[5,4],[7,7]],8],[[8,3],8]]", //
        "[[9,3],[[9,9],[6,[4,9]]]]", //
        "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]", //
        "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]");
    assertEquals(4140, calcMagnitude(solveA(testInput)));

    announceResultA();
    List<String> lines = getInput();
    String result = solveA(lines);
    long m = calcMagnitude(result);
    System.out.println(m);
    assertEquals(3763, m);

    testInput = Arrays.asList( //
        "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]", //
        "[[[5,[2,8]],4],[5,[[9,9],0]]]", //
        "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]", //
        "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]", //
        "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]", //
        "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]", //
        "[[[[5,4],[7,7]],8],[[8,3],8]]", //
        "[[9,3],[[9,9],[6,[4,9]]]]", //
        "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]", //
        "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]");
    assertEquals(3993, solveB(testInput));

    announceResultB();
    m = solveB(lines);
    System.out.println(m);
    assertEquals(4664, m);
  }

}
