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

    if (rp.contains("\"")) {
      return rp.replace("\"", "");
    } else if (rp.contains("|")) {
      String[] lr = rp.split("\\s*\\|\\s*");
      assert lr.length == 2;

      String left = createRegexFromKeys(lr[0], cnt8, cnt11);
      String right = createRegexFromKeys(lr[1], cnt8, cnt11);
      return "(" + left + "|" + right + ")";
    } else {
      return createRegexFromKeys(rp, cnt8, cnt11);
    }
  }

  private String createRegexFromKeys(String keysString, int cnt8, int cnt11) {
    String[] keys = keysString.split("\\s");
    return Stream.of(keys) //
        .map(Integer::parseInt) //
        .map(k -> createRegex(k, cnt8, cnt11)) //
        .collect(Collectors.joining());
  }

  private boolean matches(String message, int pos, int[] rulesToMatch) {
    if (message.length() == pos) {
      return rulesToMatch.length == 0;
    }
    if (rulesToMatch.length == 0) {
      return false;
    }

    String rule = rule2rules.get(rulesToMatch[0]);
    if (rule.indexOf('"') >= 0) { // a or b
      if (rule.indexOf(message.charAt(pos)) >= 0) {
        int[] newRules = Arrays.stream(rulesToMatch).skip(1).toArray();
        return matches(message, pos + 1, newRules);
      } else {
        return false;
      }
    } else {
      String[] lr = rule.split("\\s*\\|\\s*");

      return Stream.of(lr).anyMatch(rr -> {
        String[] parts = rr.split("\\s");
        int[] newRules = Stream.concat(Stream.of(parts).map(Integer::parseInt), Arrays.stream(rulesToMatch).skip(1).boxed()).mapToInt(i -> i).toArray();
        return matches(message, pos, newRules);
      });
    }
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

  private long solveAlt() {
    int[] start = {
        0
    };
    return messages.stream().filter(msg -> matches(msg, 0, start)).count();
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
    assertEquals(2, solveAlt());

    announceResultA();
    List<String> lines = getInput();
    parseInputA(lines);
    long result = solve();
    System.out.println(result);
    assertEquals(250, result);

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
