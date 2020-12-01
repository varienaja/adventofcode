package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * TODO.
 *
 * @author Arjan Verstoep
 */
public class Solutions {

  @Test
  public void testDay10() throws IOException, URISyntaxException {
    List<String> lines = new LinkedList<>();
    lines.add("value 5 goes to bot 2");
    lines.add("bot 2 gives low to bot 1 and high to bot 0");
    lines.add("value 3 goes to bot 1");
    lines.add("bot 1 gives low to output 1 and high to bot 0");
    lines.add("bot 0 gives low to output 2 and high to output 0");
    lines.add("value 2 goes to bot 2");
    assertEquals(2, calculateSolution10a(lines, 5, 2));

    System.out.print("Solution 10a: ");
    lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2016/day10.txt").toURI()));
    System.out.println(calculateSolution10a(lines, 61, 17));

    lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2016/day10.txt").toURI()));
    System.out.print("Solution 10b: ");
    System.out.println(calculateSolution10b(lines));
  }

  @Test
  public void testDay11() throws IOException, URISyntaxException {
    List<String> floor1 = new LinkedList<>();
    List<String> floor2 = new LinkedList<>();
    List<String> floor3 = new LinkedList<>();
    List<String> floor4 = new LinkedList<>();

    floor1.add("HM");
    floor1.add("LM");
    floor2.add("HG");
    floor3.add("LG");
    List<List<String>> stuff = Arrays.asList(floor1, floor2, floor3, floor4);
    Queue<String> steps = new LinkedList<>();
    assertEquals(11, calculateSteps(steps, 0, stuff));

    floor1.clear();
    floor2.clear();
    floor3.clear();
    floor4.clear();
    floor1.add("PG");
    floor1.add("TG");
    floor1.add("TM");
    floor1.add("pG");
    floor1.add("RG");
    floor1.add("RM");
    floor1.add("CG");
    floor1.add("CM");
    floor2.add("PM");
    floor3.add("pM");
    steps.clear();
    System.out.print("Solution 10a: ");
    System.out.println(calculateSteps(steps, 0, stuff));
  }

  @Test
  public void testDay1a() {
    int x = 0;
    int y = 0;
    int direction = 0; // 0,1,2,3: north, west, south, east
    String input = "R1, R3, L2, L5, L2, L1, R3, L4, R2, L2, L4, R2, L1, R1, L2, R3, L1, L4, R2, L5, R3, R4, L1, R2, L1, R3, L4, R5, L4, L5, R5, L3, R2, L3, L3, R1, R3, L4, R2, R5, L4, R1, L1, L1, R5, L2, R1, L2, R188, L5, L3, R5, R1, L2, L4, R3, R5, L3, R3, R45, L4, R4, R72, R2, R3, L1, R1, L1, L1, R192, L1, L1, L1, L4, R1, L2, L5, L3, R5, L3, R3, L4, L3, R1, R4, L2, R2, R3, L5, R3, L1, R1, R4, L2, L3, R1, R3, L4, L3, L4, L2, L2, R1, R3, L5, L1, R4, R2, L4, L1, R3, R3, R1, L5, L2, R4, R4, R2, R1, R5, R5, L4, L1, R5, R3, R4, R5, R3, L1, L2, L4, R1, R4, R5, L2, L3, R4, L4, R2, L2, L4, L2, R5, R1, R4, R3, R5, L4, L4, L5, L5, R3, R4, L1, L3, R2, L2, R1, L3, L5, R5, R5, R3, L4, L2, R4, R5, R1, R4, L3";

    String[] steps = input.split(",\\s");
    for (String step : steps) {
      int a = Integer.parseInt(step.substring(1));
      if (step.startsWith("R")) {
        direction--;
      } else if (step.startsWith("L")) {
        direction++;
      }

      if (direction < 0) {
        direction = 3;
      }
      if (direction > 3) {
        direction = 0;
      }

      switch (direction) {
        case 0:
          x -= a;
          break;
        case 1:
          y -= a;
          break;
        case 2:
          x += a;
          break;
        case 3:
          y += a;
          break;
      }
    }

    System.out.println("x=" + x + ", y=" + y + ", sum=" + (Math.abs(x) + Math.abs(y)));
  }

  @Test
  public void testDay1b() {
    int x = 0;
    int y = 0;
    int direction = 0; // 0,1,2,3: north, west, south, east
    String input = "R1, R3, L2, L5, L2, L1, R3, L4, R2, L2, L4, R2, L1, R1, L2, R3, L1, L4, R2, L5, R3, R4, L1, R2, L1, R3, L4, R5, L4, L5, R5, L3, R2, L3, L3, R1, R3, L4, R2, R5, L4, R1, L1, L1, R5, L2, R1, L2, R188, L5, L3, R5, R1, L2, L4, R3, R5, L3, R3, R45, L4, R4, R72, R2, R3, L1, R1, L1, L1, R192, L1, L1, L1, L4, R1, L2, L5, L3, R5, L3, R3, L4, L3, R1, R4, L2, R2, R3, L5, R3, L1, R1, R4, L2, L3, R1, R3, L4, L3, L4, L2, L2, R1, R3, L5, L1, R4, R2, L4, L1, R3, R3, R1, L5, L2, R4, R4, R2, R1, R5, R5, L4, L1, R5, R3, R4, R5, R3, L1, L2, L4, R1, R4, R5, L2, L3, R4, L4, R2, L2, L4, L2, R5, R1, R4, R3, R5, L4, L4, L5, L5, R3, R4, L1, L3, R2, L2, R1, L3, L5, R5, R5, R3, L4, L2, R4, R5, R1, R4, L3";
    Set<Point> visited = new LinkedHashSet<>();
    visited.add(new Point(0, 0));

    String[] steps = input.split(",\\s");
    for (String step : steps) {
      int a = Integer.parseInt(step.substring(1));
      if (step.startsWith("R")) {
        direction--;
      } else if (step.startsWith("L")) {
        direction++;
      }

      if (direction < 0) {
        direction = 3;
      }
      if (direction > 3) {
        direction = 0;
      }

      while (a > 0) {
        switch (direction) {
          case 0:
            x -= 1;
            break;
          case 1:
            y -= 1;
            break;
          case 2:
            x += 1;
            break;
          case 3:
            y += 1;
            break;
        }
        a--;
        Point p = new Point(x, y);
        if (!visited.add(p)) {
          System.out.println("x=" + x + ", y=" + y + ", sum=" + (Math.abs(x) + Math.abs(y)));
          return;
        }
      }
    }
  }

  @Test
  public void testDay2() {
    String[] pad = new String[] {
        "     ", //
        " 123 ", //
        " 456 ", //
        " 789 ", //
        "     "
    };
    String testInput = "ULL\nRRDDD\nLURDL\nUUUUD";
    assertEquals("1985", calcDay2b(testInput, pad));
    String input = "URULLLLLRLDDUURRRULLLDURRDRDRDLURURURLDLLLLRUDDRRLUDDDDDDLRLRDDDUUDUDLDULUDLDURDULLRDDURLLLRRRLLRURLLUDRDLLRRLDDRUDULRRDDLUUUDRLDLURRRULURRDLLLDDDLUDURDDRLDDDLLRULDRUDDDLUDLURUDLLRURRUURUDLLLUUUUDDURDRDDDLDRRUDURDLLLULUDURURDUUULRULUDRUUUUDLRLUUUUUDDRRDDDURULLLRRLDURLDLDRDLLLUULLRRLLLLDRLRDRRDRRUDDLULUUDDDDRRUUDDLURLRDUUDRRLDUDLRRRLRRUUDURDRULULRDURDRRRDLDUUULRDDLRLRDLUUDDUDDRLRRULLLULULLDDDRRDUUUDDRURDDURDRLRDLDRDRULRLUURUDRLULRLURLRRULDRLRDUDLDURLLRLUDLUDDURDUURLUDRLUL\nLLLUUURUULDDDULRRDLRLLLLLLLLRURRDLURLUDRRDDULDRRRRRRLDURRULDDULLDDDRUUDLUDULLDLRRLUULULRULURDURLLDULURDUDLRRLRLLDULLRLDURRUULDLDULLRDULULLLULDRLDLDLDLDDLULRLDUDRULUDDRDDRLRLURURRDULLUULLDRRDRRDLDLLRDLDDUUURLUULDDRRRUULDULDDRDDLULUDRURUULLUDRURDRULDRUULLRRDURUDDLDUULLDDRLRRDUDRLRRRLDRLRULDRDRRUDRLLLDDUDLULLURRURRLUURDRLLDLLDUDLUUURRLRDDUDRLUDLLRULLDUUURDLUUUDUDULRLDLDRUUDULRDRRUDLULRLRDLDRRDDDUDLDLDLRUURLDLLUURDLDLRDLDRUDDUURLLLRDRDRRULLRLRDULUDDDLUDURLDUDLLRULRDURDRDLLULRRDLLLDUURRDUDDLDDRULRRRRLRDDRURLLRRLLL\nDRURLDDDDRLUDRDURUDDULLRRLLRLDDRLULURLDURRLDRRLRLUURDDRRDLRDLDLULDURUDRLRUDULRURURLRUDRLLDDUDDRDLDRLLDDLRRDRUUULDUUDRUULRLLDLLULLLRRDRURDLDDRRDDUDDULLDUUULDRUDLDLURLDRURUDLRDDDURRLRDDUDLLLRRUDRULRULRRLLUUULDRLRRRLLLDLLDUDDUUDRURLDLRRUUURLUDDDRRDDLDDDDLUURDDULDRLRURLULLURRDRLLURLLLURDURLDLUDUUDUULLRLDLLLLULRDDLDUDUDDDUULURRLULDLDRLRDRLULLUDDUUUUURDRURLDUULDRRDULUDUDLDDRDLUDDURUDURLDULRUDRRDLRLRDRRURLDLURLULULDDUUDLRLLLLURRURULDDRUUULLDULDRDULDDDLLLRLULDDUDLRUDUDUDURLURLDDLRULDLURD\nDRUDRDURUURDLRLUUUUURUDLRDUURLLDUULDUULDLURDDUULDRDDRDULUDDDRRRRLDDUURLRDLLRLRURDRRRDURDULRLDRDURUDLLDDULRDUDULRRLLUDLLUUURDULRDDLURULRURDDLRLLULUDURDRRUDLULLRLDUDLURUDRUULDUDLRDUDRRDULDDLDRLRRULURULUURDULRRLDLDULULRUUUUULUURLURLRDLLRRRRLURRUDLRLDDDLDRDRURLULRDUDLRLURRDRRLRLLDLDDLLRRULRLRLRUDRUUULLDUULLDDRLUDDRURLRLDLULDURLLRRLDLLRDDDUDDUULLUDRUDURLLRDRUDLUDLLUDRUUDLRUURRRLLUULLUUURLLLRURUULLDLLDURUUUULDDDLRLURDRLRRRRRRUDLLLRUUULDRRDLRDLLDRDLDDLDLRDUDLDDRDDDDRULRRLRDULLDULULULRULLRRLLUURUUUDLDLUDUDDDLUUDDDDUDDDUURUUDRDURRLUULRRDUUDDUDRRRDLRDRLDLRRURUUDRRRUUDLDRLRDURD\nDDDLRURUDRRRURUUDLRLRDULDRDUULRURRRUULUDULDDLRRLLRLDDLURLRUDRLRRLRDLRLLDDLULDLRRURDDRDLLDDRUDRRRURRDUDULUDDULRRDRLDUULDLLLDRLUDRDURDRRDLLLLRRLRLLULRURUUDDRULDLLRULDRDLUDLULDDDLLUULRRLDDUURDLULUULULRDDDLDUDDLLLRRLLLDULRDDLRRUDDRDDLLLLDLDLULRRRDUDURRLUUDLLLLDUUULDULRDRULLRDRUDULRUUDULULDRDLDUDRRLRRDRLDUDLULLUDDLURLUUUDRDUDRULULDRDLRDRRLDDRRLUURDRULDLRRLLRRLDLRRLDLDRULDDRLURDULRRUDURRUURDUUURULUUUDLRRLDRDLULDURUDUDLUDDDULULRULDRRRLRURLRLRLUDDLUUDRRRLUUUDURLDRLRRDRRDURLLL";
    System.out.print("Solution 2a: ");
    System.out.println(calcDay2b(input, pad));

    pad = new String[] {
        "       ", //
        "   1   ", //
        "  234  ", //
        " 56789 ", //
        "  ABC  ", //
        "   D   ", //
        "       "
    };
    assertEquals("5DB3", calcDay2b(testInput, pad));
    System.out.print("Solution 2b: ");
    System.out.println(calcDay2b(input, pad));

  }

  @Test
  public void testDay3() throws IOException, URISyntaxException {
    assertFalse(isValidTriangle("5 10 25"));

    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2016/day3.txt").toURI()));
    System.out.print("Solution 3a: ");
    System.out.println(lines.stream().filter(t -> isValidTriangle(t)).count());

    // So, we create a new List<String> which in a transformation from the original one.
    // We move in steps of three
    List<String> transformed = new LinkedList<>();
    int i = 0;
    do {
      String l1 = lines.get(i++);
      String l2 = lines.get(i++);
      String l3 = lines.get(i++);

      String[] parts1 = l1.trim().split("\\s+");
      String[] parts2 = l2.trim().split("\\s+");
      String[] parts3 = l3.trim().split("\\s+");

      transformed.add(parts1[0] + " " + parts2[0] + " " + parts3[0]);
      transformed.add(parts1[1] + " " + parts2[1] + " " + parts3[1]);
      transformed.add(parts1[2] + " " + parts2[2] + " " + parts3[2]);
    } while (i < lines.size());
    System.out.print("Solution 3b: ");
    System.out.println(transformed.stream().filter(t -> isValidTriangle(t)).count());

    // Now, our pairs of three are top-down..., so we should transform the input or alter the algorithm.
  }

  /**
   * Each room consists of an encrypted name (lowercase letters separated by
   * dashes) followed by a dash, a sector ID,* and a checksum in square
   * brackets. A room is real (not a decoy) if the checksum is the five most
   * common letters in the encrypted name, in order, with ties broken by
   * alphabetization. For example:
   * aaaaa-bbb-z-y-x-123[abxyz] is a real room because the most common letters
   * are a (5), b (3), and then a tie between x, y, and z, which are listed
   * alphabetically.
   * a-b-c-d-e-f-g-h-987[abcde] is a real room because although the letters are
   * all tied (1 of each), the first five are listed alphabetically.
   * not-a-real-room-404[oarel] is a real room.
   * totally-real-room-200[decoy] is not.
   * Of the real rooms from the list above, the sum of their sector IDs is 1514.
   * What is the sum of the sector IDs of the real rooms?
   *
   * @throws URISyntaxException not expected
   * @throws IOException not expected
   */
  @Test
  public void testDay4() throws IOException, URISyntaxException {
    assertEquals(123, isRealRoom("aaaaa-bbb-z-y-x-123[abxyz]"));
    assertEquals(987, isRealRoom("a-b-c-d-e-f-g-h-987[abcde]"));
    assertEquals(404, isRealRoom("not-a-real-room-404[oarel]"));
    assertEquals(0, isRealRoom("totally-real-room-200[decoy]"));

    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2016/day4.txt").toURI()));
    long sum = lines.stream().map(l -> isRealRoom(l)).mapToInt(i -> i).sum();
    System.out.print("Solution 4a: ");
    System.out.println(sum);

    assertEquals("very encrypted name", decryptRoom("qzmt-zixmtkozy-ivhz-343[lalal]"));
    System.out.print("Solution 4b: ");
    for (String line : lines) {
      if (decryptRoom(line).startsWith("north")) {
        System.out.println(isRealRoom(line));
      }
    }
  }

  /**
   * A hash indicates the next character in the password if its hexadecimal
   * representation starts with five zeroes. If it does, the sixth character in
   * the hash is the next character of the password.
   * For example, if the Door ID is abc:
   * The first index which produces a hash that starts with five zeroes is
   * 3231929, which we find by hashing abc3231929; the sixth character of the
   * hash, and thus the first character of the password, is 1.
   * 5017308 produces the next interesting hash, which starts with
   * 000008f82..., so the second character of the password is 8.
   * The third time a hash starts with five zeroes is for abc5278568,
   * discovering the character f. In this example, after continuing this search
   * a total of eight times, the password is 18f47a30.
   * Given the actual Door ID, what is the password?
   * Your puzzle input is uqwqemis.
   */
  @Test
  public void testDay5() {
    assertEquals("18f47a30", pwdFor("abc"));
    System.out.print("Solution 5a: ");
    System.out.println(pwdFor("uqwqemis"));

    assertEquals("05ace8e3", altPwdFor("abc"));
    System.out.print("Solution 5b: ");
    System.out.println(altPwdFor("uqwqemis"));
  }

  @Test
  public void testDay6() throws IOException, URISyntaxException {
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2016/day6.txt").toURI()));

    System.out.print("Solution 6a: ");
    int mx = lines.get(0).length();
    int i = 0;
    while (i < mx) {
      Map<Character, Integer> frequency = new HashMap<>();
      for (String line : lines) {
        char c = line.charAt(i);
        Integer count = frequency.getOrDefault(c, 0);
        frequency.put(c, ++count);
      }
      List<Character> res = frequency.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())).map(e -> e.getKey()).limit(1)
          .collect(Collectors.toList());
      System.out.print(res.get(0));
      i++;
    }
    System.out.println();

    System.out.print("Solution 6a: ");
    i = 0;
    while (i < mx) {
      Map<Character, Integer> frequency = new HashMap<>();
      for (String line : lines) {
        char c = line.charAt(i);
        Integer count = frequency.getOrDefault(c, 0);
        frequency.put(c, ++count);
      }
      List<Character> res = frequency.entrySet().stream().sorted((e1, e2) -> e1.getValue().compareTo(e2.getValue())).map(e -> e.getKey()).limit(1)
          .collect(Collectors.toList());
      System.out.print(res.get(0));
      i++;
    }
    System.out.println();
  }

  @Test
  public void testDay7() throws IOException, URISyntaxException {
    assertTrue(supportsTLS("abba[mnop]qrst"));
    assertFalse(supportsTLS("abcd[bddb]xyyx"));
    assertFalse(supportsTLS("aaaa[qwer]tyui"));
    assertTrue(supportsTLS("ioxxoj[asdfgh]zxcvbn"));

    assertTrue(supportsTLS("vjqhodfzrrqjshbhx[lezezbbswydnjnz]ejcflwytgzvyigz[hjdilpgdyzfkloa]mxtkrysovvotkuyekba"));

    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2016/day7.txt").toURI()));

    System.out.print("Solution 7a: ");
    int counter = 0;
    for (String line : lines) {
      if (supportsTLS(line)) {
        counter++;
        // System.out.println(line);
      }
    }
    System.out.println(counter);

    assertTrue(supportsSSL("aba[bab]xyz"));
    assertFalse(supportsSSL("xyx[xyx]xyx"));
    assertTrue(supportsSSL("aaa[kek]eke"));
    assertTrue(supportsSSL("zazbz[bzb]cdb"));

    System.out.print("Solution 7b: ");
    counter = 0;
    for (String line : lines) {
      if (supportsSSL(line)) {
        counter++;
        // System.out.println(line);
      }
    }
    System.out.println(counter);
  }

  @Test
  public void testDay8() throws IOException, URISyntaxException {
    StringBuilder[] grid = new StringBuilder[3];
    for (int i = 0; i < grid.length; i++) {
      grid[i] = new StringBuilder(".......");
    }

    transformGrid(grid, "rect 3x2");
    assertEquals("[###...., ###...., .......]", Arrays.toString(grid));
    transformGrid(grid, "rotate column x=1 by 1");
    assertEquals("[#.#...., ###...., .#.....]", Arrays.toString(grid));
    transformGrid(grid, "rotate row y=0 by 4");
    assertEquals("[....#.#, ###...., .#.....]", Arrays.toString(grid));
    transformGrid(grid, "rotate column x=1 by 1");
    assertEquals("[.#..#.#, #.#...., .#.....]", Arrays.toString(grid));

    System.out.print("Solution 8a: ");
    grid = new StringBuilder[6];
    for (int i = 0; i < grid.length; i++) {
      grid[i] = new StringBuilder("..................................................");
    }
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2016/day8.txt").toURI()));
    for (String operation : lines) {
      transformGrid(grid, operation);
    }
    int counter = 0;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length(); j++) {
        if (grid[i].charAt(j) == '#') {
          counter++;
        }
      }
    }
    System.out.println(counter);

    System.out.println("Solution 8b: ");
    for (int i = 0; i < grid.length; i++) {
      System.out.println(grid[i]);
    } // ZFHFSFOGPO
  }

  @Test
  public void testDay9() throws IOException, URISyntaxException {
    assertEquals("ADVENT", decompress("ADVENT"));
    assertEquals("ABBBBBC", decompress("A(1x5)BC"));
    assertEquals("ABCBCDEFEFG", decompress("A(2x2)BCD(2x2)EFG"));
    assertEquals("(1x3)A", decompress("(6x1)(1x3)A"));
    assertEquals("X(3x3)ABC(3x3)ABCY", decompress("X(8x2)(3x3)ABCY"));

    System.out.print("Solution 9a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2016/day9.txt").toURI()));
    assertEquals(1, lines.size());
    final String decompressed = decompress(lines.get(0));
    long counter = 0L;
    for (char c : decompressed.toCharArray()) {
      if (c != ' ') {
        counter++;
      }
    }
    System.out.println(counter);

    assertEquals(20, decompressB(decompress("X(8x2)(3x3)ABCY")));
    assertEquals(241920, decompressB(decompress("(27x12)(20x12)(13x14)(7x10)(1x12)A")));
    assertEquals(445, decompressB(decompress("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN")));
    System.out.print("Solution 9b: ");
    counter = decompressB(decompressed);
    System.out.println(counter);
  }

  private String altPwdFor(String inStart) {
    int i = 0;
    Set<Integer> usedIndices = new HashSet<>();
    char[] pwd = new char[8];
    Arrays.fill(pwd, ' ');

    while (usedIndices.size() < 8) {
      try {
        String md5;
        MessageDigest digester = MessageDigest.getInstance("MD5");
        do {
          String tester = inStart + i++;
          byte[] digested = digester.digest(tester.getBytes(Charset.forName("UTF-8")));
          StringBuffer hexString = new StringBuffer();
          for (int j = 0; j < digested.length; j++) {
            String hex = Integer.toHexString(0xff & digested[j]);
            if (hex.length() == 1) {
              hexString.append('0');
            }
            hexString.append(hex);
          }

          md5 = hexString.toString();
        } while (!md5.startsWith("00000"));

        if (Character.isDigit(md5.charAt(5))) {
          int index = Integer.parseInt("" + md5.charAt(5));
          if (index >= 0 && index < pwd.length && pwd[index] == ' ') {
            usedIndices.add(index);
            pwd[index] = md5.charAt(6);
            // System.out.println(Arrays.toString(pwd));
          }
        }
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }
    }

    return new String(pwd);
  }

  private String calcDay2b(String inInput, String[] inPad) {
    // We point to the 5 initially
    int x = 0;
    int y = 0;
    for (int cY = 0; cY < inPad.length; cY++) {
      int cX = inPad[cY].indexOf('5');
      if (cX >= 0) {
        x = cX;
        y = cY;
        break;
      }
    }

    StringBuilder result = new StringBuilder();
    String[] steps = inInput.split("\n");
    for (String step : steps) {
      for (char c : step.toCharArray()) {
        int newX = x;
        int newY = y;
        switch (c) {
          case 'U':
            newY -= 1;
            break;
          case 'D':
            newY += 1;
            break;
          case 'R':
            newX += 1;
            break;
          case 'L':
            newX -= 1;
            break;
        }
        if (' ' != inPad[newY].charAt(newX)) {
          x = newX;
          y = newY;
        }
      }
      result.append(inPad[y].charAt(x));
    }

    return result.toString();
  }

  private int calculateSolution10a(List<String> inInstructions, int inV1, int inV2) {
    // We're interested in which bot compares V1 with V2
    List<String> lines = new LinkedList<>();
    lines.add("value 5 goes to bot 2");
    lines.add("bot 2 gives low to bot 1 and high to bot 0");
    lines.add("value 3 goes to bot 1");
    lines.add("bot 1 gives low to output 1 and high to bot 0");
    lines.add("bot 0 gives low to output 2 and high to output 0");
    lines.add("value 2 goes to bot 2");

    // What do we do.. execute value x goes to bot a initially?
    // then... execute give-instructions...
    Pattern valueTransfer = Pattern.compile("value (\\d+) goes to bot (\\d+)");
    Pattern botGives = Pattern.compile("bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)");

    Map<Integer, TreeSet<Integer>> bots = new HashMap<>();
    Map<Integer, Integer> outputs = new HashMap<>();

    Iterator<String> it = inInstructions.iterator();
    while (it.hasNext()) {
      String instruction = it.next();
      Matcher m = valueTransfer.matcher(instruction);
      if (m.matches()) {
        Integer value = Integer.parseInt(m.group(1));
        Integer botNr = Integer.parseInt(m.group(2));

        TreeSet<Integer> bot = bots.computeIfAbsent(botNr, i -> new TreeSet<>());
        bot.add(value);

        if (bot.contains(inV1) && bot.contains(inV2)) {
          return botNr;
        }
        it.remove();
      }
    }

    while (!inInstructions.isEmpty()) {
      it = inInstructions.iterator();
      while (it.hasNext()) {
        String instruction = it.next();
        Matcher m = botGives.matcher(instruction);
        if (m.matches()) {
          Integer botNr = Integer.parseInt(m.group(1));
          Integer botLow = Integer.parseInt(m.group(3));
          Integer botHigh = Integer.parseInt(m.group(5));

          String toLow = m.group(2);
          String toHigh = m.group(4);

          TreeSet<Integer> bot = bots.computeIfAbsent(botNr, i -> new TreeSet<>());

          if (bot.size() == 2) {
            if ("output".equals(toLow) || bots.computeIfAbsent(botLow, i -> new TreeSet<>()).size() < 2) {
              if ("output".equals(toHigh) || bots.computeIfAbsent(botHigh, i -> new TreeSet<>()).size() < 2) { // execute!
                if ("output".equals(toLow)) {
                  outputs.put(botLow, bot.first());
                } else {
                  TreeSet<Integer> toBot = bots.get(botLow);
                  toBot.add(bot.first());
                  if (toBot.contains(inV1) && toBot.contains(inV2)) {
                    return botLow;
                  }
                }
              }

              if ("output".equals(toHigh)) {
                outputs.put(botHigh, bot.last());
              } else {
                TreeSet<Integer> toBot = bots.get(botHigh);
                toBot.add(bot.last());
                if (toBot.contains(inV1) && toBot.contains(inV2)) {
                  return botHigh;
                }
              }
              bot.clear();
              it.remove();
            }
          }
        }
      }
    }

    return -1;
  }

  private int calculateSolution10b(List<String> inInstructions) {
    // We're interested in which bot compares V1 with V2
    List<String> lines = new LinkedList<>();
    lines.add("value 5 goes to bot 2");
    lines.add("bot 2 gives low to bot 1 and high to bot 0");
    lines.add("value 3 goes to bot 1");
    lines.add("bot 1 gives low to output 1 and high to bot 0");
    lines.add("bot 0 gives low to output 2 and high to output 0");
    lines.add("value 2 goes to bot 2");

    // What do we do.. execute value x goes to bot a initially?
    // then... execute give-instructions...
    Pattern valueTransfer = Pattern.compile("value (\\d+) goes to bot (\\d+)");
    Pattern botGives = Pattern.compile("bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)");

    Map<Integer, TreeSet<Integer>> bots = new HashMap<>();
    Map<Integer, Integer> outputs = new HashMap<>();

    Iterator<String> it = inInstructions.iterator();
    while (it.hasNext()) {
      String instruction = it.next();
      Matcher m = valueTransfer.matcher(instruction);
      if (m.matches()) {
        Integer value = Integer.parseInt(m.group(1));
        Integer botNr = Integer.parseInt(m.group(2));

        TreeSet<Integer> bot = bots.computeIfAbsent(botNr, i -> new TreeSet<>());
        bot.add(value);
        it.remove();
      }
    }

    while (!inInstructions.isEmpty()) {
      it = inInstructions.iterator();
      while (it.hasNext()) {
        String instruction = it.next();
        Matcher m = botGives.matcher(instruction);
        if (m.matches()) {
          Integer botNr = Integer.parseInt(m.group(1));
          Integer botLow = Integer.parseInt(m.group(3));
          Integer botHigh = Integer.parseInt(m.group(5));

          String toLow = m.group(2);
          String toHigh = m.group(4);

          TreeSet<Integer> bot = bots.computeIfAbsent(botNr, i -> new TreeSet<>());

          if (bot.size() == 2) {
            if ("output".equals(toLow) || bots.computeIfAbsent(botLow, i -> new TreeSet<>()).size() < 2) {
              if ("output".equals(toHigh) || bots.computeIfAbsent(botHigh, i -> new TreeSet<>()).size() < 2) { // execute!
                if ("output".equals(toLow)) {
                  outputs.put(botLow, bot.first());
                } else {
                  TreeSet<Integer> toBot = bots.get(botLow);
                  toBot.add(bot.first());
                }
              }

              if ("output".equals(toHigh)) {
                outputs.put(botHigh, bot.last());
              } else {
                TreeSet<Integer> toBot = bots.get(botHigh);
                toBot.add(bot.last());
              }
              bot.clear();
              it.remove();
            }
          }
        }
      }
    }

    return outputs.get(0) * outputs.get(1) * outputs.get(2);
  }

  private int calculateSteps(Queue<String> inSteps, int inElevator, List<List<String>> inStuff) {
    if (inStuff.get(0).isEmpty() && inStuff.get(1).isEmpty() && inStuff.get(2).isEmpty()) {
      return inSteps.size();
    }

    // Queue must consist of steps to take,
    // I'd need a secondary method to verify the outcome of a list of steps
    // and then, using breadth-first find the 1st solution.

    int[] directions = {
        -1, 1
    };
    int elevator = inElevator;

    do {
      for (int d : directions) { // which direction to move elevator
        int test = inElevator + d;
        if (test < 0 || test >= inStuff.size()) {
          continue; // Don't investigate impossible floors
        }

        List<String> stuffOnThisFloor = inStuff.get(elevator);
        int itemsOnThisFloor = stuffOnThisFloor.size();
        for (int i1 = 0; i1 <= itemsOnThisFloor; i1++) {
          for (int i2 = 0; i2 < itemsOnThisFloor; i2++) {
            String item1 = i1 == itemsOnThisFloor ? "  " : inStuff.get(inElevator).remove(i1);
            String item2 = i2 == itemsOnThisFloor ? "  " : inStuff.get(inElevator).remove(i2);
            boolean validLiftContent = true;
            // TODO check if current floor's content is still legal
            // TODO check if lift contents is legal
            // TODO check if target floor contents is legal
            // TODO if all legal, put to Q;
            // TODO if new situation is target return queue-length, else remove elt from Q; revert to last state;
            // continue (recurse)

            if (inStuff.get(0).isEmpty() && inStuff.get(1).isEmpty() && inStuff.get(2).isEmpty()) {
              return inSteps.size();
            }
          }
        }
      }
    } while (true);

    // Create queue;
    // then, in a loop add all possible elements to the q; check if solution; if not: pop
    // then, increase stepsize?
    // for 1 to stepsize: add possible elts to q; check if solution.

    // Done if 4th floor contains everything

    // elevator up/down
    // nothing, xG+xM, xM, xM+yM enter lift

    // So our Elevator goes up and down taking random stuff with it
    // until everything is at floor #4. We want the solution with fewest steps

    // Minimum: breath-first search, until we have a solution
    // So: take all steps, then recurse
    // So: go up/down, taking either nothing, 1*G + 1*M, 1*M, 2*M

    // Have to think...
  }

  private boolean containsABBA(String inString) {
    for (int i = 0; i < inString.length() - 3; i++) {
      if (inString.charAt(i) == inString.charAt(i + 3) //
          && inString.charAt(i + 1) == inString.charAt(i + 2) //
          && inString.charAt(i) != inString.charAt(i + 1)) {
        return true;
      }
    }
    return false;
  }

  private String decompress(String inCompressed) {
    StringBuilder sb = new StringBuilder();

    char[] arr = inCompressed.toCharArray();
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == '(') { // find x and l
        StringBuilder l = new StringBuilder();
        while (arr[++i] != 'x') {
          l.append(arr[i]);
        }
        int length = Integer.parseInt(l.toString());

        StringBuilder x = new StringBuilder();
        while (arr[++i] != ')') {
          x.append(arr[i]);
        }
        i++;
        int times = Integer.parseInt(x.toString());

        for (int j = 0; j < times; j++) {
          for (int k = 0; k < length; k++) {
            sb.append(arr[i + k]);
          }
        }
        i += length - 1;
      } else {
        sb.append(arr[i]);
      }
    }

    return sb.toString();
  }

  private long decompressB(String inUncompressed) {
    long result = 0;
    String rest = inUncompressed;
    while (!rest.isEmpty()) {
      int start = rest.indexOf('(');
      if (start == -1) {
        return result + rest.length();
      }

      result += start;

      StringBuilder l = new StringBuilder();
      while (rest.charAt(++start) != 'x') {
        l.append(rest.charAt(start));
      }
      int length = Integer.parseInt(l.toString());

      StringBuilder x = new StringBuilder();
      while (rest.charAt(++start) != ')') {
        x.append(rest.charAt(start));
      }
      int times = Integer.parseInt(x.toString());
      start++;

      long subLength = decompressB(rest.substring(start, start + length));
      for (int j = 0; j < times; j++) {
        result += subLength;
      }

      rest = rest.substring(start + length);
    }

    return result;
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

  private boolean isValidTriangle(String inLine) {
    String[] parts = inLine.trim().split("\\s+");
    assertEquals(3, parts.length);
    int[] sides = new int[] {
        Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])
    };

    // x y z : x+y>z
    // x y z : x+z>y
    // x y z : y+z>x

    return sides[0] + sides[1] > sides[2] && sides[0] + sides[2] > sides[1] && sides[1] + sides[2] > sides[0];
  }

  private String pwdFor(String inStart) {
    int i = 0;
    StringBuilder sb = new StringBuilder();

    while (sb.length() < 8) {
      try {
        String md5;
        MessageDigest digester = MessageDigest.getInstance("MD5");
        do {
          String tester = inStart + i++;
          byte[] digested = digester.digest(tester.getBytes(Charset.forName("UTF-8")));
          StringBuffer hexString = new StringBuffer();
          for (int j = 0; j < digested.length; j++) {
            String hex = Integer.toHexString(0xff & digested[j]);
            if (hex.length() == 1) {
              hexString.append('0');
            }
            hexString.append(hex);
          }

          md5 = hexString.toString();
        } while (!md5.startsWith("00000"));
        sb.append(md5.charAt(5));
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }
    }

    return sb.toString();
  }

  private boolean supportsSSL(String inString) {
    StringBuilder normal = new StringBuilder();
    StringBuilder hyper = new StringBuilder();
    boolean hyperNet = false;
    for (char c : inString.toCharArray()) {
      if (c == '[') {
        hyper.append("_");
        hyperNet = true;
        continue;
      }
      if (c == ']') {
        normal.append("_");
        hyperNet = false;
        continue;
      }
      if (hyperNet) {
        hyper.append(c);
      } else {
        normal.append(c);
      }
    }

    String normalS = normal.toString();
    String hyperS = hyper.toString();
    for (int i = 0; i < normalS.length() - 2; i++) {
      if (normalS.charAt(i) == normalS.charAt(i + 2) //
          && normalS.charAt(i) != normalS.charAt(i + 1)) {

        if (hyperS.contains("" + normalS.charAt(i + 1) + normalS.charAt(i) + normalS.charAt(i + 1))) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean supportsTLS(String inString) {
    StringBuilder normal = new StringBuilder();
    StringBuilder hyper = new StringBuilder();
    boolean hyperNet = false;
    for (char c : inString.toCharArray()) {
      if (c == '[') {
        hyper.append("_");
        hyperNet = true;
        continue;
      }
      if (c == ']') {
        normal.append("_");
        hyperNet = false;
        continue;
      }
      (hyperNet ? hyper : normal).append(c);
    }
    return containsABBA(normal.toString()) && !containsABBA(hyper.toString());
  }

  private void transformGrid(StringBuilder[] inGrid, String inOperation) {
    if (inOperation.startsWith("rect")) {
      int xPos = inOperation.indexOf('x');
      int w = Integer.parseInt(inOperation.substring(5, xPos));
      int h = Integer.parseInt(inOperation.substring(xPos + 1));
      for (int i = 0; i < h; i++) {
        for (int j = 0; j < w; j++) {
          inGrid[i].setCharAt(j, '#');
        }
      }
    } else if (inOperation.startsWith("rotate column")) {
      int aPos = inOperation.indexOf("x=");
      int bPos = inOperation.indexOf(" by ");
      int x = Integer.parseInt(inOperation.substring(aPos + 2, bPos));
      int by = Integer.parseInt(inOperation.substring(bPos + 4));

      for (int j = 0; j < by; j++) {
        for (int i = inGrid.length - 1; i > 0; i--) {
          char tmp = inGrid[i].charAt(x);
          inGrid[i].setCharAt(x, inGrid[i - 1].charAt(x));
          inGrid[i - 1].setCharAt(x, tmp);
        }
      }
    } else if (inOperation.startsWith("rotate row")) {
      int aPos = inOperation.indexOf("y=");
      int bPos = inOperation.indexOf(" by ");
      int y = Integer.parseInt(inOperation.substring(aPos + 2, bPos));
      int by = Integer.parseInt(inOperation.substring(bPos + 4));

      for (int j = 0; j < by; j++) {
        for (int i = inGrid[y].length() - 1; i > 0; i--) {
          char tmp = inGrid[y].charAt(i - 1);
          inGrid[y].setCharAt(i - 1, inGrid[y].charAt(i));
          inGrid[y].setCharAt(i, tmp);
        }
      }
    }
  }

}
