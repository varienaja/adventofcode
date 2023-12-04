package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle07 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(1642503L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(6999588L, result);
  }

  @Test
  public void testA() {
    assertEquals(95437L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(24933642L, solveB(getTestInput()));
  }

  @Override
  protected List<String> getTestInput() {
    return List.of( //
        "$ cd /", //
        "$ ls", //
        "dir a", //
        "14848514 b.txt", //
        "8504156 c.dat", //
        "dir d", //
        "$ cd a", //
        "$ ls", //
        "dir e", //
        "29116 f", //
        "2557 g", //
        "62596 h.lst", //
        "$ cd e", //
        "$ ls", //
        "584 i", //
        "$ cd ..", //
        "$ cd ..", //
        "$ cd d", //
        "$ ls", //
        "4060174 j", //
        "8033020 d.log", //
        "5626152 d.ext", //
        "7214296 k");
  }

  private Map<String, Long> solve(List<String> lines) {
    Map<String, Long> dirname2size = new TreeMap<>();
    Stack<String> pwd = new Stack<>();

    for (String line : lines) {
      if ("$ cd /".equals(line)) { // to root
        pwd = new Stack<>();
        pwd.push("/");
      } else if ("$ cd ..".equals(line)) { // go up
        pwd.pop();
      } else if (line.startsWith("$ cd ")) { // go in
        pwd.push(line.substring(5) + "/");
      } else if ("$ ls".equals(line)) { // Ignore
      } else if (line.startsWith("dir ")) { // Ignore
      } else { // File
        long fileSize = Long.parseLong(line.split("\\s")[0]);

        StringBuilder path = new StringBuilder();
        for (String dir : pwd) {
          path.append(dir);
          dirname2size.compute(path.toString(), (k, v) -> v == null ? fileSize : v + fileSize);
        }
      }
    }

    return dirname2size;
  }

  private long solveA(List<String> lines) {
    Map<String, Long> dirname2size = solve(lines);

    return dirname2size.values().stream().mapToLong(l -> l).filter(l -> l < 100000).sum();
  }

  private long solveB(List<String> lines) {
    Map<String, Long> dirname2size = solve(lines);

    long usedTotal = dirname2size.get("/");
    long usedMax = 70000000L - 30000000L;
    long needToFree = usedTotal - usedMax;

    return dirname2size.values().stream().mapToLong(l -> l).filter(l -> l > needToFree).min().orElse(Long.MIN_VALUE);
  }

}
