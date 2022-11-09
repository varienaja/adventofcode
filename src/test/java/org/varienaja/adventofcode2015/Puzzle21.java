package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle21 extends PuzzleAbs {
  class Item {
    int cost;
    int damage;
    int armor;

    Item(int c, int d, int a) {
      cost = c;
      damage = d;
      armor = a;
    }
  }

  private List<Item> weapons = List.of( //
      new Item(8, 4, 0), // Dagger
      new Item(10, 5, 0), // Shortsword
      new Item(25, 6, 0), // Warhammer
      new Item(40, 7, 0), // Longsword
      new Item(74, 8, 0) // Greataxe
  );

  private List<Item> armor = List.of( //
      new Item(0, 0, 0), // Nothing
      new Item(13, 0, 1), // Leather
      new Item(31, 0, 2), // Chainmail
      new Item(53, 0, 3), // Splintmail
      new Item(75, 0, 4), // Bandedmail
      new Item(102, 0, 5) // Platemail
  );

  private List<Item> rings = List.of( //
      new Item(0, 0, 0), // Nothing
      new Item(0, 0, 0), // Nothing
      new Item(25, 1, 0), // Damage +1
      new Item(50, 2, 0), // Damage +2
      new Item(100, 3, 0), // Damage +3
      new Item(20, 0, 1), // Defense +1
      new Item(40, 0, 2), // Defense +2
      new Item(80, 0, 3) // Defense +3
  );

  private int fight(int bossHP, int bossDamage, int bossArmor, int myHP, int myDamage, int myArmor) {
    boolean done = false;
    while (!done) {
      bossHP -= Math.max(1, myDamage - bossArmor);
      done = bossHP <= 0;

      if (!done) {
        myHP -= Math.max(1, bossDamage - myArmor);
      }
    }
    return myHP;
  }

  private long solveA(List<String> input) {
    int bossHP = Integer.parseInt(input.get(0).substring(1 + input.get(0).lastIndexOf(' ')));
    int bossDamage = Integer.parseInt(input.get(1).substring(1 + input.get(1).lastIndexOf(' ')));
    int bossArmor = Integer.parseInt(input.get(2).substring(1 + input.get(2).lastIndexOf(' ')));

    // I buy one weapon
    // at most one armor
    // 0,1,2 rings
    // Which way, I win with least money spent?
    NavigableSet<Integer> winCosts = new TreeSet<>();
    for (Item w : weapons) {
      for (Item a : armor) {
        for (Item r1 : rings) {
          for (Item r2 : rings) {
            if (r1 == r2) {
              continue; // Same Ring twice nog allowed
            }

            int myHP = 100;
            int myArmor = w.armor + a.armor + r1.armor + r2.armor;
            int myDamage = w.damage + a.damage + r1.damage + r2.damage;
            if (fight(bossHP, bossDamage, bossArmor, myHP, myDamage, myArmor) > 0) { // I win
              winCosts.add(w.cost + a.cost + r1.cost + r2.cost);
            }
          }
        }
      }
    }

    return winCosts.first();
  }

  private long solveB(List<String> input) {
    int bossHP = Integer.parseInt(input.get(0).substring(1 + input.get(0).lastIndexOf(' ')));
    int bossDamage = Integer.parseInt(input.get(1).substring(1 + input.get(1).lastIndexOf(' ')));
    int bossArmor = Integer.parseInt(input.get(2).substring(1 + input.get(2).lastIndexOf(' ')));

    // I buy one weapon
    // at most one armor
    // 0,1,2 rings
    // Which way, I lose with max money spent?
    NavigableSet<Integer> winCosts = new TreeSet<>();
    for (Item w : weapons) {
      for (Item a : armor) {
        for (Item r1 : rings) {
          for (Item r2 : rings) {
            if (r1 == r2) {
              continue; // Same Ring twice nog allowed
            }

            int myHP = 100;
            int myArmor = w.armor + a.armor + r1.armor + r2.armor;
            int myDamage = w.damage + a.damage + r1.damage + r2.damage;
            if (fight(bossHP, bossDamage, bossArmor, myHP, myDamage, myArmor) <= 0) { // I loose
              winCosts.add(w.cost + a.cost + r1.cost + r2.cost);
            }
          }
        }
      }
    }

    return winCosts.last();
  }

  @Test
  public void testDay00() {
    assertEquals(2, fight(12, 7, 2, 8, 5, 5));

    announceResultA();
    List<String> lines = getInput();
    long sum = solveA(lines);
    System.out.println(sum);
    assertEquals(78, sum);

    announceResultB();
    sum = solveB(lines);
    System.out.println(sum);
    assertEquals(148, sum);
  }

}
