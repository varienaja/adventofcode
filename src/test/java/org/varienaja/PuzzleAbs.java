package org.varienaja;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PuzzleAbs {

  private void announceResult() {
    System.out.print("Solution ");
    System.out.print(getMyNumber());
  }

  protected void announceResultA() {
    announceResult();
    System.out.print("a: ");
  }

  protected void announceResultB() {
    announceResult();
    System.out.print("b: ");
  }

  protected void announceResultC() {
    announceResult();
    System.out.print("c: ");
  }

  protected List<String> getInput() {
    return getInput(false);
  }

  private List<String> getInput(boolean large) {
    try {
      String suffix = large ? "_large" : "";
      String resourceName = getMyYear() + "/day" + getMyNumber() + suffix + ".txt";
      return Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource(resourceName).toURI()));
    } catch (Exception e) { // Ignore
    }
    return null;
  }

  protected String getInputString() {
    return getInput().get(0);
  }

  protected String getLargeInputString() {
    return getInput(true).get(0);
  }

  protected List<Long> getLongInput() {
    return getInput().stream().map(Long::parseLong).collect(Collectors.toCollection(ArrayList::new));
  }

  private String getMyNumber() {
    StringBuilder result = new StringBuilder();

    for (char c : this.getClass().getSimpleName().toCharArray()) {
      if (Character.isDigit(c)) {
        result.append(c);
      }
    }

    return result.toString();
  }

  private String getMyYear() {
    String packageName = getClass().getPackageName();
    String year = packageName.substring(packageName.length() - 4, packageName.length());
    return year;
  }

}
