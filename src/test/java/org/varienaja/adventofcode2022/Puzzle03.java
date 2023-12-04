package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle03 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    assertEquals(8072L, result);
    System.out.println(result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    assertEquals(2567L, result);
    System.out.println(result);
  }

  @Test
  public void testA() {
    assertEquals(157L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(70L, solveB(getTestInput()));
  }

  @Override
  protected List<String> getTestInput() {
    return List.of( //
        "vJrwpWtwJgWrhcsFMMfFFhFp", //
        "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL", //
        "PmmdzqPrVvPwwTWBwg", //
        "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn", //
        "ttgJtRGJQctTZtZT", //
        "CrZsJsPPZsGzwwsLwLmpwMDw");
  }

  private Set<Character> collectChars(String s) {
    return s.chars().mapToObj(i -> Character.valueOf((char)i)).collect(Collectors.toSet());
  }

  private long solveA(List<String> lines) {
    long result = 0L;

    for (String rucksack : lines) {
      int len = rucksack.length() / 2;
      Set<Character> comp1 = collectChars(rucksack.substring(0, len));
      Set<Character> comp2 = collectChars(rucksack.substring(len));
      comp1.retainAll(comp2);

      result += comp1.stream().mapToInt(c -> Character.isLowerCase(c) ? 1 + c.charValue() - 'a' : 27 + c.charValue() - 'A').sum();
    }
    return result;
  }

  private long solveB(List<String> lines) {
    long result = 0L;

    Iterator<String> it = lines.iterator();
    while (it.hasNext()) {
      Set<Character> comp1 = collectChars(it.next());
      comp1.retainAll(collectChars(it.next()));
      comp1.retainAll(collectChars(it.next()));

      result += comp1.stream().mapToInt(c -> Character.isLowerCase(c) ? 1 + c.charValue() - 'a' : 27 + c.charValue() - 'A').sum();
    }
    return result;
  }

}
