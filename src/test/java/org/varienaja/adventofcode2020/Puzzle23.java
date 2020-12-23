package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle23 extends PuzzleAbs {

  static class Node {
    int value;
    Node next;
  }

  private long solve(List<Integer> lines, int moves, boolean partA) {
    if (!partA) {
      IntStream.rangeClosed(10, 1000000).forEach(lines::add);
    }

    Node[] nodes = new Node[lines.size() + 1];
    Node ll = null;
    Node prev = null;

    for (int l : lines) {
      Node last = new Node();
      if (ll == null) {
        ll = last;
      }
      last.value = l;
      nodes[l] = last;
      if (prev != null) {
        prev.next = last;
      }
      prev = last;
    }

    for (int i = 0; i < moves; i++) {
      Node current = ll;
      LinkedList<Integer> pickedUp = new LinkedList<>();
      Node picked = ll.next;
      pickedUp.add(ll.next.value);
      pickedUp.add(ll.next.next.value);
      pickedUp.add(ll.next.next.next.value);

      int destination = current.value - 1;
      while (destination < 1 || pickedUp.contains(destination)) {
        destination = (destination < 1) ? lines.size() : destination - 1;
      }

      Node dest = nodes[destination];
      ll = picked.next.next.next;
      picked.next.next.next = dest.next;
      if (dest.next == null) {
        prev = picked.next.next;
      }
      dest.next = picked;
      prev.next = current;
      current.next = null;
      prev = current;
    }

    prev.next = ll; // Make list round, so we cannot NPE when iterating
    long result = 0;
    if (partA) {
      Node n1 = nodes[1];
      Node n = n1.next;
      do {
        result = result * 10 + n.value;
        n = n.next;
      } while (n != n1);
    } else {
      Node n1 = nodes[1].next;
      Node n2 = n1.next;
      result = (long)n1.value * (long)n2.value;
    }

    return result;

  }

  private long solveA(List<Integer> lines, int moves) {
    return solve(new LinkedList<>(lines), moves, true);
  }

  private long solveB(List<Integer> lines, int moves) {
    return solve(new LinkedList<>(lines), moves, false);
  }

  @Test
  public void testDay23() {
    assertEquals(92658374L, solveA(Arrays.asList(3, 8, 9, 1, 2, 5, 4, 6, 7), 10));
    assertEquals(67384529L, solveA(Arrays.asList(3, 8, 9, 1, 2, 5, 4, 6, 7), 100));

    announceResultA();
    long result = solveA(Arrays.asList(9, 7, 4, 6, 1, 8, 3, 5, 2), 100);
    assertEquals(75893264L, result);
    System.out.println(result);

    assertEquals(149245887792L, solveB(Arrays.asList(3, 8, 9, 1, 2, 5, 4, 6, 7), 10000000));
    announceResultB();
    result = solveB(Arrays.asList(9, 7, 4, 6, 1, 8, 3, 5, 2), 10000000);
    assertEquals(38162588308L, result);
    System.out.println(result);
  }

}
