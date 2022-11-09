package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle07 extends PuzzleAbs {

  private Map<String, Integer> solveA(List<String> instructions) {
    Pattern set = Pattern.compile("(\\w+) -> (\\w+)");
    Pattern and = Pattern.compile("(\\w+) AND (\\w+) -> (\\w+)");
    Pattern or = Pattern.compile("(\\w+) OR (\\w+) -> (\\w+)");
    Pattern not = Pattern.compile("NOT (\\w+) -> (\\w+)");
    Pattern lshift = Pattern.compile("(\\w+) LSHIFT (\\w+) -> (\\w+)");
    Pattern rshift = Pattern.compile("(\\w+) RSHIFT (\\w+) -> (\\w+)");

    Set<String> toHandle = new HashSet<>();
    for (String instruction : instructions) {
      toHandle.add(instruction);
    }

    Map<String, Integer> wireValues = new HashMap<>();
    while (!toHandle.isEmpty()) {
      Iterator<String> it = toHandle.iterator();
      while (it.hasNext()) {
        boolean handled = false;
        String instruction = it.next();

        Matcher m = set.matcher(instruction);
        try {
          if (m.matches()) {// Set a wire
            Integer val = wireValues.containsKey(m.group(1)) ? wireValues.get(m.group(1)) : Integer.parseInt(m.group(1));
            wireValues.put(m.group(2), val);
            // System.out.println(instruction + ": " + m.group(2) + " -> " + val);
            handled = true;
          } else {
            m = and.matcher(instruction);
            if (m.matches()) {
              Integer v1 = wireValues.containsKey(m.group(1)) ? wireValues.get(m.group(1)) : Integer.parseInt(m.group(1));
              Integer v2 = wireValues.containsKey(m.group(2)) ? wireValues.get(m.group(2)) : Integer.parseInt(m.group(2));
              Integer val = v1 & v2;
              wireValues.put(m.group(3), val);
              // System.out.println(instruction + ": " + v1 + " and " + v2 + " : " + m.group(3) + " -> " + val);
              handled = true;
            } else {
              m = or.matcher(instruction);
              if (m.matches()) {
                Integer v1 = wireValues.containsKey(m.group(1)) ? wireValues.get(m.group(1)) : Integer.parseInt(m.group(1));
                Integer v2 = wireValues.containsKey(m.group(2)) ? wireValues.get(m.group(2)) : Integer.parseInt(m.group(2));
                Integer val = v1 | v2;
                wireValues.put(m.group(3), val);
                // System.out.println(instruction + ": " + v1 + " or " + v2 + " : " + m.group(3) + " -> " + val);
                handled = true;
              } else {
                m = not.matcher(instruction);
                if (m.matches()) {
                  Integer val = wireValues.containsKey(m.group(1)) ? wireValues.get(m.group(1)) : Integer.parseInt(m.group(1));
                  Integer notted = ~val;
                  if (notted < 0) {
                    notted += 65536;
                  }
                  wireValues.put(m.group(2), notted);
                  // System.out.println(instruction + ": not " + val + " : " + m.group(2) + " -> " + notted);
                  handled = true;
                } else {
                  m = rshift.matcher(instruction);
                  if (m.matches()) {
                    Integer v1 = wireValues.containsKey(m.group(1)) ? wireValues.get(m.group(1)) : Integer.parseInt(m.group(1));
                    Integer v2 = wireValues.containsKey(m.group(2)) ? wireValues.get(m.group(2)) : Integer.parseInt(m.group(2));
                    Integer val = v1 >> v2;
                    wireValues.put(m.group(3), val);
                    // System.out.println(instruction + ": " + v1 + " >> " + v2 + " : " + m.group(3) + " -> " + val);
                    handled = true;
                  } else {
                    m = lshift.matcher(instruction);
                    if (m.matches()) {
                      Integer v1 = wireValues.containsKey(m.group(1)) ? wireValues.get(m.group(1)) : Integer.parseInt(m.group(1));
                      Integer v2 = wireValues.containsKey(m.group(2)) ? wireValues.get(m.group(2)) : Integer.parseInt(m.group(2));
                      Integer val = v1 << v2;
                      if (val > 65536) {
                        val -= 65536;
                      }
                      wireValues.put(m.group(3), val);
                      // System.out.println(instruction + ": " + v1 + " << " + v2 + " : " + m.group(3) + " -> " + val);
                      handled = true;
                    }
                  }
                }
              }
            }
          }
        } catch (NumberFormatException e) {// OK, well maybe another time

        }

        if (handled) {
          it.remove();
        }
      }
    }

    return wireValues;
  }

  @Test
  public void testDay07() {
    Map<String, Integer> outcome = solveA(List.of( //
        "123 -> x", //
        "456 -> y", //
        "x AND y -> d", //
        "x OR y -> e", //
        "x LSHIFT 2 -> f", //
        "y RSHIFT 2 -> g", //
        "NOT x -> h", //
        "NOT y -> i"));
    assertEquals(72, (int)outcome.get("d"));
    assertEquals(507, (int)outcome.get("e"));
    assertEquals(492, (int)outcome.get("f"));
    assertEquals(114, (int)outcome.get("g"));
    assertEquals(65412, (int)outcome.get("h"));
    assertEquals(65079, (int)outcome.get("i"));
    assertEquals(123, (int)outcome.get("x"));
    assertEquals(456, (int)outcome.get("y"));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines).get("a");
    assertEquals(16076, result);
    System.out.println(result);

    announceResultB();
    // b: set b to value of a (16076); reset all others, what is a now?
    Pattern set = Pattern.compile("(\\d+) -> (\\w+)");
    for (int i = 0; i < lines.size(); i++) {
      Matcher m = set.matcher(lines.get(i));
      if (m.matches()) {
        if ("b".equals(m.group(2))) {
          lines.set(i, "16076 -> b");
        } else {
          lines.set(i, "0 -> " + m.group(2));
        }
      }
    }
    result = solveA(lines).get("a");
    assertEquals(2797, result);
    System.out.println(result);
  }

}
