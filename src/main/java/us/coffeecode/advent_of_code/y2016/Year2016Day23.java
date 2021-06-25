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
package us.coffeecode.advent_of_code.y2016;

import java.nio.file.Files;
import java.util.List;

import us.coffeecode.advent_of_code.Utils;
import us.coffeecode.advent_of_code.y2016.assembunny.Instruction;
import us.coffeecode.advent_of_code.y2016.assembunny.Interpreter;
import us.coffeecode.advent_of_code.y2016.assembunny.State;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/23">Year 2016, day 23</a>. This problem uses the Assembunny code from day
 * 12, but with a new instruction. We need to run a program and get the results.
 * </p>
 * <p>
 * The two parts are basically the same, except the execution time for part 2 is a lot longer. The key here is to
 * identify what the code is doing, and make an optimization. The short version is the code takes the factorial of the
 * input, then adds the product of two numbers hidden in the source code. Calculating a factorial using repeated
 * addition is extremely slow, and even slower when the only addition is incrementing by one. The expected optimization
 * is to implement a multiply opcode and change the source appropriately. I found the block that does the alleged
 * multiplication (repeated incrementing based on register contents) and patched it to use real multiplication instead.
 * I padded with no-ops to ensure it is unnecessary to modify jumps elsewhere in the program.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day23 {

  public long calculatePart1() {
    final State state = State.load(getInput());
    state.reg[0] = 7;
    new Interpreter().execute(state);
    return state.reg[0];
  }

  public long calculatePart2() {
    final State state = State.load(getInput());
    state.reg[0] = 12;
    // Patch the input program to use multiplication instead of repeated incrementing.
    state.instructions[4] = new Instruction("mul b d a");
    state.instructions[5] = state.instructions[6] =
      state.instructions[7] = state.instructions[8] = state.instructions[9] = new Instruction("nop");
    new Interpreter().execute(state);
    return state.reg[0];
  }

  /** Get the input data for this solution. */
  private List<String> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2016, 23));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
