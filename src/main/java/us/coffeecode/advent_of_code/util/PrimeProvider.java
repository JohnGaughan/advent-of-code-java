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
package us.coffeecode.advent_of_code.util;

import org.springframework.stereotype.Component;

/**
 * This is an object that sieves primes and returns the results. A small number of problems require working with prime
 * numbers, but the solution itself should focus on the solution and not working with a sieve. This class provides a
 * simple interface for accomplishing that goal.
 */
@Component
public class PrimeProvider {

  /**
   * Get all prime numbers equal to or less than the provided upper bound.
   *
   * @param max the value of the maximum allowed number
   * @return an array of all unique prime number equal to or less than the maximum value.
   */
  public int[] getPrimesUpTo(final int max) {
    // Sieve omits 1 and all even numbers. Mark composites as true, since the default array element is false.
    final boolean[] sieve = new boolean[max >> 1];
    int numPrimes = 0;
    for (int i = 0; i < sieve.length; ++i) {
      int value = (i << 1) + 3;
      if (!sieve[i]) {
        ++numPrimes;
        for (int j = i + value; j < sieve.length; j += value) {
          sieve[j] = true;
        }
      }
    }
    final int[] primes = new int[numPrimes];
    primes[0] = 2;
    int j = 0;
    for (int i = 1; i < primes.length; ++i) {
      while (sieve[j]) {
        ++j;
      }
      primes[i] = (j << 1) + 3;
      ++j;
    }
    return primes;
  }

}
