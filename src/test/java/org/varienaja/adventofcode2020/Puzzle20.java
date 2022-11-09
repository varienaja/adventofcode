package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle20 extends PuzzleAbs {

  private class Tile {
    int key;
    char[][] tile;

    public Tile(int key, char[][] tile) {
      this.key = key;
      this.tile = tile;
    }

    public Tile(List<String> input) {
      key = 0;
      tile = new char[input.size()][input.size()];
      int i = 0;
      for (String line : input) {
        tile[i++] = line.toCharArray();
      }
    }

    private String getColumn(int x) {
      StringBuilder sb = new StringBuilder();
      for (int y = 0; y < 10; y++) {
        sb.append(tile[y][x]);
      }
      return sb.toString();
    }

    public String getLeftBorder() {
      return getColumn(0);
    }

    public String getLowerBorder() {
      return new String(tile[9]);
    }

    public String getRightBorder() {
      return getColumn(9);
    }

    public String getUpperBorder() {
      return new String(tile[0]);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("Tile: ");
      sb.append(key);
      sb.append("\n");

      for (int x = 0; x < tile.length; x++) {
        sb.append(new String(tile[x]));
        sb.append("\n");
      }

      return sb.toString();
    }
  }

  private Map<Integer, Tile> key2tile = new HashMap<>();
  private int size = 0;
  private List<Tile> upperLeftPieces;

  private long calcRoughness(Tile world) {
    List<String> seamonster = Arrays.asList( //
        "                  # ", //
        "#    ##    ##    ###", //
        " #  #  #  #  #  #   " //
    );
    char[][] monster = new char[3][20];
    int i = 0;
    for (String line : seamonster) {
      monster[i++] = line.toCharArray();
    }

    outer: for (int rotate = 0; rotate < 4; rotate++) {
      rotate(world);
      for (int flipH = 0; flipH < 2; flipH++) {
        flipH(world);
        for (int flipV = 0; flipV < 2; flipV++) {
          flipV(world);

          long monsterCount = 0;
          for (int y = 0; y < world.tile.length - monster.length; y++) {
            for (int x = 0; x < world.tile.length - monster[0].length; x++) {
              if (monsterAt(world, monster, x, y)) {
                monsterCount++;
              }
            }
          }

          if (monsterCount != 0) {
            break outer;
          }
        }
      }
    }
    // System.out.println(world);

    long result = 0;
    for (int y = 0; y < world.tile.length; y++) {
      for (int x = 0; x < world.tile.length; x++) {
        if (world.tile[x][y] == '#') {
          result++;
        }
      }
    }

    return result;
  }

  private boolean doPuzzle(Map<Point, Integer> solution, Collection<Tile> candidates) {
    if (candidates.isEmpty()) {
      return true;
    }
    for (Tile candidate : candidates) {
      for (int x = 0; x < size; x++) {
        for (int y = 0; y < size; y++) {
          Point candidatePoint = new Point(x, y);
          if (!solution.containsKey(candidatePoint) && isAdjacentToExisting(solution, candidatePoint)) {

            for (int rotate = 0; rotate < 4; rotate++) {
              rotate(candidate);
              for (int flipH = 0; flipH < 2; flipH++) {
                flipH(candidate);
                for (int flipV = 0; flipV < 2; flipV++) {
                  flipV(candidate);

                  if (fits(solution, candidatePoint, candidate)) {
                    solution.put(candidatePoint, candidate.key);

                    List<Tile> newCandidates = new LinkedList<>(candidates);
                    newCandidates.remove(candidate);

                    boolean result = doPuzzle(solution, newCandidates);
                    if (!result) {
                      solution.remove(candidatePoint); // did not fit anyway; continue trying differently
                    } else {
                      return result;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    return false; // no solution?
  }

  private boolean fits(Map<Point, Integer> solution, Point candidatePos, Tile candidate) {
    if (solution.isEmpty()) {
      return true;
    }

    for (Point existing : solution.keySet()) {
      if (existing.nextTo(candidatePos)) {
        Tile ex = key2tile.get(solution.get(existing));

        if (existing.x == candidatePos.x) {
          if (existing.y < candidatePos.y) { // candidate below existing
            for (int x = 0; x < 10; x++) {
              if (ex.tile[x][9] != candidate.tile[x][0]) {
                return false;
              }
            }
          } else { // candidate above existing
            for (int x = 0; x < 10; x++) {
              if (ex.tile[x][0] != candidate.tile[x][9]) {
                return false;
              }
            }
          }
        } else {
          if (existing.x < candidatePos.x) { // candidate to the right of existing
            for (int y = 0; y < 10; y++) {
              if (ex.tile[9][y] != candidate.tile[0][y]) {
                return false;
              }
            }
          } else { // candidate to the left of existing
            for (int y = 0; y < 10; y++) {
              if (ex.tile[0][y] != candidate.tile[9][y]) {
                return false;
              }
            }
          }
        }
      }
    }

    return true;
  }

  private List<String> fixOrder(List<String> l) {
    List<String> result = new LinkedList<>();
    for (String border : l) {
      String reversed = new StringBuilder(border).reverse().toString();
      if (border.compareTo(reversed) > 0) {
        result.add(border);
      } else {
        result.add(reversed);
      }
    }

    return result;
  }

  private void flipH(Tile tile) {
    char[][] flipped = new char[tile.tile.length][tile.tile.length];
    for (int x = 0; x < tile.tile.length; x++) {
      for (int y = 0; y < tile.tile.length; y++) {
        flipped[x][y] = tile.tile[x][tile.tile.length - 1 - y];
      }
    }
    tile.tile = flipped;
  }

  private void flipV(Tile tile) {
    char[][] flipped = new char[tile.tile.length][tile.tile.length];
    for (int x = 0; x < tile.tile.length; x++) {
      for (int y = 0; y < tile.tile.length; y++) {
        flipped[x][y] = tile.tile[tile.tile.length - 1 - x][y];
      }
    }
    tile.tile = flipped;
  }

  private boolean isAdjacentToExisting(Map<Point, Integer> solution, Point candidatePoint) {
    if (solution.isEmpty()) {
      return true;
    }

    for (Point existing : solution.keySet()) {
      if (existing.nextTo(candidatePoint)) {
        return true;
      }
    }
    return false;
  }

  private boolean monsterAt(Tile world, char[][] monster, int x, int y) {
    for (int dx = 0; dx < monster[0].length; dx++) {
      for (int dy = 0; dy < monster.length; dy++) {
        if (monster[dy][dx] == '#') {
          if (world.tile[x + dx][y + dy] != '#') {
            return false;
          }
        }
      }
    }

    // Found monster; change world
    for (int dx = 0; dx < monster[0].length; dx++) {
      for (int dy = 0; dy < monster.length; dy++) {
        if (monster[dy][dx] == '#') {
          world.tile[x + dx][y + dy] = 'O';
        }
      }
    }

    return true;
  }

  private void parse(List<String> lines) {
    key2tile = new HashMap<>();

    int key = 0;
    int row = 0;
    char[][] tile = new char[0][0];
    for (String line : lines) {
      if (line.startsWith("Tile")) {
        key = Integer.parseInt(line.substring(5, 9));
        tile = new char[10][10];
        row = 0;
      } else if (line.isEmpty()) {
        Tile nt = new Tile(key, tile);
        key2tile.put(key, nt);
      } else {
        tile[row++] = line.toCharArray();
      }
    }
    size = (int)Math.sqrt(key2tile.size());
  }

  private void rotate(Tile tile) {
    char[][] rotated = new char[tile.tile.length][tile.tile.length];
    for (int x = 0; x < tile.tile.length; x++) {
      for (int y = 0; y < tile.tile.length; y++) {
        rotated[x][y] = tile.tile[tile.tile.length - 1 - y][x];
      }
    }
    tile.tile = rotated;
  }

  private List<String> solutionToLines(Map<Point, Integer> solution, boolean pretty, boolean withBorder) {
    List<String> result = new LinkedList<>();

    int start = withBorder ? 0 : 1;
    int end = withBorder ? 10 : 9;

    if (solution.size() == size * size) {
      for (int x = 0; x < 10 * size; x++) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < 10 * size; y++) {
          Point p = new Point(x / 10, y / 10);
          Tile t = key2tile.get(solution.get(p));
          int xx = x % 10;
          int yy = y % 10;
          if (withBorder) {
            sb.append(t.tile[xx][yy]);
          } else {
            if ((xx >= start && xx < end) && (yy >= start && yy < end)) {
              sb.append(t.tile[xx][yy]);
            }
          }
        }
        result.add(sb.toString());
        sb = new StringBuilder();
      }
    }

    Iterator<String> it = result.iterator();
    while (it.hasNext()) {
      String line = it.next();
      if (line.isEmpty()) {
        it.remove();
      }
    }

    return result;
  }

  private long solveA(List<String> lines) {
    parse(lines);

    Map<Tile, List<String>> tile2Border = new HashMap<>();
    for (Tile t : key2tile.values()) {
      List<String> l = fixOrder(Arrays.asList(t.getUpperBorder(), t.getLowerBorder(), t.getLeftBorder(), t.getRightBorder()));
      tile2Border.put(t, l);
    }

    Map<String, List<Tile>> border2Tiles = new HashMap<>();
    for (Entry<Tile, List<String>> e : tile2Border.entrySet()) {
      for (String b : e.getValue()) {
        border2Tiles.computeIfAbsent(b, border -> new LinkedList<>()).add(e.getKey());
      }
    }

    // Tileborders that are shared by only one tile are tiles that belong on the puzzle border
    // I need the tiles that have two such borders, those are the four edges

    List<String> onlyOneSharedBorder = new LinkedList<>();
    for (Entry<String, List<Tile>> e : border2Tiles.entrySet()) {
      if (new HashSet<>(e.getValue()).size() == 1) {
        onlyOneSharedBorder.add(e.getKey());
      }
    }

    upperLeftPieces = new LinkedList<>();
    long result = 1L;
    for (Entry<Tile, List<String>> e : tile2Border.entrySet()) {
      List<String> oneSided = new LinkedList<>(onlyOneSharedBorder);
      oneSided.retainAll(e.getValue());
      if (oneSided.size() == 2) {
        Tile t = e.getKey();
        result *= t.key;

        if (oneSided.get(0).equals(t.getUpperBorder()) && oneSided.get(1).equals(t.getLeftBorder())
            || oneSided.get(1).equals(t.getUpperBorder()) && oneSided.get(0).equals(t.getLeftBorder())) {
          upperLeftPieces.add(e.getKey());
        }
      }
    }

    return result;
  }

  private Tile solveB(List<String> lines) {
    Tile upperLeft = upperLeftPieces.get(0);
    List<Tile> remaining = new LinkedList<>(key2tile.values());
    remaining.remove(upperLeft);

    Map<Point, Integer> solution = new HashMap<>();
    solution.put(new Point(0, 0), upperLeft.key);
    doPuzzle(solution, remaining);

    Tile map = new Tile(solutionToLines(solution, false, false));
    // System.out.println(map);
    return map;
  }

  @Test
  public void testDay20() {
    List<String> input = Arrays.asList( //
        "Tile 2311:", //
        "..##.#..#.", //
        "##..#.....", //
        "#...##..#.", //
        "####.#...#", //
        "##.##.###.", //
        "##...#.###", //
        ".#.#.#..##", //
        "..#....#..", //
        "###...#.#.", //
        "..###..###", //
        "", //
        "Tile 1951:", //
        "#.##...##.", //
        "#.####...#", //
        ".....#..##", //
        "#...######", //
        ".##.#....#", //
        ".###.#####", //
        "###.##.##.", //
        ".###....#.", //
        "..#.#..#.#", //
        "#...##.#..", //
        "", //
        "Tile 1171:", //
        "####...##.", //
        "#..##.#..#", //
        "##.#..#.#.", //
        ".###.####.", //
        "..###.####", //
        ".##....##.", //
        ".#...####.", //
        "#.##.####.", //
        "####..#...", //
        ".....##...", //
        "", //
        "Tile 1427:", //
        "###.##.#..", //
        ".#..#.##..", //
        ".#.##.#..#", //
        "#.#.#.##.#", //
        "....#...##", //
        "...##..##.", //
        "...#.#####", //
        ".#.####.#.", //
        "..#..###.#", //
        "..##.#..#.", //
        "", //
        "Tile 1489:", //
        "##.#.#....", //
        "..##...#..", //
        ".##..##...", //
        "..#...#...", //
        "#####...#.", //
        "#..#.#.#.#", //
        "...#.#.#..", //
        "##.#...##.", //
        "..##.##.##", //
        "###.##.#..", //
        "", //
        "Tile 2473:", //
        "#....####.", //
        "#..#.##...", //
        "#.##..#...", //
        "######.#.#", //
        ".#...#.#.#", //
        ".#########", //
        ".###.#..#.", //
        "########.#", //
        "##...##.#.", //
        "..###.#.#.", //
        "", //
        "Tile 2971:", //
        "..#.#....#", //
        "#...###...", //
        "#.#.###...", //
        "##.##..#..", //
        ".#####..##", //
        ".#..####.#", //
        "#..#.#..#.", //
        "..####.###", //
        "..#.#.###.", //
        "...#.#.#.#", //
        "", //
        "Tile 2729:", //
        "...#.#.#.#", //
        "####.#....", //
        "..#.#.....", //
        "....#..#.#", //
        ".##..##.#.", //
        ".#.####...", //
        "####.#.#..", //
        "##.####...", //
        "##..#.##..", //
        "#.##...##.", //
        "", //
        "Tile 3079:", //
        "#.#.#####.", //
        ".#..######", //
        "..#.......", //
        "######....", //
        "####.#..#.", //
        ".#...#.##.", //
        "#.#####.##", //
        "..#.###...", //
        "..#.......", //
        "..#.###...", //
        ""//
    );
    assertEquals(20899048083289L, solveA(input));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(60145080587029L, result);
    System.out.println(result);

    input = Arrays.asList( //
        ".#.#..#.##...#.##..#####", //
        "###....#.#....#..#......", //
        "##.##.###.#.#..######...", //
        "###.#####...#.#####.#..#", //
        "##.#....#.##.####...#.##", //
        "...########.#....#####.#", //
        "....#..#...##..#.#.###..", //
        ".####...#..#.....#......", //
        "#..#.##..#..###.#.##....", //
        "#.####..#.####.#.#.###..", //
        "###.#.#...#.######.#..##", //
        "#.####....##..########.#", //
        "##..##.#...#...#.#.#.#..", //
        "...#..#..#.#.##..###.###", //
        ".#.#....#.##.#...###.##.", //
        "###.#...#..#.##.######..", //
        ".#.#.###.##.##.#..#.##..", //
        ".####.###.#...###.#..#.#", //
        "..#.#..#..#.#.#.####.###", //
        "#..####...#.#.#.###.###.", //
        "#####..#####...###....##", //
        "#.##..#..#...#..####...#", //
        ".#.###..##..##..####.##.", //
        "...###...##...#...#..###"//
    );
    Tile testMap = new Tile(input);
    result = calcRoughness(testMap);
    assertEquals(273, result);

    announceResultB();
    Tile realMap = solveB(lines);
    result = calcRoughness(realMap);
    assertEquals(1901L, result);
    System.out.println(result);
  }

}
