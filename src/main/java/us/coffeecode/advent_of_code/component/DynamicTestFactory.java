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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.jupiter.api.DynamicTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;

/**
 * Factory for constructing dynamic tests. This works with {@link SolverExecutor} by locating solution classes and
 * creating executors for each combination of puzzle part and input. This makes it possible to implement a new test by
 * adding a new solution class if needed, then adding arbitrary input files and answers in the corresponding properties
 * files for the puzzle.
 */
@Component
public final class DynamicTestFactory {

  @Autowired
  private ApplicationContext ac;

  @Autowired
  private InputLocator il;

  /**
   * Get tests that match the provided {@link TestContext}. This will discover all classes in the project annotated with
   * {@link AdventOfCodeSolution} using Spring's bean discovery, then filter them as appropriate using the "matches"
   * methods in {@link TestContext}.
   */
  public Collection<DynamicTest> getTests(final TestContext tc) {
    final Collection<SolverExecutable> tests = new ArrayList<>(500);
    for (final Object impl : ac.getBeansWithAnnotation(AdventOfCodeSolution.class)
                               .values()) {
      final AdventOfCodeSolution aoc = impl.getClass()
                                           .getAnnotation(AdventOfCodeSolution.class);
      if (tc.matches(aoc)) {
        tests.addAll(getTests(tc, impl, SolverExecutable.class));
      }
    }
    return tests.stream()
                .map(t -> DynamicTest.dynamicTest(t.getDescription(), t))
                .toList();
  }

  /**
   * Get all matching tests on the annotated class. Helper to the above method.
   */
  private <T extends AbstractSolverExecutable> Collection<T> getTests(final TestContext tc, final Object impl, final Class<T> execClazz) {
    final Collection<T> tests = new ArrayList<>();
    final AdventOfCodeSolution aoc = impl.getClass()
                                         .getAnnotation(AdventOfCodeSolution.class);
    final int year = aoc.year();
    final int day = aoc.day();
    final String title = aoc.title();
    for (final Method m : impl.getClass()
                              .getDeclaredMethods()) {
      m.setAccessible(true);
      final Solver solver = m.getAnnotation(Solver.class);
      if (tc.matches(solver)) {
        for (final String inputId : il.getInputIds(year, day, solver.part())) {
          if (tc.matches(inputId)) {
            final String answer = il.getAnswer(year, day, solver.part(), inputId);
            final Map<String, String> parameters = il.getParameters(year, day);
            final PuzzleContext pc = new PuzzleContext(year, day, solver.part(), inputId, answer, parameters);
            try {
              final Constructor<T> c = execClazz.getConstructor(Object.class, Method.class, PuzzleContext.class, String.class);
              tests.add(c.newInstance(impl, m, pc, title));
            }
            catch (final RuntimeException ex) {
              throw ex;
            }
            catch (final Exception ex) {
              throw new RuntimeException(ex);
            }
          }
        }
      }
    }
    return tests;
  }

  /**
   * Get the visualizer test executable for the given test context. These run the tests except they return visualization
   * results instead of validating the answers.
   */
  public Collection<VisualizerExecutable> getTestsForVisualization(final TestContext tc) {
    final Collection<VisualizerExecutable> tests = new ArrayList<>(500);
    for (final Object impl : ac.getBeansWithAnnotation(AdventOfCodeSolution.class)
                               .values()) {
      final AdventOfCodeSolution aoc = impl.getClass()
                                           .getAnnotation(AdventOfCodeSolution.class);
      if (tc.matches(aoc)) {
        tests.addAll(getTests(tc, impl, VisualizerExecutable.class));
      }
    }
    return tests;
  }

}
