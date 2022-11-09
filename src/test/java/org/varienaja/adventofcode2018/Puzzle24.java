package org.varienaja.adventofcode2018;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2018.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2018">adventofcode.com</a>
 */
public class Puzzle24 extends PuzzleAbs {
  class Group {
    String team;
    int units;
    int hp;
    int attack;
    int initiative;
    String attackType;
    List<String> weakness;
    List<String> immunity;

    Group(String team, String config) {
      this.team = team;
      String[] parts = config.replace("(", "").split(" ");
      units = Integer.parseInt(parts[0]);
      hp = Integer.parseInt(parts[4]);
      initiative = Integer.parseInt(parts[parts.length - 1]);
      attack = Integer.parseInt(after(parts, "does", 1));
      attackType = after(parts, "does", 2);
      weakness = after(parts, "weak", "to");
      immunity = after(parts, "immune", "to");
    }

    String after(String[] parts, String keyword, int after) {
      int ix = 0;
      while (ix < parts.length) {
        if (keyword.equals(parts[ix])) {
          return parts[ix + after];
        }
        ix++;
      }

      return "";
    }

    List<String> after(String[] parts, String keyword1, String keyword2) {
      int ix = 0;
      while (ix < parts.length) {
        if (keyword1.equals(parts[ix])) {
          if (keyword2.equals(parts[ix + 1])) {
            ix += 2;
            List<String> result = new LinkedList<>();
            String thing = "";
            boolean lastThing = false;
            do {
              thing = parts[ix++];
              lastThing = thing.endsWith(")") || thing.endsWith(";");
              if (lastThing || thing.endsWith(",")) {
                thing = thing.substring(0, thing.length() - 1);
              }
              result.add(thing);
            } while (!lastThing);
            return result;
          }
        }
        ix++;
      }

      return Collections.emptyList();
    }

    void attack(Group defender) {
      int damage = calcDamage(defender);
      defender.takeDamage(damage);
    }

    int calcDamage(Group defender) {
      int modifier = 1;
      if (defender.weakness.contains(attackType)) {
        modifier = 2;
      }
      if (defender.immunity.contains(attackType)) {
        modifier = 0;
      }
      return effectivePower() * modifier;
    }

    int effectivePower() {
      return units * attack;
    }

    int initiative() {
      return initiative;
    }

    void takeDamage(int damage) {
      units -= Math.min(damage / hp, units);
    }

    @Override
    public String toString() {
      return team + " (" + effectivePower() + ")";
    }

  }

  private static final String IMMUNESYSTEM = "Immune System:";

  private void boost(List<Group> groups, int boost) {
    groups.stream().filter(g -> g.team.equals(IMMUNESYSTEM)).forEach(g -> g.attack += boost);
  }

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(20753, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(3013, sum);
  }

  private Map<String, Integer> fight(List<Group> groups) {
    while (true) {
      // Target selection
      NavigableMap<Group, Group> attacker2defender = new TreeMap<>(Comparator.comparing(Group::initiative).reversed());
      List<Group> alive = groups.stream().filter(g -> g.units > 0).collect(Collectors.toList());
      alive.sort(Comparator.comparing(Group::effectivePower).thenComparing(Group::initiative).reversed());
      for (Group attacker : alive) {
        List<Group> defenders = alive.stream().filter(d -> !d.team.equals(attacker.team)).collect(Collectors.toList());
        defenders.removeAll(attacker2defender.values()); // Groups can only be attacked once

        Comparator<Group> c = Comparator.comparing(defender -> attacker.calcDamage(defender));
        Group mostDamaged = defenders.stream() //
            .filter(defender -> attacker.calcDamage(defender) > 0) //
            .max(c).orElse(null);
        attacker2defender.put(attacker, mostDamaged);
      }
      // System.out.println(attacker2defender);

      // Fight
      int unitssum = groups.stream().mapToInt(g -> g.units).sum();
      attacker2defender.entrySet().stream() //
          .filter(e -> e.getValue() != null) //
          .forEach(e -> e.getKey().attack(e.getValue()));
      if (unitssum == groups.stream().mapToInt(g -> g.units).sum()) {
        throw new IllegalStateException("Stalemate detected!");
      }

      Map<String, Integer> team2Units = groups.stream(). //
          collect(Collectors.groupingBy(g -> g.team, Collectors.summingInt(g -> g.units)));
      if (team2Units.containsValue(0)) {
        return team2Units;
      }
    }
  }

  private List<Group> parse(List<String> input) {
    List<Group> groups = new LinkedList<>();
    Iterator<String> it = input.iterator();
    String team = it.next();
    while (it.hasNext()) {
      String line = it.next();
      if (line.isEmpty()) {
        team = it.next();
        line = it.next();
      }
      groups.add(new Group(team, line));
    }
    return groups;
  }

  private long solveA(List<String> input) {
    List<Group> groups = parse(input);

    Map<String, Integer> team2Units = fight(groups);
    for (int v : team2Units.values()) {
      if (v != 0) {
        return v;
      }
    }
    return -1;
  }

  private long solveB(List<String> input) {
    Map<String, Integer> team2Units = null;
    int boost = 1;
    do {
      List<Group> groups = parse(input);
      boost++;
      boost(groups, boost);
      try {
        team2Units = fight(groups);
      } catch (IllegalStateException e) { // Stalemate, continue next
        team2Units = Map.of(IMMUNESYSTEM, 0);
      }
    } while (team2Units.get(IMMUNESYSTEM) == 0);
    return team2Units.get(IMMUNESYSTEM);
  }

  @Test
  public void testA() {
    assertEquals(5216, solveA(testInput()));
  }

  @Test
  public void testB() {
    assertEquals(51, solveB(testInput()));
  }

  private List<String> testInput() {
    return List.of( //
        IMMUNESYSTEM, //
        "17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2", //
        "989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3", //
        "", //
        "Infection:", //
        "801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1", //
        "4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4");
  }

}
