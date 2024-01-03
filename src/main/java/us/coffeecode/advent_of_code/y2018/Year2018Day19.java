/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2018;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.PrimeProvider;

@AdventOfCodeSolution(year = 2018, day = 19, title = "Go With The Flow")
@Component
public final class Year2018Day19 {

  @Autowired
  private PrimeProvider primeProvider;

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final State state = new State(il.lines(pc));
    execute(state, Long.MAX_VALUE);
    return state.register[0];
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final State state = new State(il.lines(pc));
    state.register[0] = 1;
    // Let the program execute for a few steps to calculate the number we need.
    execute(state, 30);
    final long number = state.register[3];
    // Get the prime factors, then combine them to get all of the factors.
    final int[] primeFactors = getPrimeFactors(number);
    long sum = 0;
    for (final int factor : getAllFactors(primeFactors)) {
      sum += factor;
    }
    return sum;
  }

  private void execute(final State state, final long maxIterations) {
    for (long i = 0; i < maxIterations && -1 < state.register[state.ip_reg]
      && state.register[state.ip_reg] < state.instructions.size(); ++i) {
      final Instruction inst = state.instructions.get((int) state.register[state.ip_reg]);
      inst.opcode().apply(state.register, inst.a(), inst.b(), inst.c());
      ++state.register[state.ip_reg];
    }
  }

  private int[] getPrimeFactors(final long number) {
    final int[] primes = primeProvider.getPrimesUpTo((int) number);
    final List<Integer> factors = new ArrayList<>();
    long value = number;
    int i = 0;
    // Divide by each prime factor, in turn, until it no longer divides whatever is left of the number.
    while ((value > 1) && (i < primes.length)) {
      if (value % primes[i] == 0) {
        value /= primes[i];
        factors.add(Integer.valueOf(primes[i]));
      }
      else {
        ++i;
      }
    }
    return factors.stream().mapToInt(Integer::intValue).toArray();
  }

  private int[] getAllFactors(final int[] primeFactors) {
    final Set<Integer> factors = new HashSet<>();
    // Loop counter holds the bits for each array position to use.
    for (int bits = 0; bits < (1 << primeFactors.length); ++bits) {
      int factor = 1;
      // Inner counter is the index into the prime array for the factor to use.
      for (int j = 0; j < primeFactors.length; ++j) {
        if (((bits >> j) & 1) > 0) {
          factor *= primeFactors[j];
        }
      }
      factors.add(Integer.valueOf(factor));
    }
    return factors.stream().mapToInt(Integer::intValue).toArray();
  }
}
