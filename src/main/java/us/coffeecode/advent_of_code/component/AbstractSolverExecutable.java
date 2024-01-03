/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.component;

import java.lang.reflect.Method;

/**
 * Base for a class that runs a solver and does something with the result.
 */
public class AbstractSolverExecutable {

  protected final Object impl;

  protected final Method method;

  protected final PuzzleContext solution;

  protected final String title;

  protected final String description;

  public AbstractSolverExecutable(final Object _impl, final Method _method, final PuzzleContext _solution, final String _title) {
    impl = _impl;
    method = _method;
    solution = _solution;
    title = _title;
    description = "Year " + solution.getYear() + ", Day " + solution.getDay() + " " + title + ", Part " + solution.getPart()
      + " (" + solution.getInputId() + ")";
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return description;
  }

}
