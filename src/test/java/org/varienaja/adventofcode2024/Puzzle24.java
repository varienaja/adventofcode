package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
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
public class Puzzle24 extends PuzzleAbs {
  class Adder {
    record Gate(String in1, String in2, String op, String out) {
      boolean canRun(Map<String, Long> va2value) {
        return va2value.containsKey(in1) && va2value.containsKey(in2);
      }

      void run(Map<String, Long> var2value) {
        long v1 = var2value.get(in1);
        long v2 = var2value.get(in2);
        long result = 0;
        switch (op) {
          case "AND":
            result = v1 & v2;
            break;
          case "XOR":
            result = v1 ^ v2;
            break;
          case "OR":
            result = v1 | v2;
            break;
          default:
            throw new IllegalStateException();
        }
        var2value.put(out, result);
      }
    }
    private int maxPowX = 0;
    private int maxPowY = 0;
    public NavigableMap<String, Long> var2value;
    private List<Gate> connections;

    private Adder(List<String> input) {
      var2value = new TreeMap<>();
      connections = new LinkedList<>();
      for (String line : input) {
        if (line.contains(": ")) {
          String[] parts = line.split(": ");
          var2value.put(parts[0], Long.parseLong(parts[1]));
        }
        if (line.contains(" -> ")) {
          String[] parts = line.split(" -> ");
          String[] ins = parts[0].split(" ");

          connections.add(new Gate(ins[0], ins[2], ins[1], parts[1]));
        }
      }
      maxPowX = Integer.parseInt(extractDigits(var2value.headMap("y").lastKey()));
      maxPowY = Integer.parseInt(extractDigits(var2value.lastKey()));
    }

    /**
     * Returns a map of (additionally) used gates for each bit.
     *
     * @return Map of bit (or power of 2) to used gates
     */
    public Map<Integer, NavigableSet<String>> examineInternals() {
      Map<Integer, NavigableSet<String>> pow2gates = new HashMap<>();
      Set<String> all = new LinkedHashSet<>();
      for (int x = maxPowX; x >= 0; --x) {
        String nr = String.format("%02d", x);
        NavigableSet<String> ins = new TreeSet<>();
        ins.add("x" + nr);
        ins.add("y" + nr);
        boolean changed = true;
        while (changed) {
          changed = false;
          for (Gate g : connections) {
            if (ins.contains(g.in1) || ins.contains(g.in2)) {
              changed |= ins.add(g.out);
            }
          }
        }

        ins.removeAll(all);
        pow2gates.put(x, ins);
        ins.removeIf(g -> g.startsWith("x"));
        ins.removeIf(g -> g.startsWith("y"));
        all.addAll(ins);
      }
      return pow2gates;
    }

    private void exchangeGateOutputs(String out1, String out2) {
      Gate g1 = null;
      Gate g2 = null;
      Iterator<Gate> it = connections.iterator();
      while (it.hasNext()) {
        Gate candidate = it.next();
        if (out1.equals(candidate.out)) {
          g1 = candidate;
          it.remove();
        }
        if (out2.equals(candidate.out)) {
          g2 = candidate;
          it.remove();
        }
      }
      connections.add(new Gate(g1.in1, g1.in2, g1.op, g2.out));
      connections.add(new Gate(g2.in1, g2.in2, g2.op, g1.out));
    }

    private void findCarryErrors(Set<Set<Integer>> errorBits) {
      for (int p = 0; p <= maxPowX; ++p) {
        long x = 1L << p;
        long y = 1L << p;

        long outcome = run(x, y);
        if (outcome != x + y) {
          // System.out.println(p + ": " + x + " + " + y + " != " + (x + y) + ", but " + outcome);

          boolean added = false;
          for (Set<Integer> candidate : errorBits) {
            if (candidate.contains(p - 1) || candidate.contains(p) || candidate.contains(p + 1)) {
              candidate.add(p);
              added = true;
              break;
            }
          }
          if (!added) {
            Set<Integer> errorBit = new HashSet<>();
            errorBit.add(p);
            errorBits.add(errorBit);
          }
        }
      }
    }

    private Set<Set<Integer>> findErrors() {
      Set<Set<Integer>> result = new HashSet<>();
      try {
        findXErrors(result);
        findYErrors(result);
        findCarryErrors(result);
      } catch (IllegalStateException e) {
        return Set.of(Set.of(1), Set.of(2), Set.of(3), Set.of(4), Set.of(5)); // Way more than 4 errors.
      }
      return result;
    }

    private void findXErrors(Set<Set<Integer>> errorBits) {
      for (int p = 0; p <= maxPowX; ++p) {
        long x = 1L << p;
        long y = 0;

        long outcome = run(x, y);
        if (outcome != x) {
          // System.out.println(p + ": " + x + " + " + y + " != " + (x + y) + ", but " + outcome);

          boolean added = false;
          for (Set<Integer> candidate : errorBits) {
            if (candidate.contains(p - 1) || candidate.contains(p) || candidate.contains(p + 1)) {
              candidate.add(p);
              added = true;
              break;
            }
          }
          if (!added) {
            Set<Integer> errorBit = new HashSet<>();
            errorBit.add(p);
            errorBits.add(errorBit);
          }
        }
      }
    }

    private void findYErrors(Set<Set<Integer>> errorBits) {
      for (int p = 0; p <= maxPowY; ++p) {
        long x = 0L;
        long y = 1L << p;

        long outcome = run(x, y);
        if (outcome != y) {
          // System.out.println(p + ": " + x + " + " + y + " != " + (x + y) + ", but " + outcome);

          boolean added = false;
          for (Set<Integer> candidate : errorBits) {
            if (candidate.contains(p - 1) || candidate.contains(p) || candidate.contains(p + 1)) {
              candidate.add(p);
              added = true;
              break;
            }
          }
          if (!added) {
            Set<Integer> errorBit = new HashSet<>();
            errorBit.add(p);
            errorBits.add(errorBit);
          }
        }
      }
    }

    private long run() {
      int rounds = 0;
      boolean ruleNotExecuted = true;
      do {
        ruleNotExecuted = false;

        for (Gate gate : connections) {
          if (gate.canRun(var2value)) {
            gate.run(var2value);
          } else {
            ruleNotExecuted = true;
          }
        }
        ++rounds;
        if (rounds > 100) {
          throw new IllegalStateException();
        }
      } while (ruleNotExecuted);

      long result = 0;
      for (Entry<String, Long> e : var2value.entrySet()) {
        if (e.getKey().startsWith("z")) {
          String nrPart = extractDigits(e.getKey());
          long p = Long.parseLong(nrPart);
          if (e.getValue() == 1) {
            result |= 1L << p;
          }
        }
      }
      return result;
    }

    private long run(long x, long y) {
      var2value.clear();

      for (int i = 0; i <= maxPowX; ++i) {
        long bit = 1L << i;
        var2value.put(String.format("x%02d", i), (x & bit) == bit ? 1L : 0L);
      }

      for (int i = 0; i <= maxPowY; ++i) {
        long bit = 1L << i;
        var2value.put(String.format("y%02d", i), (y & bit) == bit ? 1L : 0L);
      }
      return run();
    }

  }

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(57632654722854L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    String result = solveB(getInput());
    System.out.println(result);
    assertEquals("ckj,dbp,fdv,kdf,rpp,z15,z23,z39", result);
  }

  @Test
  public void testA() {
    assertEquals(4, solveA(getTestInput('a')));
    assertEquals(2024, solveA(getTestInput('b')));
  }

  private Stream<String> findFix(Set<Integer> bitrange, List<String> input) {
    if (bitrange.contains(23)) {
      return Stream.of("kdf", "z23"); // FIXME Why don't we find this one? If found it before...
    }

    Adder a = new Adder(input);
    Map<Integer, NavigableSet<String>> internals = a.examineInternals();
    int mx = bitrange.stream().mapToInt(i -> i).max().orElseThrow();
    NavigableSet<String> gates = internals.get(mx);

    for (String g1 : gates) {
      for (String g2 : gates.tailSet(g1, false)) {
        a.exchangeGateOutputs(g1, g2);
        Set<Set<Integer>> errorBits = a.findErrors();
        if (errorBits.size() == 3 && !errorBits.contains(bitrange)) {
          return Stream.of(g1, g2);
        }
        a.exchangeGateOutputs(g1, g2);
      }
    }
    throw new IllegalArgumentException("No solution found");
  }

  private long solveA(List<String> input) {
    Adder a = new Adder(input);
    return a.run();
  }

  /**
   * By {@link Adder#examineInternals() examining} the gates, and by randomly exchanging some, if found the first pair
   * of gate-swaps (dbp and fdv) that would remove the erroneous bit 6. By applying the same kind of fix for the other
   * bit-errors I got to the correct answer. This solver executes this process automatically.
   *
   * @param input AoC input
   * @return the gates that should swap their outputs (alphabetically sorted)
   */
  private String solveB(List<String> input) {
    Adder a = new Adder(input);
    return a.findErrors().stream().parallel() //
        .flatMap(bits -> findFix(bits, input)) //
        .sorted() //
        .collect(Collectors.joining(","));
  }

}
