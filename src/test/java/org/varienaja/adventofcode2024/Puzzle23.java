package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle23 extends PuzzleAbs {
  private Map<String, NavigableSet<String>> connections;

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getStreamingInput());
    System.out.println(result);
    assertEquals(1163, result);
  }

  @Test
  public void doB() {
    announceResultB();
    String result = solveB(getStreamingInput());
    System.out.println(result);
    assertEquals("bm,bo,ee,fo,gt,hv,jv,kd,md,mu,nm,wx,xh", result);
  }

  @Test
  public void testA() {
    assertEquals(7, solveA(getStreamingTestInput()));
  }

  @Test
  public void testB() {
    assertEquals("co,de,ka,ta", solveB(getStreamingTestInput()));
  }

  /**
   * Calculates all maximal cliques.
   *
   * @see https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
   * @param r initially empty possible maximal clique
   * @param p all edges
   * @param x initially empty
   * @return all maximal cliques
   */
  private Set<Set<String>> calcBronKerbosch(Set<String> r, Set<String> p, Set<String> x) {
    if (p.isEmpty() && x.isEmpty()) {
      return Set.of(r);
    }

    Set<Set<String>> result = new HashSet<>();

    Iterator<String> it = p.iterator();
    while (it.hasNext()) {
      String edge = it.next();

      Set<String> rv = new HashSet<>(r);
      rv.add(edge);

      Set<String> pnv = new HashSet<>(p);
      pnv.retainAll(connections.get(edge));

      Set<String> xnv = new HashSet<>(x);
      xnv.retainAll(connections.get(edge));

      result.addAll(calcBronKerbosch(rv, pnv, xnv));
      it.remove();
      x.add(edge);
    }
    return result;
  }

  private void parse(Stream<String> input) {
    connections = new HashMap<>();
    input.map(s -> s.split("-")).forEach(parts -> {
      connections.compute(parts[0], (k, v) -> v == null ? new TreeSet<>() : v).add(parts[1]);
      connections.compute(parts[1], (k, v) -> v == null ? new TreeSet<>() : v).add(parts[0]);
    });
  }

  private long solveA(Stream<String> input) {
    parse(input);

    Set<Set<String>> interconnected = new HashSet<>();
    for (Entry<String, NavigableSet<String>> e : connections.entrySet()) {
      for (String c1 : e.getValue()) {
        for (String c2 : e.getValue().tailSet(c1, false)) {
          if (connections.get(c1).contains(c2)) { // Triplet found
            Set<String> triplet = Set.of(e.getKey(), c1, c2);
            if (triplet.stream().anyMatch(f -> f.startsWith("t"))) {
              interconnected.add(triplet);
            }
          }
        }
      }
    }

    return interconnected.size();
  }

  private String solveB(Stream<String> input) {
    parse(input);

    Set<Set<String>> cliques = calcBronKerbosch(new HashSet<>(), new HashSet<>(connections.keySet()), new HashSet<>());
    Set<String> biggest = cliques.stream().max(Comparator.comparing(Set::size)).orElseThrow();
    Set<String> edges = biggest.stream().collect(Collectors.toCollection(TreeSet::new));
    return edges.stream().collect(Collectors.joining(","));
  }

}
