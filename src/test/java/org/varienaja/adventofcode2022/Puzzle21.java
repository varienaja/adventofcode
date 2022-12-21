package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle21 extends PuzzleAbs {
    private static final String ROOT = "root";
    private static final String HUMN = "humn";

    @Test
    public void doA() {
        announceResultA();
        long result = solveA(getInput());
        System.out.println(result);
        assertEquals(155708040358220L, result);
    }

    @Test
    public void doB() {
        announceResultB();
        long result = solveB(getInput());
        System.out.println(result);
        assertEquals(3342154812537L, result);
    }

    @Test
    public void testA() {
        assertEquals(152L, solveA(getTestInput()));
    }

    @Test
    public void testB() {
        assertEquals(301, solveB(getTestInput()));
    }

    private List<String> getTestInput() {
        return List.of("root: pppw + sjmn", //
                "dbpl: 5", //
                "cczh: sllz + lgvd", //
                "zczc: 2", //
                "ptdq: humn - dvpt", //
                "dvpt: 3", //
                "lfqf: 4", //
                "humn: 5", //
                "ljgn: 2", //
                "sjmn: drzm * dbpl", //
                "sllz: 4", //
                "pppw: cczh / lfqf", //
                "lgvd: ljgn * ptdq", //
                "drzm: hmdt - zczc", //
                "hmdt: 32");
    }

    private Map<String, String> parse(Collection<String> lines) {
        Map<String, String> monkey2op = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split(":\\s");
            monkey2op.put(parts[0], parts[1]);
        }
        return monkey2op;
    }

    private long evaluate(Map<String, String> monkey2op, Map<String, Long> solution, String root) {
        while (!solution.containsKey(root)) {
            boolean couldEvaluate = false;
            Iterator<Entry<String, String>> it = monkey2op.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> e = it.next();
                String[] parts = e.getValue().split("\\s");
                if (parts.length == 1) {
                    long l = Long.parseLong(e.getValue());
                    solution.put(e.getKey(), l);
                    couldEvaluate = true;
                    it.remove();
                } else {
                    Long l1 = solution.get(parts[0]);
                    Long l2 = solution.get(parts[2]);
                    if (l1 != null && l2 != null) {
                        if ("+".equals(parts[1])) {
                            solution.put(e.getKey(), l1 + l2);
                        } else if ("*".equals(parts[1])) {
                            solution.put(e.getKey(), l1 * l2);
                        } else if ("-".equals(parts[1])) {
                            solution.put(e.getKey(), l1 - l2);
                        } else if ("/".equals(parts[1])) {
                            solution.put(e.getKey(), l1 / l2);
                        }
                        couldEvaluate = true;
                        it.remove();
                    }
                }
            }
            if (!couldEvaluate) {
                return Long.MIN_VALUE;
            }
        }

        return solution.get(root);
    }

    private long solve(Collection<String> lines, String root) {
        return evaluate(parse(lines), new HashMap<>(), root);
    }

    private long solveA(List<String> lines) {
        return solve(lines, ROOT);
    }

    private long solveB(List<String> lines) {
        Set<String> mLines = new HashSet<>(lines);
        for (String line : lines) {
            if (line.startsWith(HUMN)) {
                mLines.remove(line);
            } else if (line.startsWith(ROOT)) {
                mLines.remove(line);
                mLines.add(line.replace('+', '='));
            }
        }

        Map<String, String> monkey2op = parse(mLines);
        Map<String, Long> solution = new HashMap<>();

        String toEvaluate = ROOT;
        long toFind = 0;
        while (true) {
            String parts[] = monkey2op.get(toEvaluate).split("\\s");

            long ff = evaluate(monkey2op, solution, parts[0]);
            if (ff == Long.MIN_VALUE) {
                toEvaluate = parts[0];
                ff = evaluate(monkey2op, solution, parts[2]);
            } else {
                toEvaluate = parts[2];
            }

            if ("+".equals(parts[1])) {
                toFind = toFind - ff;
            } else if ("/".equals(parts[1])) {
                // a/2=6 --> a=6*2
                // 2/a=6 --> a=6/2!!!
                toFind = toEvaluate.equals(parts[0]) ? toFind * ff : toFind / ff;
            } else if ("*".equals(parts[1])) {
                toFind = toFind / ff;
            } else if ("-".equals(parts[1])) {
                // a-2=6 --> a=6+2
                // 2-a=6 --> a=6-2!!!
                toFind = toEvaluate.equals(parts[0]) ? toFind + ff : ff - toFind;
            } else { // =
                toFind = ff;
            }

            if (HUMN.equals(toEvaluate)) {
                return toFind;
            }
        }
    }

}
