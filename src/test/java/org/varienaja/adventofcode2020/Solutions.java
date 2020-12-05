package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Solutions {

  private int solveDay1a(int[] numbers) {
    for (int i = 0; i < numbers.length - 1; i++) {
      for (int j = i + 1; j < numbers.length; j++) {
        if (numbers[i] + numbers[j] == 2020) {
          return numbers[i] * numbers[j];
        }
      }
    }
    return -1;
  }

  private int solveDay1b(int[] numbers) {
    for (int i = 0; i < numbers.length - 2; i++) {
      for (int j = i + 1; j < numbers.length - 1; j++) {
        for (int k = j + 1; k < numbers.length; k++) {
          if (numbers[i] + numbers[j] + numbers[k] == 2020) {
            return numbers[i] * numbers[j] * numbers[k];
          }
        }
      }
    }
    return -1;
  }

  private int solveDay2a(List<String> lines) {
    int matches = 0;
    Pattern p = Pattern.compile("(\\d+)-(\\d+)\\s+(.):\\s+(.*)");
    for (String line : lines) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        int min = Integer.valueOf(m.group(1));
        int max = Integer.valueOf(m.group(2));
        char theChar = m.group(3).charAt(0);
        String pwd = m.group(4);

        int charCount = 0;
        for (char c : pwd.toCharArray()) {
          if (c == theChar) {
            charCount++;
          }
        }
        if (charCount >= min && charCount <= max) {
          matches++;
        }
      }

    }
    return matches;
  }

  private int solveDay2b(List<String> lines) {
    int matches = 0;
    Pattern p = Pattern.compile("(\\d+)-(\\d+)\\s+(.):\\s+(.*)");
    for (String line : lines) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        int pos1 = Integer.valueOf(m.group(1)) - 1;
        int pos2 = Integer.valueOf(m.group(2)) - 1;
        char theChar = m.group(3).charAt(0);
        String pwd = m.group(4);

        if (pwd.charAt(pos1) == theChar ^ pwd.charAt(pos2) == theChar) {
          matches++;
        }
      }

    }
    return matches;
  }

  private long solveDay3a(List<String> lines, int dX, int dY) {
    int width = lines.get(0).length();
    int x = dX;
    int y = dY;

    long treeCount = 0;
    do {
      if (lines.get(y).charAt(x) == '#') {
        treeCount++;
      }

      x += dX;
      if (x >= width) {
        x -= width;
      }
      y += dY;
    } while (y < lines.size());
    return treeCount;
  }

  private long solveDay3b(List<String> lines) {
    return solveDay3a(lines, 1, 1) * //
        solveDay3a(lines, 3, 1) * //
        solveDay3a(lines, 5, 1) * //
        solveDay3a(lines, 7, 1) * //
        solveDay3a(lines, 1, 2);
  }

  private long solveDay4a(List<String> lines) {
    List<String> mandatory = Arrays.asList("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
    int validCount = 0;

    final StringBuilder ppProps = new StringBuilder();
    for (String line : lines) {
      ppProps.append(line).append(" ");

      if (line.isEmpty()) { // verify
        if (mandatory.stream().allMatch(m -> ppProps.indexOf(m + ":") >= 0)) {
          validCount++;
        }
        ppProps.setLength(0);
      }
    }
    return validCount;
  }

  private long solveDay4b(List<String> lines) {
    class Validation {
      Pattern p;
      int minValue;
      int maxValue;

      Validation(String regex) {
        this(regex, -1, -1);
      }

      Validation(String regex, int min, int max) {
        p = Pattern.compile(regex);
        minValue = min;
        maxValue = max;
      }

      boolean isValid(String value) {
        Matcher m = p.matcher(value);
        if (!m.matches()) {
          return false;
        }

        if (m.groupCount() == 0) {
          return true;
        }

        int v = Integer.parseInt(m.group(1));
        return v >= minValue && v <= maxValue;
      }
    }

    class Validations {
      Validation[] validations;

      Validations(Validation... vs) {
        validations = vs;
      }

      boolean isValid(String value) {
        return Stream.of(validations).anyMatch(v -> v.isValid(value));
      }
    }

    Map<String, Validations> mandatory = new HashMap<>();
    mandatory.put("byr", new Validations(new Validation("(\\d{4})", 1920, 2002)));
    mandatory.put("iyr", new Validations(new Validation("(\\d{4})", 2010, 2020)));
    mandatory.put("eyr", new Validations(new Validation("(\\d{4})", 2020, 2030)));
    mandatory.put("hgt", new Validations(new Validation("(\\d+)in", 59, 76), new Validation("(\\d+)cm", 150, 193)));
    mandatory.put("hcl", new Validations(new Validation("#[0-9a-f]{6}")));
    mandatory.put("ecl", new Validations(new Validation("amb|blu|brn|gry|grn|hzl|oth")));
    mandatory.put("pid", new Validations(new Validation("\\d{9}")));

    final StringBuilder ppProps = new StringBuilder();
    Predicate<Entry<String, Validations>> p = e -> {
      int start = ppProps.indexOf(e.getKey() + ":");
      if (start == -1) {
        return false;
      }
      start += 1 + e.getKey().length();
      int end = ppProps.indexOf(" ", start);
      return e.getValue().isValid(ppProps.substring(start, end));
    };

    int validCount = 0;
    for (String line : lines) {
      ppProps.append(line).append(" ");

      if (line.isEmpty()) { // verify
        if (mandatory.entrySet().stream().allMatch(p)) {
          validCount++;
        }
        ppProps.setLength(0);
      }
    }
    return validCount;
  }

  private int solveDay5a(String input) {
    String r = input.replaceAll("F|L", "0").replaceAll("B|R", "1");
    return Integer.parseInt(r, 2);
  }

  @Test
  public void testDay01() throws IOException, URISyntaxException {
    int[] input = {
        1721, 979, 366, 299, 675, 1456
    };
    assertEquals(514579, solveDay1a(input));

    System.out.print("Solution 1a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2020/day1.txt").toURI()));
    int[] numbers = lines.stream().map(Integer::parseInt).mapToInt(i -> i).toArray();
    System.out.println(solveDay1a(numbers));

    assertEquals(241861950, solveDay1b(input));
    System.out.print("Solution 1b: ");
    System.out.println(solveDay1b(numbers));
  }

  @Test
  public void testDay02() throws IOException, URISyntaxException {
    List<String> input = Arrays.asList("1-3 a: abcde", "1-3 b: cdefg", "2-9 c: ccccccccc");
    assertEquals(2, solveDay2a(input));

    System.out.print("Solution 2a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2020/day2.txt").toURI()));
    System.out.println(solveDay2a(lines));

    assertEquals(1, solveDay2b(input));
    System.out.print("Solution 2b: ");
    System.out.println(solveDay2b(lines));
  }

  @Test
  public void testDay03() throws IOException, URISyntaxException {
    List<String> input = Arrays.asList("..##.......", //
        "#...#...#..", //
        ".#....#..#.", //
        "..#.#...#.#", //
        ".#...##..#.", //
        "..#.##.....", //
        ".#.#.#....#", //
        ".#........#", //
        "#.##...#...", //
        "#...##....#", //
        ".#..#...#.#");
    assertEquals(7, solveDay3a(input, 3, 1));

    System.out.print("Solution 3a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2020/day3.txt").toURI()));
    System.out.println(solveDay3a(lines, 3, 1));

    assertEquals(336, solveDay3b(input));
    System.out.print("Solution 3b: ");
    System.out.println(solveDay3b(lines));
  }

  @Test
  public void testDay04() throws IOException, URISyntaxException {
    assertEquals(1, solveDay4b(Arrays.asList(//
        "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd", //
        "byr:1937 iyr:2017 cid:147 hgt:183cm", //
        "")));

    assertEquals(0, solveDay4b(Arrays.asList(//
        "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884", //
        "hcl:#cfa07d byr:1929", //
        "")));
    assertEquals(1, solveDay4b(Arrays.asList(//
        "hcl:#ae17e1 iyr:2013", //
        "eyr:2024", //
        "ecl:brn pid:760753108 byr:1931", //
        "hgt:179cm", //
        "")));
    assertEquals(0, solveDay4b(Arrays.asList(//
        "hcl:#cfa07d eyr:2025 pid:166559648", //
        "iyr:2011 ecl:brn hgt:59in", //
        "")));

    System.out.print("Solution 4a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2020/day4.txt").toURI()));
    System.out.println(solveDay4a(lines));

    assertEquals(0, solveDay4b(Arrays.asList(//
        "eyr:1972 cid:100", //
        "hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926", //
        "")));
    assertEquals(0, solveDay4b(Arrays.asList(//
        "iyr:2019", //
        "hcl:#602927 eyr:1967 hgt:170cm", //
        "ecl:grn pid:012533040 byr:1946", //
        "")));
    assertEquals(0, solveDay4b(Arrays.asList(//
        "hcl:dab227 iyr:2012", //
        "ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277", //
        "")));
    assertEquals(0, solveDay4b(Arrays.asList(//
        "hgt:59cm ecl:zzz", //
        "eyr:2038 hcl:74454a iyr:2023", //
        "pid:3556412378 byr:2007", //
        "")));
    assertEquals(1, solveDay4b(Arrays.asList(//
        "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980", //
        "hcl:#623a2f", //
        "")));
    assertEquals(1, solveDay4b(Arrays.asList(//
        "eyr:2029 ecl:blu cid:129 byr:1989", //
        "iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm", //
        "")));
    assertEquals(1, solveDay4b(Arrays.asList(//
        "hcl:#888785", //
        "hgt:164cm byr:2001 iyr:2015 cid:88", //
        "pid:545766238 ecl:hzl", //
        "eyr:2022", //
        "")));
    assertEquals(1, solveDay4b(Arrays.asList(//
        "iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719", //
        "")));

    System.out.print("Solution 4b: ");
    System.out.println(solveDay4b(lines));
  }

  @Test
  public void testDay05() throws IOException, URISyntaxException {
    assertEquals(567, solveDay5a("BFFFBBFRRR"));
    assertEquals(119, solveDay5a("FFFBBBFRRR"));
    assertEquals(820, solveDay5a("BBFFBBFRLL"));

    System.out.print("Solution 5a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2020/day5.txt").toURI()));
    IntSummaryStatistics s = lines.stream().map(this::solveDay5a).mapToInt(i -> i).summaryStatistics();
    System.out.println(s.getMax());

    Set<Integer> allSeats = IntStream.rangeClosed(s.getMin(), s.getMax()).boxed().collect(Collectors.toSet());
    lines.stream().map(this::solveDay5a).forEach(allSeats::remove);
    System.out.print("Solution 5b: ");
    System.out.println(allSeats);
  }

}
