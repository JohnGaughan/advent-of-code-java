/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2024  John Gaughan
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
package us.coffeecode.advent_of_code.y2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 24, title = "Crossed Wires")
@Component
public class Year2024Day24 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = getInput(pc);
    return calculate(input.wires, input.operations);
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    return "NO SOLUTION FOUND";
  }

  /** Perform a calculation of the system given the provided starting state. */
  private long calculate(final Map<String, Integer> wires, final List<Operation> operations) {
    boolean updated = false;
    do {
      updated = false;
      for (final var iter = operations.iterator(); iter.hasNext();) {
        final Operation op = iter.next();
        if (op.execute(wires)) {
          // Go once more through the loop, and remove this instruction: once run, it never runs again.
          updated = true;
          iter.remove();
        }
      }
    } while (updated);
    return extractValue(wires, "z");
  }

  /** Extract the value of the specified register wires as a long integer. */
  private long extractValue(final Map<String, Integer> wires, final String prefix) {
    long result = 0;
    for (final var entry : wires.entrySet()) {
      final String key = entry.getKey();
      if (key.startsWith(prefix)) {
        final int shift = Integer.parseInt(key.substring(1));
        final long bit = entry.getValue()
                              .intValue();
        result += (bit << shift);
      }
    }
    return result;
  }

  /** Read the input and parse it into an input data structure. */
  private Input getInput(final PuzzleContext pc) {
    final List<List<String>> groups = il.groups(pc);
    return new Input(makeWires(groups.getFirst()), makeOperations(groups.getLast()));
  }

  /** Given raw string input, make a wire mapping. */
  private Map<String, Integer> makeWires(final List<String> group) {
    final Map<String, Integer> wires = new HashMap<>();
    for (final String s : group) {
      wires.put(s.substring(0, 3), Integer.valueOf((s.codePointAt(5) == '1') ? 1 : 0));
    }
    return wires;
  }

  /** Given raw string input, convert each line an operation. */
  private List<Operation> makeOperations(final List<String> group) {
    final List<Operation> operations = new ArrayList<>(group.size());
    for (final String s : group) {
      final String[] tokens = OPERATION_SPLIT.split(s);
      operations.add(new Operation(Operator.valueOf(tokens[1]), tokens[0], tokens[2], tokens[4]));
    }
    return operations;
  }

  private static final Pattern OPERATION_SPLIT = Pattern.compile(" ");

  /** Represents one operator that can be performed on two operands. */
  private enum Operator {

    AND {

      @Override
      public int apply(final int operand1, final int operand2) {
        return ((operand1 == 1) && (operand2 == 1)) ? 1 : 0;
      }
    },
    OR {

      @Override
      public int apply(final int operand1, final int operand2) {
        return ((operand1 == 1) || (operand2 == 1)) ? 1 : 0;
      }
    },
    XOR {

      @Override
      public int apply(final int operand1, final int operand2) {
        return ((operand1 == 1) ^ (operand2 == 1)) ? 1 : 0;
      }
    };

    public abstract int apply(final int operand1, final int operand2);
  }

  /** Contains all data needed to perform an operation on two operands and store it in a destination wire. */
  private record Operation(Operator operator, String operand1, String operand2, String destination) {

    /** Attempt to execute the operation, returning true if it was able to do so. */
    boolean execute(final Map<String, Integer> wires) {
      if (wires.containsKey(operand1) && wires.containsKey(operand2) && !wires.containsKey(destination)) {
        int value = operator.apply(wires.get(operand1)
                                        .intValue(),
          wires.get(operand2)
               .intValue());
        wires.put(destination, Integer.valueOf(value));
        return true;
      }
      return false;
    }
  }

  /** Contains all data in the input file. */
  private record Input(Map<String, Integer> wires, List<Operation> operations) {}
}
