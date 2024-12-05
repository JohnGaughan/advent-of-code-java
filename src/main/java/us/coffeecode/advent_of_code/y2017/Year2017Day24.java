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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 24, title = "Electromagnetic Moat")
@Component
public final class Year2017Day24 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Set<Chain> chains = chains(getInput(pc), (ls, c) -> c.strength() > ls.strongest);
    return chains.stream()
                 .mapToInt(Chain::strength)
                 .max()
                 .getAsInt();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Set<Chain> chains = chains(getInput(pc), (ls, c) -> c.length() >= ls.longest);
    final int longest = chains.stream()
                              .mapToInt(Chain::length)
                              .max()
                              .getAsInt();
    return chains.stream()
                 .filter(c -> c.length() == longest)
                 .mapToInt(Chain::strength)
                 .max()
                 .getAsInt();
  }

  /** Recursive base case: start the chains. */
  private Set<Chain> chains(final List<Component> components, final BiPredicate<LongestStrongest, Chain> add) {
    final Set<Chain> chains = new HashSet<>(1 << 21);
    for (int i = 0; i < components.size(); ++i) {
      final Component component = components.get(i);
      if (component.canStartChain()) {
        final List<Component> recurseComponents = new ArrayList<>(components.size() - 1);
        recurseComponents.addAll(components.subList(0, i));
        recurseComponents.addAll(components.subList(i + 1, components.size()));

        final Chain chain = new Chain(new Component[] { component }, component.getEndFromStart());

        chains(chains, recurseComponents, chain, new LongestStrongest(), add);
      }
    }
    return chains;
  }

  /** Recursive case: add to the provided chain. */
  private void chains(final Set<Chain> chains, final List<Component> components, final Chain chain, final LongestStrongest ls, final BiPredicate<LongestStrongest, Chain> add) {
    boolean terminal = true;
    for (int i = 0; i < components.size(); ++i) {
      final Component component = components.get(i);
      if (component.canAddTo(chain)) {
        terminal = false;
        final List<Component> recurseComponents = new ArrayList<>(components.size() - 1);
        recurseComponents.addAll(components.subList(0, i));
        recurseComponents.addAll(components.subList(i + 1, components.size()));

        final Chain recurseChain = Chain.of(chain, component);

        chains(chains, recurseComponents, recurseChain, ls, add);
      }
    }
    // Add this chain if it qualifies to be considered.
    if (terminal && add.test(ls, chain)) {
      chains.add(chain);
      ls.calculate(chain);
    }
  }

  /** Get the input data for this solution. */
  private List<Component> getInput(final PuzzleContext pc) {
    return il.linesAsObjects(pc, Component::make);
  }

  private class LongestStrongest {

    int longest = 0;

    int strongest = 0;

    void calculate(final Chain chain) {
      longest = Math.max(longest, chain.length());
      strongest = Math.max(strongest, chain.strength());
    }
  }

  private record Chain(Component[] components, int end) {

    static Chain of(final Chain current, final Component next) {
      final Component[] newComponents = new Component[current.components.length + 1];
      System.arraycopy(current.components, 0, newComponents, 0, current.components.length);
      newComponents[newComponents.length - 1] = next;
      final Component oldLast = current.components[current.components.length - 1];
      int end = next.a;
      if ((next.a == oldLast.a) || (next.a == oldLast.b)) {
        end = next.b;
      }
      return new Chain(newComponents, end);
    }

    int length() {
      return components.length;
    }

    int strength() {
      return Arrays.stream(components)
                   .mapToInt(Component::strength)
                   .sum();
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof Chain) {
        return Arrays.equals(components, ((Chain) obj).components);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Arrays.hashCode(components);
    }

    @Override
    public String toString() {
      return Arrays.toString(components) + " = " + strength();
    }
  }

  private record Component(int a, int b) {

    static Component make(final String line) {
      final int delimiter = line.indexOf('/');
      final int a = Integer.parseInt(line.substring(0, delimiter));
      final int b = Integer.parseInt(line.substring(delimiter + 1, line.length()));
      return new Component(a, b);
    }

    boolean canStartChain() {
      return (a == 0) || (b == 0);
    }

    boolean canAddTo(final Chain chain) {
      return (chain.end == a) || (chain.end == b);
    }

    int getEndFromStart() {
      if (a == 0) {
        return b;
      }
      return a;
    }

    int strength() {
      return a + b;
    }

    @Override
    public String toString() {
      return a + "/" + b;
    }
  }
}
