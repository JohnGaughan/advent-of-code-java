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

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 18)
@Component
public final class Year2017Day18 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final State state = new State();
    final List<Instruction> instructions = il.linesAsObjects(pc, Instruction::new);
    while ((state.pointer >= 0) && (state.pointer < instructions.size())) {
      final Instruction instruction = instructions.get(state.pointer);
      if (instruction.op == OpCode.snd) {
        state.frequency = instruction.arg1isReg ? state.registers[instruction.arg1] : instruction.arg1;
        ++state.pointer;
      }
      else if (instruction.op == OpCode.rcv) {
        if (state.registers[instruction.arg1] != 0) {
          return state.frequency;
        }
        ++state.pointer;
      }
      else {
        instruction.op.apply(instruction, state);
      }
    }
    return 0;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final BlockingQueue<Long> q1 = new LinkedBlockingQueue<>();
    final BlockingQueue<Long> q2 = new LinkedBlockingQueue<>();
    final State state0 = new State(0, q1, q2);
    final State state1 = new State(1, q2, q1);

    final List<Instruction> instructions = il.linesAsObjects(pc, Instruction::new);
    try (final ExecutorService exec = Executors.newFixedThreadPool(2)) {

      final Future<?> prog0 = exec.submit(new Part2Processor(instructions, state0));
      final Future<?> prog1 = exec.submit(new Part2Processor(instructions, state1));

      while (!prog0.isDone() && !prog1.isDone()) {
        try {
          Thread.sleep(5);
        }
        catch (InterruptedException ex) {
          break;
        }
      }

      return state1.sendCounter;
    }
  }

  /** Implementation of a processor that maintains state and can execute instructions. */
  private static final class Part2Processor
  implements Callable<Void> {

    final List<Instruction> instructions;

    final State state;

    Part2Processor(final List<Instruction> _instructions, final State _state) {
      instructions = _instructions;
      state = _state;
    }

    @Override
    public Void call() throws Exception {
      while (state.pointer >= 0 && state.pointer < instructions.size()) {
        final Instruction instruction = instructions.get(state.pointer);
        if (instruction.op == OpCode.snd) {
          state.output.add(Long.valueOf(instruction.arg1isReg ? state.registers[instruction.arg1] : instruction.arg1));
          ++state.sendCounter;
          ++state.pointer;
        }
        else if (instruction.op == OpCode.rcv) {
          final Long value = state.input.poll(5, TimeUnit.MILLISECONDS);
          if (value == null) {
            return null;
          }
          state.registers[instruction.arg1] = value.longValue();
          ++state.pointer;
        }
        else {
          instruction.op.apply(instruction, state);
        }
      }
      return null;
    }

  }

  /** Operation codes representing actions a processor can take. */
  private static enum OpCode {

    snd {

      @Override
      public void apply(final Instruction instruction, final State state) {
        // Implementation varies between parts one and two
        throw new UnsupportedOperationException();
      }
    },
    set {

      @Override
      public void apply(final Instruction instruction, final State state) {
        final long n2 = instruction.arg2isReg ? state.registers[instruction.arg2] : instruction.arg2;
        state.registers[instruction.arg1] = n2;
        ++state.pointer;
      }
    },
    add {

      @Override
      public void apply(final Instruction instruction, final State state) {
        final long n2 = instruction.arg2isReg ? state.registers[instruction.arg2] : instruction.arg2;
        state.registers[instruction.arg1] += n2;
        ++state.pointer;
      }
    },
    mul {

      @Override
      public void apply(final Instruction instruction, final State state) {
        final long n2 = instruction.arg2isReg ? state.registers[instruction.arg2] : instruction.arg2;
        state.registers[instruction.arg1] *= n2;
        ++state.pointer;
      }
    },
    mod {

      @Override
      public void apply(final Instruction instruction, final State state) {
        final long n2 = instruction.arg2isReg ? state.registers[instruction.arg2] : instruction.arg2;
        state.registers[instruction.arg1] %= n2;
        ++state.pointer;
      }
    },
    rcv {

      @Override
      public void apply(final Instruction instruction, final State state) {
        // Implementation varies between parts one and two
        throw new UnsupportedOperationException();
      }
    },
    jgz {

      @Override
      public void apply(final Instruction instruction, final State state) {
        final long n1 = instruction.arg1isReg ? state.registers[instruction.arg1] : instruction.arg1;
        final long n2 = instruction.arg2isReg ? state.registers[instruction.arg2] : instruction.arg2;
        if (n1 > 0) {
          state.pointer += n2;
        }
        else {
          ++state.pointer;
        }
      }
    };

    public abstract void apply(final Instruction instruction, final State state);
  }

  /** Represents the current state of a single program. */
  private static final class State {

    final long[] registers = new long[26];

    final BlockingQueue<Long> input;

    final BlockingQueue<Long> output;

    int sendCounter;

    long frequency;

    int pointer = 0;

    State() {
      this(0, null, null);
    }

    State(final int id, final BlockingQueue<Long> _input, final BlockingQueue<Long> _output) {
      registers['p' - 'a'] = id;
      input = _input;
      output = _output;
    }

  }

  /** Instructions are opcodes with operands/arguments. */
  private static final class Instruction {

    private static final Pattern SEPARATOR = Pattern.compile(" ");

    final OpCode op;

    final int arg1;

    final int arg2;

    final boolean arg1isReg;

    final boolean arg2isReg;

    Instruction(final String input) {
      final String[] tokens = SEPARATOR.split(input);
      op = OpCode.valueOf(tokens[0]);
      // First argument is always a register identified by a character.
      final int a1 = tokens[1].codePointAt(0);
      if (Character.isDigit(a1)) {
        arg1 = Integer.parseInt(tokens[1]);
        arg1isReg = false;
      }
      else {
        arg1 = tokens[1].codePointAt(0) - 'a';
        arg1isReg = true;
      }
      // Second argument might not exist, or could be either a character or integer.
      if (tokens.length < 3) {
        arg2 = 0;
        arg2isReg = false;
      }
      else if (tokens[2].length() == 1) {
        // Could be a single-digit integer or a register.
        final int ch = tokens[2].codePointAt(0);
        if (Character.isLetter(ch)) {
          arg2 = ch - 'a';
          arg2isReg = true;
        }
        else {
          arg2 = Integer.parseInt(tokens[2]);
          arg2isReg = false;
        }
      }
      else {
        // Cannot possibly a register, assume integer.
        arg2 = Integer.parseInt(tokens[2]);
        arg2isReg = false;
      }
    }

  }

}
