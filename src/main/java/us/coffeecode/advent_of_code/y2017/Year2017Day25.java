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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/25">Year 2017, day 25</a>. The final puzzle for 2017 is an implementation
 * of Turing machine. We need to run it for a given number of iterations then count the number of true values on the
 * tape. Note that this is not a real Turning machine: as the instructions state, that is a theoretical model.
 * Specifically, this implementation lacks an infinite tape and we know it will halt since it runs a finite number of
 * iterations.
 * </p>
 * <p>
 * This is straightforward, and the bulk of the effort is in parsing the input, not running the simulation.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day25 {

  public long calculatePart1() {
    final Input input = getInput();
    final boolean[] tape = new boolean[1 << 14];
    State state = input.states.get(input.start);
    int location = tape.length / 2;
    int checksum = 0;
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

  /** Get the input data for this solution. */
  private Input getInput() {
    try {
      return Input.valueOf(Utils.getLineGroups(Files.readAllLines(Utils.getInput(2017, 25))));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
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

  private static final class Instruction {

    final boolean write;

    final int move;

    final StateId next;

    private Instruction(final boolean _write, final int _move, final StateId _next) {
      write = _write;
      move = _move;
      next = _next;
    }

    @Override
    public String toString() {
      return "Write=" + write + ",Move=" + move + ",Next=" + next.name();
    }

    static Instruction valueOf(final List<String> lines) {
      String[] line0 = SEPARATOR.split(lines.get(0).trim());
      String[] line1 = SEPARATOR.split(lines.get(1).trim());
      String[] line2 = SEPARATOR.split(lines.get(2).trim());
      boolean write = '1' == line0[4].codePointAt(0);
      int move = "left.".equals(line1[6]) ? -1 : 1;
      StateId next = StateId.valueOf(line2[4].substring(0, 1));
      return new Instruction(write, move, next);
    }
  }

  private static final class State {

    final StateId id;

    final Instruction whenFalse;

    final Instruction whenTrue;

    private State(final StateId _id, final Instruction _whenFalse, final Instruction _whenTrue) {
      id = _id;
      whenFalse = _whenFalse;
      whenTrue = _whenTrue;
    }

    @Override
    public String toString() {
      return "Id=" + id + ",WhenFalse=[" + whenFalse + "],WhenTrue=[" + whenTrue + "]";
    }

    static State valueOf(final List<String> lines) {
      String[] line0 = SEPARATOR.split(lines.get(0));
      StateId id = StateId.valueOf(line0[2].substring(0, 1));
      return new State(id, Instruction.valueOf(lines.subList(2, 5)), Instruction.valueOf(lines.subList(6, 9)));
    }

  }

  private static final class Input {

    final int iterations;

    final StateId start;

    final Map<StateId, State> states;

    private Input(final int _iterations, final StateId _start, final Map<StateId, State> _states) {
      iterations = _iterations;
      start = _start;
      states = _states;
    }

    @Override
    public String toString() {
      return "Iterations=" + iterations + ",Start=" + start.name() + ",States=" + states;
    }

    static Input valueOf(final List<List<String>> groups) {
      StateId start = null;
      int iterations = -1;
      final Map<StateId, State> instructions = new HashMap<>();
      for (final List<String> group : groups) {
        // Initial text describing start conditions and iterations.
        if (group.get(0).startsWith("Begin")) {
          String[] line0 = SEPARATOR.split(group.get(0));
          String[] line1 = SEPARATOR.split(group.get(1));
          start = StateId.valueOf(line0[3].substring(0, 1));
          iterations = Integer.parseInt(line1[5]);
        }
        // State descriptor
        else {
          State state = State.valueOf(group);
          instructions.put(state.id, state);
        }
      }
      return new Input(iterations, start, instructions);
    }

  }

}
