package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle16 extends PuzzleAbs {
  Map<String, BitSet> allowed;
  List<int[]> tickets;
  int[] mytIcket;

  private void parse(List<String> lines) {
    allowed = new HashMap<>();
    tickets = new LinkedList<>();
    boolean myTicket = false;
    boolean otherTicket = false;
    for (String line : lines) {
      int ix = line.indexOf(": ");
      if (ix >= 0) {
        String[] parts = line.split(": ");

        BitSet bs = new BitSet(1024);
        String[] ranges = parts[1].split(" or ");
        for (String r : ranges) {
          String[] minmax = r.split("-");
          for (int i = Integer.parseInt(minmax[0]); i <= Integer.parseInt(minmax[1]); i++) {
            bs.set(i);
          }
        }

        allowed.put(parts[0], bs);
      }

      if (line.startsWith("your ticket:")) {
        myTicket = true;
        otherTicket = false;
        continue;
      } else if (line.startsWith("nearby tickets:")) {
        myTicket = false;
        otherTicket = true;
        continue;
      }
      if (myTicket || otherTicket) {
        if (!line.isEmpty()) {
          String[] parts = line.split(",");
          int[] ticket = Stream.of(parts).map(Integer::parseInt).mapToInt(i -> i).toArray();
          if (myTicket) {
            mytIcket = ticket;
          } else {
            tickets.add(ticket);
          }
        }
      }

    }
  }

  private long solveA(List<String> lines) {
    parse(lines);
    long result = 0;
    for (int[] ticket : tickets) {
      for (int i : ticket) {
        boolean valid = false;
        for (BitSet bs : allowed.values()) {
          if (bs.get(i)) {
            valid = true;
          }
        }
        if (!valid) {
          result += i;
        }
      }
    }

    return result;
  }

  private long solveB(List<String> lines) {
    parse(lines);
    Iterator<int[]> it = tickets.iterator();
    while (it.hasNext()) {
      int[] ticket = it.next();
      for (int i : ticket) {
        boolean valid = false;
        for (BitSet bs : allowed.values()) {
          if (bs.get(i)) {
            valid = true;
          }
        }
        if (!valid) {
          it.remove();
        }
      }
    }
    // all tickets valid now

    long result = 1L;
    LinkedList<String>[] validCount = new LinkedList[allowed.size()];
    for (int i = 0; i < validCount.length; i++) {
      validCount[i] = new LinkedList<>();
    }

    for (Entry<String, BitSet> entry : allowed.entrySet()) {
      for (int f = 0; f < allowed.size(); f++) {
        boolean valid = true;
        for (int[] ticket : tickets) {
          if (!entry.getValue().get(ticket[f])) {
            valid = false;
          }
        }
        if (valid) {
          validCount[f].add(entry.getKey());
        }

      }
    }

    Map<String, Integer> mapping = new HashMap<>();
    while (mapping.size() < allowed.size()) {
      for (int i = 0; i < validCount.length; i++) {
        List<String> matches = validCount[i];
        if (matches.size() == 1) { // property i MUST be the contents of the list
          String key = matches.get(0);
          mapping.put(key, i);

          for (int j = 0; j < validCount.length; j++) {
            validCount[j].remove(key);
          }
        }
      }
    }

    // multiply mytIcket[x]*mytIcket[y]...
    for (Entry<String, Integer> e : mapping.entrySet()) {

      if (e.getKey().startsWith("departure")) {
        result *= mytIcket[e.getValue()];
      }
    }
    return result;
  }

  private long solveBRes(List<String> lines) {
    parse(lines);
    Iterator<int[]> it = tickets.iterator();
    while (it.hasNext()) {
      int[] ticket = it.next();
      for (int i : ticket) {
        boolean valid = false;
        for (BitSet bs : allowed.values()) {
          if (bs.get(i)) {
            valid = true;
          }
        }
        if (!valid) {
          it.remove();
        }
      }
    }
    // all tickets valid now

    long result = 0;
    Map<String, Integer> fields = new HashMap<>();
    while (fields.size() < allowed.size()) {
      int[] validCount = new int[allowed.size()];
      for (int f = 0; f < allowed.size(); f++) {
        if (fields.values().contains(f)) {
          continue;
        }
        for (Entry<String, BitSet> entry : allowed.entrySet()) {
          // if (fields.containsKey(entry.getKey())) {
          // continue f;
          // }
          boolean valid = true;
          for (int[] ticket : tickets) {
            if (!entry.getValue().get(ticket[f])) {
              valid = false;
            }
          }
          if (valid) {
            validCount[f]++;
          }
        }
      }

      int min = Arrays.stream(validCount).filter(i -> i > 0).summaryStatistics().getMin();
      for (int x = 0; x < validCount.length; x++) {
        if (validCount[x] == min) {
          // xth entry is out
          int c = 0;
          Iterator<Entry<String, BitSet>> it2 = allowed.entrySet().iterator();
          while (it2.hasNext()) {
            Entry<String, BitSet> e = it2.next();
            if (c == x) {
              // it2.remove();
              fields.put(e.getKey(), c);
            }
            c++;
          }

        }
      }
    }

    // multiply mytIcket[x]*mytIcket[y]...
    result = 1L;
    for (Entry<String, Integer> e : fields.entrySet()) {

      if (e.getKey().startsWith("departure")) {
        result *= mytIcket[e.getValue()];
      }
    }
    return result;
  }

  @Test
  public void testDay16() {
    List<String> input = Arrays.asList( //
        "class: 1-3 or 5-7", //
        "row: 6-11 or 33-44", //
        "seat: 13-40 or 45-50", //
        "", //
        "your ticket:", //
        "7,1,14", //
        "", //
        "nearby tickets:", //
        "7,3,47", //
        "40,4,50", //
        "55,2,20", //
        "38,6,12" //
    );
    assertEquals(71, solveA(input));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    // assertEquals(0L, result);
    System.out.println(result);

    input = Arrays.asList(//
        "class: 0-1 or 4-19", //
        "row: 0-5 or 8-19", //
        "seat: 0-13 or 16-19", //
        "", //
        "your ticket:", //
        "11,12,13", //
        "", //
        "nearby tickets:", //
        "3,9,18", //
        "15,1,5", //
        "5,14,9" //
    );
    // assertEquals(1L, solveB(input));
    announceResultB();
    result = solveB(lines);
    // assertEquals(0L, result);
    System.out.println(result);
  }

}
