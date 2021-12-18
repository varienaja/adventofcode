package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle18 extends PuzzleAbs {

  private String add(String s, String t) {
    return "[" + s + "," + t + "]";
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

    // find pair more than 4 deep -> explode
    Deque<Character> st = new ArrayDeque<>();

    char[] ss = s.toCharArray();

    for (int i = 0; i < ss.length; i++) {
      char c = ss[i];
      if (c == '[') {
        st.push(']');

        if (st.size() >= 5) { // TODO such that next bracket is a closing one
          int nextCloseBracket = s.indexOf(']', i + 1);
          int nextOpenBracket = s.indexOf('[', i + 1);

          if (nextOpenBracket == -1 || nextCloseBracket < nextOpenBracket) {
            // find a pair that is inside 4 pairs
            int closeBracket = s.indexOf(']', i);
            String pair = s.substring(i + 1, closeBracket);

            String[] ab = pair.split(",");
            int a = Integer.parseInt(ab[0]);
            int b = Integer.parseInt(ab[1]);

            int start1 = 0;
            int start2 = s.length() - 1;
            int end1 = 0;
            int end2 = s.length() - 1;

            for (int as = i; as >= 0; as--) {
              if (ss[as] != '[' && ss[as] != ']' && ss[as] != ',') {
                end1 = as + 1;
                while (ss[as] != '[' && ss[as] != ']' && ss[as] != ',') {
                  as--;
                }
                start1 = as + 1;

                break;
              }
            }

            for (int bs = closeBracket; bs < ss.length; bs++) {
              if (ss[bs] != '[' && ss[bs] != ']' && ss[bs] != ',') {
                start2 = bs;
                while (ss[bs] != '[' && ss[bs] != ']' && ss[bs] != ',') {
                  bs++;
                }
                end2 = bs;

                break;
              }
            }

            String aa = s.substring(start1, end1);
            String bb = s.substring(start2, end2);

            String p1 = s.substring(0, start1);
            String aaa = aa.isEmpty() ? "" : Integer.toString(Integer.parseInt(aa) + a);
            String p2 = s.substring(end1, i);

            String p3 = s.substring(i + pair.length() + 2, start2);
            String bbb = bb.isEmpty() ? "" : Integer.toString(Integer.parseInt(bb) + b);
            String p4 = s.substring(end2);

            // System.out.println(" explode " + a + " and " + b);

            String result = p1 + aaa + p2 + "0" + p3 + bbb + p4;
            return result;

            // a --> find nr to the left

            // b --> find nr to the right

            // add left to left
            // add right to right
            // replace [pair] by 0

          }
        }

      }
      if (c == ']') {
        assert c == st.pop();
      }
    }

    // System.out.println();
    return s;
  }

  private String solveA(List<String> lines) {

    // System.out.println();
    String sum = lines.get(0);

    for (int i = 1; i < lines.size(); i++) {
      sum = add(sum, lines.get(i));
      // System.out.print(sum);

      boolean changed = true;
      while (changed) {
        boolean exploded = true;
        changed = false;

        while (exploded) {
          String result = explode(sum);
          exploded = !result.equals(sum);
          sum = result;
          // System.out.print(sum);
          if (exploded) {
            changed = true;
          }
        }

        boolean split = true;
        String result = split(sum);
        split = !result.equals(sum);
        sum = result;
        // System.out.print(sum);
        if (split) {
          changed = true;
        }
      }

      // System.out.println();
      // System.out.println("** " + sum);
    }

    // calc magnitude

    return sum;
  }

  private long solveB(List<String> lines) {
    long max = Long.MIN_VALUE;
    for (String a : lines) {
      for (String b : lines) {
        if (a.equals(b)) {
          continue;
        }
        String sum = solveA(Arrays.asList(a, b));
        long mag = calcMagnitude(sum);
        if (mag > max) {
          System.out.println(a + " + " + b + "=" + mag);
          max = mag;
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
      if (ss[i] >= '0' && ss[i] <= '9') {
        int start = i;
        StringBuilder sb = new StringBuilder();
        while (ss[i] >= '0' && ss[i] <= '9') {
          sb.append(ss[i]);
          i++;
        }
        int nr = Integer.parseInt(sb.toString());
        if (nr > 9) {
          int aa = nr / 2;
          int bb = nr - aa;

          String newPair = "[" + aa + "," + bb + "]";
          // System.out.println(" split " + nr + " to " + aa + " and " + bb);

          return s.substring(0, start) + newPair + s.substring(i);
        }
      }
    }

    // System.out.println();
    return s;
  }

  @Test
  public void testDay18() {
    assertEquals("[7,[6,[5,[7,0]]]]", explode("[7,[6,[5,[4,[3,2]]]]]"));
    assertEquals("[[[[0,9],2],3],4]", explode("[[[[[9,8],1],2],3],4]"));
    assertEquals("[[[[0,7],4],[15,[0,13]]],[1,1]]", explode("[[[[0,7],4],[7,[[8,4],9]]],[1,1]]"));

    // assertEquals("[[[[4,0],[5,4]],[[7,7],[0,[6,7]]]],[[5,[5,5]],[[0,D],[0,6]]]]",
    // split("[[[[4,0],[5,4]],[[7,7],[0,[6,7]]]],[[5,:],[[0,D],[0,6]]]]"));

    // assertEquals("[[[[4,0],[5,4]],[[7,0],[?,5]]],[:,[[0,[;,3]],[[6,3],[8,8]]]]]",
    // explode("[[[[4,0],[5,4]],[[7,0],[?,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]"));

    assertEquals("[[1,2],[[3,4],5]]", add("[1,2]", "[[3,4],5]"));

    assertEquals("[[6,[5,[7,0]]],3]", explode("[[6,[5,[4,[3,2]]]],1]"));
    assertEquals("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", explode("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"));
    assertEquals("[[3,[2,[8,0]]],[9,[5,[7,0]]]]", explode("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"));

    assertEquals("", explode(""));

    assertEquals("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]", add("[[[[4,3],4],4],[7,[[8,4],9]]]", "[1,1]"));
    assertEquals("[[[[0,7],4],[7,[[8,4],9]]],[1,1]]", explode("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"));
    assertEquals("[[[[0,7],4],[15,[0,13]]],[1,1]]", explode("[[[[0,7],4],[7,[[8,4],9]]],[1,1]]"));

    assertEquals("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]", split("[[[[0,7],4],[15,[0,13]]],[1,1]]"));
    assertEquals("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]", split("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"));

    assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", explode("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]"));

    List<String> testInput = Arrays.asList( //
        "[1,1]", //
        "[2,2]", //
        "[3,3]", //
        "[4,4]", //
        "[5,5]", //
        "[6,6]");
    assertEquals("[[[[5,0],[7,4]],[5,5]],[6,6]]", solveA(testInput));

    testInput = Arrays.asList( //
        "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]", //
        "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]");
    assertEquals("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]", solveA(testInput));

    testInput = Arrays.asList( //
        "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]", //
        "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]", //
        "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]", //
        "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]", //
        "[7,[5,[[3,8],[1,4]]]]", //
        "[[2,[2,2]],[8,[8,1]]]", //
        "[2,9]", //
        "[1,[[[9,3],9],[[9,0],[0,7]]]]", //
        "[[[5,[7,4]],7],1]", //
        "[[[[4,2],2],6],[8,7]]");
    assertEquals("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", solveA(testInput));

    assertEquals(29, calcMagnitude("[9,1]"));
    assertEquals(129, calcMagnitude("[[9,1],[1,9]]"));
    assertEquals(3488, calcMagnitude("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"));

    announceResultA();
    List<String> lines = getInput();
    String result = solveA(lines);
    System.out.println(result);

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
    long r = solveB(lines);
    System.out.println(r);
    assertEquals(-1, result);
  }

}
