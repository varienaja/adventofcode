package org.varienaja.adventofcode2022;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2022.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2022">adventofcode.com</a>
 */
public class Puzzle13 extends PuzzleAbs {

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInput());
    System.out.println(result);
    assertEquals(5393L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInput());
    System.out.println(result);
    assertEquals(26712L, result);
  }

  @Test
  public void testA() {
    assertEquals(13L, solveA(getTestInput()));
  }

  @Test
  public void testB() {
    assertEquals(140L, solveB(getTestInput()));
  }

  private int compare(List<Object> left, List<Object> right) {
    int result = 0;

    Iterator<Object> iLeft = left.iterator();
    Iterator<Object> iRight = right.iterator();
    while (iLeft.hasNext() && result == 0) {
      Object l = iLeft.next();
      Object r = null;
      if (!iRight.hasNext()) {
        return 1;
      }
      r = iRight.next();
      result = compare(l, r);
    }
    if (result == 0 && iRight.hasNext()) {
      result = -1;
    }
    return result;
  }

  @SuppressWarnings({
      "unchecked", "rawtypes"
  })
  private int compare(Object left, Object right) {
    if (left instanceof Integer && right instanceof Integer) {
      return ((Integer)left).compareTo((Integer)right);
    } else if (left instanceof List && right instanceof List) {
      return compare((List)left, (List)right);
    }
    return compare(left instanceof List ? left : Collections.singletonList((Integer)left),
        right instanceof List ? right : Collections.singletonList((Integer)right));
  }

  private List<String> getTestInput() {
    return List.of( //
        "[1,1,3,1,1]", //
        "[1,1,5,1,1]", //
        "", //
        "[[1],[2,3,4]]", //
        "[[1],4]", //
        "", //
        "[9]", //
        "[[8,7,6]]", //
        "", //
        "[[4,4],4,4]", //
        "[[4,4],4,4,4]", //
        "", //
        "[7,7,7,7]", //
        "[7,7,7]", //
        "", //
        "[]", //
        "[3]", //
        "", //
        "[[[]]]", //
        "[[]]", //
        "", //
        "[1,[2,[3,[4,[5,6,7]]]],8,9]", //
        "[1,[2,[3,[4,[5,6,0]]]],8,9]");
  }

  private Object parse(Queue<Character> line) {
    if ('[' == line.peek()) {
      return parseList(line);
    }
    return parseNumber(line);
  }

  private List<Object> parseItems(Queue<Character> line) {
    List<Object> result = new ArrayList<>();

    char c;
    while ((c = line.peek()) != ']') {
      if (Character.isDigit(c)) {
        result.add(parseNumber(line));
      } else {
        if (c == ',') {
          line.poll();
        }
        result.add(parse(line));
      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  private List<Object> parseLine(String line) {
    return (List<Object>)parse(line.chars().mapToObj(c -> (char)c).collect(Collectors.toCollection(LinkedList::new)));
  }

  private List<Object> parseList(Queue<Character> line) {
    line.poll(); // [
    List<Object> result = new ArrayList<>();
    result.addAll(parseItems(line));
    line.poll(); // ]
    return result;
  }

  private Integer parseNumber(Queue<Character> line) {
    StringBuilder sb = new StringBuilder();

    while (Character.isDigit(line.peek())) {
      sb.append(line.poll());
    }

    return Integer.parseInt(sb.toString());
  }

  private long solveA(List<String> lines) {
    long result = 0;

    long ix = 1;
    Iterator<String> it = lines.iterator();
    while (it.hasNext()) {
      String left = it.next();
      String right = it.next();

      if (compare(parseLine(left), parseLine(right)) <= 0) {
        result += ix;
      }
      ix++;
      if (it.hasNext()) {
        it.next(); // Skip empty line between pairs
      }
    }

    return result;
  }

  private long solveB(List<String> lines) {
    List<String> dividers = List.of("[[2]]", "[[6]]");
    List<String> readable = Stream.concat(lines.stream().filter(Predicate.not(String::isEmpty)), dividers.stream()) //
        .map(this::parseLine) //
        .sorted(this::compare) //
        .map(Object::toString) //
        .collect(Collectors.toList());
    return (1 + readable.indexOf(dividers.get(0))) * (1 + readable.indexOf(dividers.get(1)));
  }

}
