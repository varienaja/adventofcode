package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle20 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(6387L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(2455057187825L, result);
  }

  @Test
  public void testA() {
    assertEquals(3L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(1623178306L, solveB(getTestInput()));
  }

  @Override
  protected List<String> getTestInput() {
    return List.of("1", "2", "-3", "3", "-2", "0", "4");
  }

  private long solve(List<String> lines, long decryptionkey, int times) {
    List<Long> list = new LinkedList<>();
    Map<Integer, Long> ix2nr = new LinkedHashMap<>(); // Make input numbers unique

    for (int ix = 0; ix < lines.size(); ix++) {
      ix2nr.put(ix, Long.parseLong(lines.get(ix)) * decryptionkey);
      list.add((long)ix);
    }

    // Mix list according to rules
    for (int count = 0; count < times; count++) {
      for (Entry<Integer, Long> e : ix2nr.entrySet()) {
        Long i = e.getKey().longValue();
        int ix = list.indexOf(i);
        list.remove(i);

        long newIx = (ix + e.getValue()) % list.size();
        if (newIx < 0) {
          newIx += list.size();
        }

        list.add((int)newIx, i);
      }
    }

    // Put original numbers back
    ListIterator<Long> it = list.listIterator();
    while (it.hasNext()) {
      Integer ix = it.next().intValue();
      it.set(ix2nr.get(ix));
    }

    long result = 0;
    int zIx = list.indexOf(Long.valueOf(0));
    for (int i : new int[] {
        1000, 2000, 3000
    }) {
      result += list.get((zIx + i) % list.size());
    }
    return result;
  }

  private long solveA(List<String> lines) {
    return solve(lines, 1L, 1);
  }

  private long solveB(List<String> lines) {
    return solve(lines, 811589153L, 10);
  }

}
