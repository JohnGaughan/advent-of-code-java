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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 25)
@Component
public final class Year2017Day25 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = Input.make(il.groups(pc));
    final boolean[] tape = new boolean[1 << 14];
    State state = input.states.get(input.start);
    int location = tape.length >> 1;
    long checksum = 0;
    for (int i = 0; i < input.iterations; ++i) {
      final Instruction instruction = tape[location] ? state.whenTrue : state.whenFalse;
      if (tape[location] && !instruction.write) {
        --checksum;
      }
      else if (!tape[location] && instruction.write) {
        ++checksum;
      }
      tape[location] = instruction.write;
      location += instruction.move;
      state = input.states.get(instruction.next);
    }
    return checksum;
  }

  private static final Pattern SEPARATOR = Pattern.compile("\\s+");

  private static enum StateId {
    A,
    B,
    C,
    D,
    E,
    F;
  }

  private static record Instruction(boolean write, int move, StateId next) {

    static Instruction make(final List<String> lines) {
      String[] line0 = SEPARATOR.split(lines.getFirst()
                                            .trim());
      String[] line1 = SEPARATOR.split(lines.get(1)
                                            .trim());
      String[] line2 = SEPARATOR.split(lines.get(2)
                                            .trim());
      boolean write = '1' == line0[4].codePointAt(0);
      int move = "left.".equals(line1[6]) ? -1 : 1;
      StateId next = StateId.valueOf(line2[4].substring(0, 1));
      return new Instruction(write, move, next);
    }
  }

  private static record State(StateId id, Instruction whenFalse, Instruction whenTrue) {

    static State make(final List<String> lines) {
      String[] line0 = SEPARATOR.split(lines.getFirst());
      StateId id = StateId.valueOf(line0[2].substring(0, 1));
      return new State(id, Instruction.make(lines.subList(2, 5)), Instruction.make(lines.subList(6, 9)));
    }

  }

  private static record Input(int iterations, StateId start, Map<StateId, State> states) {

    static Input make(final Iterable<List<String>> groups) {
      StateId start = null;
      int iterations = -1;
      final Map<StateId, State> instructions = new HashMap<>();
      for (final List<String> group : groups) {
        // Initial text describing start conditions and iterations.
        if (group.getFirst()
                 .startsWith("Begin")) {
          String[] line0 = SEPARATOR.split(group.getFirst());
          String[] line1 = SEPARATOR.split(group.get(1));
          start = StateId.valueOf(line0[3].substring(0, 1));
          iterations = Integer.parseInt(line1[5]);
        }
        // State descriptor
        else {
          State state = State.make(group);
          instructions.put(state.id, state);
        }
      }
      return new Input(iterations, start, instructions);
    }

  }

}
