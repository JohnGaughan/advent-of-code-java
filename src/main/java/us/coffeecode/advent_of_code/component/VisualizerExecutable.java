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
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.visualization.IVisualizationResult;

/**
 * This object executes a test and returns its visualization results.
 */
public class VisualizerExecutable
extends AbstractSolverExecutable
implements Callable<Collection<IVisualizationResult>> {

  public VisualizerExecutable(final Object _impl, final Method _method, final PuzzleContext _solution, final String _title) {
    super(_impl, _method, _solution, _title);
  }

  /** Run the test method and return the results, wrapping any checked exceptions. */
  @SuppressWarnings("unchecked")
  @Override
  public Collection<IVisualizationResult> call() {
    try {
      final Object results = method.invoke(impl, solution);
      check(results);
      return (Collection<IVisualizationResult>) results;
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** Ensure that the returned object is a list and contains only IVisualizationResult instances. */
  private void check(final Object results) {
    if (results == null) {
      throw new IllegalArgumentException("Visualization results are null.");
    }
    else if (!(results instanceof Collection)) {
      throw new IllegalArgumentException("Visualization results is not an instance of Collection [" + results.getClass()
                                                                                                             .getName()
        + "].");
    }
    final Collection<?> c = (Collection<?>) results;
    if (c.stream()
         .anyMatch(o -> o == null)) {
      throw new IllegalArgumentException("Visualization results exist but contain at least one null value.");
    }
    final String badClasses = c.stream()
                               .filter(o -> !(o instanceof IVisualizationResult))
                               .map(o -> o.getClass()
                                          .getName())
                               .distinct()
                               .collect(Collectors.joining(", "));
    if (badClasses.length() > 0) {
      throw new IllegalArgumentException(
        "Visualization results is a Collection, but contains invalid elements with classes [" + badClasses + "].");
    }
  }
}
