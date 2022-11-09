package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle14 extends PuzzleAbs {
  class Ingredient {
    String name;
    long amount;

    Ingredient(String s) {
      String[] parts = s.split("\\s");
      amount = Long.parseLong(parts[0]);
      name = parts[1];
    }

    @Override
    public boolean equals(Object o) {
      if (o instanceof Ingredient) {
        Ingredient other = (Ingredient)o;
        return name.equals(other.name) && amount == other.amount;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, amount);
    }

    @Override
    public String toString() {
      return "" + amount + " " + name;
    }
  }

  private static final String ORE = "ORE";

  public void calcOre(Map<String, Long> surplus, Map<String, Long> needed, Map<Ingredient, List<Ingredient>> output2inputs) {
    while (needed.size() != 1) {
      Map<String, Long> toAdd = new HashMap<>();

      Iterator<Entry<String, Long>> it = needed.entrySet().iterator();
      while (it.hasNext()) {
        Entry<String, Long> e = it.next();
        if (!ORE.equals(e.getKey())) {
          for (Ingredient candidate : output2inputs.keySet()) {
            if (candidate.name.equals(e.getKey())) {
              long need = e.getValue();
              if (surplus.containsKey(e.getKey())) {
                long have = surplus.get(e.getKey());
                if (have > need) {
                  surplus.put(e.getKey(), have - need);
                  need = 0;
                } else if (have == need) {
                  surplus.remove(e.getKey());
                  need = 0;
                } else if (have < need) {
                  surplus.remove(e.getKey());
                  need -= have;
                }
              }

              it.remove();
              if (need > 0) {
                long toomuch = -1;
                long mul = 1;
                if (candidate.amount < need) {
                  mul = need / candidate.amount;
                  if (mul * candidate.amount % need > 0) {
                    mul++;
                  }
                }
                toomuch = mul * candidate.amount - need;
                long multiplier = mul;
                long surpluss = toomuch;

                output2inputs.get(candidate).stream().forEach(i -> {
                  toAdd.compute(i.name, (k, v) -> v == null ? multiplier * i.amount : v + multiplier * i.amount);
                });
                if (surpluss > 0) {
                  surplus.compute(e.getKey(), (k, v) -> v == null ? surpluss : v + surpluss);
                }
                break;
              }
            }
          }
        }
      }
      for (Entry<String, Long> e : toAdd.entrySet()) {
        needed.compute(e.getKey(), (k, v) -> v == null ? e.getValue() : v + e.getValue());
      }
    }
  }

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA(getInput());
    System.out.println(sum);
    assertEquals(202617, sum);
  }

  @Test
  public void doB() {
    announceResultB();
    long sum = solveB(getInput());
    System.out.println(sum);
    assertEquals(7863863, sum);
  }

  Map<Ingredient, List<Ingredient>> parse(List<String> input) {
    Map<Ingredient, List<Ingredient>> output2inputs = new HashMap<>();
    for (String line : input) {
      String[] parts = line.split("\\s=>\\s");
      Ingredient key = new Ingredient(parts[1]);
      List<Ingredient> ingredients = new LinkedList<>();
      for (String s : parts[0].split(",\\s")) {
        ingredients.add(new Ingredient(s));
      }
      output2inputs.put(key, ingredients);
    }
    return output2inputs;
  }

  private long solveA(List<String> input) {
    Map<Ingredient, List<Ingredient>> output2inputs = parse(input);

    // Now how many ORE do we need to produce 1 FUEL?
    Ingredient fuel = new Ingredient("1 FUEL");
    Map<String, Long> surplus = new HashMap<>();
    Map<String, Long> needed = new HashMap<>();
    output2inputs.get(fuel).stream().forEach(i -> {
      needed.put(i.name, i.amount);
    });

    calcOre(surplus, needed, output2inputs);

    return needed.get(ORE);
  }

  private long solveB(List<String> input) {
    Map<Ingredient, List<Ingredient>> output2inputs = parse(input);
    long oreSupply = 1000000000000L;
    long oreFor1Fuel = solveA(input);
    long min = oreSupply / oreFor1Fuel;
    long max = min * 2; //

    Ingredient fuel = new Ingredient("1 FUEL");
    AtomicLong multiplicator = new AtomicLong();
    long oreNeeded = 1;

    while (max - min > 1) {
      long avg = (max + min) / 2;
      multiplicator.set(avg);
      Map<String, Long> surplus = new HashMap<>();
      Map<String, Long> needed = new HashMap<>();
      output2inputs.get(fuel).stream().forEach(i -> {
        needed.put(i.name, multiplicator.get() * i.amount);
      });

      calcOre(surplus, needed, output2inputs);
      oreNeeded = needed.get(ORE);
      if (oreNeeded > oreSupply) {
        max = multiplicator.get();
      } else {
        min = multiplicator.get();
      }
    }

    return min;
  }

  @Test
  public void testA() {
    assertEquals(31, solveA(testInput1()));
    assertEquals(165, solveA(testInput2()));
    assertEquals(13312, solveA(testInput3()));
    assertEquals(180697, solveA(testInput4()));
    assertEquals(2210736, solveA(testInput5()));
  }

  @Test
  public void testB() {
    assertEquals(82892753L, solveB(testInput3()));
    assertEquals(5586022L, solveB(testInput4()));
    assertEquals(460664L, solveB(testInput5()));
  }

  private List<String> testInput1() {
    return List.of( //
        "10 ORE => 10 A", //
        "1 ORE => 1 B", //
        "7 A, 1 B => 1 C", //
        "7 A, 1 C => 1 D", //
        "7 A, 1 D => 1 E", //
        "7 A, 1 E => 1 FUEL");
  }

  private List<String> testInput2() {
    return List.of( //
        "9 ORE => 2 A", //
        "8 ORE => 3 B", //
        "7 ORE => 5 C", //
        "3 A, 4 B => 1 AB", //
        "5 B, 7 C => 1 BC", //
        "4 C, 1 A => 1 CA", //
        "2 AB, 3 BC, 4 CA => 1 FUEL");
  }

  private List<String> testInput3() {
    return List.of( //
        "157 ORE => 5 NZVS", //
        "165 ORE => 6 DCFZ", //
        "44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL", //
        "12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ", //
        "179 ORE => 7 PSHF", //
        "177 ORE => 5 HKGWZ", //
        "7 DCFZ, 7 PSHF => 2 XJWVT", //
        "165 ORE => 2 GPVTF", //
        "3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT");
  }

  private List<String> testInput4() {
    return List.of( //
        "2 VPVL, 7 FWMGM, 2 CXFTF, 11 MNCFX => 1 STKFG", //
        "17 NVRVD, 3 JNWZP => 8 VPVL", //
        "53 STKFG, 6 MNCFX, 46 VJHF, 81 HVMC, 68 CXFTF, 25 GNMV => 1 FUEL", //
        "22 VJHF, 37 MNCFX => 5 FWMGM", //
        "139 ORE => 4 NVRVD", //
        "144 ORE => 7 JNWZP", //
        "5 MNCFX, 7 RFSQX, 2 FWMGM, 2 VPVL, 19 CXFTF => 3 HVMC", //
        "5 VJHF, 7 MNCFX, 9 VPVL, 37 CXFTF => 6 GNMV", //
        "145 ORE => 6 MNCFX", //
        "1 NVRVD => 8 CXFTF", //
        "1 VJHF, 6 MNCFX => 4 RFSQX", //
        "176 ORE => 6 VJHF");
  }

  private List<String> testInput5() {
    return List.of( //
        "171 ORE => 8 CNZTR", //
        "7 ZLQW, 3 BMBT, 9 XCVML, 26 XMNCP, 1 WPTQ, 2 MZWV, 1 RJRHP => 4 PLWSL", //
        "114 ORE => 4 BHXH", //
        "14 VRPVC => 6 BMBT", //
        "6 BHXH, 18 KTJDG, 12 WPTQ, 7 PLWSL, 31 FHTLT, 37 ZDVW => 1 FUEL", //
        "6 WPTQ, 2 BMBT, 8 ZLQW, 18 KTJDG, 1 XMNCP, 6 MZWV, 1 RJRHP => 6 FHTLT", //
        "15 XDBXC, 2 LTCX, 1 VRPVC => 6 ZLQW", //
        "13 WPTQ, 10 LTCX, 3 RJRHP, 14 XMNCP, 2 MZWV, 1 ZLQW => 1 ZDVW", //
        "5 BMBT => 4 WPTQ", //
        "189 ORE => 9 KTJDG", //
        "1 MZWV, 17 XDBXC, 3 XCVML => 2 XMNCP", //
        "12 VRPVC, 27 CNZTR => 2 XDBXC", //
        "15 KTJDG, 12 BHXH => 5 XCVML", //
        "3 BHXH, 2 VRPVC => 7 MZWV", //
        "121 ORE => 7 VRPVC", //
        "7 XCVML => 6 RJRHP", //
        "5 BHXH, 4 VRPVC => 5 LTCX");
  }

}
