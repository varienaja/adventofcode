package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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

  @Test
  public void testDay1a() throws IOException, URISyntaxException {
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

}
