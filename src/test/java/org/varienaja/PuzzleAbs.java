package org.varienaja;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PuzzleAbs {

  public static void print(Map<Point, Character> world) {
    Map<Integer, StringBuilder> toPrint = new TreeMap<>();
    for (Entry<Point, Character> e : world.entrySet()) {
      StringBuilder sb = toPrint.computeIfAbsent(e.getKey().y, StringBuilder::new);
      while (sb.length() < e.getKey().x + 1) {
        sb.append(" ");
      }
      sb.setCharAt(e.getKey().x, e.getValue());
    }
    for (StringBuilder sb : toPrint.values()) {
      System.out.println(sb.toString());
    }
  }

  public long lcm(long a, long b) { // Least common multiple
    long lcm = 0;
    while ((lcm += Math.max(a, b)) % Math.min(a, b) != 0) {
    }
    return lcm;
  }

  public List<Long> parseNumbers(String numbers, String separatorRegex) {
    return Arrays.stream(numbers.split(separatorRegex)).mapToLong(Long::parseLong).boxed().toList();
  }

  protected void announceResultA() {
    announceResult();
    System.out.print("a: ");
  }

  protected void announceResultB() {
    announceResult();
    System.out.print("b: ");
  }

  protected List<String> getInput() {
    return getInput(getResourceName());
  }

  protected String getInputString() {
    return getInput().get(0);
  }

  protected List<Long> getLongInput() {
    return getInput().stream().map(Long::parseLong).collect(Collectors.toCollection(ArrayList::new));
  }

  protected Stream<String> getStreamingInput() {
    return getStreamingInput(getResourceName());
  }

  protected Stream<String> getStreamingTestInput() {
    return getStreamingTestInput((Character)null);
  }

  protected Stream<String> getStreamingTestInput(Character c) {
    return getStreamingInput(getTestResourceName(c));
  }

  protected List<String> getTestInput() {
    return getInput(getTestResourceName(null));
  }

  protected List<String> getTestInput(Character c) {
    return getInput(getTestResourceName(c));
  }

  protected String getTestInputString() {
    return getTestInput().get(0);
  }

  Path getPath(String resource) throws URISyntaxException {
    return Paths.get(getClass().getClassLoader().getResource(getResourceName()).toURI());
  }

  private void announceResult() {
    System.out.print("Solution ");
    System.out.print(getMyNumber());
  }

  private List<String> getInput(String resourceName) {
    try {
      return Files.readAllLines(Paths.get(getClass().getClassLoader().getResource(resourceName).toURI()));
    } catch (Exception e) { // Ignore
    }
    return null;
  }

  private String getMyNumber() {
    StringBuilder result = new StringBuilder();

    for (char c : getClass().getSimpleName().toCharArray()) {
      if (Character.isDigit(c)) {
        result.append(c);
      }
    }

    return result.toString();
  }

  private String getMyYear() {
    String packageName = getClass().getPackageName();
    return packageName.substring(packageName.length() - 4);
  }

  private String getResourceName() {
    return getMyYear() + "/day" + getMyNumber() + ".txt";
  }

  private Stream<String> getStreamingInput(String resourceName) {
    try {
      return Files.lines(Paths.get(getClass().getClassLoader().getResource(resourceName).toURI()));
    } catch (Exception e) { // Ignore
    }
    return null;
  }

  private String getTestResourceName(Character c) {
    return getMyYear() + "/day" + getMyNumber() + "-test" + (c == null ? "" : "-" + c) + ".txt";
  }

}
