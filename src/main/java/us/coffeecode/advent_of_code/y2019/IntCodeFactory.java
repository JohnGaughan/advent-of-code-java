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
package us.coffeecode.advent_of_code.y2019;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

/**
 * Creates IntCode instances. Indirection through a factory class makes it Springy.
 */
@Component
public class IntCodeFactory {

  @Autowired
  private InputLoader il;

  public IntCode make(final PuzzleContext pc, final ExecutionOption... options) {
    return new IntCode(il, pc, new LongQueue(), options);
  }

  public IntCode make(final PuzzleContext pc, final IntCodeIoQueue input, final ExecutionOption... options) {
    return new IntCode(il, pc, input, options);
  }

  public IntCode make(final IntCode original, final ExecutionOption... options) {
    return new IntCode(original, options);
  }

  public IntCode make(final IntCode original, final IntCodeIoQueue input, final ExecutionOption... options) {
    return new IntCode(original, input, options);
  }

}
