package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle19 extends PuzzleAbs {
  private static final int MAXDEPTH = 4;
  private Map<Integer, String> rule2rules;
  private List<String> messages;
  private String orig8;
  private String orig11;

  private String createRegex(int key, int cnt8, int cnt11) {
    String rp = rule2rules.get(key);

    // Part B: cut off too deep recursions
    if (key == 8) {
      cnt8++;
      if (cnt8 > MAXDEPTH) {
        rp = orig8;
      }
    }
    if (key == 11) {
      cnt11++;
      if (cnt11 > MAXDEPTH) {
        rp = orig11;
      }
    }

    if (rp.contains("\"")) { // a or b
      return rp.replace("\"", "");
    } else if (rp.contains("|")) { //
      String[] lr = rp.split("\\s*\\|\\s*");
      assert lr.length == 2;

      String[] lRules = lr[0].split("\\s");
      String left = createRegexFromKeys(lRules, cnt8, cnt11);

      String[] rRules = lr[1].split("\\s");
      String right = createRegexFromKeys(rRules, cnt8, cnt11);
      return "(" + left + "|" + right + ")";
    } else {
      String[] rules = rp.split("\\s");
      return createRegexFromKeys(rules, cnt8, cnt11);
    }
  }

  private String createRegexFromKeys(String[] keys, int cnt8, int cnt11) {
    return Stream.of(keys) //
        .map(Integer::parseInt) //
        .map(k -> createRegex(k, cnt8, cnt11)) //
        .collect(Collectors.joining());
  }

  private void parseInputA(List<String> lines) {
    rule2rules = new HashMap<>();
    messages = new LinkedList<>();
    boolean rules = true;
    for (String line : lines) {
      if (line.isEmpty()) {
        rules = false;
        continue;
      }
      if (rules) {
        String[] parts = line.split(": ");
        rule2rules.put(Integer.parseInt(parts[0]), parts[1]);
      } else {
        messages.add(line);
      }
    }
  }

  private void parseInputB(List<String> lines) {
    parseInputA(lines);
    orig8 = rule2rules.put(8, "42 | 42 8");
    orig11 = rule2rules.put(11, "42 31 | 42 11 31");
  }

  private long solve() {
    String p = createRegex(0, 0, 0);
    // System.out.println(p);
    Pattern pat = Pattern.compile(p);
    return messages.stream().map(msg -> pat.matcher(msg)).filter(m -> m.matches()).count();
  }

  @Test
  public void testDay19() {
    List<String> input = Arrays.asList( //
        "0: 4 1 5", //
        "1: 2 3 | 3 2", //
        "2: 4 4 | 5 5", //
        "3: 4 5 | 5 4", //
        "4: \"a\"", //
        "5: \"b\"", //
        "", //
        "ababbb", //
        "bababa", //
        "abbbab", //
        "aaabbb", //
        "aaaabbb" //
    );
    parseInputA(input);
    assertEquals(2, solve());

    announceResultA();
    List<String> lines = getInput();
    parseInputA(lines);
    long result = solve();
    System.out.println(result);

    input = Arrays.asList( //
        "42: 9 14 | 10 1", //
        "9: 14 27 | 1 26", //
        "10: 23 14 | 28 1", //
        "1: \"a\"", //
        "11: 42 31", //
        "5: 1 14 | 15 1", //
        "19: 14 1 | 14 14", //
        "12: 24 14 | 19 1", //
        "16: 15 1 | 14 14", //
        "31: 14 17 | 1 13", //
        "6: 14 14 | 1 14", //
        "2: 1 24 | 14 4", //
        "0: 8 11", //
        "13: 14 3 | 1 12", //
        "15: 1 | 14", //
        "17: 14 2 | 1 7", //
        "23: 25 1 | 22 14", //
        "28: 16 1", //
        "4: 1 1", //
        "20: 14 14 | 1 15", //
        "3: 5 14 | 16 1", //
        "27: 1 6 | 14 18", //
        "14: \"b\"", //
        "21: 14 1 | 1 14", //
        "25: 1 1 | 1 14", //
        "22: 14 14", //
        "8: 42", //
        "26: 14 22 | 1 20", //
        "18: 15 15", //
        "7: 14 5 | 1 21", //
        "24: 14 1", //
        "", //
        "abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa", //
        "bbabbbbaabaabba", //
        "babbbbaabbbbbabbbbbbaabaaabaaa", //
        "aaabbbbbbaaaabaababaabababbabaaabbababababaaa", //
        "bbbbbbbaaaabbbbaaabbabaaa", //
        "bbbababbbbaaaaaaaabbababaaababaabab", //
        "ababaaaaaabaaab", //
        "ababaaaaabbbaba", //
        "baabbaaaabbaaaababbaababb", //
        "abbbbabbbbaaaababbbbbbaaaababb", //
        "aaaaabbaabaaaaababaa", //
        "aaaabbaaaabbaaa", //
        "aaaabbaabbaaaaaaabbbabbbaaabbaabaaa", //
        "babaaabbbaaabaababbaabababaaab", //
        "aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba" // "
    );
    parseInputB(input);

    assertEquals(12, solve());
    parseInputB(lines);
    announceResultB();
    result = solve();
    assertEquals(359, result);
    System.out.println(result);
  }

}
