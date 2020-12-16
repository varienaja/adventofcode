package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
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
  private Map<String, BitSet> allowed;
  private List<int[]> tickets;
  private int[] myTicket;

  private void parse(List<String> lines) {
    allowed = new HashMap<>();
    tickets = new LinkedList<>();
    boolean parsingMyTicket = false;
    boolean parsingOtherTicket = false;

    for (String line : lines) {
      if (line.isEmpty()) {
        continue;
      } else if (line.startsWith("your ticket:")) {
        parsingMyTicket = true;
        parsingOtherTicket = false;
        continue;
      } else if (line.startsWith("nearby tickets:")) {
        parsingMyTicket = false;
        parsingOtherTicket = true;
        continue;
      }

      if (!parsingMyTicket && !parsingOtherTicket) {
        String[] parts = line.split(": ");

        BitSet bs = new BitSet(1024);
        String[] ranges = parts[1].split(" or ");
        for (String r : ranges) {
          String[] minmax = r.split("-");
          for (int i = Integer.parseInt(minmax[0]); i <= Integer.parseInt(minmax[1]); i++) {
            bs.set(i);
          }

          allowed.put(parts[0], bs);
        }
      }

      if (parsingMyTicket || parsingOtherTicket) {
        String[] parts = line.split(",");
        int[] ticket = Stream.of(parts).map(Integer::parseInt).mapToInt(i -> i).toArray();
        if (parsingMyTicket) {
          myTicket = ticket;
        } else {
          tickets.add(ticket);
        }
      }
    }
  }

  private long solveA(List<String> lines) {
    parse(lines);
    long result = 0;

    Iterator<int[]> it = tickets.iterator();
    while (it.hasNext()) {
      for (int i : it.next()) {
        if (!allowed.values().stream().anyMatch(bs -> bs.get(i))) {
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
      for (int i : it.next()) {
        if (!allowed.values().stream().anyMatch(bs -> bs.get(i))) {
          it.remove();
        }
      }
    }
    // all tickets valid now

    List<List<String>> ix2MatchingKeys = new ArrayList<>();
    for (int f = 0; f < allowed.size(); f++) {
      List<String> matchingKeys = new LinkedList<>();
      ix2MatchingKeys.add(matchingKeys);
      for (Entry<String, BitSet> entry : allowed.entrySet()) {
        int F = f;
        if (tickets.stream().allMatch(ticket -> entry.getValue().get(ticket[F]))) {
          matchingKeys.add(entry.getKey());
        }
      }
    }
    // We now know which indices are candidate for which keys

    Map<String, Integer> mapping = new HashMap<>();
    while (mapping.size() < allowed.size()) {
      ListIterator<List<String>> it2 = ix2MatchingKeys.listIterator();
      while (it2.hasNext()) {
        List<String> matches = it2.next();
        if (matches.size() == 1) { // property key MUST at position i
          String key = matches.get(0);
          mapping.put(key, it2.previousIndex());
          ix2MatchingKeys.stream().forEach(l -> l.remove(key));
        }
      }
    }
    // We now know which key maps to which index

    return mapping.entrySet().stream() //
        .filter(e -> e.getKey().startsWith("departure")) //
        .mapToLong(e -> myTicket[e.getValue()]) //
        .reduce((a, b) -> a * b) //
        .orElseGet(() -> 1L);
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
    assertEquals(19093L, result);
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
    assertEquals(1L, solveB(input));
    announceResultB();
    result = solveB(lines);
    assertEquals(5311123569883L, result);
    System.out.println(result);
  }

}
