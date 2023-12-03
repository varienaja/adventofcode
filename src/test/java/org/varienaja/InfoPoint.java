package org.varienaja;

public class InfoPoint<T extends Object> extends Point {
  private T info;

  public InfoPoint(int x, int y, T info) {
    super(x, y);
    this.info = info;
  }

  public T getInfo() {
    return info;
  }

}
