package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2016.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2016">adventofcode.com</a>
 */
public class Puzzle21 extends PuzzleAbs {
  private String inputA = "abcdefgh";
  private String inputB = "fbgdceah";

  private char[] movePosition(char[] arr, int x, int y) {
    char[] t = new char[arr.length];
    System.arraycopy(arr, 0, t, 0, arr.length);

    arr[y] = arr[x];
    if (x < y) {
      System.arraycopy(t, x + 1, arr, x, y - x);
    } else {
      System.arraycopy(t, y, arr, y + 1, x - y);
    }

    return arr;
  }

  private char[] reverse(char[] arr, int x, int y) {
    int i = x;
    int target = y;
    while (i < target) {
      char t = arr[i];
      arr[i] = arr[target];
      arr[target] = t;
      i++;
      target--;
    }

    return arr;
  }

  private char[] rotateLeft(char[] arr, int steps) {
    char[] t = new char[arr.length];
    System.arraycopy(arr, 0, t, 0, arr.length);
    if (steps > arr.length) {
      steps = steps % arr.length;
    }

    System.arraycopy(t, steps, arr, 0, arr.length - steps);
    System.arraycopy(t, 0, arr, arr.length - steps, steps);
    return arr;
  }

  private char[] rotateOnLetter(char[] arr, char x) {
    int ixX = 0;
    while (arr[ixX] != x) {
      ixX++;
    }

    if (ixX >= 4) {
      ixX++;
    }

    return rotateRight(arr, 1 + ixX);
  }

  private char[] rotateOnLetterReverse(char[] arr, char x) {
    char[] t = new char[arr.length];
    System.arraycopy(arr, 0, t, 0, arr.length);

    char[] tT = new char[arr.length];

    int steps = 0;
    do {
      System.arraycopy(arr, 0, tT, 0, arr.length);
      steps++;
      rotateLeft(tT, steps);
    } while (!Arrays.equals(t, rotateOnLetter(tT, x)));

    rotateLeft(arr, steps);
    return arr;
  }

  private char[] rotateRight(char[] arr, int steps) {
    char[] t = new char[arr.length];
    System.arraycopy(arr, 0, t, 0, arr.length);
    if (steps > arr.length) {
      steps = steps % arr.length;
    }

    System.arraycopy(t, 0, arr, steps, arr.length - steps);
    System.arraycopy(t, arr.length - steps, arr, 0, steps);
    return arr;
  }

  private String solveA(List<String> input, String toScramble) {
    Pattern swapPosition = Pattern.compile("swap position (\\d+) with position (\\d+)");
    Pattern swapLetter = Pattern.compile("swap letter (\\w) with letter (\\w)");
    Pattern rotate = Pattern.compile("rotate (\\w+) (\\d+) step[s]*");
    Pattern rotateOnLetter = Pattern.compile("rotate based on position of letter (\\w)");
    Pattern reverse = Pattern.compile("reverse positions (\\d+) through (\\d+)");
    Pattern movePosition = Pattern.compile("move position (\\d+) to position (\\d+)");

    char[] arr = toScramble.toCharArray();
    for (String line : input) {
      Matcher m = swapPosition.matcher(line);
      if (m.matches()) {
        swapPos(arr, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
      }

      m = swapLetter.matcher(line);
      if (m.matches()) {
        swapLetter(arr, m.group(1).charAt(0), m.group(2).charAt(0));
      }

      m = rotate.matcher(line);
      if (m.matches()) {
        if ("left".equals(m.group(1))) {
          rotateLeft(arr, Integer.parseInt(m.group(2)));
        } else {
          rotateRight(arr, Integer.parseInt(m.group(2)));
        }
      }

      m = rotateOnLetter.matcher(line);
      if (m.matches()) {
        rotateOnLetter(arr, m.group(1).charAt(0));
      }

      m = reverse.matcher(line);
      if (m.matches()) {
        reverse(arr, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
      }

      m = movePosition.matcher(line);
      if (m.matches()) {
        movePosition(arr, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
      }

    }

    return String.valueOf(arr);
  }

  private String solveB(List<String> input, String toUnscramble) {
    Pattern swapPosition = Pattern.compile("swap position (\\d+) with position (\\d+)");
    Pattern swapLetter = Pattern.compile("swap letter (\\w) with letter (\\w)");
    Pattern rotate = Pattern.compile("rotate (\\w+) (\\d+) step[s]*");
    Pattern rotateOnLetter = Pattern.compile("rotate based on position of letter (\\w)");
    Pattern reverse = Pattern.compile("reverse positions (\\d+) through (\\d+)");
    Pattern movePosition = Pattern.compile("move position (\\d+) to position (\\d+)");

    List<String> inputReversed = new LinkedList<>(input);
    Collections.reverse(inputReversed);

    char[] arr = toUnscramble.toCharArray();
    for (String line : inputReversed) {
      Matcher m = swapPosition.matcher(line);
      if (m.matches()) {
        swapPos(arr, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
      }

      m = swapLetter.matcher(line);
      if (m.matches()) {
        swapLetter(arr, m.group(1).charAt(0), m.group(2).charAt(0));
      }

      m = rotate.matcher(line);
      if (m.matches()) {
        if ("right".equals(m.group(1))) { // Swapped left/right
          rotateLeft(arr, Integer.parseInt(m.group(2)));
        } else {
          rotateRight(arr, Integer.parseInt(m.group(2)));
        }
      }

      m = rotateOnLetter.matcher(line);
      if (m.matches()) {
        rotateOnLetterReverse(arr, m.group(1).charAt(0));
      }

      m = reverse.matcher(line);
      if (m.matches()) {
        reverse(arr, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
      }

      m = movePosition.matcher(line);
      if (m.matches()) { // INVERSER ORDER
        movePosition(arr, Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
      }

    }

    return String.valueOf(arr);
  }

  private char[] swapLetter(char[] arr, char x, char y) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == x) {
        arr[i] = y;
      } else if (arr[i] == y) {
        arr[i] = x;
      }
    }
    return arr;
  }

  private char[] swapPos(char[] arr, int x, int y) {
    char t = arr[x];
    arr[x] = arr[y];
    arr[y] = t;
    return arr;
  }

  @Test
  public void testDay21() {
    assertEquals("ebcda", String.valueOf(swapPos("abcde".toCharArray(), 4, 0)));
    assertEquals("edcba", String.valueOf(swapLetter("ebcda".toCharArray(), 'd', 'b')));
    assertEquals("abcde", String.valueOf(reverse("edcba".toCharArray(), 0, 4)));
    assertEquals("bcdea", String.valueOf(rotateLeft("abcde".toCharArray(), 1)));
    assertEquals("eabcd", String.valueOf(rotateRight("abcde".toCharArray(), 1)));
    assertEquals("bdeac", String.valueOf(movePosition("bcdea".toCharArray(), 1, 4)));
    assertEquals("abdec", String.valueOf(movePosition("bdeac".toCharArray(), 3, 0)));
    assertEquals("ecabd", String.valueOf(rotateOnLetter("abdec".toCharArray(), 'b')));
    assertEquals("decab", String.valueOf(rotateOnLetter("ecabd".toCharArray(), 'd')));

    List<String> testInput = List.of( //
        "swap position 4 with position 0", //
        "swap letter d with letter b", //
        "reverse positions 0 through 4", //
        "rotate left 1 step", //
        "move position 1 to position 4", //
        "move position 3 to position 0", //
        "rotate based on position of letter b", //
        "rotate based on position of letter d");
    assertEquals("decab", solveA(testInput, "abcde"));

    announceResultA();
    List<String> lines = getInput();
    String answer = solveA(lines, inputA);
    System.out.println(answer);
    assertEquals("gcedfahb", answer);

    // Inverse operations
    assertEquals("decab", String.valueOf(rotateOnLetter("ecabd".toCharArray(), 'e')));
    assertEquals("decab", String.valueOf(rotateOnLetter("ecabd".toCharArray(), 'd')));

    assertEquals("abdec", String.valueOf(rotateOnLetterReverse("ecabd".toCharArray(), 'b')));
    assertEquals("ecabd", String.valueOf(rotateOnLetterReverse("decab".toCharArray(), 'd')));
    assertEquals("bdeac", String.valueOf(movePosition("abdec".toCharArray(), 0, 3)));
    assertEquals("bcdea", String.valueOf(movePosition("bdeac".toCharArray(), 4, 1)));
    assertEquals("abcde", String.valueOf(rotateLeft("eabcd".toCharArray(), 1)));
    assertEquals("abcde", String.valueOf(rotateRight("bcdea".toCharArray(), 1)));
    assertEquals("edcba", String.valueOf(reverse("abcde".toCharArray(), 0, 4)));
    assertEquals("ebcda", String.valueOf(swapLetter("edcba".toCharArray(), 'd', 'b')));
    assertEquals("abcde", String.valueOf(swapPos("ebcda".toCharArray(), 4, 0)));

    announceResultB();
    assertEquals("abcde", solveB(testInput, "decab"));
    answer = solveB(lines, inputB);
    System.out.println(answer);
    assertEquals("hegbdcfa", answer);
  }

}
