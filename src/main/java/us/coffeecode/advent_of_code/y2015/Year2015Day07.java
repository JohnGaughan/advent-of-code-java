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
package us.coffeecode.advent_of_code.y2015;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 7, title = "Some Assembly Required")
@Component
public final class Year2015Day07 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Map<String, Operation> ops = il.linesAsMap(pc, Operation::new, o -> o.output, Function.identity());
    return getValue("a", ops, new HashMap<>());
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<String, Operation> ops = il.linesAsMap(pc, Operation::new, o -> o.output, Function.identity());
    final int a = getValue("a", ops, new HashMap<>());
    ops.put("b", new Operation(Integer.toString(a) + " -> b"));
    return getValue("a", ops, new HashMap<>());
  }

  /** Get the value of a wire. */
  private int getValue(final String wire, final Map<String, Operation> ops, final Map<String, Integer> memoizer) {
    // If this value has already been seen, use the memoized value instead.
    if (memoizer.containsKey(wire)) {
      return memoizer.get(wire)
                     .intValue();
    }
    final Operation thisOp = ops.get(wire);
    final int value;
    if (Operator.AND == thisOp.operator) {
      final int op1 = getValue(thisOp.operands.getFirst(), ops, memoizer);
      final int op2 = getValue(thisOp.operands.get(1), ops, memoizer);
      value = op1 & op2;
    }
    else if (Operator.ASSIGN == thisOp.operator) {
      value = getValue(thisOp.operands.getFirst(), ops, memoizer);
    }
    else if (Operator.LSHIFT == thisOp.operator) {
      final int op1 = getValue(thisOp.operands.getFirst(), ops, memoizer);
      final int op2 = getValue(thisOp.operands.get(1), ops, memoizer);
      value = op1 << op2;
    }
    else if (Operator.NOT == thisOp.operator) {
      final int op1 = getValue(thisOp.operands.getFirst(), ops, memoizer);
      value = ~op1;
    }
    else if (Operator.OR == thisOp.operator) {
      final int op1 = getValue(thisOp.operands.getFirst(), ops, memoizer);
      final int op2 = getValue(thisOp.operands.get(1), ops, memoizer);
      value = op1 | op2;
    }
    else if (Operator.RSHIFT == thisOp.operator) {
      final int op1 = getValue(thisOp.operands.getFirst(), ops, memoizer);
      final int op2 = getValue(thisOp.operands.get(1), ops, memoizer);
      value = op1 >> op2;
    }
    else {
      throw new IllegalArgumentException();
    }
    memoizer.put(wire, Integer.valueOf(value));
    return value;
  }

  /** Get the value of an operand which may be a literal value or may delegate to another wire. */
  private int getValue(final Object value, final Map<String, Operation> ops, final Map<String, Integer> memoizer) {
    if (value instanceof Integer i) {
      return i.intValue();
    }
    return getValue(value.toString(), ops, memoizer);
  }

  /** Logical operators */
  private static enum Operator {
    ASSIGN,
    AND,
    OR,
    NOT,
    LSHIFT,
    RSHIFT;
  }

  /** One operation including operator and operands */
  private static final class Operation {

    /** Splits a line into input and output portions. */
    private static final Pattern IO_SPLIT = Pattern.compile(" -> ");

    /** Splits an expression containing a binary operator. */
    private static final Pattern BINARY_OPERATOR_SPLIT = Pattern.compile(" ");

    /** Matches any number of digits. */
    private static final Pattern NUMERIC = Pattern.compile("\\d+");

    final Operator operator;

    final List<Object> operands;

    final String output;

    /** Constructs a <code>Operation</code>. */
    Operation(final String line) {
      final String[] io = IO_SPLIT.split(line);
      final List<Object> inputs = new ArrayList<>();
      output = io[1];
      // NOT is unary
      if (io[0].contains(Operator.NOT.name())) {
        operator = Operator.NOT;
        inputs.add(io[0].substring(io[0].indexOf(' ') + 1));
      }
      // Assignment is also unary, and will have a number and no spaces.
      else if (io[0].indexOf(' ') < 0) {
        operator = Operator.ASSIGN;
        if (NUMERIC.matcher(io[0])
                   .matches()) {
          inputs.add(Integer.valueOf(io[0]));
        }
        else {
          inputs.add(io[0]);
        }
      }
      // Everything else is binary
      else {
        final String[] tokens = BINARY_OPERATOR_SPLIT.split(io[0]);

        operator = Operator.valueOf(tokens[1]);

        // Add the inputs as either integers or strings.
        if (NUMERIC.matcher(tokens[0])
                   .matches()) {
          inputs.add(Integer.valueOf(tokens[0]));
        }
        else {
          inputs.add(tokens[0]);
        }
        if (NUMERIC.matcher(tokens[2])
                   .matches()) {
          inputs.add(Integer.valueOf(tokens[2]));
        }
        else {
          inputs.add(tokens[2]);
        }
      }
      operands = Collections.unmodifiableList(inputs);
    }

    @Override
    public String toString() {
      final StringBuilder str = new StringBuilder(64);
      str.append(output)
         .append("=(")
         .append(operator)
         .append(",")
         .append(operands.stream()
                         .map(Object::toString)
                         .collect(Collectors.joining(",")))
         .append(")");
      return str.toString();
    }

  }

}
