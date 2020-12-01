package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
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

  private int getWeight(Map<String, Set<String>> inTree, String inKey, Map<String, Integer> inNode2weight) {
    int sum = inNode2weight.get(inKey);
    Set<String> sub = inTree.get(inKey);
    for (String s : sub) {
      sum += getWeight(inTree, s, inNode2weight);
    }
    return sum;
  }

  private int isBalanced(Map<String, Set<String>> inTree, String inKey, Map<String, Integer> inNode2weight) throws Exception {
    Set<String> sub = inTree.get(inKey);
    if (sub.isEmpty()) {
      return inNode2weight.get(inKey);
    }

    Map<Integer, Set<String>> weight2Keys = new HashMap<>();
    for (String s : sub) {
      weight2Keys.computeIfAbsent(isBalanced(inTree, s, inNode2weight), HashSet::new).add(s);
    }
    if (weight2Keys.size() > 1) {
      int toBe = 0;
      int wrong = 0;
      String wrongKey = "";
      for (Entry<Integer, Set<String>> e : weight2Keys.entrySet()) {
        if (e.getValue().size() == 1) {
          wrong = e.getKey();
          wrongKey = e.getValue().iterator().next();
        } else {
          toBe = e.getKey();
        }
      }
      int toAdd = toBe - wrong;
      int result = inNode2weight.get(wrongKey) + toAdd;
      throw new Exception(Integer.toString(result)); // WTF return from recursion by exception...
    }

    Entry<Integer, Set<String>> e = weight2Keys.entrySet().iterator().next();
    return inNode2weight.get(inKey) + e.getKey() * e.getValue().size();
  }

  private boolean isPassPhraseValid4a(String inPassphrase) {
    String[] words = inPassphrase.split("\\s+");
    Set<String> wordsSet = new HashSet<>();
    for (String word : words) {
      wordsSet.add(word);
    }
    return wordsSet.size() == words.length;
  }

  private boolean isPassPhraseValid4b(String inPassphrase) {
    class Word {
      private String _word;

      public Word(String inWord) {
        _word = inWord;
      }

      @Override
      public boolean equals(Object inOther) {
        String other = inOther.toString();
        char[] my = _word.toCharArray();
        Arrays.sort(my);
        char[] otherChars = other.toCharArray();
        Arrays.sort(otherChars);
        return Arrays.equals(my, otherChars);
      }

      @Override
      public int hashCode() {
        return 1;
      }

      @Override
      public String toString() {
        return _word;
      }
    }

    String[] words = inPassphrase.split("\\s+");
    Set<Word> wordsSet = new HashSet<>();
    for (String word : words) {
      wordsSet.add(new Word(word));
    }
    return wordsSet.size() == words.length;
  }

  private int putCell(Map<Point, Integer> inGrid, Point inPoint) {
    int sum = 0;
    for (int x = -1; x <= 1; x++) {
      for (int y = -1; y <= 1; y++) {
        Point neighbour = new Point(inPoint.x + x, inPoint.y + y);
        Integer value = inGrid.get(neighbour);
        if (value != null) {
          sum += value;
        }
      }
    }

    if (inPoint.x == 0 && inPoint.y == 0) {
      sum = 1;
    }
    inGrid.put(inPoint, sum);
    return sum;
  }

  private int solveDay1a(String inInput) {
    char[] chars = inInput.toCharArray();
    int sum = 0;
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] == chars[(i + 1) % chars.length]) {
        sum += chars[i] - '0';
      }
    }

    return sum;
  }

  private int solveDay1b(String inInput) {
    char[] chars = inInput.toCharArray();
    int sum = 0;
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] == chars[(i + chars.length / 2) % chars.length]) {
        sum += chars[i] - '0';
      }
    }

    return sum;
  }

  private int solveDay2a(List<String> inInput) {
    int checksum = 0;
    for (String line : inInput) {
      Pattern pattern = Pattern.compile("\\s+");
      IntSummaryStatistics stats = pattern.splitAsStream(line).map(Integer::parseInt).collect(Collectors.summarizingInt(Integer::intValue));
      int diff = stats.getMax() - stats.getMin();
      checksum += diff;
    }

    return checksum;
  }

  private int solveDay2b(List<String> inInput) {
    int checksum = 0;
    outer: for (String line : inInput) {
      Pattern pattern = Pattern.compile("\\s+");
      Integer[] numbers = pattern.splitAsStream(line).map(Integer::parseInt).toArray(Integer[]::new);
      // find divisors
      for (int i = 0; i < numbers.length; i++) {
        for (int j = 0; j < numbers.length; j++) {
          if (numbers[i] > numbers[j] && numbers[i] % numbers[j] == 0) {
            int diff = numbers[i] / numbers[j];
            checksum += diff;
            continue outer;
          }
        }
      }
    }

    return checksum;
  }

  private int solveDay3a(int inInput) {
    Map<Integer, Point> grid = new HashMap<>();

    int number = 1;
    int x = 0;
    int y = 0;
    int ribLength = 1;
    while (number <= inInput) {
      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        grid.put(number++, newPoint);
        x++;
      }

      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        grid.put(number++, newPoint);
        y--;
      }
      ribLength++;

      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        grid.put(number++, newPoint);
        x--;
      }

      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        grid.put(number++, newPoint);
        y++;
      }
      ribLength++;
    }

    Point p = grid.get(inInput);
    return Math.abs(p.x) + Math.abs(p.y);
  }

  private int solveDay3b(int inInput) {
    Map<Point, Integer> grid = new HashMap<>();

    int x = 0;
    int y = 0;
    int ribLength = 1;
    while (true) {
      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        int valueWritten = putCell(grid, newPoint);
        if (valueWritten > inInput) {
          return valueWritten;
        }
        x++;
      }

      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        int valueWritten = putCell(grid, newPoint);
        if (valueWritten > inInput) {
          return valueWritten;
        }
        y--;
      }
      ribLength++;

      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        int valueWritten = putCell(grid, newPoint);
        if (valueWritten > inInput) {
          return valueWritten;
        }
        x--;
      }

      for (int c = 0; c < ribLength; c++) {
        Point newPoint = new Point(x, y);
        int valueWritten = putCell(grid, newPoint);
        if (valueWritten > inInput) {
          return valueWritten;
        }
        y++;
      }
      ribLength++;
    }
  }

  private int solveDay4(List<String> inInput, Function<String, Boolean> inFunction) {
    int validPassPhrases = 0;
    for (String passphrase : inInput) {
      if (inFunction.apply(passphrase)) {
        validPassPhrases++;
      }
    }
    return validPassPhrases;
  }

  private int solveDay5a(String inInput) {
    Pattern pattern = Pattern.compile("\\\\n");
    Integer[] code = pattern.splitAsStream(inInput).map(Integer::parseInt).toArray(Integer[]::new);

    int exPointer = 0;
    int steps = 0;
    while (exPointer >= 0 && exPointer < code.length) {
      int jump = code[exPointer]++;
      exPointer += jump;
      steps++;
    }

    return steps;
  }

  private int solveDay5b(String inInput) {
    Pattern pattern = Pattern.compile("\\\\n");
    Integer[] code = pattern.splitAsStream(inInput).map(Integer::parseInt).toArray(Integer[]::new);

    int exPointer = 0;
    int steps = 0;
    while (exPointer >= 0 && exPointer < code.length) {
      int jump = code[exPointer];
      code[exPointer] += jump >= 3 ? -1 : 1;
      exPointer += jump;
      steps++;
    }

    return steps;
  }

  private int solveDay6a(String inInput) {
    Pattern pattern = Pattern.compile("\\s+");
    int[] register = pattern.splitAsStream(inInput).map(Integer::parseInt).mapToInt(v -> v).toArray();

    Set<String> seen = new HashSet<>();
    while (seen.add(Arrays.toString(register))) {
      // Find highest block
      int ixMax = 0;
      int max = Integer.MIN_VALUE;
      for (int i = 0; i < register.length; i++) {
        if (register[i] > max) {
          ixMax = i;
          max = register[i];
        }
      }

      // distribute this value over all blocks
      register[ixMax] = 0;
      for (int i = 0; i < max; i++) {
        ixMax = (ixMax + 1) % register.length;
        register[ixMax]++;
      }
    }
    return seen.size();
  }

  private int solveDay6b(String inInput) {
    Pattern pattern = Pattern.compile("\\s+");
    int[] register = pattern.splitAsStream(inInput).map(Integer::parseInt).mapToInt(v -> v).toArray();

    Integer oldPosition = null;
    Map<String, Integer> seen = new HashMap<>();
    while ((oldPosition = seen.put(Arrays.toString(register), seen.size())) == null) {
      // Find highest block
      int ixMax = 0;
      int max = Integer.MIN_VALUE;
      for (int i = 0; i < register.length; i++) {
        if (register[i] > max) {
          ixMax = i;
          max = register[i];
        }
      }

      // distribute this value over all blocks
      register[ixMax] = 0;
      for (int i = 0; i < max; i++) {
        ixMax = (ixMax + 1) % register.length;
        register[ixMax]++;
      }
    }
    return seen.size() - oldPosition;
  }

  private String solveDay7a(List<String> inLines) {
    Pattern p = Pattern.compile("(\\w+)\\s+\\((\\d+)\\)(\\s+->(.*))*");
    Pattern p2 = Pattern.compile("\\W+");

    Map<String, Set<String>> tree = new HashMap<>();

    for (String line : inLines) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        String src = m.group(1);
        String dest = m.group(3);
        Set<String> targets = dest == null ? Collections.emptySet() : p2.splitAsStream(dest.substring(4)).collect(Collectors.toCollection(HashSet::new));
        tree.put(src, targets);
      }
    }

    while (tree.size() > 1) {
      Iterator<Entry<String, Set<String>>> it = tree.entrySet().iterator();
      Set<String> endpoints = new HashSet<>();
      while (it.hasNext()) {
        Entry<String, Set<String>> entry = it.next();
        if (entry.getValue().isEmpty()) {
          it.remove();
          endpoints.add(entry.getKey());
        }
      }

      it = tree.entrySet().iterator();
      while (it.hasNext()) {
        Entry<String, Set<String>> entry = it.next();
        entry.getValue().removeAll(endpoints);
      }
    }

    return tree.entrySet().iterator().next().getKey();
  }

  private int solveDay7b(List<String> inLines) {
    Pattern p = Pattern.compile("(\\w+)\\s+\\((\\d+)\\)(\\s+->(.*))*");
    Pattern p2 = Pattern.compile("\\W+");
    Map<String, Integer> node2Weight = new HashMap<>();

    Map<String, Set<String>> tree = new HashMap<>();
    Map<String, Set<String>> treeCpy = new HashMap<>();

    for (String line : inLines) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        String src = m.group(1);
        String dest = m.group(3);
        Set<String> targets = dest == null ? Collections.emptySet() : p2.splitAsStream(dest.substring(4)).collect(Collectors.toCollection(HashSet::new));
        tree.put(src, targets);
        node2Weight.put(src, Integer.parseInt(m.group(2)));
      }
    }
    for (Entry<String, Set<String>> entry : tree.entrySet()) {
      treeCpy.put(entry.getKey(), new HashSet<>(entry.getValue()));
    }

    while (tree.size() > 1) {
      Iterator<Entry<String, Set<String>>> it = tree.entrySet().iterator();
      Set<String> endpoints = new HashSet<>();
      while (it.hasNext()) {
        Entry<String, Set<String>> entry = it.next();
        if (entry.getValue().isEmpty()) {
          it.remove();
          endpoints.add(entry.getKey());
        }
      }

      it = tree.entrySet().iterator();
      while (it.hasNext()) {
        Entry<String, Set<String>> entry = it.next();
        entry.getValue().removeAll(endpoints);
      }
    }

    String top = tree.entrySet().iterator().next().getKey();

    // Now verify Tree's weight:

    try {
      isBalanced(treeCpy, top, node2Weight);
    } catch (Exception e) {
      return Integer.parseInt(e.getMessage());
    }
    return -1;

    /*
     * while (tree.size() > 1) {
     * Iterator<Entry<String, Set<String>>> it = tree.entrySet().iterator();
     * Set<String> endpoints = new HashSet<>();
     * while (it.hasNext()) {
     * Entry<String, Set<String>> entry = it.next();
     * if (entry.getValue().isEmpty()) {
     * it.remove();
     * endpoints.add(entry.getKey());
     * }
     * }
     * it = tree.entrySet().iterator();
     * while (it.hasNext()) {
     * Entry<String, Set<String>> entry = it.next();
     * if (endpoints.containsAll(entry.getValue())) {
     * Map<Integer, Set<String>> weights = new HashMap<>();
     * for (String s : entry.getValue()) {
     * weights.computeIfAbsent(node2weight.remove(s), HashSet::new).add(s);
     * }
     * if (weights.size() == 1) {
     * int sum = node2weight.get(entry.getKey()) + entry.getValue().size() *
     * weights.entrySet().iterator().next().getKey();
     * entry.getValue().clear();
     * node2weight.put(entry.getKey(), sum);
     * } else {
     * int toBe = 0;
     * int wrong = 0;
     * String wrongKey = "";
     * for (Entry<Integer, Set<String>> e : weights.entrySet()) {
     * if (e.getValue().size() == 1) {
     * wrong = e.getKey();
     * wrongKey = e.getValue().iterator().next();
     * } else {
     * toBe = e.getKey();
     * }
     * }
     * int toAdd = toBe - wrong;
     * return originalnode2weight.get(wrongKey) + toAdd;
     * }
     * }
     * }
     * }
     */

  }

  @Test
  public void testDay1() {
    assertEquals(3, solveDay1a("1122"));
    assertEquals(4, solveDay1a("1111"));
    assertEquals(0, solveDay1a("1234"));
    assertEquals(9, solveDay1a("91212129"));

    final String input = "494751136895345894732582362629576539599184296195318162664695189393364372585778868512194863927652788149779748657989318645936221887731542718562643272683862627537378624843614831337441659741281289638765171452576466381314558821636595394981788588673443769343597851883955668818165723174939893841654914556681324133667446412138511724424292394454166623639872425168644336248217213826339741267546823779383343362789527461579565822966859349777937921933694912369552152772735167832762563719664315456987186713541153781499646178238762644186484381142249926194743713139262596264878458636595896487362658672224346241358667234115974528626523648311919886566497837217169673923935143386823757293148719377821517314629812886912412829924484513493885672343964151252433622341141661523814465991516961684511941471572895453711624986269342398786175846925783918686856442684489873327497698963658862856336682422797551251489126661954848572297228765445646745256499679451426358865477844467458533962981852292513358871483321161973583245698763531598395467675529181496911117769834127516441369261275244225978893617456524385518493112272169767775861256649728253754964675812534546226295535939697352141217337346738553495616832783757866928174519145357234834584788253893618549484385733283627199445369658339175644484859385884574943219267922729967571943843794565736975716174727852348441254492886794362934343868643337828637454277582276962353246357835493338372219824371517526474283541714897994127864461433627894831268659336264234436872715374727211764167739169341999573855627775114848275268739159272518673316753672995297888734844388928439859359992475637439771269232916542385876779616695129412366735112593669719335783511355773814685491876721452994714318863716542473187246351548626157775143333161422867924437526253865859969947366972895674966845993244925218766937543487875485647329995285821739359369998935331986126873726737672159265827566443794515755939813676194755474477224152139987944419463371386499841415227734673733555261543871359797796529847861748979527579985757964742667473767269248335229836818297477665453189662485548925521497365877771665365728224394427883312135322325169141784";
    System.out.print("Solution 1b: ");
    System.out.println(solveDay1a(input));

    assertEquals(6, solveDay1b("1212"));
    assertEquals(0, solveDay1b("1221"));
    assertEquals(4, solveDay1b("123425"));
    assertEquals(12, solveDay1b("123123"));
    assertEquals(4, solveDay1b("12131415"));
    System.out.print("Solution 1b: ");
    System.out.println(solveDay1b(input));
  }

  @Test
  public void testDay2() throws Exception {
    List<String> testLines = Arrays.asList("5 1 9 5", "7 5 3", "2 4 6 8");
    assertEquals(18, solveDay2a(testLines));

    System.out.print("Solution 2a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2017/day2.txt").toURI()));
    System.out.println(solveDay2a(lines));

    testLines = Arrays.asList("5 8 2 8", "9 4 7 3", "3 8 6 5");
    assertEquals(9, solveDay2b(testLines));

    System.out.print("Solution 2b: ");
    System.out.println(solveDay2b(lines));
  }

  @Test
  public void testDay3() {
    assertEquals(0, solveDay3a(1));
    assertEquals(3, solveDay3a(12));
    assertEquals(2, solveDay3a(23));
    assertEquals(31, solveDay3a(1024));

    System.out.print("Solution 3a: ");
    System.out.println(solveDay3a(347991));

    System.out.print("Solution 3b: ");
    System.out.println(solveDay3b(347991));
  }

  @Test
  public void testDay4() throws Exception {
    System.out.print("Solution 4a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2017/day4.txt").toURI()));
    System.out.println(solveDay4(lines, this::isPassPhraseValid4a));

    assertTrue(isPassPhraseValid4b("abcde fghij"));
    assertFalse(isPassPhraseValid4b("abcde xyz ecdab"));
    assertTrue(isPassPhraseValid4b("a ab abc abd abf abj"));
    assertTrue(isPassPhraseValid4b("iiii oiii ooii oooi oooo"));
    assertFalse(isPassPhraseValid4b("oiii ioii iioi iiio"));

    System.out.print("Solution 4b: ");
    System.out.println(solveDay4(lines, this::isPassPhraseValid4b));
  }

  @Test
  public void testDay5() throws Exception {
    assertEquals(5, solveDay5a("0\\n3\\n0\\n1\\n-3"));

    System.out.print("Solution 5a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2017/day5.txt").toURI()));
    String line = lines.stream().collect(Collectors.joining("\\n"));
    System.out.println(solveDay5a(line));

    assertEquals(10, solveDay5b("0\\n3\\n0\\n1\\n-3"));
    System.out.print("Solution 5b: ");
    System.out.println(solveDay5b(line));
  }

  @Test
  public void testDay6() {
    String input = "0 2 7 0";
    assertEquals(5, solveDay6a(input));

    System.out.print("Solution 6a: ");
    input = "14 0   15  12  11  11  3   5   1   6   8   4   9   1   8   4";
    System.out.println(solveDay6a(input));

    input = "0 2 7 0";
    assertEquals(4, solveDay6b(input));

    System.out.print("Solution 6b: ");
    input = "14 0   15  12  11  11  3   5   1   6   8   4   9   1   8   4";
    System.out.println(solveDay6b(input));
  }

  @Test
  public void testDay7() throws Exception {
    List<String> testLines = Arrays.asList("pbga (66)", "xhth (57)", "ebii (61)", "havc (66)", "ktlj (57)", "fwft (72) -> ktlj, cntj, xhth", "qoyq (66)",
        "padx (45) -> pbga, havc, qoyq", "tknk (41) -> ugml, padx, fwft", "jptl (61)", "ugml (68) -> gyxo, ebii, jptl", "gyxo (61)", "cntj (57)");
    assertEquals("tknk", solveDay7a(testLines));

    System.out.print("Solution 7a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2017/day7.txt").toURI()));
    System.out.println(solveDay7a(lines));

    assertEquals(60, solveDay7b(testLines));
    System.out.print("Solution 7b: ");
    System.out.println(solveDay7b(lines));
  }

}
