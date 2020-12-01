package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import org.junit.Test;

public class Solutions {

  /**
   * @see <a href="http://adventofcode.com/day/10">Day 10</a>
   * @param inString
   * @return
   */
  private String solveDay10a(String inString) {
    // iterate over chars; count them; if they change: output count + the char
    StringBuilder sb = new StringBuilder();
    char current = 0;
    int counter = 0;
    char[] arr = inString.toCharArray();

    for (int i = 0; i < arr.length; i++) {
      if (current != arr[i] || i == arr.length) {
        if (counter > 0) {
          sb.append(Integer.toString(counter));
          sb.append(current);
        }
        counter = 1;
        current = arr[i];
      } else {
        counter++;
      }
    }
    sb.append(Integer.toString(counter));
    sb.append(current);

    return sb.toString();
  }

  private boolean solveDay11a(String inPassword) {
    if (inPassword.indexOf('i') >= 0) {
      return false;
    }
    if (inPassword.indexOf('o') >= 0) {
      return false;
    }
    if (inPassword.indexOf('l') >= 0) {
      return false;
    }

    boolean containsStraight = false;
    for (int i = 0; i < inPassword.length() - 2; i++) {
      if (inPassword.charAt(i + 2) - inPassword.charAt(i + 1) == 1 //
          && inPassword.charAt(i + 1) - inPassword.charAt(i) == 1) {
        containsStraight = true;
        break;
      }
    }
    if (!containsStraight) {
      return false;
    }

    int pairCount = 0;
    int i = 0;
    while (i < inPassword.length() - 1) {
      if (inPassword.charAt(i + 1) == inPassword.charAt(i)) {
        pairCount++;
        i += 2;
      } else {
        i++;
      }
    }

    return pairCount > 1;
  }

  private String solveDay11aInc(String inString) {
    StringBuilder increased = new StringBuilder();
    for (int i = inString.length() - 1; i >= 0; i--) {
      char c = (char)(inString.charAt(i) + 1);
      if (c > 'z') {
        c = 'a';
        increased.insert(0, c);
      } else {
        increased.insert(0, c);
        increased.insert(0, inString.substring(0, i));
        return increased.toString();
      }
    }

    return increased.toString();
  }

  private int solveDay12b(InputStream inJson) {
    Stack<Event> stack = new Stack<>();
    int sum = 0;
    int subsum = 0;
    int objectDepth = 0;
    int redAt = Integer.MAX_VALUE;
    JsonParser parser = Json.createParser(inJson);
    while (parser.hasNext()) {
      Event e = parser.next();
      switch (e) {
        case START_ARRAY:
          stack.push(e);
          break;
        case END_ARRAY:
          stack.pop();
          break;
        case END_OBJECT:
          stack.pop();
          objectDepth--;
          if (objectDepth < redAt) {
            sum += subsum;
            subsum = 0;
            redAt = Integer.MAX_VALUE;
          }
          break;
        case START_OBJECT:
          stack.push(e);
          objectDepth++;
          break;
        case VALUE_STRING:
          if ("red".equals(parser.getString()) && stack.peek() != Event.START_ARRAY) {
            redAt = Math.min(objectDepth, redAt);
          }
          break;
        case VALUE_NUMBER:
          int i = parser.getInt();
          subsum += redAt <= objectDepth ? 0 : i;
          break;
        default:
          break;
      }
    }
    return sum + (redAt <= objectDepth ? 0 : subsum);
  }

  private int solveDay1a(String input) {
    int floor = 0;
    for (char c : input.toCharArray()) {
      switch (c) {
        case '(':
          floor++;
          break;
        case ')':
          floor--;
          break;
        default:
          break;
      }
    }
    return floor;
  }

  private int solveDay1b(String input) {
    int pos = 0;
    int floor = 0;
    for (char c : input.toCharArray()) {
      pos++;
      switch (c) {
        case '(':
          floor++;
          break;
        case ')':
          floor--;
          break;
        default:
          break;
      }
      if (floor == -1) {
        break;
      }
    }
    return pos;
  }

  private int solveDay2a(String dimension) {
    String[] parts = dimension.split("x");
    assert parts.length == 3;

    int[] nums = new int[parts.length]; // l,w,h
    for (int i = 0; i < parts.length; i++) {
      nums[i] = Integer.parseInt(parts[i]);
    }

    int[] sides = new int[parts.length];
    sides[0] = nums[0] * nums[1];
    sides[1] = nums[1] * nums[2];
    sides[2] = nums[2] * nums[0];

    int min = Math.min(Math.min(sides[0], sides[1]), sides[2]);

    return 2 * sides[0] + 2 * sides[1] + 2 * sides[2] + min;
  }

  private int solveDay2b(String dimension) {
    String[] parts = dimension.split("x");
    assert parts.length == 3;

    int[] nums = new int[parts.length]; // l,w,h
    for (int i = 0; i < parts.length; i++) {
      nums[i] = Integer.parseInt(parts[i]);
    }

    int max = Math.max(Math.max(nums[0], nums[1]), nums[2]);

    return 2 * nums[0] + 2 * nums[1] + 2 * nums[2] - 2 * max + nums[0] * nums[1] * nums[2];
  }

  private int solveDay3a(String input) {
    Map<Point, Integer> v = new HashMap<>();
    v.put(new Point(0, 0), 1);
    int cX = 0;
    int cY = 0;
    for (char c : input.toCharArray()) {
      switch (c) {
        case '^':
          cY--;
          break;
        case 'v':
          cY++;
          break;
        case '<':
          cX--;
          break;
        case '>':
          cX++;
          break;
        default:
          break;
      }
      Point p = new Point(cX, cY);
      Integer visits = v.get(p);
      v.put(p, visits == null ? 1 : visits + 1);
    }

    return v.size();
  }

  private int solveDay3b(String input) {
    Map<Point, Integer> v = new HashMap<>();
    v.put(new Point(0, 0), 1);
    int cXSanta = 0;
    int cYSanta = 0;
    int cXBot = 0;
    int cYBot = 0;

    boolean isSantasTurn = true;
    for (char c : input.toCharArray()) {
      switch (c) {
        case '^':
          if (isSantasTurn) {
            cYSanta--;
          } else {
            cYBot--;
          }
          break;
        case 'v':
          if (isSantasTurn) {
            cYSanta++;
          } else {
            cYBot++;
          }
          break;
        case '<':
          if (isSantasTurn) {
            cXSanta--;
          } else {
            cXBot--;
          }
          break;
        case '>':
          if (isSantasTurn) {
            cXSanta++;
          } else {
            cXBot++;
          }
          break;
        default:
          break;
      }
      Point p = isSantasTurn ? new Point(cXSanta, cYSanta) : new Point(cXBot, cYBot);
      Integer visits = v.get(p);
      v.put(p, visits == null ? 1 : visits + 1);
      isSantasTurn = !isSantasTurn;
    }

    return v.size();
  }

  private int solveDay4a(String input) {
    int result = -1;
    try {
      String md5;
      MessageDigest digester = MessageDigest.getInstance("MD5");
      do {
        String key = input + ++result;
        byte[] digested = digester.digest(key.getBytes(Charset.forName("UTF-8")));
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digested.length; i++) {
          String hex = Integer.toHexString(0xff & digested[i]);
          if (hex.length() == 1) {
            hexString.append('0');
          }
          hexString.append(hex);
        }

        md5 = hexString.toString();
      } while (!md5.startsWith("00000"));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return result;
  }

  private int solveDay4b(String input) {
    int result = -1;
    try {
      String md5;
      MessageDigest digester = MessageDigest.getInstance("MD5");
      do {
        String key = input + ++result;
        byte[] digested = digester.digest(key.getBytes(Charset.forName("UTF-8")));
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digested.length; i++) {
          String hex = Integer.toHexString(0xff & digested[i]);
          if (hex.length() == 1) {
            hexString.append('0');
          }
          hexString.append(hex);
        }

        md5 = hexString.toString();
      } while (!md5.startsWith("000000"));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return result;
  }

  private boolean solveDay5a(String input) {
    // may not contain ab, cd, pq, xy
    if (input.contains("ab")) {
      return false;
    }
    if (input.contains("cd")) {
      return false;
    }
    if (input.contains("pq")) {
      return false;
    }
    if (input.contains("xy")) {
      return false;
    }

    // must contains at least one letter twice in a row
    boolean twiceInARow = false;
    for (char c : input.toCharArray()) {
      if (input.contains("" + c + c)) {
        twiceInARow = true;
        break;
      }
    }

    if (twiceInARow) {
      // must contain 3 vowels
      int vowelCount = 0;
      for (char c : input.toCharArray()) {
        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
          vowelCount++;
        }
      }
      return vowelCount >= 3;
    }

    return false;
  }

  private boolean solveDay5b(String input) {
    char[] c = input.toCharArray();

    boolean pairExistsTwice = false;
    for (int i = 1; i < input.length(); i++) {
      String pair = "" + c[i - 1] + c[i];
      if (input.substring(i + 1).contains(pair)) {
        pairExistsTwice = true;
        break;
      }
    }

    if (pairExistsTwice) {
      // must contain xYx
      for (int i = 2; i < input.length(); i++) {
        if (c[i - 2] == c[i]) {
          return true;
        }
      }

    }

    return false;
  }

  private int solveDay6a(String... instructions) {
    final int size = 1000;
    BitSet[] field = new BitSet[size];
    for (int i = 0; i < size; i++) {
      field[i] = new BitSet(size);
    }
    Pattern p = Pattern.compile(".*?(\\d+),(\\d+) through (\\d+),(\\d+)");

    for (String instruction : instructions) {
      Matcher m = p.matcher(instruction);
      if (m.matches()) {
        int sX = Integer.parseInt(m.group(1));
        int sY = Integer.parseInt(m.group(2));
        int eX = Integer.parseInt(m.group(3));
        int eY = Integer.parseInt(m.group(4));

        boolean set = instruction.startsWith("turn on");
        boolean reset = instruction.startsWith("turn off");
        boolean flip = instruction.startsWith("toggle");

        for (int x = sX; x <= eX; x++) {
          if (set) {
            field[x].set(sY, eY + 1);
          } else if (reset) {
            field[x].clear(sY, eY + 1);
          } else if (flip) {
            field[x].flip(sY, eY + 1);
          }
        }
      }
    }

    int counter = 0;
    for (int i = 0; i < size; i++) {
      counter += field[i].cardinality();
    }

    return counter;
  }

  private long solveDay6b(String... instructions) {
    final int size = 1000;
    int[][] field = new int[size][size];
    Pattern p = Pattern.compile(".*?(\\d+),(\\d+) through (\\d+),(\\d+)");

    for (String instruction : instructions) {
      Matcher m = p.matcher(instruction);
      if (m.matches()) {
        int sX = Integer.parseInt(m.group(1));
        int sY = Integer.parseInt(m.group(2));
        int eX = Integer.parseInt(m.group(3));
        int eY = Integer.parseInt(m.group(4));

        boolean set = instruction.startsWith("turn on");
        boolean reset = instruction.startsWith("turn off");
        boolean flip = instruction.startsWith("toggle");

        for (int x = sX; x <= eX; x++) {
          for (int y = sY; y <= eY; y++) {
            if (set) {
              field[x][y]++;
            } else if (reset) {
              field[x][y]--;
              if (field[x][y] < 0) {
                field[x][y] = 0;
              }
            } else if (flip) {
              field[x][y] += 2;
            }
          }
        }
      }
    }

    long counter = 0;
    for (int x = 0; x < size; x++) {
      for (int y = 0; y < size; y++) {
        counter += field[x][y];
      }
    }
    return counter;
  }

  private Map<String, Integer> solveDay7a(String... instructions) {
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
            System.out.println(instruction + ": " + m.group(2) + " -> " + val);
            handled = true;
          } else {
            m = and.matcher(instruction);
            if (m.matches()) {
              Integer v1 = wireValues.containsKey(m.group(1)) ? wireValues.get(m.group(1)) : Integer.parseInt(m.group(1));
              Integer v2 = wireValues.containsKey(m.group(2)) ? wireValues.get(m.group(2)) : Integer.parseInt(m.group(2));
              Integer val = v1 & v2;
              wireValues.put(m.group(3), val);
              System.out.println(instruction + ": " + v1 + " and " + v2 + " : " + m.group(3) + " -> " + val);
              handled = true;
            } else {
              m = or.matcher(instruction);
              if (m.matches()) {
                Integer v1 = wireValues.containsKey(m.group(1)) ? wireValues.get(m.group(1)) : Integer.parseInt(m.group(1));
                Integer v2 = wireValues.containsKey(m.group(2)) ? wireValues.get(m.group(2)) : Integer.parseInt(m.group(2));
                Integer val = v1 | v2;
                wireValues.put(m.group(3), val);
                System.out.println(instruction + ": " + v1 + " or " + v2 + " : " + m.group(3) + " -> " + val);
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
                  System.out.println(instruction + ": not " + val + " : " + m.group(2) + " -> " + notted);
                  handled = true;
                } else {
                  m = rshift.matcher(instruction);
                  if (m.matches()) {
                    Integer v1 = wireValues.containsKey(m.group(1)) ? wireValues.get(m.group(1)) : Integer.parseInt(m.group(1));
                    Integer v2 = wireValues.containsKey(m.group(2)) ? wireValues.get(m.group(2)) : Integer.parseInt(m.group(2));
                    Integer val = v1 >> v2;
                    wireValues.put(m.group(3), val);
                    System.out.println(instruction + ": " + v1 + " >> " + v2 + " : " + m.group(3) + " -> " + val);
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
                      System.out.println(instruction + ": " + v1 + " << " + v2 + " : " + m.group(3) + " -> " + val);
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

  private int solveDay8a(String... lines) {
    int charCount = 0;
    int memCount = 0;
    for (String line : lines) {
      StringBuilder sb = new StringBuilder();
      charCount += line.length();
      String decoded = line.replaceAll("^\"", "").replaceAll("\"$", "");
      int i = 0;
      while (i < decoded.length()) {
        if (decoded.charAt(i) == '\\') {
          if (decoded.charAt(i + 1) == '"') {
            sb.append('"');
            i += 2;
            continue;
          } else if (decoded.charAt(i + 1) == '\\') {
            sb.append('\\');
            i += 2;
            continue;
          } else if (decoded.charAt(i + 1) == 'x') {
            try {
              String tester = decoded.substring(i + 2, i + 4);
              sb.append((char)Integer.parseInt(tester, 16));
              i += 4;
              continue;
            } catch (NumberFormatException e) { // No hex
            }
          }
        }
        sb.append(decoded.charAt(i));
        i++;
      }

      decoded = sb.toString();
      memCount += decoded.length();
      System.out.println(line + " --> " + decoded);
    }

    return charCount - memCount;
  }

  private int solveDay8b(String... lines) {
    int charCount = 0;
    int memCount = 0;
    for (String line : lines) {
      charCount += line.length();
      String encoded = "\"" + line.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
      System.out.println(line + " --> " + encoded);
      memCount += encoded.length();
    }
    return memCount - charCount;
  }

  private int solveDay9a(String... lines) {
    // find places; try all orderings, output minimum traveling distance
    Pattern desc = Pattern.compile("(\\w+) to (\\w+) = (\\d+)");
    Set<String> p = new HashSet<>();
    Map<String, Integer> distances = new HashMap<>();
    for (String line : lines) {
      Matcher m = desc.matcher(line);
      if (m.matches()) {
        p.add(m.group(1));
        p.add(m.group(2));

        distances.put(m.group(1) + "_" + m.group(2), Integer.parseInt(m.group(3)));
        distances.put(m.group(2) + "_" + m.group(1), Integer.parseInt(m.group(3)));
      }
    }

    Map<String, Integer> route2distance = new HashMap<>();
    solveDay9aOrderings(p, distances, route2distance, new LinkedList<String>());

    int result = Integer.MAX_VALUE;
    for (Entry<String, Integer> entry : route2distance.entrySet()) {
      if (entry.getValue() < result) {
        result = entry.getValue();
        System.out.println(entry.getKey());
      }
    }

    return result;
  }

  private void solveDay9aOrderings(Set<String> places, Map<String, Integer> distances, Map<String, Integer> route2distance, List<String> route) {
    if (places.isEmpty()) {
      System.out.print(route);
      int cost = 0;
      String lastPlace = null;
      for (String p : route) {
        if (lastPlace == null) {
          lastPlace = p;
        } else {
          cost += distances.get(lastPlace + "_" + p);
          lastPlace = p;
        }
      }
      System.out.print(" ");
      System.out.println(cost);
      route2distance.put(route.toString(), cost);
    }

    for (String p : places) {
      route.add(p);
      Set<String> pcs = new HashSet<>(places);
      pcs.remove(p);
      solveDay9aOrderings(pcs, distances, route2distance, route);
      route.remove(p);
    }

  }

  private int solveDay9b(String... lines) {
    // find places; try all orderings, output maximum traveling distance
    Pattern desc = Pattern.compile("(\\w+) to (\\w+) = (\\d+)");
    Set<String> p = new HashSet<>();
    Map<String, Integer> distances = new HashMap<>();
    for (String line : lines) {
      Matcher m = desc.matcher(line);
      if (m.matches()) {
        p.add(m.group(1));
        p.add(m.group(2));

        distances.put(m.group(1) + "_" + m.group(2), Integer.parseInt(m.group(3)));
        distances.put(m.group(2) + "_" + m.group(1), Integer.parseInt(m.group(3)));
      }
    }

    Map<String, Integer> route2distance = new HashMap<>();
    solveDay9aOrderings(p, distances, route2distance, new LinkedList<String>());

    int result = Integer.MIN_VALUE;
    for (Entry<String, Integer> entry : route2distance.entrySet()) {
      if (entry.getValue() > result) {
        result = entry.getValue();
        System.out.println(entry.getKey());
      }
    }

    return result;
  }

  /**
   * @see <a href="http://adventofcode.com/day/1">Day 1</a>
   */
  @Test
  public void testDay1() {
    assertEquals(0, solveDay1a("(())"));
    assertEquals(0, solveDay1a("()()"));

    assertEquals(3, solveDay1a("((("));
    assertEquals(3, solveDay1a("(()(()("));
    assertEquals(3, solveDay1a("))((((("));

    assertEquals(-1, solveDay1a("())"));
    assertEquals(-1, solveDay1a("))("));

    assertEquals(-3, solveDay1a(")))"));
    assertEquals(-3, solveDay1a(")())())"));

    final String input = "()(((()))(()()()((((()(((())(()(()((((((()(()(((())))((()(((()))((())(()((()()()()(((())(((((((())))()()(()(()(())(((((()()()((())(((((()()))))()(())(((())(())((((((())())))(()())))()))))()())()())((()()((()()()()(()((((((((()()())((()()(((((()(((())((())(()))()((((()((((((((())()((()())(())((()))())((((()())(((((((((((()()(((((()(()))())(((()(()))())((()(()())())())(()(((())(())())()()(()(()((()))((()))))((((()(((()))))((((()(()(()())())()(((()((((())((((()(((()()(())()()()())((()((((((()((()()))()((()))()(()()((())))(((()(((()))((()((()(()))(((()()(()(()()()))))()()(((()(((())())))))((()(((())()(()(())((()())))((((())))(()(()(()())()((()())))(((()((()(())()()((()((())(()()((())(())()))()))((()(())()))())(((((((()(()()(()(())())))))))(()((((((())((((())((())())(()()))))()(())(()())()())((())(()))))(()))(()((()))()(()((((((()()()()((((((((()(()(())((()()(()()))(())()())()((())))()))()())(((()))(())()(())()))()((()((()(()()())(())()()()((())())))((()()(()()((()(())()()())(((()(()()))))(())))(()(()())()))()()))))))()))))((((((())))())))(()(())())(()())))))(()))()))))))()((()))))()))))(()(()((()())())(()()))))(((())()))())())())(((()(()()))(())()(())(())((((((()()))))((()(()))))))(()))())(((()()(()))()())()()()())))))))))))))(())(()))(()))((()(())(()())(())())(()())(())()()(()())))()()()))(())())()))())())(())((())))))))(())))(())))))()))))((())(()(((()))))(()))()((()(())))(()())(((((()))()())()()))))()))))()))())(()(()()()))()))))))((()))))))))))()((()))((()(())((())()()(()()))()(()))))()()(()))()))(((())))(())()((())(())(()())()())())))))))())))()((())))()))(()))()()))(((((((()))())(()()))(()()(()))()(()((()())()))))))(((()()()())))(())()))()())(()()))()()))))))))(())))()))()()))))))()))()())))()(())(())))))()(())()()(()()))))())((()))))()))))(()(((((()))))))))())))())()(())()()))))(())))())()()())()()())()(()))))()))()))))))))())))((()))()))()))())))()())()()())))())))(()((())()((()))())))))())()(())((())))))))))))())()())(())())())(()))(()))()))())(()(())())()())()()(()))))(()(())))))))(())))())(())))))))())()()(())())())))(())))))()))()(()())()(()))())())))))()()(()))()))))())))))))))()))))()))))))())()())()()))))()())))())))))))))))()()))))()()(((()))()()(())()))))((()))))(()))(())())))(())()))))))(()))()))))(())())))))()))(()())))))))))))))())))))))))()((()())(()())))))))((()))))(())(())))()(()())())))())())(()()()())))()))))))())))))())()()())))))))))))()()(()))))()())()))((()())(()))))()(()))))))))))()())())(((())(()))))())()))()))()))))))()))))))(()))))()))))()(())))(())))(()))())()()(()()))()))(()()))))))))()))(()))())(()()(()(()())()()))()))))))))(())))))((()()(()))())())))))()))())(()())()()))())))()(()()()()))((())())))())()(()()))()))))))))(()))(())))()))))(()(()())(()))))()())())()))()()))())))))))))))())()))))))()))))))))())))))()))))())(()())))(())()))())())))))()()(()()())(()())))()()))(((()))(()()()))))()))))()))))((())))()((((((()()))))))())))))))))))(((()))))))))))))(())())))))())(()))))))(()))((()))())))()(()((()))()))()))))))))))())()))()(()()))))())))())(())()(()))()))())(()))()))))(()()))()()(())))))()))(())(()(()()))(()()())))))(((()))))))()))))))))))))(())(()))))()())())()()((()()))())))))(()))))())))))))()()()))))))))())))()(((()()))(())))))(((())())))))((()))()(()))(()))))(()())))(()))())))))()))))(())(())))()((()))(())())))()()))()))))))))()))(()()()(()()()(()))())(())()())(((()))(())))))))))(((()())))()()))))))))()(())(()))()((((())(())(()())))()))(((())()()()))((()))(()))())())))())))(()))())()())())(()(())())()()()(())))())(())))(())))(())()))()))(()((()))))))))())(()))))))())(()()))()()))()(()(()())))()()(()((()((((((()))(())))()()()))())()))((()()(()))())((()(()(()))(()()))))()())))()))()())))))))()()((()())(())))()))(()))(())(()))())(()(())))()()))))))(((()(((()()))()(()(())())((()()))()))()))()))()(()()()(()))((()())()(())))()()))(((())()()())(())()((()()()()(()(())(()()))()(((((()())))((())))))(()()()))))(((()(())))()))((()((()(())()(()((())))((()())()(()))(((()())()()(()))(())(((()((()())()((())()())(((()()))((()((())(()))(()())(()()()))((()))(())(()((()()())((()))(())))(())(())(())))(()())))(((((()(()(((((()())((((()(()())(())(()()(((())((()(((()()(((()()((((((())))())(()((((((()(()))()))()()((()((()))))()(()()(()((()()))))))(((((()(((((())()()()(())())))))))()))((()()(())))(())(()()()())))))(()((((())))))))()()(((()(()(()(()(()())()()()(((((((((()()())()(()))((()()()()()(((((((()())()((())()))((((((()(()(()(()())(((()(((((((()(((())(((((((((())(())())()))((()(()))(((()()())(())(()(()()(((()(())()))())))(())((((((())(()()())()()(((()(((())(()(((())(((((((()(((((((((()))(())(()(()(()))))((()))()(())())())((()(()((()()))((()()((()(())(())(()((())(((())(((()()()((((((()()(())((((())()))))(())((()(()((())))(((((()(()()())())((())())))((())((()((()()((((((())(((()()(()())())(()(()))(()(()))())())()(((((((()(((()(())()()((())((()(()()((()(()()(((((((((((())((())((((((())((()((((()(()((((()(((((((())()((()))))())()((()((((()(()(((()((()())))(())())(((()(((())((((((()(((((((((()()(())))(()(((((()((((()())))((()((()((()(()()(((())((((((((((((()(((())(()(((((()))(()()(()()()()()()((())(((((((())(((((())))))())()(()()(()(()(((()()(((((())(()((()((()(((()()((()((((())()))()((((())(())))()())(((())(())(()()((()(((()()((((((((((()()(()())())(((((((((())((((()))()()((((())(()((((()(((())())(((((((((((()((((())))(())(()(((()(((()((())(((((()((()()(()(()()((((((()((((()((()(()((()(()((((((()))))()()(((((()((()(()(())()))(())(((((((()((((()())(()((()((()(()))())))(())((()))))(((((((()()()())(()))(()()((()())()((()((()()()(()(()()))(()())(())(((((()(((((((((((()((()(((()(((((((()()((((((()(((((()(()((()(((((())((((((()))((((())((()()((())(((())()(((((()()(((((()((()(()(((((((()(((((()((()((()((())(())((())(()))()()))(()()(()(()()(((((((()(((()(((())()(((((()((((((()())((((())()((()((()(()()())(()))((((()()((((((()((()(()(()((((()((()((())((((((()(()(())((((((()((((((((((()((())()))()(()(()(((((()()()))((())))()(()((((((((((((((()(((()((((()((())((()((()(((()()(()(((()((())(()()())))()(()(()(((((()()(()(()((((()(((((())()(()(()))(((((()()(((()()(())((((((((((((((())((())(((((((((((())()()()(())()(()(()(((((((((())(((()))(()()())(()((((()(())(((((()())(())((((((((())()((((()((((((())(()((()(())(((()((((()))(((((((((()()))((((()(())()()()(())(()((())((()()))()(((())(((((())((((((()()))(((((((((()((((((())))(((((((()((()(()(())))())(()(()))()(((((()())(()))()(()(())(((()))))())()())))(((((()))())()((()(()))))((()()()((((((()))()()((((((((())((()(()(((()(()((())((()())(()((((())(()(((()()()(()(()()))())())((((((((((())())((()))()((())(())(())))())()(()()(())))())(()))(((()(()()(((()(((())))()(((()(())()((((((())()))()))()((((((()(()(((((()())))()))))())()()(((()(((((())((()()(()((()((()(()(()(())))(()()()()((()(())(((()((()))((((()))())(())))())(()))()()()())()))(((()()())()((())))(())(()()()()(()())((()(()()((((())))((()((()(())((()(()((())()(()()(((()())()()())((()))((())(((()()(())))()()))(((()((())()(((((()())(())((())()())())((((((()(()(((((()))(()(";
    System.out.println(solveDay1a(input));

    assertEquals(1, solveDay1b(")"));
    assertEquals(5, solveDay1b("()())"));
    System.out.println(solveDay1b(input));
  }

  /**
   * @see <a href="http://adventofcode.com/day/10">Day 10</a>
   */
  @Test
  public void testDay10() {
    assertEquals("11", solveDay10a("1"));
    assertEquals("21", solveDay10a("11"));
    assertEquals("1211", solveDay10a("21"));
    assertEquals("111221", solveDay10a("1211"));
    assertEquals("312211", solveDay10a("111221"));

    String input = "3113322113";
    for (int i = 1; i <= 50; i++) {
      input = solveDay10a(input);
      System.out.print(i);
      System.out.print("\t");
      System.out.print(input.length());
      System.out.println("\t");
    }
  }

  /**
   * @see <a href="http://adventofcode.com/day/11">Day 11</a>
   */
  @Test
  public void testDay11() {
    assertFalse(solveDay11a("hijklmmn"));
    assertFalse(solveDay11a("abbceffg"));
    assertFalse(solveDay11a("abbcegjk"));
    assertFalse(solveDay11a("abcdefgh"));
    assertTrue(solveDay11a("abcdffaa"));
    assertFalse(solveDay11a("ghijklmn"));
    assertTrue(solveDay11a("ghjaabcc"));

    String candidate = "hxbxwxba";
    while (!solveDay11a(candidate)) {
      candidate = solveDay11aInc(candidate);
    }
    System.out.println(candidate);

    candidate = solveDay11aInc(candidate);
    while (!solveDay11a(candidate)) {
      candidate = solveDay11aInc(candidate);
    }
    System.out.println(candidate);

  }

  /**
   * @see <a href="http://adventofcode.com/day/12">Day 12</a>
   */
  @Test
  public void testDay12() throws Exception {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(Solutions.class.getClassLoader().getResourceAsStream("2015/day12.txt")))) {
      String line = null;
      while ((line = r.readLine()) != null) {
        Pattern p = Pattern.compile("(-?\\d+)");
        int sum = 0;
        Matcher m = p.matcher(line);
        int i = 0;
        while (m.find(i)) {
          sum += Integer.parseInt(line.substring(m.start(), m.end()));
          i = m.end();
        }
        System.out.println(sum);
      }
    }

    assertEquals(6, solveDay12b(new ByteArrayInputStream("[1,2,3]".getBytes())));
    assertEquals(4, solveDay12b(new ByteArrayInputStream("[1,{\"c\":\"red\",\"b\":2},3]".getBytes())));
    assertEquals(0, solveDay12b(new ByteArrayInputStream("{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}".getBytes())));
    assertEquals(6, solveDay12b(new ByteArrayInputStream("[1,\"red\",5]".getBytes())));

    int sum = solveDay12b(Solutions.class.getClassLoader().getResourceAsStream("day12.txt"));
    System.out.println(sum);

  }

  /**
   * @see <a href="http://adventofcode.com/day/2">Day 2</a>
   */
  @Test
  public void testDay2() throws Exception {
    assertEquals(58, solveDay2a("2x3x4"));
    assertEquals(43, solveDay2a("1x1x10"));
    try (BufferedReader r = new BufferedReader(new InputStreamReader(Solutions.class.getClassLoader().getResourceAsStream("2015/day2.txt")))) {
      int sum = 0;
      String line = null;
      while ((line = r.readLine()) != null) {
        sum += solveDay2a(line);
      }
      System.out.println(sum);
    }

    assertEquals(34, solveDay2b("2x3x4"));
    assertEquals(14, solveDay2b("1x1x10"));
    try (BufferedReader r = new BufferedReader(new InputStreamReader(Solutions.class.getClassLoader().getResourceAsStream("2015/day2.txt")))) {
      int sum = 0;
      String line = null;
      while ((line = r.readLine()) != null) {
        sum += solveDay2b(line);
      }
      System.out.println(sum);
    }
  }

  /**
   * @see <a href="http://adventofcode.com/day/3">Day 3</a>
   */
  @Test
  public void testDay3() {
    assertEquals(2, solveDay3a(">"));
    assertEquals(4, solveDay3a("^>v<"));
    assertEquals(2, solveDay3a("^v^v^v^v^v"));
    final String input = "v>v<vvv<<vv^v<v>vv>v<<<^^^^^<<^<vv>^>v^>^>^>^>^><vvvv<^>^<<^><<<^vvvv>^>^><^v^><^<>^^>^vvv^<vv>>^>^^<>><>^>vvv>>^vv>^<><>^<v^>^>^><vv^vv^>><<^><<v>><>^<^>>vvv>v>>>v<<^<><^<v<>v>^^v^^^<^v^^>>><^>^>v<>^<>>^>^^v^><v<v>>><>v<v^v>^v<>>^><v>^<>v^>^<>^v^^^v^^>>vv<<^^><^<vvv>^>^^<^>>^^^^^v^<v>vv<>>v^v<^v^^<><^<^vv^><>><><>v>vvv^vv^^<<><<vvv><<^v^><v<>vvv^<^>vvvv^>^>>^v^<v^vv<^^v<>v>vv^<>><v<<<^v^<<><v<^<^<><^^^>^>>v>^>v^<>v><^<^<v^>^^vv<^^<>v^v^vv<>>>>v^v<>><^^v>vv^^>v^v>v<vv>>v>><v^v^v>vv>^^>^v><<vv^v^^vv<^v><^<<v<v^>vv^^^<v^>v>v^^^>><^^<v^<^>>v><vv<v^^>^^v>>v^^^<^^v>^v>><^<^<>>v<<^^vv>^^^v<^<^<v<v^^vv>^vv^>>v^><v>><<<>^vv^<^<>v^^<<<v<^>^><><v^^>>^^^<^vv<^^^>><^^v>^^v^<v^v^>^^<v>^<^v<^<<<<^<v^>v^<^^<>^^>^><<>>^v><>><^<v><^^^>>vv>^><vv>^^^^^v^vvv><><^<^>v>v^v^>^><><^<^><>v<<vv<^>><>^v^^v>^<<<>^v^>^<<v^vv<>v^<v^^vv><<v^<>>>^<v>vv>v>>>^<^>><vv<>>>>v<v>>>^v>v><>>vvv<^^><<^>^>v<^vvvv<v><vv<><^^^v^^^>v^v<>v<^^v>>><>v<v^>>v><v^>>^^<v<<<^<v<><^^v><<v^><<<<^vv<^<>^><vv<<<<^>>>^v>^v>vv>^v<>v>v<v><^>>v>>^>^><^<v^v^>^v<><><^^>^<vvvv^^<>^^^>vv^v^v>^v^^v^^v><v^<^<>><^<v>v>>vv<<v>>vvvv<vv><>>^v^>^>>v^v^<<<vv<><v<<>>>^v<<v>^^vv^><>v>^>v><<<<<<<^>^^v^<<^^>>vvv^<><>><>^^v<<vv><^^v<^^><vv>v^>>>v^v><v^v<^>v^><>v<<>v>^^v><<<<><^v^v>>^<>^<<>^<v<<>>v<<>><^<<<<^v>^<^v>v>vv<v<v<<>^>v<^<<>v^<vvvv^>v>><<v><v<>v>v>>v^vvv^^>>>v^<^<<^^<<<><v>v^<<v<<<>v<^^<><v<v^^<v>^>v>>v<>^>^^>>^v<<>v^^^>>>^vv<^v<v>^>v>^><>v^^<>^^v^^vv^<^>^<<>><<^>^v>>><<<vvvv><<><v<^v^v<vvv^<><<^<vv><v^v^v^>v>v^<vvv^><^><^<vv><>>v^>^^^<>><v^<^^^<>v<<v<^v>>>^>>v^><<>vvv><^>>v><v><>v>>^>v><<><<>^<>^^^vv><v^>v^^>>^>^<^v<v<^^<^vvvv>v<v>^>v^>^><^<vvvv><^><><<v<>v<v^><^<v^>^v^^<<<<^><^^<^><>>^v<<^<<^vv>v>>v<^<^vv>><v<vv>v<v<v>^v<>^>v<>^v<<<v>>^^v>>><vvv>v^>^v^v>^^^v<vvvv>><^>vvv^<vv^^vv><<<>v<>v>^<vvv^<^<v<v<^vv^^>>vv^<^^v^><^^^^^v<^<v<^>>>vv^v^>^<v>^<><v^<^v>>><^v^<<v<<v<>v>^v<v^v>>^^v<<v<v<<>>>vv>>^v>>^<<<<^><<<><^^>>v<>^vvvv>v^^^>^^^>^<vvvv><^^v<v<>v<^v^v<<v^^^v^<v<^v>v^^<>^>^<^v>vv<v^vv<^<<>v><<^><><^^v<<><^^><>^v>^<><<^<^^<<>vv<>^^<<^>><<<>>vvv>^>v^^v^><<^>v>^>^<^<<>v<^>vv^v^v<>vv<<v>vv<vv><^>v^<>^vv^v^<v<^>>>>v^v><^<><<>vv^<vvv^>>vvv^>v>>><^^vv<vvvv>v<^<^>>^^>^^vv>>><^v<>^v^<<>v^^^<v>^>>^<^<v>>^v<^^^<v>^v>^>>v<vv>>^<v^<<>>^>>><v>v^<<^<v>>^<<^^<>v<^v<^<>v^v>^^v<vvvv>^vv>vvv>v^<^>><v^^vv<<<^>vvvv<>>^^<>v^<><>v<^<>v<>^>v<>vv<v^v>>v<v<^<v^^v^vv^vvv><^^>v>><>>^<^^<>>^>^<v^>>vvv^v><v>>^>^>v><><<><vv^v>v<>^v<^vv^^^<>^^<<^^^v<>><v<^<^<^<^^><v^v<^>v^>vvvv>^^v^>^<v<^^^>>^<<vv^<><><^^^^<<>^<><v>vv^<><^>^^<>v^<>>>v><>vvvvv>v>v^^>^<<vvvv<>vv>>v<<^<>^^^v^<><>>^<<<v<v<>>>><><v>v<v<>>^>^^^^vv^^<<><^^<<vv<^<>v>vv<v<><<<^<<v<<<<>v<>>^<^>^>><v>v>><^^<>><<<><<><v^^v<<><^<^v<v^><^^v<<>><<<<^>v^<v>><v^><v<vvv>v^v^<v><<>>v<><<v>^<>><>>^><>v^v>v<<>v<>v^^><<>>>v<<>>>>^>v>><v<<>>>vv>v>^<^^^<>v<v>^<^^v^vvv^>vv>^<v><vvvv>^<<>vvv<<<vv>^^<^>^>>v>v<<<<<>^^vv^>>v>^<^<v^v^>^v>>v>^v<><>^<^>v>v<<<^^^v>^<<<>vvv^v^^>^>>^>v>v<>^^><>>v>^>v<<<^^^v^<v^vv>><><^<^<><vvv<v^>>^v>vv<^v<<^vv>v^<<v>v>v>^v^>^v<<^v^vv>v<v>^<<><v^>>v<>><v<<<^v<<>vvv^<vv<vvv<<>^vv^^v><^>v^vv<<v^<<^^^<^<>^^<<>v<><<v>^><>^<><<v<v^^>vv<>^<v<^<vvv>vv>v><^^v<>><^v^v><><>><v<v>vv<>>><v^^v<>><<^>>><^^^vvv<<<vv<<^v<<<>><<vv>>>>v<<<<<vv><><v>v^^<<^vv^<vv<>>vv>^<>^v^^<>^^^vv>v^^<v<><v>v<v>>^v<v<>>^<v^^><>v^^^>v^^v<vv><^>v^v^<>v>v<v<^^>>v<^^vv^v<^^^^vv<<><<^>>^^<<v^^<<^>v^>>^^^><^^>^v^v>^<<v<vv<<<v<^^^>^>>^v<>^<^>v>^>^v^<^^^<^vv<v><^^>>v<v>^>^v^>>>>^v>^^<<^<v^v<^<<v<<^><^^<v^<><v>v^<<v^^<><<>>><vv<<><>^<>>>v<<v^^^v^^<<<vv<<^<^<^vv^<><><<^^<^^>v^>^<v<>>v^v<><<v>^^v>^<^<vvv<v>v^v>>>^^<^<v^>^vv<<<v<<>^><><^<>v>>>v<v^<>v>><^^^v^^^v<^^<vv^^^>v>v<>>^^<><>v>^<v<>^>>>><>v>^v>^vv^v<vv<<^^>><v<>^>^^<v<^>^<vvv>><>^<<>>><<<><>^^<<<v<>v^>v>v<v>^^^>^>^v<<>v>vv>><<<v>^^<v><vv<<v^^>^>>^><^>v<^<^v>><^^>v<vv^^><><>^><<><>v^>v<><^^>><>^<^^v<^<<v>><v><<<^^<<v<^vv^v<>><>>>^>v<vvv^>^<><v^><^<<^vv<^v^v^v<>v^^v>v^<^>^vv^>>><<>v^vv^<>^v^><<v^v<v>v^<><>>v^v^><>v^vvv^^^<<^<<v<<v<^vv^>>v^v>^^<v<>><>v>>v^<>^>v>^>><<>v^v><^v>v>>><v<v><^<^^>vv<v><^>^<^>^^v><><v<^^v<<><^<<v^<v<<><^^vvv^v>^>^<>>vv>v^^v^^vv<^^>><v^^vv><^v>v^<<v<^v>vvv<>>^v><<>^v<<<>^><^vv><<^^<v^>v<<v>^vv<>^v>>>><<<<^^<^v>^<^^<^<^^>>^^v>^^^^v^^^<<>^^vv<<v^^><v>><^<<><>^>v<>>v^^^>^v^^v^<v^v>v>>>>>^v>^>^^<vvv^^<v^<<<v<<>v>><^^^v<<^^<v>>^<^<^><^<<v^v><<vv<^<>>v>v>^v<><<v>^>vv^v<v>v><^<v>><>^<vv<v^^^^v<^^>><<^^>v>v>^^^<>v>^v^^>vv^vv<^^>><>^>^<>v>><>^v<<v>v>^><^^^v^<vv><<^v^>v^>vv>v^<>v><vv><^v>v<><v^v^v<^v<>^v<v^<<><<v>>^v><v>^^<>vvv^>^<<v^>><^>><^<>^v<v<v<^vvv<><<^v^<v>><<<v>^<^<v>v>^vv^v>v<^^vv<<vvv^<v>><>vv^>v<<>v<vvvv>>v>^^>>><<<^>^vv>><v>^^^>v<^vv<>v<<<v<<<<v>>>>^<^^^^>v<^^<><v>v>v<v^>vv^>v>v<^>^v^<>v>>vvv>^^><^vvv>><>>>^<<^<v<>>>v^^><v<v>>^><>v<^^v^<<v><>^<>>><^v^v>>>^vvvv^<><<<v<^>>v>^v^<v<v<<^<<v^vv^v>v<v<>>v<v^<<<><v^>><^<<^>^^><v>v<^v^<^>v>^<<v>v^<>v^<>vv^<>^>^>v^>^vv<>^^<<>>v<>^v<><v^><><<<vv>v>v^>vv^><<<<v>^v<><>^^<vv>v^^v^^^<v<^^><v^v<>><v<vv>^<>>><vv<^v<<>>^><>>v<v^v^>>>v<<>v<<<<<<<^v<<^^^v<^v<>v^^<<<^<>>v^vv<v>^<^^<^^<<^>vv><^<^^v<<<^><^v<^><>v<vv^>^v^^>>><<vv^^v><^<<^<>>^>>^<<<<v^vv<>>>v>^v>><>v>>v>><>v>><^^><v>^^vv<^^<^>vv><<^>><<><v>^vvv><^v^>vvv^>>^<><^>^<<>>v^v>v<<>^>>^>v<^^<^<<>^^v<vvvvv^^^<^<>^^v>v<>^<^^<<v>v^^vvv^^v>^vv<v^>^<>v<^v^>^<v><v<<<^v<v<v^^<vvv>vv<<vv>v^<<v<^<vv><^>^><^^<^^<<v^^<v^v<v^^^^>^>vv^<>^<>^>^^<^v><<<^>vv^vv>v^v<>^^v^<^^^vvv^><v^<v^^<v<>v^<><>v>vv<^v^>>^v<^^vv>vv>^>><<<<v^^<^><>^><>>v<>>v>^v<^vv>^^>^<^<<v^>>v^v<^^v<vv<^<><^^>^^<>^^^<vv<v<<^^>^>^vv<^>><^<vvv^<>>vv^><v>v^>^vv>^>v^^<>>^v<>>v<^>^v>vv^<vv<^^>>^<v>>>>vvv>vv>^><^v<<<>^^v>v^v<^^^v^^>^><<^^>^<v>><^^^^^<v<vv<v<^<>^^<^v<^>>vv>>^v^vv<>><>^>>>^<v>^^^^><^<<<v<>^v<><vvv^<^^>vv^>>v<vvvv><v^v><^vv<^v<><vvv<vv>v<>^v^<<>>>>v^^>^vv<<vvv<^^><v><><<>v^v<^<^>><vv>^^><^>^><<><v<^v^><^<><>vv>>>>^><<^^^<^v^>^>^^>^<^><v><^^<^^<>><><v>><<<>^>^^v<>^<<<v>>vv>^>>^>^<>>vv<^^vv<>v<>^^>^v<v^^^^v<>^<v>v^v>^^^<v>v<<<^vv^><>^<v>>^^vv>v^<<^><>>vv^^^^^>v>>v<<<>^<vvv<<><><^v<^v<^>^<>^vvv>^>v><<<vv<>v>vv<v<<v>^<^^>v^v>^<^v^<<vvv^^<>^v<<^>^<><>^^<>>^^<^v^<^<v<><<^><v<>v^^>v^v^^^<^v<<^v>^>>^^^^^><<<vv^>>v^><v^^vv><>v^^<^v<^<v^^><<v>v^^^><^^^><<<<<>^<<^<>>v<<v^v^^v<<>^<vv>>><^^^<>>>>vvv>v<>>>v^v^v<^<<^>^<<>v>>^>^^><^><<^v^^<^<>v^v>vv<>>>>>>v<<><v^<v<>>^^>v<<<>^<<v><^><<^v>vv>>>><><>v^<^v><v^<<<<^v><^>v>>^^^v<^>>^>>v<<^<<>vvv>>^v<>>^v><<<^v^v<><v>^vvv<v<v>^^^<><vv^<<>vvv<v<^^v^^><v<^v<^v^<v<^>^^^>>v>^<v^>>^<><<><vv<>vv>^v^>>^<<v<^^v>v<v<vvv>><><<><vvvvv<^v<^>^^><>^<<>^v<<>>v^vv<<>^^v^v^v><^>v>v<^<<^<^>vv>^v<<^>^>>v^<<v^>v^^v^^<v^v>>><vv><<<>^v>><><v<vv<^>v<>><^v>^^v<<<<^v^vv<<<<><><^<^<^v><<^^v^<<<<<^^><^^>vv<v<^<v>v<^><><v<>vvv^<vv>v^>^>^^^v<<^<^^>vv<v^v^v>^vv^><^v^<<>v<^^>^vv<<>^<<><^>v^<<^<>v><><>v<<^^><^^^v>>v>^vv<v^>>^v^^<><<<<<^>^v^<^<^^>^vv<^>v^^v^<>v<><v>v^v>vvv><><<><>vv<vvv^v>^^>^^^<><^>^^^>v<vvvv<>vv<v<v^^>><>v<>>v^>v^^vv^>v>>><v<<<<v<^v>><^^>^v^v<v^v^^^vvv>>>vv<^>><<<^>><^<^>^<^>^>>v^<^<>^<^^<><vvv^^<>^<>>><<v>^<^<v<<><^<<^><^^>vv<>^^><v^v<vv<^<vvv<<^>v^>>v>>>v<<^vv^<><>>>^^<^v^>>^>>><<v<<^<vv><^<>^>>^v>>><^^^<<<vv<<v<v>^vv><><<>^^^<>^<vv^<^<<v>^^><vv>><>>>^>vv>^<^<>>^<^^><v>v^><v>vv><><>>><><<^^v<<^v<v>vv<><><<^v>^v<>^<^^^v^>^<^><^v>v>^v<>><^^v^^^^^<><v<>>vvv<v^^<>v>>>>^<<><^v>vv>>^^><<><><^^^<^<^<<^v>^^^><v>>>>><<v<v>v^^^<>>v<vv<^<>v^^^v<><^>v>><<><>v<^><<>>><>v>^<>>^>v^v<<<<>^<v^vv^>vv<<><v^vv<v<v<<>>>>>vv<><>^<^v>vv^<<v<^v^^<<^<<^^v^>>><<>^<>><^>>><v<>><<>^^>><<<^^^^^v>>^<<>>vvvv<^v<v^^<^>^vv<vv<>v<<<^><>>>>vv^<^v>v<^<>^v>>^<^^v^>>><>^^<^v>>v<<>vv<vvvv<>vv>^><>v^<>^<<^vv<v^^v<vvvv><^>>^v^>^^<<<^>>^^>^<^^<^<<<v^<^^v<<vv^<<^^^vv><v<vv^>v^^v<v>^^<^v<^>>><<>vv<<^><<v^v^^^v<vv>^>vv<^>>^<v<>vv>>>^>>><<v<^<>^<<<>>^<<>><^<<^^^>>v^^>v<<<>v>v>v<v<^>^<>>>^vvv><<^^<<><v<><^<v<vvv>v>>>>vv^^v<v<^<^><v>^v<<v<vv>>v>v<<<<><<>vv<><^^^<>>v<v<vvv><v^<vv^>>><v^^<>>>^^<><^<^v^><vv>>^^v>^<<v^>v>^^>^v^<v<^<v^v><>>v^^<^v^^<<>^^>v^^>><<<<^<^^v>^^v>v<<vv^^vv>^>v^<v<v><>vv>>^<v^v^<v<^>^v>v^^>vvvvv<v><<>vv>vvvvvv>>v>>^^^<v>vv^^><<v>>v^^^^v>vv>v<^v>>>>^>^><v^>^<v<vv>v>^>><v>><<>>^vv<vv^^<^^>>>>><><<^<v<><<v>^><^vv^v>>>>>v>^>^<vv>^v^>v<^v^<^<<vv<<>v<>>^vv<<>^v^v>><><<>>v^^<<>^^<v><>v<<^^<^^>^^>^<^><>>v<>>^^<^>><<<v<>>>^v^>v>v<<^^<<^>v<v^>>v^^v^^<<>^v>v><v^>v<^^>^<vv><vv^<>v<><^<<<vv<<v>v<^<<<<^^>v^v^^><<><^^^<v>v^^>>>vvv><>vv<>>^^v^v<<^>v^^v^>vv>^<<v<^<v^>^^<<v<^^>^v^^<^^v<<>>vv<^>>^><><>v>>v<>^<v^^><<>>>";
    System.out.println(solveDay3a(input));

    assertEquals(3, solveDay3b("^v"));
    assertEquals(3, solveDay3b("^>v<"));
    assertEquals(11, solveDay3b("^v^v^v^v^v"));
    System.out.println(solveDay3b(input));
  }

  /**
   * @see <a href="http://adventofcode.com/day/4">Day 4</a>
   */
  @Test
  public void testDay4() {
    assertEquals(609043, solveDay4a("abcdef"));
    assertEquals(1048970, solveDay4a("pqrstuv"));

    final String input = "yzbqklnj";
    System.out.println(solveDay4a(input));

    System.out.println(solveDay4b(input));
  }

  /**
   * @see <a href="http://adventofcode.com/day/5">Day 5</a>
   */
  @Test
  public void testDay5() throws Exception {
    assertTrue(solveDay5a("ugknbfddgicrmopn"));
    assertTrue(solveDay5a("aaa"));
    assertFalse(solveDay5a("jchzalrnumimnmhp"));
    assertFalse(solveDay5a("haegwjzuvuyypxyu"));
    assertFalse(solveDay5a("dvszwmarrgswjxmb"));
    try (BufferedReader r = new BufferedReader(new InputStreamReader(Solutions.class.getClassLoader().getResourceAsStream("2015/day5.txt")))) {
      int sum = 0;
      String line = null;
      while ((line = r.readLine()) != null) {
        if (solveDay5a(line)) {
          sum++;
        }
      }
      System.out.println(sum);
    }

    assertTrue(solveDay5b("qjhvhtzxzqqjkmpb"));
    assertTrue(solveDay5b("xxyxx"));
    assertFalse(solveDay5b("uurcxstgmygtbstg"));
    assertFalse(solveDay5b("ieodomkazucvgmuy"));
    try (BufferedReader r = new BufferedReader(new InputStreamReader(Solutions.class.getClassLoader().getResourceAsStream("2015/day5.txt")))) {
      int sum = 0;
      String line = null;
      while ((line = r.readLine()) != null) {
        if (solveDay5b(line)) {
          sum++;
        }
      }
      System.out.println(sum);
    }
  }

  /**
   * @see <a href="http://adventofcode.com/day/6">Day 6</a>
   */
  @Test
  public void testDay6() throws Exception {
    assertEquals(1000000, solveDay6a("turn on 0,0 through 999,999"));
    assertEquals(1000, solveDay6a("turn on 0,0 through 999,0"));
    assertEquals(1000000 - 4, solveDay6a("turn on 0,0 through 999,999", "turn off 499,499 through 500,500"));

    List<String> instructions = new LinkedList<>();
    try (BufferedReader r = new BufferedReader(new InputStreamReader(Solutions.class.getClassLoader().getResourceAsStream("2015/day6.txt")))) {
      String line = null;
      while ((line = r.readLine()) != null) {
        instructions.add(line);
      }
    }
    int litLights = solveDay6a(instructions.toArray(new String[instructions.size()]));
    System.out.println(litLights);

    long brightness = solveDay6b(instructions.toArray(new String[instructions.size()]));
    System.out.println(brightness);
  }

  /**
   * @see <a href="http://adventofcode.com/day/7">Day 7</a>
   */
  @Test
  public void testDay7() throws Exception {
    Map<String, Integer> outcome = solveDay7a( //
        "123 -> x", //
        "456 -> y", //
        "x AND y -> d", //
        "x OR y -> e", //
        "x LSHIFT 2 -> f", //
        "y RSHIFT 2 -> g", //
        "NOT x -> h", //
        "NOT y -> i");
    assertEquals(Integer.valueOf(72), outcome.get("d"));
    assertEquals(Integer.valueOf(507), outcome.get("e"));
    assertEquals(Integer.valueOf(492), outcome.get("f"));
    assertEquals(Integer.valueOf(114), outcome.get("g"));
    assertEquals(Integer.valueOf(65412), outcome.get("h"));
    assertEquals(Integer.valueOf(65079), outcome.get("i"));
    assertEquals(Integer.valueOf(123), outcome.get("x"));
    assertEquals(Integer.valueOf(456), outcome.get("y"));

    System.out.println("***");
    List<String> instructions = new LinkedList<>();
    try (BufferedReader r = new BufferedReader(new InputStreamReader(Solutions.class.getClassLoader().getResourceAsStream("2015/day7.txt")))) {
      String line = null;
      while ((line = r.readLine()) != null) {
        instructions.add(line);
      }
    }
    outcome = solveDay7a(instructions.toArray(new String[instructions.size()]));
    System.out.println("***");
    System.out.println(outcome.get("a"));

    // b: set b to value of a (16076); reset all others, what is a now?
    Pattern set = Pattern.compile("(\\d+) -> (\\w+)");
    for (int i = 0; i < instructions.size(); i++) {
      Matcher m = set.matcher(instructions.get(i));
      if (m.matches()) {
        if ("b".equals(m.group(2))) {
          instructions.set(i, "16076 -> b");
        } else {
          instructions.set(i, "0 -> " + m.group(2));
        }
      }
    }
    outcome = solveDay7a(instructions.toArray(new String[instructions.size()]));
    System.out.println("***");
    System.out.println(outcome.get("a"));
  }

  /**
   * @see <a href="http://adventofcode.com/day/8">Day 8</a>
   */
  @Test
  public void testDay8() throws Exception {
    assertEquals(2, solveDay8a("\"\""));
    assertEquals(2, solveDay8a("\"abc\""));
    assertEquals(3, solveDay8a("\"aaa\\\"aaa\""));
    assertEquals(5, solveDay8a("\"\\x26\""));

    solveDay8a("\"hjg\\\\w\\\"\\x78uoqbsdikbjxpip\\\"w\\\"jnhzec\"");

    List<String> lines = new LinkedList<>();
    try (BufferedReader r = new BufferedReader(new InputStreamReader(Solutions.class.getClassLoader().getResourceAsStream("2015/day8.txt")))) {
      String line = null;
      while ((line = r.readLine()) != null) {
        lines.add(line);
      }
    }
    int result = solveDay8a(lines.toArray(new String[lines.size()]));
    System.out.println(result);

    assertEquals(4, solveDay8b("\"\""));
    assertEquals(4, solveDay8b("\"abc\""));
    assertEquals(6, solveDay8b("\"aaa\\\"aaa\""));
    assertEquals(5, solveDay8b("\"\\x27\""));
    result = solveDay8b(lines.toArray(new String[lines.size()]));
    System.out.println(result);
  }

  /**
   * @see <a href="http://adventofcode.com/day/9">Day 9</a>
   */
  @Test
  public void testDay9() throws Exception {
    assertEquals(605, solveDay9a("London to Dublin = 464", "London to Belfast = 518", "Dublin to Belfast = 141"));

    List<String> lines = new LinkedList<>();
    try (BufferedReader r = new BufferedReader(new InputStreamReader(Solutions.class.getClassLoader().getResourceAsStream("2015/day9.txt")))) {
      String line = null;
      while ((line = r.readLine()) != null) {
        lines.add(line);
      }
    }

    System.out.println(solveDay9a(lines.toArray(new String[lines.size()])));
    System.out.println(solveDay9b(lines.toArray(new String[lines.size()])));
  }

}
