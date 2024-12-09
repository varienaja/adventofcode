package org.varienaja.adventofcode2024;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.IntStream;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2024.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2024">adventofcode.com</a>
 */
public class Puzzle09 extends PuzzleAbs {

  abstract class FileSystem {
    protected static final int HOLE = -1;
    /** BlockNr -&gt; fileId lookup, contents is {@link HOLE}, if no file present. */
    protected int[] blocks;
    /** fileId -&gt; fileSize lookup. */
    protected int[] fileSizes;
    /** fileId -&gt; fileSize lookup. */
    protected int[] fileIxs;
    /** Hole-size -&gt; block-numbers lookup. */
    protected TreeMap<Integer, SortedSet<Integer>> holeSize2Ixs;

    public FileSystem(String line) {
      fileSizes = new int[1 + line.length() / 2];
      fileIxs = new int[1 + line.length() / 2];
      holeSize2Ixs = new TreeMap<>();
      IntStream.range(0, 10).forEach(i -> holeSize2Ixs.put(i, new TreeSet<>()));

      ArrayList<Integer> tmpBlocks = new ArrayList<>();
      // ix 0,2,4,... : file of length this char
      // ix 1,3,5,... : empty space of length this char
      for (int i = 0; i < line.length(); ++i) {
        int fileId = i % 2 == 0 ? i / 2 : HOLE;
        int size = line.charAt(i) - '0';
        if (i % 2 == 0) { // file
          fileSizes[fileId] = size;
          fileIxs[fileId] = tmpBlocks.size();
        } else { // hole
          holeSize2Ixs.get(size).add(tmpBlocks.size());
        }
        for (int x = 0; x < size; ++x) {
          tmpBlocks.add(fileId);
        }
      }
      blocks = tmpBlocks.stream().mapToInt(i -> i).toArray();
    }

    public abstract FileSystem defrag();

    protected long checksum() {
      long result = 0;
      for (int i = 0; i < blocks.length; ++i) {
        result += blocks[i] == HOLE ? 0 : i * blocks[i];
      }
      return result;
    }
  }

  class FileSystemA extends FileSystem {
    public FileSystemA(String line) {
      super(line);
    }

    @Override
    public FileSystem defrag() {
      int holeIx = 0;
      int fileIx = blocks.length - 1;

      while (holeIx < fileIx) {
        while (blocks[holeIx] != HOLE) {
          ++holeIx;
        }
        while (blocks[fileIx] == HOLE) {
          --fileIx;
        }
        if ((holeIx < fileIx)) { // Swap hole with file
          blocks[holeIx] = blocks[fileIx];
          blocks[fileIx] = HOLE;
        }
      }
      return this;
    }
  }

  class FileSystemB extends FileSystem {
    public FileSystemB(String line) {
      super(line);
    }

    @Override
    public FileSystem defrag() {
      // Move whole files. Start with highest fileId. Move file to first possible hole (even if hole is too big).
      // Consider files only once.
      for (int fileId = fileSizes.length - 1; fileId >= 0; --fileId) {
        int fileSize = fileSizes[fileId];
        int fileIx = fileIxs[fileId];
        Optional<Entry<Integer, SortedSet<Integer>>> oHole = holeSize2Ixs //
            .tailMap(fileSize).entrySet().stream() // search holes of all sizes > fileSize
            .filter(e -> !e.getValue().isEmpty()) // there must be candidates
            .filter(e -> e.getValue().first() < fileIx) // // index must be < than fileIx to move file to the left
            .sorted((e1, e2) -> e1.getValue().first() - e2.getValue().first()) // order by hole-index
            .findFirst(); // find first hole-index

        if (oHole.isEmpty()) { // No hole found, cannot move file
          if (fileSize == 1) {
            break; // Performance: we can stop now; there are no more holes of any size before current fileIx
          }
        } else { // Move file to hole (and hole to where the file was).
          int hIx = oHole.get().getValue().first();
          int size = oHole.get().getKey();
          holeSize2Ixs.get(size).remove(hIx);
          if (size > fileSize) { // If hole was bigger than file, a new hole appears
            holeSize2Ixs.get(size - fileSize).add(hIx + fileSize);
          }

          System.arraycopy(blocks, fileIx, blocks, hIx, fileSize); // Move file
          for (int i = 0; i < fileSize; ++i) { // Move hole
            blocks[fileIx + i] = HOLE;
          }
        }
      }
      return this;
    }
  }

  @Test
  public void doA() {
    announceResultA();
    long result = solveA(getInputString());
    System.out.println(result);
    assertEquals(6346871685398L, result);
  }

  @Test
  public void doB() {
    announceResultB();
    long result = solveB(getInputString());
    System.out.println(result);
    assertEquals(6373055193464L, result);
  }

  @Test
  public void testA() {
    assertEquals(1928, solveA(getTestInputString()));
  }

  @Test
  public void testB() {
    assertEquals(2858, solveB(getTestInputString()));
  }

  private long solveA(String line) {
    return new FileSystemA(line).defrag().checksum();
  }

  private long solveB(String line) {
    return new FileSystemB(line).defrag().checksum();
  }

}
