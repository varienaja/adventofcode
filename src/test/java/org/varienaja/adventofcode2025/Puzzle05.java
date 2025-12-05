package org.varienaja.adventofcode2025;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2025.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2025">adventofcode.com</a>
 */
public class Puzzle05 extends PuzzleAbs {

  class Ranges {
    record Range(long from, long to) {
      long size() {
        return 1 + to - from;
      }
    }

    private List<Range> ranges = new LinkedList<>();
    private List<Long> ids = new LinkedList<>();

    public Ranges(List<String> lines) {
      for (String line : lines) {
        if (line.contains("-")) {
          String[] parts = line.split("-");
          addRange(new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
        } else if (!line.isEmpty()) {
          ids.add(Long.parseLong(line));
        }
      }
    }

    public long freshIdCount() {
      return ids.stream().filter(this::contains).count();
    }

    public long size() {
      return ranges.stream().mapToLong(Range::size).sum();
    }

    private void addRange(Range rr) {
      Iterator<Range> it = ranges.iterator();
      while (it.hasNext()) {
        Range r = it.next();

        if (r.from >= rr.from && r.to <= rr.to) {
          it.remove(); // existing range contained in new one: eat it
          continue;
        }
        if (rr.from >= r.from && rr.from <= r.to) {
          rr = new Range(r.to + 1, rr.to); // remove left overlap
        }
        if (rr.to >= r.from && rr.to <= r.to) {
          rr = new Range(rr.from, r.from - 1); // remove right overlap
        }
        if (rr.from > rr.to) {
          return; // nothing left to add
        }
      }
      ranges.add(rr); // add whatever remained of the original Range
    }

    private boolean contains(long id) {
      return ranges.stream().anyMatch(r -> r.from <= id && r.to >= id);
    }
  }

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(638L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(352946349407338L, result);
  }

  @Test
  public void testA() {
    assertEquals(3L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(14L, solveB(getTestInput()));
  }

  private long solveA(List<String> lines) {
    return new Ranges(lines).freshIdCount();
  }

  private long solveB(List<String> lines) {
    return new Ranges(lines).size();
  }

}
