package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle17 extends PuzzleAbs {

  private long solveA(int input) {
    List<Integer> buffer = new LinkedList<>();
    buffer.add(0);

    ListIterator<Integer> it = buffer.listIterator();
    for (int i = 1; i <= 2017; i++) {
      for (int step = 0; step < input; step++) {
        if (!it.hasNext()) {
          it = buffer.listIterator();
        }
        it.next();
      }
      it.add(i);
    }

    if (!it.hasNext()) {
      it = buffer.listIterator();
    }

    return it.next();
  }

  private long solveB(int input) {
    // 0 is at index 0
    // we then do 349 skips; insert 1 at that position
    // we then do 349 skips; insert 2 at that position
    int ix1 = -1;
    int size = 1;
    int insertionIx = 0;
    for (int i = 1; i <= 50000000; i++) {
      insertionIx = 1 + (insertionIx + input % size) % size;
      if (insertionIx == 1) {
        ix1 = i;
      }
      size++;
    }

    return ix1;
  }

  @Test
  public void testDay17() {
    assertEquals(638, solveA(3));

    announceResultA();
    long result = solveA(349);
    System.out.println(result);
    assertEquals(640, result);

    announceResultB();
    result = solveB(349);
    System.out.println(result);
    assertEquals(47949463, result);
  }

}
