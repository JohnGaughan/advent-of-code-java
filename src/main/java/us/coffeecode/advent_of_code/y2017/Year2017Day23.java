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

import java.util.Arrays;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.PrimeProvider;

@AdventOfCodeSolution(year = 2017, day = 23)
@Component
public final class Year2017Day23 {

  @Autowired
  private PrimeProvider primeProvider;

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final State state = new State();
    final Instruction[] instructions = il.linesAsObjects(pc, Instruction::new)
                                         .toArray(Instruction[]::new);
    long multiplies = 0;
    while ((state.pointer >= 0) && (state.pointer < instructions.length)) {
      if (instructions[state.pointer].op == OpCode.mul) {
        ++multiplies;
      }
      instructions[state.pointer].op.apply(instructions[state.pointer], state);
    }
    return multiplies;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Instruction[] input = il.linesAsObjects(pc, Instruction::new)
                                  .toArray(Instruction[]::new);
    final int b = (input[4].arg2 * input[0].arg2) - input[5].arg2;
    final int c = b - input[7].arg2;

    long composites = 0;
    final int[] primes = primeProvider.getPrimesUpTo(c);
    for (int i = b; i <= c; i += 17) {
      if (Arrays.binarySearch(primes, i) < 0) {
        ++composites;
      }
    }

    return composites;
  }

  /** Operation codes representing actions a processor can take. */
  private static enum OpCode {

    set {

      @Override
      public void apply(final Instruction instruction, final State state) {
        final long n2 = instruction.arg2isReg ? state.registers[instruction.arg2] : instruction.arg2;
        state.registers[instruction.arg1] = n2;
        ++state.pointer;
      }
    },
    sub {

      @Override
      public void apply(final Instruction instruction, final State state) {
        final long n2 = instruction.arg2isReg ? state.registers[instruction.arg2] : instruction.arg2;
        state.registers[instruction.arg1] -= n2;
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
    jnz {

      @Override
      public void apply(final Instruction instruction, final State state) {
        final long n1 = instruction.arg1isReg ? state.registers[instruction.arg1] : instruction.arg1;
        final long n2 = instruction.arg2isReg ? state.registers[instruction.arg2] : instruction.arg2;
        if (n1 != 0) {
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

    final long[] registers = new long[8];

    int pointer = 0;
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
