/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2025  John Gaughan
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
package us.coffeecode.advent_of_code.y2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.ToLongFunction;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.IntSort;
import com.microsoft.z3.Optimize;
import com.microsoft.z3.Optimize.Handle;
import com.microsoft.z3.Status;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2025, day = 10)
@Component
public class Year2025Day10 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, this::getMinimumPressesForLights);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, this::getMinimumPressesForJoltages);
  }

  private long calculate(final PuzzleContext pc, final ToLongFunction<Machine> f) {
    return il.linesAsObjects(pc, this::parse)
             .stream()
             .mapToLong(f)
             .sum();
  }

  /** Get the minimum button presses to achieve the light state. */
  private long getMinimumPressesForLights(final Machine m) {
    final Queue<LightState> queue = new LinkedList<>();
    queue.add(new LightState(new boolean[m.lights.length], new int[0]));
    while (!queue.isEmpty()) {
      final LightState current = queue.remove();
      for (int i = 0; i < m.buttons.length; ++i) {
        if (Arrays.binarySearch(current.buttonsPressed, i) < 0) {
          final boolean[] newLights = Arrays.copyOf(current.lights, current.lights.length);
          for (final int button : m.buttons[i]) {
            newLights[button] = !newLights[button];
          }
          if (Arrays.equals(newLights, m.lights)) {
            return current.buttonsPressed.length + 1;
          }
          final int[] newButtonsPressed = Arrays.copyOf(current.buttonsPressed, current.buttonsPressed.length + 1);
          newButtonsPressed[newButtonsPressed.length - 1] = i;
          Arrays.sort(newButtonsPressed);
          queue.add(new LightState(newLights, newButtonsPressed));
        }
      }
    }
    return 0;
  }

  /** Intermediate state object used to track button presses and light statuses during BFS. */
  private record LightState(boolean[] lights, int[] buttonsPressed) {}

  /**
   * Get the minimum button presses to achieve the joltage state. Due to Java's inability to mix array types with
   * generics in a way the Z3 API requires, we need to suppress these warnings.
   */
  @SuppressWarnings("unchecked")
  private long getMinimumPressesForJoltages(final Machine m) {
    try (final Context ctx = new Context()) {
      // Make an optimizing solver that is able to find the lowest result.
      final Optimize solver = ctx.mkOptimize();

      /*
       * Make variables for each button press. Each variable will simply be the string representation of the button's
       * array index. Note that we do not stream the buttons here, we instead create an array in parallel of the same
       * size.
       */
      final IntExpr[] buttons = IntStream.range(0, m.buttons.length)
                                         .mapToObj(Integer::toString)
                                         .map(ctx::mkIntConst)
                                         .toArray(IntExpr[]::new);

      /*
       * Add constraints that each variable must be greater than or equal to zero. Otherwise we could have negative
       * button presses which makes no sense and is explicitly forbidden in the puzzle's text.
       */
      for (final IntExpr button : buttons) {
        solver.Add(ctx.mkGe(button, ctx.mkInt(0)));
      }

      /*
       * Add constraints for joltage sums. This builds a system of linear equations where each individual equation is a
       * joltage value equal to the IDs of each button that affects it. For example: one such equation could be
       * visualized as "7 = b0 + b1 + b3"
       */
      for (int i = 0; i < m.joltages.length; ++i) {
        // Convert the joltage value to a Z3 object.
        final IntNum joltage = ctx.mkInt(m.joltages[i]);
        // Iterate all buttons and find the ones that affect this joltage.
        final List<IntExpr> b = new ArrayList<>();
        for (int j = 0; j < m.buttons.length; ++j) {
          if (Arrays.binarySearch(m.buttons[j], i) >= 0) {
            b.add(buttons[j]);
          }
        }
        // Build a sum expression from the buttons.
        final Expr<?> sum = ctx.mkAdd(b.toArray(Expr[]::new));
        // Add a constraint that this joltage must be equal to the sum of button presses that affect it.
        solver.Add(ctx.mkEq(joltage, sum));
      }

      /*
       * Solve for the minimum value. Note that the handle must be created before solving even though it is not used
       * until after calling Check() which performs the actual computation.
       */
      final Handle<IntSort> minimize = solver.MkMinimize(ctx.mkAdd(buttons));
      if (Status.SATISFIABLE == solver.Check()) {
        return ((IntNum) minimize.getLower()).getInt64();
      }
      else {
        // All inputs must represent a system of linear equations with a solution.
        throw new IllegalStateException();
      }
    }
  }

  /** Parse a single line of input into a single machine. */
  private Machine parse(final String line) {
    final String[] tokens = LINE_SPLIT.split(line);

    final int numLights = tokens[0].length() - 2;

    final boolean[] lights = new boolean[numLights];
    for (int i = 1; i < tokens[0].length() - 1; ++i) {
      lights[i - 1] = (tokens[0].codePointAt(i) == LIGHT_ON);
    }

    final int[][] buttons = new int[tokens.length - 2][];
    for (int i = 1; i < tokens.length - 1; ++i) {
      buttons[i - 1] = Arrays.stream(TOKEN_SPLIT.split(tokens[i].substring(1, tokens[i].length() - 1)))
                             .mapToInt(Integer::parseInt)
                             .toArray();
    }

    final int[] joltages =
      Arrays.stream(TOKEN_SPLIT.split(tokens[tokens.length - 1].substring(1, tokens[tokens.length - 1].length() - 1)))
            .mapToInt(Integer::parseInt)
            .toArray();

    return new Machine(lights, buttons, joltages);
  }

  /** Code point representing a machine's light being on. */
  private static int LIGHT_ON = '#';

  /** Splits a line into lights, buttons, and joltages. */
  private static final Pattern LINE_SPLIT = Pattern.compile(" ");

  /** Splits an individual token into a list of numbers. */
  private static final Pattern TOKEN_SPLIT = Pattern.compile(",");

  /**
   * Represents a single machine that needs to be solved. This contains an array with the desired light status, an array
   * of buttons with the lights they affect, and an array of target joltages.
   */
  private record Machine(boolean[] lights, int[][] buttons, int[] joltages) {}
}
