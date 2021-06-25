/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package us.coffeecode.advent_of_code.y2016;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/14">Year 2016, day 14</a>. This problem asks us to calculate a ton of MD5
 * hashes and find patterns in the results. Specifically, a hash with a run of three hex digits where there is another
 * hash with a run of the same digit except five times. Part two adds 2016 "hasing the hash" operations.
 * </p>
 * <p>
 * The algorithm is not ideal but it is simple. The big problem is the MD5 hashing: it is slow, because there is so much
 * hashing to perform. For this reason I split the MD5 operations across multiple threads and kept the simple search
 * algorithm instead of something clever, but that wouldn't save any practical time.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day14 {

  /** Number of threads to use for calculating hashes. */
  private static final int THREAD_COUNT = 16;

  /** Number of hashes to calculate. */
  private static final int LIMIT = 24_000;

  /** How many hashes to look ahead for a quintuple. */
  private static final int LOOKAHEAD = 1_000;

  /** Array index that stores triples. */
  private static final int TRIPLE = 0;

  /** Array index that stores quintuples. */
  private static final int QUINTUPLE = 1;

  public long calculatePart1() {
    return calculate(calculateHashData(false, getInput()));
  }

  public long calculatePart2() {
    return calculate(calculateHashData(true, getInput()));
  }

  private long calculate(final boolean[][][] hashData) {
    int key = 0;
    for (int i = 0; i < hashData.length - LOOKAHEAD; ++i) {
      // See if this potential key has a triplet.
      for (int j = 0; j < hashData[i][TRIPLE].length; ++j) {
        if (hashData[i][TRIPLE][j]) {
          // Triplet found! Search for a corresponding quintuple.
          for (int k = i + 1; k < i + 1 + LOOKAHEAD; ++k) {
            if (hashData[k][QUINTUPLE][j]) {
              ++key;
              if (key == 64) {
                return i;
              }
              break;
            }
          }
        }
      }
    }
    return -1;
  }

  /** Get the input data for this solution. */
  private String getInput() {
    try {
      return Files.readString(Utils.getInput(2016, 14)).trim();
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private boolean[][][] calculateHashData(final boolean stretch, final String salt) {
    final boolean[][][] result = new boolean[LIMIT][2][CHARS.length];
    final int HASHES_PER_THREAD = LIMIT / THREAD_COUNT;
    final ExecutorService exec = Executors.newFixedThreadPool(THREAD_COUNT);
    final List<Future<?>> futures = new ArrayList<>();
    for (int i = 0; i < LIMIT; i += HASHES_PER_THREAD) {
      futures.add(exec.submit(new Hasher(result, i, i + HASHES_PER_THREAD, stretch, salt)));
    }
    for (Future<?> future : futures) {
      try {
        future.get();
      }
      catch (RuntimeException ex) {
        throw ex;
      }
      catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
    return result;
  }

  private static final class Hasher
  implements Callable<Void> {

    final boolean[][][] result;

    final int lowerBound;

    final int upperBound;

    final boolean stretch;

    final String salt;

    Hasher(final boolean[][][] _result, final int _lowerBound, final int _upperBound, final boolean _stretch, final String _salt) {
      result = _result;
      lowerBound = _lowerBound;
      upperBound = _upperBound;
      stretch = _stretch;
      salt = _salt;
    }

    /** {@inheritDoc} */
    @Override
    public Void call() throws Exception {
      for (int i = lowerBound; i < upperBound; ++i) {
        final String plaintext = salt + i;
        String hex = Utils.md5ToHex(plaintext);
        if (stretch) {
          for (int j = 0; j < 2016; ++j) {
            hex = Utils.md5ToHex(hex);
          }
        }
        for (int j = 0; j < CHARS.length; ++j) {
          final String needle5 = new String(new char[] { CHARS[j], CHARS[j], CHARS[j], CHARS[j], CHARS[j] });
          result[i][QUINTUPLE][j] = hex.contains(needle5);
        }
        final int triple = getTriple(hex.toCharArray());
        if (triple >= 0) {
          result[i][TRIPLE][triple] = true;
        }
      }
      return null;
    }

    /**
     * Get the FIRST triple found in the needle. The return value is the index in the CHARS array, or a negative number
     * if no triple.
     */
    private int getTriple(final char[] md5) {
      for (int i = 0; i < md5.length - 2; ++i) {
        if (md5[i] == md5[i + 1]) {
          if (md5[i] == md5[i + 2]) {
            return Arrays.binarySearch(CHARS, md5[i]);
          }
          ++i;
          continue;
        }
      }
      return -1;
    }

  }

  /** Hexadecimal character set. */
  private static final char[] CHARS = "0123456789abcdef".toCharArray();

}
