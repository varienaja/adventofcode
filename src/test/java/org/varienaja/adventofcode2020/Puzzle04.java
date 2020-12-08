package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle04 extends PuzzleAbs {

  private long solveA(List<String> lines) {
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

  private long solveB(List<String> lines) {
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

  @Test
  public void testDay04() throws IOException, URISyntaxException {
    assertEquals(1, solveA(Arrays.asList(//
        "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd", //
        "byr:1937 iyr:2017 cid:147 hgt:183cm", //
        "")));
    assertEquals(0, solveA(Arrays.asList(//
        "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884", //
        "hcl:#cfa07d byr:1929", //
        "")));
    assertEquals(1, solveA(Arrays.asList(//
        "hcl:#ae17e1 iyr:2013", //
        "eyr:2024", //
        "ecl:brn pid:760753108 byr:1931", //
        "hgt:179cm", //
        "")));
    assertEquals(0, solveA(Arrays.asList(//
        "hcl:#cfa07d eyr:2025 pid:166559648", //
        "iyr:2011 ecl:brn hgt:59in", //
        "")));
    announceResultA();
    List<String> lines = getInput();
    System.out.println(solveA(lines));

    assertEquals(0, solveB(Arrays.asList(//
        "eyr:1972 cid:100", //
        "hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926", //
        "")));
    assertEquals(0, solveB(Arrays.asList(//
        "iyr:2019", //
        "hcl:#602927 eyr:1967 hgt:170cm", //
        "ecl:grn pid:012533040 byr:1946", //
        "")));
    assertEquals(0, solveB(Arrays.asList(//
        "hcl:dab227 iyr:2012", //
        "ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277", //
        "")));
    assertEquals(0, solveB(Arrays.asList(//
        "hgt:59cm ecl:zzz", //
        "eyr:2038 hcl:74454a iyr:2023", //
        "pid:3556412378 byr:2007", //
        "")));
    assertEquals(1, solveB(Arrays.asList(//
        "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980", //
        "hcl:#623a2f", //
        "")));
    assertEquals(1, solveB(Arrays.asList(//
        "eyr:2029 ecl:blu cid:129 byr:1989", //
        "iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm", //
        "")));
    assertEquals(1, solveB(Arrays.asList(//
        "hcl:#888785", //
        "hgt:164cm byr:2001 iyr:2015 cid:88", //
        "pid:545766238 ecl:hzl", //
        "eyr:2022", //
        "")));
    assertEquals(1, solveB(Arrays.asList(//
        "iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719", //
        "")));

    announceResultB();
    System.out.println(solveB(lines));
  }

}
