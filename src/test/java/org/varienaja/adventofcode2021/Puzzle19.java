package org.varienaja.adventofcode2021;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * Solutions for Advent of Code 2021.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2021">adventofcode.com</a>
 */
public class Puzzle19 extends PuzzleAbs {
  private List<int[]> distances = new LinkedList<>();
  private Set<String> points = new HashSet<>();

  private int[] fixOrientation(List<int[]> scanner, List<int[]> candidate) {
    for (List<int[]> rotatedCandidate : getAllOrientations(candidate)) {

      for (int[] sa : scanner) { // Take a point sa from the scanners' beacons list
        for (int[] sb : rotatedCandidate) { // Take a point sb from the candidate's beacons list
          int dx = sa[0] - sb[0];
          int dy = sa[1] - sb[1];
          int dz = sa[2] - sb[2]; // Calculate dx, dy, dz to have sa and sb match

          // For all points of the scanner's beacon list: check how many points
          // from the candidate's beacon list match position with the current dx, dy, dz
          int overlap = 0;

          for (int j = 0; j < scanner.size(); j++) {
            if (12 - overlap > scanner.size() - j) {
              break; // It's no use to continue; we'll never find 12 matches
            }

            int[] saa = scanner.get(j);
            int x = saa[0];
            int y = saa[1];
            int z = saa[2];

            for (int[] sbb : rotatedCandidate) {
              int xx = sbb[0] + dx;
              int yy = sbb[1] + dy;
              int zz = sbb[2] + dz;
              if (xx == x && yy == y & zz == z) {
                overlap++;

                if (overlap >= 12) {
                  // adjusted the candidate point list
                  for (int i = 0; i < candidate.size(); i++) {
                    int[] rrr = rotatedCandidate.get(i);
                    int xxxx = rrr[0] + dx;
                    int yyyy = rrr[1] + dy;
                    int zzzz = rrr[2] + dz;

                    candidate.set(i, new int[] {
                        xxxx, yyyy, zzzz
                    });
                  }
                  return new int[] {
                      dx, dy, dz
                  };
                }
                break;
              }
            }
          }
        }
      }
    }
    return null; // no match found
  }

  /**
   * Returns the 24 orientations of a 3d-point list.
   *
   * @param candidate the list of points
   * @return the orientations
   * @see <a href="https://stackoverflow.com/questions/16452383/">here</a>
   */
  private List<List<int[]>> getAllOrientations(List<int[]> candidate) {
    List<List<int[]>> result = new ArrayList<>();
    for (int rr = 0; rr < 24; rr++) {
      result.add(new ArrayList<int[]>());
    }

    for (int[] point : candidate) {
      int rr = 0;

      for (int c = 0; c < 2; c++) {
        for (int s = 0; s < 3; s++) {
          point = pointRoll(point);
          result.get(rr++).add(point);
          for (int i = 0; i < 3; i++) {
            point = pointTurn(point);
            result.get(rr++).add(point);
          }
        }
        point = pointRoll(pointTurn(pointRoll(point)));
      }
    }

    return result;
  }

  private int[] pointRoll(int[] p) {
    return new int[] {
        p[0], p[2], -p[1]
    };
  }

  private int[] pointTurn(int[] p) {
    return new int[] {
        -p[1], p[0], p[2]
    };
  }

  private void solve(List<String> lines) {
    List<List<int[]>> uncalibrated = new LinkedList<>();
    List<int[]> scanner = new ArrayList<>();
    for (String line : lines) {
      if (line.startsWith("---")) {
        if (!scanner.isEmpty()) {
          uncalibrated.add(scanner);
        }
        scanner = new ArrayList<>();
      } else if (!line.isEmpty()) {
        String[] parts = line.split(",");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        int z = Integer.parseInt(parts[2]);
        scanner.add(new int[] {
            x, y, z
        });
      }
    }

    distances = new LinkedList<>();
    List<List<int[]>> adjusted = new LinkedList<>();
    adjusted.add(scanner); // Just take one scanner result

    while (!uncalibrated.isEmpty()) { // Search for a candidate that overlaps
      outer: for (List<int[]> ok : adjusted) {
        for (List<int[]> candidate : uncalibrated) {
          int[] dist = fixOrientation(ok, candidate);
          if (dist != null) {
            distances.add(dist);
            adjusted.add(candidate);
            uncalibrated.remove(candidate);
            break outer;
          }
        }
      }
    }

    points = new HashSet<>();
    for (List<int[]> pt : adjusted) {
      for (int[] p : pt) {
        points.add(Arrays.toString(p));
      }
    }
  }

  private long solveA(List<String> lines) {
    solve(lines);
    return points.size();
  }

  private long solveB(List<String> lines) {
    int maxdist = 0;
    for (int[] d1 : distances) {
      for (int[] d2 : distances) {
        if (!Arrays.equals(d1, d2)) {
          int dx = Math.abs(d1[0] - d2[0]);
          int dy = Math.abs(d1[1] - d2[1]);
          int dz = Math.abs(d1[2] - d2[2]);
          int dist = dx + dy + dz;
          maxdist = Math.max(maxdist, dist);
        }
      }
    }

    return maxdist;
  }

  @Test
  public void testDay19() {
    List<String> testInput = Arrays.asList( //
        "--- scanner 0 ---", //
        "404,-588,-901", //
        "528,-643,409", //
        "-838,591,734", //
        "390,-675,-793", //
        "-537,-823,-458", //
        "-485,-357,347", //
        "-345,-311,381", //
        "-661,-816,-575", //
        "-876,649,763", //
        "-618,-824,-621", //
        "553,345,-567", //
        "474,580,667", //
        "-447,-329,318", //
        "-584,868,-557", //
        "544,-627,-890", //
        "564,392,-477", //
        "455,729,728", //
        "-892,524,684", //
        "-689,845,-530", //
        "423,-701,434", //
        "7,-33,-71", //
        "630,319,-379", //
        "443,580,662", //
        "-789,900,-551", //
        "459,-707,401", //
        "", //
        "--- scanner 1 ---", //
        "686,422,578", //
        "605,423,415", //
        "515,917,-361", //
        "-336,658,858", //
        "95,138,22", //
        "-476,619,847", //
        "-340,-569,-846", //
        "567,-361,727", //
        "-460,603,-452", //
        "669,-402,600", //
        "729,430,532", //
        "-500,-761,534", //
        "-322,571,750", //
        "-466,-666,-811", //
        "-429,-592,574", //
        "-355,545,-477", //
        "703,-491,-529", //
        "-328,-685,520", //
        "413,935,-424", //
        "-391,539,-444", //
        "586,-435,557", //
        "-364,-763,-893", //
        "807,-499,-711", //
        "755,-354,-619", //
        "553,889,-390", //
        "", //
        "--- scanner 2 ---", //
        "649,640,665", //
        "682,-795,504", //
        "-784,533,-524", //
        "-644,584,-595", //
        "-588,-843,648", //
        "-30,6,44", //
        "-674,560,763", //
        "500,723,-460", //
        "609,671,-379", //
        "-555,-800,653", //
        "-675,-892,-343", //
        "697,-426,-610", //
        "578,704,681", //
        "493,664,-388", //
        "-671,-858,530", //
        "-667,343,800", //
        "571,-461,-707", //
        "-138,-166,112", //
        "-889,563,-600", //
        "646,-828,498", //
        "640,759,510", //
        "-630,509,768", //
        "-681,-892,-333", //
        "673,-379,-804", //
        "-742,-814,-386", //
        "577,-820,562", //
        "", //
        "--- scanner 3 ---", //
        "-589,542,597", //
        "605,-692,669", //
        "-500,565,-823", //
        "-660,373,557", //
        "-458,-679,-417", //
        "-488,449,543", //
        "-626,468,-788", //
        "338,-750,-386", //
        "528,-832,-391", //
        "562,-778,733", //
        "-938,-730,414", //
        "543,643,-506", //
        "-524,371,-870", //
        "407,773,750", //
        "-104,29,83", //
        "378,-903,-323", //
        "-778,-728,485", //
        "426,699,580", //
        "-438,-605,-362", //
        "-469,-447,-387", //
        "509,732,623", //
        "647,635,-688", //
        "-868,-804,481", //
        "614,-800,639", //
        "595,780,-596", //
        "", //
        "--- scanner 4 ---", //
        "727,592,562", //
        "-293,-554,779", //
        "441,611,-461", //
        "-714,465,-776", //
        "-743,427,-804", //
        "-660,-479,-426", //
        "832,-632,460", //
        "927,-485,-438", //
        "408,393,-506", //
        "466,436,-512", //
        "110,16,151", //
        "-258,-428,682", //
        "-393,719,612", //
        "-211,-452,876", //
        "808,-476,-593", //
        "-575,615,604", //
        "-485,667,467", //
        "-680,325,-822", //
        "-627,-443,-432", //
        "872,-547,-609", //
        "833,512,582", //
        "807,604,487", //
        "839,-516,451", //
        "891,-625,532", //
        "-652,-548,-490", //
        "30,-46,-14");
    assertEquals(79, solveA(testInput));
    assertEquals(3621, solveB(testInput));

    announceResultA();
    List<String> lines = getInput();
    long result = solveA(lines);
    System.out.println(result);
    assertEquals(318, result);

    announceResultB();
    result = solveB(lines);
    System.out.println(result);
    assertEquals(12166, result);
  }

}
