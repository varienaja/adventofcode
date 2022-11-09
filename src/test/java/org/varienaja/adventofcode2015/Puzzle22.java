package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle22 extends PuzzleAbs {
  class Spell {
    String name;
    int manaCost;
    int damage;
    int effect;
    int heal;
    int armor;
    int manaGain;

    Spell(Spell s) {
      name = s.name;
      manaCost = s.manaCost;
      damage = s.damage;
      effect = s.effect;
      heal = s.heal;
      armor = s.armor;
      manaGain = s.manaGain;
    }

    Spell(String n, int m, int d, int e, int h, int a, int mg) {
      name = n;
      manaCost = m;
      damage = d;
      effect = e;
      heal = h;
      armor = a;
      manaGain = mg;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  private int bossDamage;
  private int minManaUsed;

  private List<Spell> spells = List.of( //
      new Spell("Recharge", 229, 0, 5, 0, 0, 101), //
      new Spell("Poison", 173, 3, 6, 0, 0, 0), //
      new Spell("Shield", 113, 0, 6, 0, 7, 0), //
      new Spell("Drain", 73, 2, 1, 2, 0, 0), //
      new Spell("Magic Missile", 53, 4, 1, 0, 0, 0) //
  );

  private void sim(int bossHP, int myHP, int myMana, List<Spell> activeSpells, boolean playerTurn, int manaUsed, boolean partB) {

    if (partB) {
      myHP--;
      if (myHP < 0) {
        return;
      }
    }

    int myArmor = 0;
    List<Spell> newActiveSpells = new LinkedList<>();
    for (Spell s : activeSpells) {
      if (s.effect > 0) {
        bossHP -= s.damage;
        myHP += s.heal;
        myArmor += s.armor;
        myMana += s.manaGain;
      }

      Spell wornoff = new Spell(s);
      wornoff.effect--;
      if (wornoff.effect > 0) {
        newActiveSpells.add(wornoff);
      }
    }

    if (bossHP <= 0) { // Win!
      if (manaUsed < minManaUsed) {
        minManaUsed = manaUsed;
      }
      return;
    }

    if (manaUsed >= minManaUsed) {
      return;
    }

    if (playerTurn) {
      Set<String> names = newActiveSpells.stream().map(sp -> sp.name).collect(Collectors.toSet());
      for (Spell s : spells) {
        if (myMana >= s.manaCost && !names.contains(s.name)) {
          List<Spell> newActiveSpellsCopy = newActiveSpells.stream().map(sp -> new Spell(sp)).collect(Collectors.toCollection(LinkedList::new));
          newActiveSpellsCopy.add(new Spell(s));
          sim(bossHP, myHP, myMana - s.manaCost, newActiveSpellsCopy, false, manaUsed + s.manaCost, partB);
        }
      }
    } else {
      myHP -= Math.max(bossDamage - myArmor, 1);
      if (myHP > 0) {
        sim(bossHP, myHP, myMana, newActiveSpells, true, manaUsed, partB);
      }
    }
  }

  private long solve(List<String> input, int myHP, int myMana, boolean partB) {
    minManaUsed = Integer.MAX_VALUE;
    int bossHP = Integer.parseInt(input.get(0).substring(1 + input.get(0).lastIndexOf(' ')));
    bossDamage = Integer.parseInt(input.get(1).substring(1 + input.get(1).lastIndexOf(' ')));

    sim(bossHP, myHP, myMana, Collections.emptyList(), true, 0, partB);
    return minManaUsed;
  }

  private long solveA(List<String> input, int myHP, int myMana) {
    return solve(input, myHP, myMana, false);
  }

  private long solveB(List<String> input, int myHP, int myMana) {
    return solve(input, myHP, myMana, true);
  }

  @Test
  public void testDay00() {
    List<String> testInput = List.of( //
        "Hit Points: 13", //
        "Damage: 8");
    assertEquals(226, solveA(testInput, 10, 250));
    testInput = List.of( //
        "Hit Points: 14", //
        "Damage: 8");
    assertEquals(641, solveA(testInput, 10, 250));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines, 50, 500);
    System.out.println(sum);
    assertEquals(1269, sum);
    // Not right: 1256
    // 1502 too high
    // 1216 too low
    // 1482 not right

    announceResultB();
    sum = solveB(lines, 50, 500);
    System.out.println(sum);
    assertEquals(1309, sum);
  }

}
