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
package us.coffeecode.advent_of_code.y2020;

import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 22, title = "Crab Combat")
@Component
public class Year2020Day22 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final List<Deque<Integer>> hands = il.groupsAsObjects(pc, this::parse);
    final Deque<Integer> p1 = hands.getFirst();
    final Deque<Integer> p2 = hands.get(1);
    while (!p1.isEmpty() && !p2.isEmpty()) {
      final Integer c1 = p1.remove();
      final Integer c2 = p2.remove();
      if (c1.compareTo(c2) > 0) {
        p1.add(c1);
        p1.add(c2);
      }
      else {
        p2.add(c2);
        p2.add(c1);
      }
    }
    return score(p1.isEmpty() ? p2 : p1);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final List<Deque<Integer>> hands = il.groupsAsObjects(pc, this::parse);
    return play(hands.getFirst(), hands.get(1)).score;
  }

  /** Play a game of recursive combat. */
  private RecursiveResult play(final Deque<Integer> p1, final Deque<Integer> p2) {
    final Set<GameState> oldStates = new HashSet<>();
    while (!p1.isEmpty() && !p2.isEmpty()) {
      final GameState currentState = new GameState(p1, p2);
      if (oldStates.contains(currentState)) {
        return new RecursiveResult(true, score(p1));
      }
      oldStates.add(currentState);
      final int c1 = p1.remove()
                       .intValue();
      final int c2 = p2.remove()
                       .intValue();
      // See if we need to recurse
      if ((p1.size() >= c1) && (p2.size() >= c2)) {
        // Recurse
        final Deque<Integer> p1new = new LinkedList<>();
        Iterator<Integer> iter = p1.iterator();
        for (int i = 0; i < c1; ++i) {
          p1new.add(iter.next());
        }
        final Deque<Integer> p2new = new LinkedList<>();
        iter = p2.iterator();
        for (int i = 0; i < c2; ++i) {
          p2new.add(iter.next());
        }
        RecursiveResult result = play(p1new, p2new);
        if (result.p1won) {
          p1.add(Integer.valueOf(c1));
          p1.add(Integer.valueOf(c2));
        }
        else {
          p2.add(Integer.valueOf(c2));
          p2.add(Integer.valueOf(c1));
        }
      }
      // Play as normal
      else if (c1 > c2) {
        p1.add(Integer.valueOf(c1));
        p1.add(Integer.valueOf(c2));
      }
      else {
        p2.add(Integer.valueOf(c2));
        p2.add(Integer.valueOf(c1));
      }
    }
    final boolean p1won = p2.isEmpty();
    return new RecursiveResult(p1won, score(p1won ? p1 : p2));
  }

  private long score(final Deque<Integer> hand) {
    long score = 0;
    long multiplier = 1;
    while (!hand.isEmpty()) {
      score += multiplier * hand.removeLast()
                                .intValue();
      ++multiplier;
    }
    return score;
  }

  /** Result of a recursive game - who won, and what was the score? */
  private static final class RecursiveResult {

    final boolean p1won;

    final long score;

    RecursiveResult(final boolean b, final long i) {
      p1won = b;
      score = i;
    }
  }

  /** Represents one historical state of the game. */
  private static final class GameState {

    private final Integer[] p1;

    private final Integer[] p2;

    private final int hash;

    GameState(final Deque<Integer> h1, final Deque<Integer> h2) {
      p1 = h1.toArray(new Integer[h1.size()]);
      p2 = h2.toArray(new Integer[h2.size()]);
      hash = Objects.hash(Integer.valueOf(Arrays.hashCode(p1)), Integer.valueOf(Arrays.hashCode(p2)));
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof GameState o) {
        return Arrays.equals(p1, o.p1) && Arrays.equals(p2, o.p2);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return hash;
    }

  }

  private Deque<Integer> parse(final List<String> input) {
    return input.stream()
                .skip(1)
                .map(Integer::valueOf)
                .collect(Collectors.toCollection(LinkedList::new));
  }

}
