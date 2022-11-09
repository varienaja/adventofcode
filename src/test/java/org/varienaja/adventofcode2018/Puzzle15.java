package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.Point;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle15 extends PuzzleAbs {
  class Elf implements Comparable<Elf> {
    Point position;
    int hp = 200;
    int attack = 3;

    Elf(int x, int y) {
      position = new Point(x, y);
    }

    Elf attack(List<? extends Elf> enemies) {
      // Find weakest enemy, and attack
      List<Point> candidates = List.of(position.getNorth(), position.getWest(), position.getEast(), position.getSouth());
      Elf weakestEnemy = null;
      int weakestEnemyHP = Integer.MAX_VALUE;
      for (Point c : candidates) {
        for (Elf enemyCandidate : enemies) {
          if (c.equals(enemyCandidate.position)) { // Found an enemy
            if (enemyCandidate.hp < weakestEnemyHP) {
              weakestEnemy = enemyCandidate;
              weakestEnemyHP = enemyCandidate.hp;
            }
          }
        }
      }

      if (weakestEnemy != null) {
        // Attack!
        weakestEnemy.hp -= attack;
        if (weakestEnemy.hp <= 0) {
          if (DEBUG) {
            System.out.println(weakestEnemy.getClass().getSimpleName() + " " + weakestEnemy.position + " dies");
          }
          if (!(weakestEnemy instanceof Goblin)) {
            elfDeaths++;
          }
          enemies.remove(weakestEnemy);
          grid.put(weakestEnemy.position, '.');
          return weakestEnemy;
        }
      }
      return null;
    }

    /**
     * Calculates the shortest path from myself to each Point in the given Map.
     * Unreachable points are removed.
     *
     * @param p2path the map which values are to be computed.
     */
    private void calcAllShortestPaths(Map<Point, List<Point>> p2path) {
      // I expand around myself, if a point from the map is reached, we compute it's value
      Set<Point> visited = new HashSet<>();
      int found = 0;
      Map<Point, List<Point>> routes = new HashMap<>();
      routes.put(position, new LinkedList<>());

      while (!routes.isEmpty() && found < p2path.size()) {
        NavigableMap<Point, List<Point>> toAdd = new TreeMap<>((p1, p2) -> {
          int result = Integer.compare(p1.y, p2.y);
          if (result == 0) {
            result = Integer.compare(p1.x, p2.x);
          }
          return result;
        });

        for (Entry<Point, List<Point>> e : routes.entrySet()) {
          Point p = e.getKey();
          List<Point> candidates = List.of(p.getNorth(), p.getWest(), p.getEast(), p.getSouth()).stream().filter(nb -> !visited.contains(nb))
              .collect(Collectors.toList());
          for (Point c : candidates) {
            List<Point> val = p2path.get(c);
            if (val != null && val.isEmpty()) {
              val.addAll(e.getValue());
              val.add(c);
              found++;
            } else {
              Character cc = grid.get(c);
              if (Character.valueOf('.').equals(cc)) {
                List<Point> newRoute = new LinkedList<>(e.getValue());
                newRoute.add(c);
                visited.add(c);
                toAdd.put(c, newRoute);
              }
            }
          }
        }

        routes = toAdd;
      }

      Iterator<Entry<Point, List<Point>>> it = p2path.entrySet().iterator();
      while (it.hasNext()) {
        Entry<Point, List<Point>> e = it.next();
        if (e.getValue().isEmpty()) {
          it.remove();
        }
      }
    }

    @Override
    public int compareTo(Elf o) {
      int result = Integer.compare(position.y, o.position.y);
      if (result == 0) {
        //
        // System.out.println("After " + round + " rounds (elfattack: " + elfAttack + "):");
        // for (int y = 0; y < input.size(); y++) {
        // for (int x = 0; x < input.get(y).length(); x++) {
        // Character c = grid.get(new Point(x, y));
        // System.out.print(c == null ? '#' : c);
        // }
        // System.out.println();
        // }

        result = Integer.compare(position.x, o.position.x);
      }
      return result;
    }

    void move(List<? extends Elf> enemies) {
      NavigableMap<Point, List<Point>> p2path = new TreeMap<>((p1, p2) -> {
        int result = Integer.compare(p1.y, p2.y);
        if (result == 0) {
          result = Integer.compare(p1.x, p2.x);
        }
        return result;
      });

      for (Elf e : enemies) {
        if (position.manhattanDistance(e.position) == 1) {
          return; // Enemy found next to me; don't move
        }

        // Find shortest route to enemy's bordering positions that are '.'
        List<Point> candidates = List.of(e.position.getNorth(), e.position.getWest(), e.position.getEast(), e.position.getSouth());
        for (Point p : candidates) {
          Character cc = grid.get(p);
          if (Character.valueOf('.').equals(cc)) {
            p2path.put(p, new LinkedList<>());
          }
        }
      }
      calcAllShortestPaths(p2path);

      if (!p2path.isEmpty()) {
        // Choose nearest (if more than one, read-direction)
        int nearest = p2path.values().stream().mapToInt(l -> l.size()).min().orElse(Integer.MAX_VALUE);
        Iterator<Entry<Point, List<Point>>> it = p2path.entrySet().iterator();
        while (it.hasNext()) {
          Entry<Point, List<Point>> e = it.next();
          if (e.getValue().size() != nearest) {
            it.remove();
          }
        }

        // Finally, move 1 towards nearest enemy
        position = p2path.firstEntry().getValue().get(0);
      }
    }

  }

  class Goblin extends Elf {
    Goblin(int x, int y) {
      super(x, y);
    }
  }

  private static final boolean DEBUG = false;
  private int elfDeaths;
  private Map<Point, Character> grid;

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(250648, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(42224, sum);
  }

  private long solve(List<String> input, boolean partB) {
    List<Elf> elves = new LinkedList<>();
    List<Goblin> goblins = new LinkedList<>();
    int elfAttack = 3;
    long round;

    do {
      elfDeaths = 0;
      grid = new HashMap<>();
      elves = new LinkedList<>();
      goblins = new LinkedList<>();
      List<Elf> all = new LinkedList<>();
      for (int y = 0; y < input.size(); y++) {
        String line = input.get(y);
        for (int x = 0; x < line.length(); x++) {
          char c = line.charAt(x);
          if (".EG".indexOf(c) != -1) {
            grid.put(new Point(x, y), c);
            if (c == 'E') {
              Elf elf = new Elf(x, y);
              elf.attack = elfAttack;
              elves.add(elf);
            } else if (c == 'G') {
              goblins.add(new Goblin(x, y));
            }
          }
        }
      }

      round = 0;
      while (!elves.isEmpty() && !goblins.isEmpty()) { // do a move
        all.clear();
        all.addAll(elves);
        all.addAll(goblins);
        Collections.sort(all);

        boolean incOK = true;
        Iterator<Elf> it = all.iterator();
        while (it.hasNext()) {

          Elf e = it.next();
          if (e.hp > 0) {
            List<? extends Elf> enemies = e.getClass().equals(Elf.class) ? goblins : elves;
            incOK = enemies.size() > 0;

            Point start = e.position;
            e.move(enemies);
            Point end = e.position;
            if (!start.equals(end)) {
              grid.put(start, '.');
              grid.put(end, e.getClass().equals(Elf.class) ? 'E' : 'G');
            }
            e.attack(enemies);
          }
        }

        if (DEBUG) {
          System.out.println("After " + round + " rounds (elfattack: " + elfAttack + "):");
          for (int y = 0; y < input.size(); y++) {
            for (int x = 0; x < input.get(y).length(); x++) {
              Character c = grid.get(new Point(x, y));
              System.out.print(c == null ? '#' : c);
            }
            System.out.println();
          }
        }

        if (incOK) {
          round++;
        }
      }
      elfAttack++;
    } while (partB && elfDeaths != 0);

    int hpLeft = elves.stream().mapToInt(e -> e.hp).sum() + goblins.stream().mapToInt(e -> e.hp).sum();
    return round * hpLeft;
  }

  private long solveA(List<String> input) {
    return solve(input, false);
  }

  private long solveB(List<String> input) {
    return solve(input, true);
  }

  @Test
  public void testA() {
    assertEquals(27730, solveA(List.of( //
        "#######", //
        "#.G...#", //
        "#...EG#", //
        "#.#.#G#", //
        "#..G#E#", //
        "#.....#", //
        "#######")));

    assertEquals(36334, solveA(List.of( //
        "#######", //
        "#G..#E#", //
        "#E#E.E#", //
        "#G.##.#", //
        "#...#E#", //
        "#...E.#", //
        "#######")));

    assertEquals(39514, solveA(List.of( //
        "#######", //
        "#E..EG#", //
        "#.#G.E#", //
        "#E.##E#", //
        "#G..#.#", //
        "#..E#.#", //
        "#######")));

    assertEquals(27755, solveA(List.of( //
        "#######", //
        "#E.G#.#", //
        "#.#G..#", //
        "#G.#.G#", //
        "#G..#.#", //
        "#...E.#", //
        "#######")));

    assertEquals(28944, solveA(List.of( //
        "#######", //
        "#.E...#", //
        "#.#..G#", //
        "#.###.#", //
        "#E#G#G#", //
        "#...#G#", //
        "#######")));

    assertEquals(18740, solveA(List.of( //
        "#########", //
        "#G......#", //
        "#.E.#...#", //
        "#..##..G#", //
        "#...##..#", //
        "#...#...#", //
        "#.G...G.#", //
        "#.....G.#", //
        "#########")));
  }

  @Test
  public void testB() {
    assertEquals(4988, solveB(List.of( //
        "#######", //
        "#.G...#", //
        "#...EG#", //
        "#.#.#G#", //
        "#..G#E#", //
        "#.....#", //
        "#######")));

    assertEquals(31284, solveB(List.of( //
        "#######", //
        "#E..EG#", //
        "#.#G.E#", //
        "#E.##E#", //
        "#G..#.#", //
        "#..E#.#", //
        "#######")));

    assertEquals(3478, solveB(List.of( //
        "#######", //
        "#E.G#.#", //
        "#.#G..#", //
        "#G.#.G#", //
        "#G..#.#", //
        "#...E.#", //
        "#######")));

    assertEquals(6474, solveB(List.of( //
        "#######", //
        "#.E...#", //
        "#.#..G#", //
        "#.###.#", //
        "#E#G#G#", //
        "#...#G#", //
        "#######")));

    assertEquals(1140, solveB(List.of( //
        "#########", //
        "#G......#", //
        "#.E.#...#", //
        "#..##..G#", //
        "#...##..#", //
        "#...#...#", //
        "#.G...G.#", //
        "#.....G.#", //
        "#########")));
  }

}
