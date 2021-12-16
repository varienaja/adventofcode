package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle16 extends PuzzleAbs {
  private int ix;

  private long calcLine(String t, boolean doCalculation) {
    long version = Long.parseLong(t.substring(ix + 0, ix + 3), 2);

    String ttt = t.substring(ix + 3, ix + 6);
    if (ttt.equals("100")) { // literal
      ix += 6;
      StringBuilder sb = new StringBuilder();
      String five;
      do {
        five = t.substring(ix, ix + 5);
        sb.append(five.substring(1));
        ix += 5;
      } while (five.startsWith("1"));
      return doCalculation ? Long.parseLong(sb.toString(), 2) : version;
    } else {
      List<Long> vals = new LinkedList<>();

      if (t.substring(ix + 6, ix + 7).equals("1")) {
        int cnt = Integer.parseInt(t.substring(ix + 7, ix + 18), 2);
        ix += 18;
        for (int i = 0; i < cnt; i++) {
          vals.add(calcLine(t, doCalculation));
        }
      } else {
        int lll = Integer.parseInt(t.substring(ix + 7, ix + 22), 2);
        ix += 22;
        int to = ix + lll;
        while (ix != to) {
          vals.add(calcLine(t, doCalculation));
        }
      }
      if (!doCalculation) {
        return version + vals.stream().mapToLong(l -> l).sum();
      }

      if (ttt.equals("000")) {
        return vals.stream().mapToLong(l -> l).sum();
      } else if (ttt.equals("001")) {
        return vals.stream().mapToLong(l -> l).reduce((a, b) -> a * b).getAsLong();
      } else if (ttt.equals("010")) {
        return vals.stream().mapToLong(l -> l).min().getAsLong();
      } else if (ttt.equals("011")) {
        return vals.stream().mapToLong(l -> l).max().getAsLong();
      } else if (ttt.equals("101")) {
        return vals.get(0) > vals.get(1) ? 1L : 0L;
      } else if (ttt.equals("110")) {
        return vals.get(0) < vals.get(1) ? 1L : 0L;
      } else if (ttt.equals("111")) {
        return vals.get(0).equals(vals.get(1)) ? 1L : 0L;
      }
    }

    return 0L;
  }

  private long solveA(String line) {
    ix = 0;
    return calcLine(transform(line),  false);
  }

  private long solveB(String line) {
    ix = 0;
    return calcLine(transform(line), true);
  }

  @Test
  public void testDay01() {
    assertEquals(16, solveA("8A004A801A8002F478"));
    assertEquals(12, solveA("620080001611562C8802118E34"));
    assertEquals(23, solveA("C0015000016115A2E0802F182340"));
    assertEquals(31, solveA("A0016C880162017C3686B18A3D4780"));

    announceResultA();
    String line = getInput().get(0);
    long result = solveA(line);
    System.out.println(result);
    assertEquals(934, result);

    assertEquals(3, solveB("C200B40A82"));
    assertEquals(54, solveB("04005AC33890"));
    assertEquals(7, solveB("880086C3E88112"));
    assertEquals(1, solveB("D8005AC2A8F0"));

    assertEquals(0, solveB("F600BC2D8F"));
    assertEquals(0, solveB("9C005AC2F8F0"));
    assertEquals(1, solveB("9C0141080250320F1802104A08"));

    announceResultB();
    result = solveB(line);
    System.out.println(result);
    assertEquals(912901337844L, result);
  }

  private String transform(String input) {
    Map<String, String> char2Bin = IntStream.range(0, 16).mapToObj(i -> i) //
        .collect(Collectors.toMap(i -> Integer.toString(i, 16).toUpperCase(), //
            i -> String.format("%4s", Integer.toString(i, 2)).replace(' ', '0')));

    return input.chars().mapToObj(Character::toString).map(char2Bin::get).collect(Collectors.joining());
  }

}
