package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle23 extends PuzzleAbs {

  private long countInRange(long[] bot, List<long[]> bots, boolean partA) {
    long inRange = 0;
    for (long[] candidate : bots) {
      long dist = Math.abs(bot[0] - candidate[0]) + Math.abs(bot[1] - candidate[1]) + Math.abs(bot[2] - candidate[2]);
      long compareWith = partA ? bot[3] : candidate[3] + bot[3];
      if (dist <= compareWith) {
        inRange++;
      }
    }

    return inRange;
  }

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(616, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(93750870, sum);
  }

  private List<long[]> parseInput(List<String> input) {
    // Parse bots, find bot with greatest r, check how many are in range
    Pattern p = Pattern.compile("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)");
    List<long[]> bots = new LinkedList<>();
    for (String line : input) {
      Matcher m = p.matcher(line);
      if (m.matches()) {
        long[] bot = new long[] {
            Long.parseLong(m.group(1)), Long.parseLong(m.group(2)), Long.parseLong(m.group(3)), Long.parseLong(m.group(4))
        };
        bots.add(bot);
      }
    }
    return bots;
  }

  private long solveA(List<String> input) {
    // Parse bots, find bot with greatest r, check how many are in range
    long[] greatest = new long[0];
    long maxR = Integer.MIN_VALUE;

    List<long[]> bots = parseInput(input);
    for (long[] bot : bots) {
      if (bot[3] > maxR) {
        maxR = bot[3];
        greatest = bot;
      }
    }

    // Count bots in range
    return countInRange(greatest, bots, true);
  }

  private long solveB(List<String> input) {
    List<long[]> bots = parseInput(input);

    Queue<long[]> queue = new PriorityQueue<>(new Comparator<long[]>() {
      @Override
      public int compare(long[] bot1, long[] bot2) {
        // Sort by overlapCount first, then distance to 0,0,0
        int result = Long.compare(bot2[4], bot1[4]);
        if (result == 0) {
          long dist1 = Math.abs(bot1[0]) + Math.abs(bot1[1]) + Math.abs(bot1[2]);
          long dist2 = Math.abs(bot2[0]) + Math.abs(bot2[1]) + Math.abs(bot2[2]);
          return Long.compare(dist1, dist2);
        } else {
          return result;
        }
      }
    });

    queue.add(new long[] {
        0, 0, 0, Integer.MAX_VALUE, -1
    });

    while (!queue.isEmpty()) {
      long[] bot = queue.poll();
      if (bot[3] == 0) { // Found!
        return bot[0] + bot[1] + bot[2];
      }
      for (long[] s : splitBot(bot)) {
        s[4] = countInRange(s, bots, false);
        queue.offer(s);
      }
    }

    return -1;
    // Find the coordinates that are in range of the largest number of nanobots. What is the shortest manhattan distance
    // between any of those points and 0,0,0?

    // Divide & Conquer:
    // Construct a bot that contains all other bots,
    // then repeatedly, break it up in smaller bots, choose the bot that contains must other bots as the one to break up
    // more
    // if the new bot has radius==0, we have found the coordinate that has the most overlaps.
  }

  private List<long[]> splitBot(long[] bot) {
    List<long[]> result = new LinkedList<>();

    long newR = 0;
    long offset = 1;
    if (bot[3] == 1) {
      result.add(new long[] {
          bot[0], bot[1], bot[2], newR, -1
      });
    } else if (bot[3] == 2) {
      newR = 1;
    } else {
      newR = (long)Math.ceil(0.556 * bot[3]);
      offset = bot[3] - newR;
    }

    result.add(new long[] {
        bot[0] - offset, bot[1], bot[2], newR, -1
    });
    result.add(new long[] {
        bot[0] + offset, bot[1], bot[2], newR, -1
    });

    result.add(new long[] {
        bot[0], bot[1] - offset, bot[2], newR, -1
    });
    result.add(new long[] {
        bot[0], bot[1] + offset, bot[2], newR, -1
    });

    result.add(new long[] {
        bot[0], bot[1], bot[2] - offset, newR, -1
    });
    result.add(new long[] {
        bot[0], bot[1], bot[2] + offset, newR, -1
    });
    return result;
  }

  @Test
  public void testA() {
    assertEquals(7, solveA(testInput()));
  }

  @Test
  public void testB() {
    assertEquals(36, solveB(List.of( //
        "pos=<10,12,12>, r=2", //
        "pos=<12,14,12>, r=2", //
        "pos=<16,12,12>, r=4", //
        "pos=<14,14,14>, r=6", //
        "pos=<50,50,50>, r=200", //
        "pos=<10,10,10>, r=5")));
  }

  private List<String> testInput() {
    return List.of( //
        "pos=<0,0,0>, r=4", //
        "pos=<1,0,0>, r=1", //
        "pos=<4,0,0>, r=3", //
        "pos=<0,2,0>, r=1", //
        "pos=<0,5,0>, r=3", //
        "pos=<0,0,3>, r=1", //
        "pos=<1,1,1>, r=1", //
        "pos=<1,1,2>, r=1", //
        "pos=<1,3,1>, r=1");
  }

}
