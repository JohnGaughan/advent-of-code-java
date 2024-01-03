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
package us.coffeecode.advent_of_code.y2021;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2021, day = 21, title = "Dirac Dice")
@Component
public final class Year2021Day21 {

  private static final int WIN_PART_1 = 1_000;

  private static final int WIN_PART_2 = 21;

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[] locations =
      il.lines(pc).stream().map(s -> s.substring(28)).mapToInt(Integer::parseInt).map(i -> (i + 9) % 10).toArray();
    final long[] scores = new long[2];
    int winner = -1;
    int nextRoll = 0;
    int who = 0;
    long rolls = 0;
    for (; winner < 0; winner = (scores[0] >= WIN_PART_1) ? 0 : ((scores[1] >= WIN_PART_1) ? 1 : -1)) {
      int roll = nextRoll;
      nextRoll = (nextRoll + 1) % 100;
      roll += nextRoll;
      nextRoll = (nextRoll + 1) % 100;
      roll += nextRoll;
      nextRoll = (nextRoll + 1) % 100;
      rolls += 3;

      roll = (roll + 3) % 10;

      locations[who] = (locations[who] + roll) % 10;
      scores[who] += (locations[who] + 1);
      who = (who + 1) % 2;
    }
    final int loser = (winner + 1) % 2;
    return rolls * scores[loser];
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[] locations = il.lines(pc).stream().map(s -> s.substring(28)).mapToInt(Integer::parseInt).toArray();
    final long[] wins = turn(new State(new int[2], Arrays.copyOf(locations, locations.length), 0), new HashMap<>(1 << 17));
    return Arrays.stream(wins).max().getAsLong();
  }

  private long[] turn(final State state, final Map<State, long[]> cache) {
    if (state.scores[0] >= WIN_PART_2) {
      return new long[] { 1, 0 };
    }
    else if (state.scores[1] >= WIN_PART_2) {
      return new long[] { 0, 1 };
    }
    else if (cache.containsKey(state)) {
      return cache.get(state);
    }
    else {
      final long[] wins = new long[2];
      for (final Roll r : ROLLS) {
        final int[] newScores = Arrays.copyOf(state.scores, 2);
        final int[] newLocations = Arrays.copyOf(state.locations, 2);
        newLocations[state.who] += r.value;
        if (newLocations[state.who] > 10) {
          newLocations[state.who] -= 10;
        }
        newScores[state.who] += newLocations[state.who];
        final long[] w = turn(new State(newScores, newLocations, (state.who + 1) & 1), cache);
        wins[0] += w[0] * r.frequency;
        wins[1] += w[1] * r.frequency;
      }
      cache.put(state, wins);
      return wins;
    }
  }

  private record State(int[] scores, int[] locations, int who) {

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof State o) {
        return (who == o.who) && Arrays.equals(scores, o.scores) && Arrays.equals(locations, o.locations);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(Integer.valueOf(locations[0]), Integer.valueOf(locations[1]), Integer.valueOf(who),
        Integer.valueOf(scores[0]), Integer.valueOf(scores[1]));
    }

  }

  private record Roll(int value, long frequency) {}

  private static final List<Roll> ROLLS =
    List.of(new Roll(3, 1), new Roll(4, 3), new Roll(5, 6), new Roll(6, 7), new Roll(7, 6), new Roll(8, 3), new Roll(9, 1));
}
