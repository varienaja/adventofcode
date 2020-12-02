package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Arjan Verstoep
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Solutions {

  private int solveDay1a(int[] numbers) {
    for (int i = 0; i < numbers.length - 1; i++) {
      for (int j = i; j < numbers.length; j++) {
        if (numbers[i] + numbers[j] == 2020) {
          return numbers[i] * numbers[j];
        }
      }
    }
    return -1;
  }

  private int solveDay1b(int[] numbers) {
    for (int i = 0; i < numbers.length - 2; i++) {
      for (int j = i; j < numbers.length - 1; j++) {
        for (int k = j; k < numbers.length; k++) {
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
        int pos1 = Integer.valueOf(m.group(1));
        int pos2 = Integer.valueOf(m.group(2));
        char theChar = m.group(3).charAt(0);
        String pwd = m.group(4);

        if (pwd.charAt(pos1 - 1) == theChar ^ pwd.charAt(pos2 - 1) == theChar) {
          matches++;
        }
      }

    }
    return matches;
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

}
