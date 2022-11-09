package org.varienaja.adventofcode2017;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2017.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2017">adventofcode.com</a>
 */
public class Puzzle04 extends PuzzleAbs {

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

  private int solveDay4(List<String> inInput, Function<String, Boolean> inFunction) {
    int validPassPhrases = 0;
    for (String passphrase : inInput) {
      if (inFunction.apply(passphrase)) {
        validPassPhrases++;
      }
    }
    return validPassPhrases;
  }

  @Test
  public void testDay4() throws Exception {
    System.out.print("Solution 4a: ");
    List<String> lines = Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource("2017/day04.txt").toURI()));
    System.out.println(solveDay4(lines, this::isPassPhraseValid4a));

    assertTrue(isPassPhraseValid4b("abcde fghij"));
    assertFalse(isPassPhraseValid4b("abcde xyz ecdab"));
    assertTrue(isPassPhraseValid4b("a ab abc abd abf abj"));
    assertTrue(isPassPhraseValid4b("iiii oiii ooii oooi oooo"));
    assertFalse(isPassPhraseValid4b("oiii ioii iioi iiio"));

    System.out.print("Solution 4b: ");
    System.out.println(solveDay4(lines, this::isPassPhraseValid4b));
  }

}
