package org.varienaja.adventofcode2016;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle10 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(86L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(22847L, result);
  }

  @Test
  public void testA() {
    assertEquals(2, calculateSolution10a(getTestInput(), 5, 2));
  }

  @Override
  protected List<String> getTestInput() {
    return List.of( //
        "value 5 goes to bot 2", //
        "bot 2 gives low to bot 1 and high to bot 0", //
        "value 3 goes to bot 1", //
        "bot 1 gives low to output 1 and high to bot 0", //
        "bot 0 gives low to output 2 and high to output 0", //
        "value 2 goes to bot 2");
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

  private long solveA(List<String> lines) {
    return calculateSolution10a(lines, 61, 17);
  }

  private long solveB(List<String> lines) {
    return calculateSolution10b(lines);
  }
}
