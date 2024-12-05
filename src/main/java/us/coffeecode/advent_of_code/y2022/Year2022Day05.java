/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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
package us.coffeecode.advent_of_code.y2022;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2022, day = 5, title = "Supply Stacks")
@Component
public class Year2022Day05 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    return calculate(pc, (s, m) -> {
      for (int i = 0; i < m.qty; ++i) {
        final Integer value = s.get(m.from)
                               .removeFirst();
        s.get(m.to)
         .addFirst(value);
      }
    });
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    return calculate(pc, (s, m) -> {
      final Deque<Integer> buffer = new LinkedList<>();
      for (int i = 0; i < m.qty; ++i) {
        final Integer value = s.get(m.from)
                               .removeFirst();
        buffer.addFirst(value);
      }
      while (!buffer.isEmpty()) {
        s.get(m.to)
         .addFirst(buffer.removeFirst());
      }
    });
  }

  private String calculate(final PuzzleContext pc, final BiConsumer<List<Deque<Integer>>, Move> action) {
    final Input input = Input.valueOf(il.groups(pc));
    input.moves.forEach(m -> action.accept(input.state, m));
    // Convert stack heads to a string
    return input.state.stream()
                      .mapToInt(d -> d.getFirst()
                                      .intValue())
                      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                      .toString();
  }

  private static final Pattern STACKS_SPLIT = Pattern.compile("\\s+");

  private static final Pattern MOVE_SPLIT = Pattern.compile("\\D+");

  private record Input(List<Deque<Integer>> state, List<Move> moves) {

    static Input valueOf(final List<List<String>> groups) {
      final List<Deque<Integer>> state = makeState(groups.getFirst());
      final List<Move> moves = makeMoves(groups.get(1));
      return new Input(state, moves);
    }

    private static List<Deque<Integer>> makeState(final List<String> group) {
      final String[] temp = STACKS_SPLIT.split(group.getLast()
                                                    .trim());
      final int stackCount = Integer.parseInt(temp[temp.length - 1]);
      final List<Deque<Integer>> piles = new ArrayList<>(stackCount);

      for (int i = 0; i < stackCount; ++i) {
        final Deque<Integer> pile = new LinkedList<>();
        final int offset = 1 + i * 4;
        for (int j = group.size() - 2; j >= 0; --j) {
          final String line = group.get(j);
          final int element = line.codePointAt(offset);
          if (element == ' ') {
            break;
          }
          pile.addFirst(Integer.valueOf(element));
        }
        piles.add(pile);
      }

      return piles;
    }

    private static List<Move> makeMoves(final List<String> group) {
      final List<Move> moves = new ArrayList<>(group.size());
      for (final String line : group) {
        final String[] parts = MOVE_SPLIT.split(line.substring(5));
        moves.add(new Move(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]) - 1));
      }
      return moves;
    }

  }

  private record Move(int qty, int from, int to) {}
}
