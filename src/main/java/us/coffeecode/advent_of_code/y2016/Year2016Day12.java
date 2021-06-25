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
import us.coffeecode.advent_of_code.y2016.assembunny.Interpreter;
import us.coffeecode.advent_of_code.y2016.assembunny.State;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/12">Year 2016, day 12</a>. This is a simple virtual machine running a
 * scaled down assembly language. We need to run a program and return the result in a register. The only difference
 * between the two parts is the starting state.
 * </p>
 * <p>
 * This is a fairly simple problem with a simple solution. I don't like passing around Object and checking its type, but
 * it is the simplest of several alternatives I considered.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day12 {

  public long calculatePart1() {
    final State state = State.load(getInput());
    new Interpreter().execute(state);
    return state.reg[0];
  }

  public long calculatePart2() {
    final State state = State.load(getInput());
    state.reg[2] = 1;
    new Interpreter().execute(state);
    return state.reg[0];
  }

  /** Get the input data for this solution. */
  private List<String> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2016, 12));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
