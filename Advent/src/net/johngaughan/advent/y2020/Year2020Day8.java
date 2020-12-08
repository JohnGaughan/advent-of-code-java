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
package net.johngaughan.advent.y2020;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.johngaughan.advent.AdventProblem;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/8">Year 2020, day 8</a>.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day8
implements AdventProblem {

  /** {@inheritDoc} */
  @Override
  public long calculatePart1(final Path path) {
    final List<Instruction> instructions = parse(path);
    final Set<Integer> seen = new HashSet<>();
    long accumulator = 0;
    int location = 0;
    while (!seen.contains(location)) {
      seen.add(location);
      final Instruction next = instructions.get(location);
      if (next.op == Operation.acc) {
        ++location;
        accumulator += next.arg;
      }
      else if (next.op == Operation.jmp) {
        location += next.arg;
      }
      else if (next.op == Operation.nop) {
        ++location;
      }
    }
    return accumulator;
  }

  /** {@inheritDoc} */
  @Override
  public long calculatePart2(final Path path) {
    final List<Instruction> instructions = parse(path);
    // We already know the program as-is does not halt, so loop through it and flip operations until it halts.
    for (int i = 0; i < instructions.size(); ++i) {
      Instruction instruction = instructions.get(i);
      // Skip acc since it cannot be changed here
      if (instruction.op != Operation.acc) {
        instruction = instruction.change();
        final List<Instruction> newInstructions = new ArrayList<>(683);
        newInstructions.addAll(instructions.subList(0, i));
        newInstructions.add(instruction);
        newInstructions.addAll(instructions.subList(i + 1, instructions.size()));
        long result = run(newInstructions);
        if (result != Long.MIN_VALUE) {
          return result;
        }
      }
    }
    return Long.MIN_VALUE;
  }

  /** Run the program, returning the result only if it halts. If not, return Long.MIN_VALUE. */
  private long run(final List<Instruction> instructions) {
    final Set<Integer> seen = new HashSet<>();
    long accumulator = 0;
    int location = 0;
    while (!seen.contains(location)) {
      seen.add(location);
      // Halt condition.
      if (location >= instructions.size()) {
        return accumulator;
      }
      final Instruction next = instructions.get(location);
      if (next.op == Operation.acc) {
        ++location;
        accumulator += next.arg;
      }
      else if (next.op == Operation.jmp) {
        location += next.arg;
      }
      else if (next.op == Operation.nop) {
        ++location;
      }
    }
    return Long.MIN_VALUE;
  }

  /** Parse the file located at the provided path location. */
  private List<Instruction> parse(final Path path) {
    try {
      return Files.readAllLines(path).stream().map(s -> new Instruction(s)).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** One instruction corresponds with one line in the input file. */
  private static final class Instruction {

    private static final Pattern SPLIT = Pattern.compile(" ");

    final Operation op;

    final int arg;

    /** Constructs a <code>Instruction</code>. */
    Instruction(final String input) {
      final String[] token = SPLIT.split(input);
      op = Operation.valueOf(token[0]);
      arg = Integer.parseInt(token[1]);
    }

    Instruction(final Operation operation, final int argument) {
      op = operation;
      arg = argument;
    }

    /** Get an instruction that is a copy of this one, except the operation is changed from nop to jmp or jmp to nop. */
    Instruction change() {
      Operation operation = op;
      if (operation == Operation.jmp) {
        operation = Operation.nop;
      }
      else if (operation == Operation.nop) {
        operation = Operation.jmp;
      }
      return new Instruction(operation, arg);
    }
  }

  /** Represents the operations supported. */
  private static enum Operation {
                                 acc,
                                 jmp,
                                 nop;
  }

}
