package org.varienaja;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PuzzleAbs {

  protected void announceResultA() {
    announceResult();
    System.out.print("a: ");
  }

  protected void announceResultB() {
    announceResult();
    System.out.print("b: ");
  }

  protected List<String> getInput() {
    try {
      String resourceName = getMyYear() + "/day" + getMyNumber() + ".txt";
      return Files.readAllLines(Paths.get(getClass().getClassLoader().getResource(resourceName).toURI()));
    } catch (Exception e) { // Ignore
    }
    return null;
  }

  protected String getInputString() {
    return getInput().get(0);
  }

  protected List<Long> getLongInput() {
    return getInput().stream().map(Long::parseLong).collect(Collectors.toCollection(ArrayList::new));
  }

  private void announceResult() {
    System.out.print("Solution ");
    System.out.print(getMyNumber());
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
    String year = packageName.substring(packageName.length() - 4, packageName.length());
    return year;
  }

}
