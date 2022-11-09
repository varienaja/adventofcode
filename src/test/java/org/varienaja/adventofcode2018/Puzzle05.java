package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle05 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals(9288, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInputString());
    System.out.println(sum);
    assertEquals(5844, sum);
  }

  private String simplify(String input) {
    List<Character> l = input.chars().mapToObj(c -> (char)c).collect(Collectors.toList());

    boolean simplified;
    do {
      simplified = false;

      ListIterator<Character> it = l.listIterator();
      while (it.hasNext()) {
        char c1 = it.next();
        if (it.hasNext()) {
          char c2 = it.next();

          if ((Character.isUpperCase(c1) && Character.toLowerCase(c1) == c2) || (Character.isLowerCase(c1) && Character.toUpperCase(c1) == c2)) {
            it.remove();
            it.previous();
            it.remove();
            simplified = true;
          }

          if (it.hasPrevious()) {
            it.previous();
          }
        }
      }

    } while (simplified);

    return l.stream().map(c -> "" + c).collect(Collectors.joining());
  }

  private long solveA(String input) {
    return simplify(input).length();
  }

  private long solveB(String input) {
    long minLength = Long.MAX_VALUE;
    for (char z = 'a'; z <= 'z'; z++) {
      char r = z;
      char R = Character.toUpperCase(r);
      List<Character> l = input.chars().mapToObj(c -> (char)c).collect(Collectors.toList());

      String filtered = l.stream().filter(c -> !c.equals(r)).filter(c -> !c.equals(R)).map(c -> "" + c).collect(Collectors.joining());
      String simplified = simplify(filtered);
      int length = simplified.length();
      if (length < minLength) {
        minLength = length;
      }
    }

    return minLength;
  }

  @Test
  public void testA() {
    assertEquals(10, solveA(testInput()));
  }

  @Test
  public void testB() {
    assertEquals(4, solveB(testInput()));
  }

  private String testInput() {
    return "dabAcCaCBAcCcaDA";
  }

}
