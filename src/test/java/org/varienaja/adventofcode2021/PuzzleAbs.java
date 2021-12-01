package org.varienaja.adventofcode2021;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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

  List<String> getInput() {
    try {
      String resourceName = "2021/day" + getMyNumber() + ".txt";
      return Files.readAllLines(Paths.get(this.getClass().getClassLoader().getResource(resourceName).toURI()));
    } catch (Exception e) { // Ignore
    }
    return null;
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

}
