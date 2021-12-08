package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle08 extends PuzzleAbs {

  public long decodeLine(String line) {
    // Hold candidates for segments A,B..F
    String A = ""; // .DDDD.
    String B = ""; // E....A
    String C = ""; // E....A
    String D = ""; // .FFFF.
    String E = ""; // G....B
    String F = ""; // G....B
    String G = ""; // .CCCC.

    String[] parts = line.split("\\s+\\|\\s+");
    List<String> input = Arrays.stream(parts[0].split("\\s+")).collect(Collectors.toList());
    List<String> output = Arrays.stream(parts[1].split("\\s+")).collect(Collectors.toList());

    Map<String, Integer> decoder = new HashMap<>();
    input.stream().forEach(s -> decoder.put(s, -1));
    output.stream().forEach(s -> decoder.put(s, -1));

    for (Entry<String, Integer> e : decoder.entrySet()) { // 1
      if (e.getKey().length() == 2 && e.getValue() == -1) {
        e.setValue(1);
        A = e.getKey();
        B = e.getKey();
      }
    }

    for (Entry<String, Integer> e : decoder.entrySet()) { // 7
      if (e.getKey().length() == 3 && e.getValue() == -1) {
        e.setValue(7);
        D = e.getKey();
        for (char c : A.toCharArray()) {
          D = D.replaceAll("" + c, "");
        }
      }
    }

    for (Entry<String, Integer> e : decoder.entrySet()) { // 4
      if (e.getKey().length() == 4 && e.getValue() == -1) {
        e.setValue(4);
        E = e.getKey();
        F = e.getKey();
        for (char c : A.toCharArray()) {
          E = E.replaceAll("" + c, "");
          F = F.replaceAll("" + c, "");
        }
      }
    }

    for (Entry<String, Integer> e : decoder.entrySet()) { // 8
      if (e.getKey().length() == 7 && e.getValue() == -1) {
        e.setValue(8);
        C = e.getKey();
        G = e.getKey();
        for (char c : A.toCharArray()) {
          C = C.replaceAll("" + c, "");
          G = G.replaceAll("" + c, "");
        }
        C = C.replaceAll(D, "");
        G = G.replaceAll(D, "");
        for (char c : E.toCharArray()) {
          C = C.replaceAll("" + c, "");
          G = G.replaceAll("" + c, "");
        }
      }
    }

    for (Entry<String, Integer> e : decoder.entrySet()) { // 0
      if (e.getKey().length() == 6 && e.getValue() == -1) {
        boolean hasABCG = true;
        for (char c : A.toCharArray()) {
          if (!e.getKey().contains("" + c)) {
            hasABCG = false;
          }
        }
        for (char c : C.toCharArray()) {
          if (!e.getKey().contains("" + c)) {
            hasABCG = false;
          }
        }
        if (hasABCG) {
          e.setValue(0);
        }
      }
    }

    for (Entry<String, Integer> e : decoder.entrySet()) { // 6 and 9
      if (e.getKey().length() == 6 && e.getValue() == -1) {
        boolean hasAB = true;
        for (char c : A.toCharArray()) {
          if (!e.getKey().contains("" + c)) {
            hasAB = false;
          }
        }
        if (hasAB) { // 9 only has C or G
          e.setValue(9);
          for (char c : G.toCharArray()) {
            if (!e.getKey().contains("" + c)) {
              C = C.replaceAll("" + c, "");
            }
          }
          G = G.replaceAll(C, "");
        } else { // 6 only has A or B
          e.setValue(6);
          for (char c : A.toCharArray()) {
            if (!e.getKey().contains("" + c)) {
              B = B.replaceAll("" + c, "");
            }
          }
          A = A.replaceAll(B, "");
        }
      }
    }

    for (Entry<String, Integer> e : decoder.entrySet()) { // 3
      if (e.getKey().length() == 5 && e.getValue() == -1) {
        boolean hasABCD = true;
        for (char c : (A + B + C + D).toCharArray()) {
          if (!e.getKey().contains("" + c)) {
            hasABCD = false;
          }
        }

        if (hasABCD) { // 3
          e.setValue(3); // 3 only has F
          for (char c : E.toCharArray()) {
            if (!e.getKey().contains("" + c)) {
              F = F.replaceAll("" + c, "");
            }
          }
          E = E.replaceAll(F, "");
        }
      }
    }

    for (Entry<String, Integer> e : decoder.entrySet()) { // 2 and 5
      if (e.getKey().length() == 5 && e.getValue() == -1) {
        // only 5 contains E
        if (e.getKey().contains("" + E)) {
          e.setValue(5);
        } else {
          e.setValue(2);
        }
      }
    }

    return Long.parseLong(output.stream().map(decoder::get).map(i -> Integer.toString(i)).collect(Collectors.joining()));
  }

  private long solveA(List<String> lines) {
    List<String> outputs = new LinkedList<>();
    for (String line : lines) {
      String[] parts = line.split("\\s+\\|\\s+");
      Arrays.stream(parts[1].split("\\s+")).forEach(outputs::add);
    }

    return outputs.stream().map(String::length).filter(l -> l == 2 || l == 3 || l == 4 || l == 7).count();
  }

  private long solveB(List<String> lines) {
    long sum = 0;
    for (String line : lines) {
      long l = decodeLine(line);
      sum += l;
    }
    return sum;
  }

  @Test
  public void testDay08() {
    List<String> testInput = Arrays.asList(//
        "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe",
        "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc",
        "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg",
        "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb",
        "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea",
        "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb",
        "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe",
        "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef",
        "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb",
        "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce");
    assertEquals(26, solveA(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(416, result);

    assertEquals(1625, decodeLine("bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef")); // not
                                                                                                                          // 1655
    assertEquals(5353, decodeLine("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"));

    assertEquals(61229, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(1043697, result);
  }

}
