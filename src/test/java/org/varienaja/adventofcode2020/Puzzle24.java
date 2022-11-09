package org.varienaja.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2020.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2020">adventofcode.com</a>
 */
public class Puzzle24 extends PuzzleAbs {
  static class Point3D {
    int x, y, z;

    public Point3D(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public Point3D add(Point3D p) {
      return new Point3D(x + p.x, y + p.y, z + p.z);
    }

    @Override
    public boolean equals(Object other) {
      if (other instanceof Point3D) {
        Point3D o = (Point3D)other;
        return x == o.x && y == o.y && z == o.z;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y, z);
    }
  }

  private static final Point3D ne = new Point3D(1, 0, -1);
  private static final Point3D nw = new Point3D(0, 1, -1);
  private static final Point3D se = new Point3D(0, -1, 1);
  private static final Point3D sw = new Point3D(-1, 0, 1);
  private static final Point3D e = new Point3D(1, -1, 0);
  private static final Point3D w = new Point3D(-1, 1, 0);
  private static final Point3D[] adjacent = new Point3D[] {
      ne, nw, se, sw, e, w
  };
  private static Map<String, Point3D> direction2Point = new LinkedHashMap<>();
  static {
    direction2Point.put("ne", ne);
    direction2Point.put("nw", nw);
    direction2Point.put("se", se);
    direction2Point.put("sw", sw);
    direction2Point.put("e", e);
    direction2Point.put("w", w);
  }

  private Set<Point3D> bCoords;

  private long solveA(List<String> lines) {
    bCoords = new HashSet<>();

    for (String line : lines) {
      Point3D p = new Point3D(0, 0, 0);

      StringBuffer sb = new StringBuffer(line);
      while (sb.length() != 0) {
        for (Entry<String, Point3D> e : direction2Point.entrySet()) {
          if (sb.indexOf(e.getKey()) == 0) {
            sb.delete(0, e.getKey().length());
            p = p.add(e.getValue());
            break;
          }
        }
      }
      if (bCoords.contains(p)) {
        bCoords.remove(p);
      } else {
        bCoords.add(p);
      }
    }

    return bCoords.size();
  }

  private long solveB(List<String> lines) {
    solveA(lines);

    for (int r = 1; r <= 100; r++) {
      Set<Point3D> toVerify = new HashSet<>();
      for (Point3D p : bCoords) {
        for (Point3D a : adjacent) {
          toVerify.add(p.add(a));
        }
      }

      Set<Point3D> today = new HashSet<>();
      for (Point3D p : toVerify) {
        int bc = 0;
        for (Point3D a : adjacent) {
          if (bCoords.contains(p.add(a))) {
            bc++;
          }
        }

        if (bCoords.contains(p)) {
          if ((bc == 1 || bc == 2)) {
            today.add(p);
          }
        } else {
          if (bc == 2) {
            today.add(p);
          }
        }
      }

      bCoords = today;
    }

    return bCoords.size();
  }

  @Test
  public void testDay24() {
    List<String> input = Arrays.asList( //
        "sesenwnenenewseeswwswswwnenewsewsw", //
        "neeenesenwnwwswnenewnwwsewnenwseswesw", //
        "seswneswswsenwwnwse", //
        "nwnwneseeswswnenewneswwnewseswneseene", //
        "swweswneswnenwsewnwneneseenw", //
        "eesenwseswswnenwswnwnwsewwnwsene", //
        "sewnenenenesenwsewnenwwwse", //
        "wenwwweseeeweswwwnwwe", //
        "wsweesenenewnwwnwsenewsenwwsesesenwne", //
        "neeswseenwwswnwswswnw", //
        "nenwswwsewswnenenewsenwsenwnesesenew", //
        "enewnwewneswsewnwswenweswnenwsenwsw", //
        "sweneswneswneneenwnewenewwneswswnese", //
        "swwesenesewenwneswnwwneseswwne", //
        "enesenwswwswneneswsenwnewswseenwsese", //
        "wnwnesenesenenwwnenwsewesewsesesew", //
        "nenewswnwewswnenesenwnesewesw", //
        "eneswnwswnwsenenwnwnwwseeswneewsenese", //
        "neswnwewnwnwseenwseesewsenwsweewe", //
        "wseweeenwnesenwwwswnew" //
    );
    assertEquals(10L, solveA(input));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    assertEquals(322L, result);
    System.out.println(result);

    assertEquals(2208L, solveB(input));
    announceResultB();
    result = solveB(lines);
    assertEquals(3831L, result);
    System.out.println(result);
  }

}
