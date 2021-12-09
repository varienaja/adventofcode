package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
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
  private Map<Integer, Integer> length2value = new HashMap<>();

  public long decodeLine(String line) {
    // .DDDD. We know the segments for 1 (AB), 7 (ABD), 4 (ABFE) and 8 (ABCDEFG)
    // E....A When we see an unknown 6-long
    // E....A ..that has 2 segments in common with 1, it is 0 or 9 else 6
    // .FFFF. ....and if it has 4 segments in common with 4 it is 9, else 0
    // G....B When we see an unknown 5-long
    // G....B ..that has 2 segments in common with 1, it is 3
    // .CCCC. ....else if it has 2 segments in common with 4 it is 2, else 5.

    String[] parts = line.split("\\s+\\|\\s+");
    List<String> input = Arrays.stream(parts[0].split("\\s+")).map(this::sort).collect(Collectors.toList());
    List<String> output = Arrays.stream(parts[1].split("\\s+")).map(this::sort).collect(Collectors.toList());

    Map<Integer, String> encoder = new HashMap<>();
    input.stream().forEach(i -> {
      Integer v = length2value.get(i.length());
      if (v != null) {
        encoder.put(v, i);
      }
    });

    input.stream().filter(l -> l.length() == 6).forEach(l -> {
      if (overlap(l, encoder.get(1)) == 2) { // 0 or 9
        encoder.put(overlap(l, encoder.get(4)) == 4 ? 9 : 0, l);
      } else { // 6
        encoder.put(6, l);
      }
    });

    input.stream().filter(l -> l.length() == 5).forEach(l -> {
      if (overlap(l, encoder.get(1)) == 2) { // 3
        encoder.put(3, l);
      } else { // 2 or 5
        encoder.put(overlap(l, encoder.get(4)) == 2 ? 2 : 5, l);
      }
    });

    Map<String, Integer> decoder = encoder.entrySet().stream().collect(Collectors.toMap(Entry::getValue, Entry::getKey));
    return Long.parseLong(output.stream().map(decoder::get).map(i -> Integer.toString(i)).collect(Collectors.joining()));
  }

  private int overlap(String one, String other) {
    return (int)one.chars().map(c -> other.indexOf(c, 0)).filter(i -> i >= 0).count();
  }

  private long solveA(List<String> lines) {
    return lines.stream().flatMap(line -> {
      return Arrays.stream(line.split("\\s+\\|\\s+")[1].split("\\s+"));
    }).map(String::length).filter(length2value::containsKey).count();
  }

  private long solveB(List<String> lines) {
    return lines.stream().mapToLong(this::decodeLine).sum();
  }

  private String sort(String s) {
    return s.chars().sorted().mapToObj(Character::toString).collect(Collectors.joining());
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
