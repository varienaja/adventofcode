package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle22 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(13461553007L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals(1499, result);
  }

  @Test
  public void testA() {
    assertEquals(15887950, nextSecretNumber(123));
    assertEquals(16495136, nextSecretNumber(15887950));
    assertEquals(527345, nextSecretNumber(16495136));
    assertEquals(704524, nextSecretNumber(527345));
    assertEquals(1553684, nextSecretNumber(704524));
    assertEquals(12683156, nextSecretNumber(1553684));
    assertEquals(11100544, nextSecretNumber(12683156));
    assertEquals(12249484, nextSecretNumber(11100544));
    assertEquals(7753432, nextSecretNumber(12249484));
    assertEquals(5908254, nextSecretNumber(7753432));

    assertEquals(8685429, nthSecretNumber(1, 2000));
    assertEquals(4700978, nthSecretNumber(10, 2000));
    assertEquals(15273692, nthSecretNumber(100, 2000));
    assertEquals(8667524, nthSecretNumber(2024, 2000));
  }

  @Test
  public void testB() {
    assertEquals(23, solveB(getStreamingTestInput()));
  }

  private long nextSecretNumber(long secret) {
    secret = secret ^ secret << 6 & 16777215L;
    secret = secret ^ secret >> 5 & 16777215L;
    secret = secret ^ secret << 11 & 16777215L;
    return secret;
  }

  private long nthSecretNumber(long secret, int n) {
    for (int i = 0; i < n; ++i) {
      secret = nextSecretNumber(secret);
    }
    return secret;
  }

  private long solveA(Stream<String> input) {
    return input.mapToLong(Long::parseLong).map(secret -> nthSecretNumber(secret, 2000)).sum();
  }

  private long solveB(Stream<String> input) {
    List<long[]> secretLists = input.mapToLong(Long::parseLong).mapToObj(secret -> {
      long[] result = new long[2001];
      result[0] = secret;
      for (int i = 1; i < result.length; ++i) {
        result[i] = nextSecretNumber(result[i - 1]);
      }
      return result;
    }).toList();

    // Encodes each last digit of a secret as a character. 0=A, so prices of 0...9 become characters between A and I
    List<String> priceLists = secretLists.stream().map(sl -> {
      StringBuilder result = new StringBuilder();
      for (int i = 0; i < sl.length; ++i) {
        int lastDigit = (int)(sl[i] % 10);
        result.append((char)('A' + lastDigit));
      }
      return result.toString();
    }).toList();

    Set<String> uniqueDiffSequences = new HashSet<>();
    // Encodes each price difference a character. 0=I, so diffs of -9...9 become characters between A and R
    // Thus, each sequence of 4 characters can be easily stored in allSequences.
    List<String> priceDiffs = priceLists.stream().map(pl -> {
      StringBuilder result = new StringBuilder();
      for (int i = 1; i < pl.length(); ++i) {
        result.append((char)('I' + pl.charAt(i) - pl.charAt(i - 1)));
        if (i >= 4) {
          uniqueDiffSequences.add(result.substring(i - 4, i));
        }
      }
      return result.toString();
    }).toList();

    return uniqueDiffSequences.stream().parallel().mapToInt(seq -> {
      int result = 0;
      for (int i = 0; i < priceLists.size(); ++i) {
        int ix = priceDiffs.get(i).indexOf(seq);
        if (ix >= 0) {
          result += priceLists.get(i).charAt(ix + 4) - 'A';
        }
      }
      return result;
    }).max().orElseThrow();
  }

}
