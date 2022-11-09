package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle25 extends PuzzleAbs {

  public static <T> List<T> findAll(String haystack, String regexp, Function<String, T> converter) {
    Matcher matcher = Pattern.compile(regexp).matcher(haystack);
    List<T> allMatches = new ArrayList<>();
    while (matcher.find()) {
      allMatches.add(converter.apply(matcher.group()));
    }
    return allMatches;
  }

  public static List<Integer> findAllInts(String haystack) {
    return findAll(haystack, "[-\\d]+", Integer::parseInt);
  }

  private long calc(String input) {
    List<String> forbidden = List.of("escape pod", "photons", "infinite loop", "molten lava", "giant electromagnet");
    List<String> stuff = new ArrayList<>();
    List<String> dirs = List.of("east", "west", "north", "south");
    Random r = new Random();
    List<String> wannaTake = new ArrayList<>();
    BlockingQueue<Long> in = new ArrayBlockingQueue<>(2000);
    BlockingQueue<Long> out = new ArrayBlockingQueue<>(2000);
    Future<Long> f = Intcode.run(input, Map.of(), in, out);
    while (true) {
      try {
        Thread.sleep(100L);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      StringBuilder sb = new StringBuilder();
      while (!out.isEmpty()) {
        Long output = out.poll();
        sb.append((char)output.intValue());
      }
      System.out.println(sb);
      if (sb.toString().contains("You can't go that way.")) {
        break;
      }
      if (sb.toString().contains("== Pressure-Sensitive Floor ==")) {
        if (sb.toString().contains("Alert! Droids on this ship are heavier than the detected value!\"")
            || sb.toString().contains("Alert! Droids on this ship are lighter than the detected value!\"")) {
          f.cancel(true);
          f = Intcode.run(input, Map.of(), in, out);
          wannaTake = new ArrayList<>();
          for (String st : stuff) {
            if (r.nextBoolean()) {
              wannaTake.add(st);
            }
          }
          continue;
        } else {
          System.out.println(sb.toString());
          return findAllInts(sb.toString().substring(sb.indexOf("typing"))).get(0);
        }
      }
      for (String s : sb.toString().split("\n")) {
        if (s.startsWith("- ")) {
          s = s.substring(2);
          if (!dirs.contains(s) && !forbidden.contains(s) && wannaTake.contains(s)) {
            String ss = "take " + s + "\n";
            for (char c : ss.toCharArray()) {
              in.offer((long)c);
            }
          }
          if (!dirs.contains(s) && !forbidden.contains(s) && !stuff.contains(s)) {
            System.out.println("Found new stuff: " + s);
            String ss = "take " + s + "\n";
            for (char c : ss.toCharArray()) {
              in.offer((long)c);
            }
            stuff.add(s);
          }
        }
      }

      String dir;
      do {
        int d = r.nextInt(4);
        dir = dirs.get(d);
      } while (!sb.toString().contains(dir));

      String ss = dir + "\n";
      for (char c : ss.toCharArray()) {
        in.offer((long)c);
      }
    }
    return 0;
  }

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInputString());
    System.out.println(sum);
    assertEquals(20372, sum);
  }

  public long solveA(String input) {
    Scanner scanner = new Scanner(System.in);

    BlockingQueue<Long> in = new ArrayBlockingQueue<>(2000);
    BlockingQueue<Long> out = new ArrayBlockingQueue<>(2000);
    Intcode.run(input, Map.of(), in, out);

    while (true) {
      try {
        Thread.sleep(100L);
      } catch (InterruptedException e) {
      }
      while (!out.isEmpty()) {
        long l = out.poll();
        System.out.print((char)l);
      }

      String command = scanner.nextLine();
      for (char c : command.toCharArray()) {
        in.offer((long)c);
      }
      in.offer(10L);
    }
  }

}
