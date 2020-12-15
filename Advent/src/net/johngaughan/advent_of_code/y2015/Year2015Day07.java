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
package net.johngaughan.advent_of_code.y2015;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/7">Year 2015, day 7</a>. This problem introduces what is essentially a
 * network of logic gates. Instead of booleans, however, they operate on integers and perform bitwise operations on
 * them. Part one asks the value of "a", the terminal gate, once everything is wired up. Part two then has us reset the
 * network and change it by assigning "b" the value "a" had in part one, and seeing how that changes "a".
 * </p>
 * <p>
 * This was an interesting puzzle that I really enjoyed programming. I saw two potential approaches. The first would be
 * to create a big tree where the parent node is the wire "a" and its operator. Each child node is either a constant
 * value or another wire with its operator, and so on until the tree can be reduced bit by bit and constants roll up to
 * the root node. That would have been a bit more tedious and a lot more code. I also noted that the digraph of wires in
 * the input is not a tree, although without any loops that is easy enough to work around. However, it indicates this is
 * probably not the best approach.
 * </p>
 * <p>
 * Instead, I went with a simpler approach. Given wire "a", recursively evaluate its children until a particular wire
 * can be reduced to a constant value. I use memoization to avoid redundancy, although the input size is small enough
 * that it does not take much time either way. Since the entire solution is calculated outside of the rules, reusing
 * them for part two is trivial.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day07 {

  public int calculatePart1(final Path path) {
    final Map<String, Operation> ops = new HashMap<>();
    for (final Operation op : parse(path)) {
      ops.put(op.output, op);
    }
    return getValue("a", ops, new HashMap<>());
  }

  public int calculatePart2(final Path path) {
    final Map<String, Operation> ops = new HashMap<>();
    for (final Operation op : parse(path)) {
      ops.put(op.output, op);
    }
    final int a = getValue("a", ops, new HashMap<>());
    ops.put("b", new Operation(Integer.toString(a) + " -> b"));
    return getValue("a", ops, new HashMap<>());
  }

  /** Get the value of a wire. */
  private int getValue(final String wire, final Map<String, Operation> ops, final Map<String, Integer> memoizer) {
    // If this value has already been seen, use the memoized value instead.
    if (memoizer.containsKey(wire)) {
      return memoizer.get(wire);
    }
    final Operation thisOp = ops.get(wire);
    final int value;
    if (Operator.AND == thisOp.operator) {
      final int op1 = getValue(thisOp.operands.get(0), ops, memoizer);
      final int op2 = getValue(thisOp.operands.get(1), ops, memoizer);
      value = op1 & op2;
    }
    else if (Operator.ASSIGN == thisOp.operator) {
      value = getValue(thisOp.operands.get(0), ops, memoizer);
    }
    else if (Operator.LSHIFT == thisOp.operator) {
      final int op1 = getValue(thisOp.operands.get(0), ops, memoizer);
      final int op2 = getValue(thisOp.operands.get(1), ops, memoizer);
      value = op1 << op2;
    }
    else if (Operator.NOT == thisOp.operator) {
      final int op1 = getValue(thisOp.operands.get(0), ops, memoizer);
      value = ~op1;
    }
    else if (Operator.OR == thisOp.operator) {
      final int op1 = getValue(thisOp.operands.get(0), ops, memoizer);
      final int op2 = getValue(thisOp.operands.get(1), ops, memoizer);
      value = op1 | op2;
    }
    else if (Operator.RSHIFT == thisOp.operator) {
      final int op1 = getValue(thisOp.operands.get(0), ops, memoizer);
      final int op2 = getValue(thisOp.operands.get(1), ops, memoizer);
      value = op1 >> op2;
    }
    else {
      throw new IllegalArgumentException();
    }
    memoizer.put(wire, value);
    return value;
  }

  /** Get the value of an operand which may be a literal value or may delegate to another wire. */
  private int getValue(final Object value, final Map<String, Operation> ops, final Map<String, Integer> memoizer) {
    if (value instanceof Integer) {
      return ((Integer) value).intValue();
    }
    return getValue(value.toString(), ops, memoizer);
  }

  /** Parse the file located at the provided path location. */
  private List<Operation> parse(final Path path) {
    try {
      return Files.readAllLines(path).stream().map(Operation::new).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
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
        if (NUMERIC.matcher(io[0]).matches()) {
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
        if (NUMERIC.matcher(tokens[0]).matches()) {
          inputs.add(Integer.valueOf(tokens[0]));
        }
        else {
          inputs.add(tokens[0]);
        }
        if (NUMERIC.matcher(tokens[2]).matches()) {
          inputs.add(Integer.valueOf(tokens[2]));
        }
        else {
          inputs.add(tokens[2]);
        }
      }
      operands = Collections.unmodifiableList(inputs);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      final StringBuilder str = new StringBuilder();
      str.append(output).append("=(").append(operator);
      for (final Object input : operands) {
        str.append(",").append(input);
      }
      str.append(")");
      return str.toString();
    }

  }

}
