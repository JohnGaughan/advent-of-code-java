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
package us.coffeecode.advent_of_code.component;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

/**
 * This class is a bridge between JUnit and Advent of Code. It executes a single test against a puzzle solution class.
 * This works with {@link DynamicTestStream} to allow dynamically finding solution classes and running them, wiring up
 * the answers from configuration as they go.
 */
public final class SolverExecutable
extends AbstractSolverExecutable
implements Executable {

  public SolverExecutable(final Object _impl, final Method _method, final PuzzleContext _solution, final String _title) {
    super(_impl, _method, _solution, _title);
  }

  @Override
  public void execute() throws Throwable {
    final Object result = method.invoke(impl, solution);
    final String actual;
    if (result instanceof String s) {
      actual = s;
    }
    else if (result == null) {
      actual = null;
    }
    else {
      actual = result.toString();
    }
    Assertions.assertEquals(solution.getAnswer(), actual);
  }

}
