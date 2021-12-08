package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
  private Map<Integer, Integer> length2value = new LinkedHashMap<>();

  public long decodeLine(String line) {
    // Hold candidates for segments A,B..F
    String A = ""; // .DDDD. We know the segments for 1 A+B, 7 A+B+D, 4 A+B+F+E and 8 (all), we now know D
    String B = ""; // E....A When we see an unknown 6-long containing ABCG it must be 0
    String C = ""; // E....A When we see an unknown 6-long containing AB it must be 9, we now know C and G
    String D = ""; // .FFFF. When we see an unknown 6-long containing CG it must be 6, we now know A and B
    String E = ""; // G....B When we see an unknown 5-long containing ABCD it must be 3, we now know E and F
    String F = ""; // G....B When we see an unknown 5-long containing E it must be 5,
    String G = ""; // .CCCC. When we see an unknown 5-long not containing E it must be 2.

    String[] parts = line.split("\\s+\\|\\s+");
    List<String> input = Arrays.stream(parts[0].split("\\s+")).map(this::sort).collect(Collectors.toList());
    List<String> output = Arrays.stream(parts[1].split("\\s+")).map(this::sort).collect(Collectors.toList());

    Map<String, Integer> decoder = new HashMap<>();
    input.stream().forEach(s -> decoder.put(s, -1));

    for (Entry<String, Integer> e : decoder.entrySet()) { // 1
      if (e.getKey().length() == 2) {
        e.setValue(1);
        A = e.getKey();
        B = A;
      }
    }

    for (Entry<String, Integer> e : decoder.entrySet()) { // 7
      if (e.getKey().length() == 3) {
        e.setValue(7);
        D = minus(e.getKey(), A, B);
      }
    }

    for (Entry<String, Integer> e : decoder.entrySet()) { // 4
      if (e.getKey().length() == 4) {
        e.setValue(4);
        E = minus(e.getKey(), A, B);
        F = E;
      }
    }

    for (Entry<String, Integer> e : decoder.entrySet()) { // 8
      if (e.getKey().length() == 7) {
        e.setValue(8);
        C = minus(e.getKey(), A, B, D, E, F);
        G = C;
      }
    }

    for (Entry<String, Integer> e : decoder.entrySet()) { // 0, 6 and 9
      if (e.getKey().length() == 6) {
        if (has(e.getKey(), A, B, C, G)) {
          e.setValue(0);
        } else {
          if (has(e.getKey(), A, B)) { // 9 only has C or G
            e.setValue(9);
            G = minus(G, e.getKey());
            C = minus(C, G);
          } else { // 6 only has A or B
            e.setValue(6);
            A = minus(B, e.getKey());
            B = minus(B, A);
          }
        }
      }
    }

    for (Entry<String, Integer> e : decoder.entrySet()) { // 3
      if (e.getKey().length() == 5) {
        if (has(e.getKey(), A, B, C, D)) { // 3
          e.setValue(3); // 3 only has F
          E = minus(E, e.getKey());
          F = minus(F, E);
        } else {
          if (has(e.getKey(), E)) { // only 5 contains E
            e.setValue(5);
          } else {
            e.setValue(2);
          }
        }
      }
    }

    return Long.parseLong(output.stream().map(decoder::get).map(i -> Integer.toString(i)).collect(Collectors.joining()));
  }

  private boolean has(String toCheck, String... contents) {
    String content = Arrays.stream(contents).collect(Collectors.joining());
    for (char c : content.toCharArray()) {
      if (toCheck.indexOf(c) == -1) {
        return false;
      }
    }
    return true;
  }

  private String minus(String s, String... substract) {
    for (String toSubstract : substract) {
      for (char c : toSubstract.toCharArray()) {
        s = s.replaceAll("" + c, "");
      }
    }
    return s;
  }

  private long solveA(List<String> lines) {
    List<String> outputs = new LinkedList<>();
    for (String line : lines) {
      String[] parts = line.split("\\s+\\|\\s+");
      Arrays.stream(parts[1].split("\\s+")).forEach(outputs::add);
    }

    return outputs.stream().map(String::length).filter(length2value::containsKey).count();
  }

  private long solveB(List<String> lines) {
    return lines.stream().mapToLong(this::decodeLine).sum();
  }

  private String sort(String s) {
    char[] c = s.toCharArray();
    Arrays.sort(c);
    return String.valueOf(c);
  }

  @Test
  public void testDay08() {
    length2value.put(2, 1);
    length2value.put(3, 7);
    length2value.put(4, 4);
    length2value.put(7, 8);

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

    assertEquals(1625, decodeLine("bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef"));
    assertEquals(5353, decodeLine("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"));

    assertEquals(61229, solveB(testInput));
    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(1043697, result);
  }

}
