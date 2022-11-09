package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle14 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    String answer = solveA(640441);
    System.out.println(answer);
    assertEquals("1041411104", answer);
  }

  @Test
  public void doB() {
    announceResultB();
    long answer = solveB("640441");
    System.out.println(answer);
    assertEquals(20174745, answer);
  }

  private String solveA(int input) {
    List<Integer> recipies = new ArrayList<>();
    recipies.add(3);
    recipies.add(7);
    int ixE1 = 0;
    int ixE2 = 1;

    while (recipies.size() < input + 10) {
      int s1 = recipies.get(ixE1);
      int s2 = recipies.get(ixE2);

      String sum = Integer.toString(s1 + s2);
      for (int l = 0; l < sum.length(); l++) {
        int toAdd = Integer.parseInt(sum.substring(l, l + 1));
        recipies.add(toAdd);
      }

      ixE1 = (ixE1 + s1 + 1) % recipies.size();
      ixE2 = (ixE2 + s2 + 1) % recipies.size();
    }

    StringBuilder sb = new StringBuilder();
    for (int i = input; i < input + 10; i++) {
      sb.append(Integer.toString(recipies.get(i)));
    }
    return sb.toString();
  }

  private int solveB(String input) {
    List<Integer> recipies = new ArrayList<>();
    recipies.add(3);
    recipies.add(7);
    int ixE1 = 0;
    int ixE2 = 1;

    String lastX = "";
    for (int i = 0; i < input.length(); i++) {
      lastX += "0";
    }
    while (true) {
      int s1 = recipies.get(ixE1);
      int s2 = recipies.get(ixE2);

      String sum = Integer.toString(s1 + s2);
      for (int l = 0; l < sum.length(); l++) {
        String p = sum.substring(l, l + 1);
        int toAdd = Integer.parseInt(p);
        recipies.add(toAdd);

        lastX = lastX.substring(1) + p;
        if (input.equals(lastX)) {
          return recipies.size() - input.length();
        }
      }

      ixE1 = (ixE1 + s1 + 1) % recipies.size();
      ixE2 = (ixE2 + s2 + 1) % recipies.size();
    }
  }

  @Test
  public void testA() {
    assertEquals("5158916779", solveA(9));
    assertEquals("0124515891", solveA(5));
    assertEquals("9251071085", solveA(18));
    assertEquals("5941429882", solveA(2018));
  }

  @Test
  public void testB() {
    assertEquals(9, solveB("51589"));
    assertEquals(5, solveB("01245"));
    assertEquals(18, solveB("92510"));
    assertEquals(2018, solveB("59414"));
  }

}
