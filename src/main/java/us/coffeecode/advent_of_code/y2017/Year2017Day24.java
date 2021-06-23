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
package us.coffeecode.advent_of_code.y2017;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/24">Year 2017, day 24</a>. This is problem about combining pairs of
 * integers like dominoes, where they form a chain and a pair can only be added to the end if one of its values matches
 * the last value on the chain. Part one wants the highest score (sum of integers) possible, while part two asks for the
 * highest score among all chains with the longest length.
 * </p>
 * <p>
 * This is a basic depth-first search, with some optimizations added so it runs in a reasonable amount of time. The key
 * is reducing the number of chains tracked from the over 800,000 possibilities to a much smaller subset. This algorithm
 * reduces time spent using several micro-optimizations. First, allocate a single result set and size it appropriately
 * to begin with. This avoids resizing hash tables repeatedly. Next, pass a single result set around and add to it
 * directly instead of returning results at each step. This avoids a ton of unnecessary set operations. Finally, do not
 * bother storing chains that are nowhere near long enough to be an answer to either part of the problem. This avoids
 * even more set operations and makes it faster to iterate when finding the result after enumerating the chains of
 * integers.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day24 {

  public long calculatePart1() {
    final Set<List<Integer>> chains = chains(getInput());
    long max = 0;
    for (final List<Integer> chain : chains) {
      max = Math.max(max, chain.stream().mapToInt(i -> i.intValue()).sum());
    }
    return max;
  }

  public long calculatePart2() {
    final Set<List<Integer>> chains = chains(getInput());
    int longest = 0;
    for (final List<Integer> chain : chains) {
      if (chain.size() >= longest) {
        longest = Math.max(longest, chain.size());
      }
    }
    System.out.println(chains.size());
    long max = 0;
    for (final List<Integer> chain : chains) {
      if (chain.size() == longest) {
        max = Math.max(max, chain.stream().mapToInt(i -> i.intValue()).sum());
      }
    }
    return max;
  }

  private Set<List<Integer>> chains(final Integer[][] components) {
    final Set<List<Integer>> chains = new HashSet<>(3210);
    for (int i = 0; i < components.length; ++i) {
      if (components[i][0].intValue() == 0) {
        final Integer[][] recurseComponents = new Integer[components.length - 1][];
        System.arraycopy(components, 0, recurseComponents, 0, i);
        System.arraycopy(components, i + 1, recurseComponents, i, components.length - i - 1);

        final List<Integer> chain = new ArrayList<>(2);
        chain.add(components[i][0]);
        chain.add(components[i][1]);

        chains(chains, recurseComponents, chain);
      }
      else if (components[i][1].intValue() == 0) {
        final Integer[][] recurseComponents = new Integer[components.length - 1][];
        System.arraycopy(components, 0, recurseComponents, 0, i);
        System.arraycopy(components, i + 1, recurseComponents, i, components.length - i - 1);

        final List<Integer> chain = new ArrayList<>(2);
        chain.add(components[i][1]);
        chain.add(components[i][0]);

        chains(chains, recurseComponents, chain);
      }
    }
    return chains;
  }

  private void chains(final Set<List<Integer>> chains, final Integer[][] components, final List<Integer> chain) {
    if (chain.size() >= 70) {
      chains.add(chain);
    }
    for (int i = 0; i < components.length; ++i) {
      if (components[i][0].equals(chain.get(chain.size() - 1))) {
        final Integer[][] recurseComponents = new Integer[components.length - 1][];
        System.arraycopy(components, 0, recurseComponents, 0, i);
        System.arraycopy(components, i + 1, recurseComponents, i, components.length - i - 1);

        final List<Integer> recurseChain = new ArrayList<>(chain.size() + 2);
        recurseChain.addAll(chain);
        recurseChain.add(components[i][0]);
        recurseChain.add(components[i][1]);

        chains(chains, recurseComponents, recurseChain);
      }
      else if (components[i][1].equals(chain.get(chain.size() - 1))) {
        final Integer[][] recurseComponents = new Integer[components.length - 1][];
        System.arraycopy(components, 0, recurseComponents, 0, i);
        System.arraycopy(components, i + 1, recurseComponents, i, components.length - i - 1);

        final List<Integer> recurseChain = new ArrayList<>(chain.size() + 2);
        recurseChain.addAll(chain);
        recurseChain.add(components[i][1]);
        recurseChain.add(components[i][0]);

        chains(chains, recurseComponents, recurseChain);
      }
    }
  }

  /** Get the input data for this solution. */
  private Integer[][] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2017, 24)).stream().map(
        s -> Arrays.stream(SEPARATOR.split(s)).map(Integer::valueOf).toArray(Integer[]::new)).toArray(Integer[][]::new);
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final Pattern SEPARATOR = Pattern.compile("/");
}
