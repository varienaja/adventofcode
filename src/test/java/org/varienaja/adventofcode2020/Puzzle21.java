package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle21 extends PuzzleAbs {
  private Set<String> allergies;
  private Map<String, AtomicInteger> ingredient2Count;
  private Map<String, Set<String>> solution;

  private long solveA(List<String> lines) {
    allergies = new HashSet<>();
    ingredient2Count = new TreeMap<>();
    solution = new HashMap<>();
    List<SimpleEntry<String, Set<String>>> input = new LinkedList<>();

    for (String line : lines) {
      String[] parts = line.split(" \\(contains ");
      List<String> ins = Arrays.asList(parts[0].split("\\s"));
      String[] alls = parts[1].replace(")", "").split(",\\s");

      ins.forEach(ingredient -> ingredient2Count.computeIfAbsent(ingredient, in -> new AtomicInteger()).incrementAndGet());
      for (String all : alls) {
        solution.computeIfAbsent(all, a -> new HashSet<>()).addAll(ins);
        allergies.add(all);
        input.add(new SimpleEntry<>(all, new HashSet<>(ins)));
      }
    }

    for (SimpleEntry<String, Set<String>> e : input) {
      List<String> ingredients = new LinkedList<>(ingredient2Count.keySet());
      ingredients.removeAll(e.getValue());
      solution.get(e.getKey()).removeAll(ingredients);
    }

    Set<String> usedIngredients = solution.values().stream() //
        .flatMap(v -> v.stream()) //
        .collect(Collectors.toSet());
    return ingredient2Count.entrySet().stream()//
        .filter(e -> !usedIngredients.contains(e.getKey())) //
        .map(e -> e.getValue().get()) //
        .mapToInt(i -> i) //
        .sum();
  }

  private String solveB() {
    SortedMap<String, String> allergie2Ingredient = new TreeMap<>();
    while (allergie2Ingredient.size() < allergies.size()) {
      for (Entry<String, Set<String>> e : solution.entrySet()) {
        String allergie = e.getKey();
        Set<String> ingredients = e.getValue();
        if (ingredients.size() == 1 && !allergie2Ingredient.keySet().contains(allergie)) {
          String in = ingredients.iterator().next();
          allergie2Ingredient.put(allergie, in);
          allergies.forEach(a -> solution.get(a).remove(in));
        }
      }
    }

    return allergie2Ingredient.entrySet().stream() //
        .map(Entry::getValue).collect(Collectors.joining(","));
  }

  @Test
  public void testDay21() {
    List<String> input = Arrays.asList( //
        "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)", //
        "trh fvjkl sbzzf mxmxvkd (contains dairy)", //
        "sqjhc fvjkl (contains soy)", //
        "sqjhc mxmxvkd sbzzf (contains fish)" //
    );
    assertEquals(5, solveA(input));
    assertEquals("mxmxvkd,sqjhc,fvjkl", solveB());

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(2072L, result);
    System.out.println(result);

    announceResultB();
    String res = solveB();
    assertEquals("fdsfpg,jmvxx,lkv,cbzcgvc,kfgln,pqqks,pqrvc,lclnj", res);
    System.out.println(res);
  }

}
