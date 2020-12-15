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
package net.johngaughan.advent_of_code.y2020;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/8">Year 2020, day 8</a>. Today's problem asks us to run a simple virtual
 * machine that executes three types of instructions. The program as given does not halt: it has an infinite loop. Part
 * one asks us to determine the value in the VM's sole register (accumulator) when it first loops over an instruction it
 * already ran. Part two says that modifying a single instruction can cause the program to halt, and to find that
 * instruction and the value of the accumulator when it does halt.
 * </p>
 * <p>
 * This is a simple VM implementation: so simple it doesn't even need any complex state, just a few local variables.
 * Part one is easily O(n) because the program never sees any given instruction more than once: otherwise, it could not
 * loop. Part two has some different implementations with differing complexity. While a naive implementation works well
 * enough, this solution adds some optimizing. First, it figures out which instructions are guaranteed to lead to the
 * program halting. Then it executes the algorithm similarly to part one, except it checks each instruction to see if
 * modifying its operation will result in landing on any instruction guaranteed to halt. If so, it modifies the
 * operation but it only does this once, per the problem requirements.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day08 {

  public long calculatePart1(final Path path) {
    final Map<Integer, Instruction> instructions = parse(path);
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

  public long calculatePart2(final Path path) {
    final Map<Integer, Instruction> instructions = parse(path);
    final Set<Integer> terminals =
      getTerminalInstructions(instructions.size(), instructions, getInstructionMapping(instructions));
    long accumulator = 0;
    int location = 0;
    boolean alreadyUncorrupted = false;
    while (true) {
      // Halt condition.
      if (location == instructions.size()) {
        break;
      }
      final Instruction next = instructions.get(location);
      Operation op = next.op;
      // See if uncorrupting the current instruction will result in the next operation being guaranteed to halt. If so,
      // uncorrupt it and mark a flag so we do not uncorrupt more than one instruction.
      if (!alreadyUncorrupted) {
        if (op == Operation.jmp && terminals.contains(location + 1)) {
          op = Operation.nop;
          alreadyUncorrupted = true;
        }
        else if (op == Operation.nop && terminals.contains(location + next.arg)) {
          op = Operation.jmp;
          alreadyUncorrupted = true;
        }
      }
      // Execute the current operation.
      if (op == Operation.acc) {
        ++location;
        accumulator += next.arg;
      }
      else if (op == Operation.jmp) {
        location += next.arg;
      }
      else if (op == Operation.nop) {
        ++location;
      }
    }
    return accumulator;
  }

  /**
   * Create a mapping of all instructions that lead to a given location, including the terminal instruction one past the
   * end.
   */
  private Map<Integer, Set<Integer>> getInstructionMapping(final Map<Integer, Instruction> instructions) {
    final Map<Integer, Set<Integer>> mapping = new HashMap<>(1024);
    for (int i = 0; i <= instructions.size(); ++i) {
      mapping.put(i, new HashSet<>());
    }
    for (final Map.Entry<Integer, Instruction> entry : instructions.entrySet()) {
      final Integer instructionNumber = entry.getKey();
      final Instruction instruction = entry.getValue();
      final Integer target;
      if (instruction.op == Operation.acc || instruction.op == Operation.nop) {
        target = instructionNumber + 1;
      }
      else {
        target = instructionNumber + instruction.arg;
      }
      mapping.get(target).add(instructionNumber);
    }
    return mapping;
  }

  /** Get the number of all instructions that are terminal, meaning they lead to the program halting. */
  private Set<Integer> getTerminalInstructions(final Integer instruction, final Map<Integer, Instruction> instructions, final Map<Integer, Set<Integer>> instructionMapping) {
    final Set<Integer> terminals = new HashSet<>();
    for (final Integer nonTerminal : instructionMapping.get(instruction)) {
      terminals.add(nonTerminal);
      terminals.addAll(getTerminalInstructions(nonTerminal, instructions, instructionMapping));
    }
    return terminals;
  }

  /** Parse the file located at the provided path location. */
  private Map<Integer, Instruction> parse(final Path path) {
    try {
      final List<Instruction> instructions =
        Files.readAllLines(path).stream().map(Instruction::new).collect(Collectors.toList());
      final Map<Integer, Instruction> map = new HashMap<>();
      for (int i = 0; i < instructions.size(); ++i) {
        map.put(i, instructions.get(i));
      }
      return map;
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

  }

  /** Represents the operations supported. */
  private static enum Operation {
    acc,
    jmp,
    nop;
  }

}
