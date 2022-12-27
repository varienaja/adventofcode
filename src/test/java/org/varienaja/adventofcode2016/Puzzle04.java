package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle04 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(409147L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(991L, result);
  }

  @Test
  public void testA() {
    assertEquals(123, isRealRoom("aaaaa-bbb-z-y-x-123[abxyz]"));
    assertEquals(987, isRealRoom("a-b-c-d-e-f-g-h-987[abcde]"));
    assertEquals(404, isRealRoom("not-a-real-room-404[oarel]"));
    assertEquals(0, isRealRoom("totally-real-room-200[decoy]"));
  }

  @Test
  public void testB() {
    assertEquals("very encrypted name", decryptRoom("qzmt-zixmtkozy-ivhz-343[lalal]"));
  }

  private String decryptRoom(String inRoomNr) {
    Pattern p = Pattern.compile("(.+?)(\\d+)\\[(\\w+)\\]");
    Matcher m = p.matcher(inRoomNr);

    assertTrue(m.matches());
    assertEquals(3, m.groupCount());
    String chars = m.group(1);
    int roomNr = Integer.parseInt(m.group(2));
    int shift = roomNr % 26;

    // Iterate over chars, rotating by roomNr
    StringBuilder sb = new StringBuilder();
    for (char c : chars.toCharArray()) {
      if (c == '-') {
        sb.append(" ");
      } else {
        c += shift;
        if (c > 'z') {
          c -= 26;
        }
        sb.append(c);
      }
    }

    return sb.toString().trim();
  }

  private int isRealRoom(String inRoomNr) {
    Pattern p = Pattern.compile("(.+?)(\\d+)\\[(\\w+)\\]");
    Matcher m = p.matcher(inRoomNr);

    assertTrue(m.matches());
    assertEquals(3, m.groupCount());
    String chars = m.group(1).replace("-", "");
    int roomNr = Integer.parseInt(m.group(2));
    String checksum = m.group(3);

    // We add the chars to a Map, thereby counting their frequency
    Map<Character, Integer> frequency = new TreeMap<>();
    for (char c : chars.toCharArray()) {
      Integer count = frequency.getOrDefault(c, 0);
      frequency.put(c, ++count);
    }

    List<Character> k = frequency.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())).map(e -> e.getKey()).limit(5)
        .collect(Collectors.toList());
    StringBuilder sb = new StringBuilder();
    for (Character c : k) {
      sb.append(c);
    }
    String expectedChecksum = sb.toString();

    if (expectedChecksum.equals(checksum)) {
      return roomNr;
    }

    return 0;
  }

  private long solveA(List<String> lines) {
    return lines.stream().mapToInt(this::isRealRoom).sum();
  }

  private long solveB(List<String> lines) {
    for (String line : lines) {
      if (decryptRoom(line).startsWith("north")) {
        return isRealRoom(line);
      }
    }

    return -1;
  }
}
