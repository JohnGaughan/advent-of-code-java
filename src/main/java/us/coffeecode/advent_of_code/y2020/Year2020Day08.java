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
package us.coffeecode.advent_of_code.y2020;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 8, title = "Handheld Halting")
@Component
public final class Year2020Day08 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Map<Integer, Instruction> instructions = getInput(pc);
    final Set<Integer> seen = new HashSet<>();
    long accumulator = 0;
    int location = 0;
    Integer key = Integer.valueOf(location);
    while (!seen.contains(key)) {
      seen.add(key);
      final Instruction next = instructions.get(key);
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
      key = Integer.valueOf(location);
    }
    return accumulator;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<Integer, Instruction> instructions = getInput(pc);
    final Set<Integer> terminals =
      getTerminalInstructions(Integer.valueOf(instructions.size()), instructions, getInstructionMapping(instructions));
    long accumulator = 0;
    int location = 0;
    boolean alreadyUncorrupted = false;
    while (true) {
      // Halt condition.
      if (location == instructions.size()) {
        break;
      }
      final Instruction next = instructions.get(Integer.valueOf(location));
      Operation op = next.op;
      // See if uncorrupting the current instruction will result in the next operation being guaranteed to halt. If so,
      // uncorrupt it and mark a flag so we do not uncorrupt more than one instruction.
      if (!alreadyUncorrupted) {
        if (op == Operation.jmp && terminals.contains(Integer.valueOf(location + 1))) {
          op = Operation.nop;
          alreadyUncorrupted = true;
        }
        else if (op == Operation.nop && terminals.contains(Integer.valueOf(location + next.arg))) {
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
      mapping.put(Integer.valueOf(i), new HashSet<>());
    }
    for (final Map.Entry<Integer, Instruction> entry : instructions.entrySet()) {
      final Integer instructionNumber = entry.getKey();
      final Instruction instruction = entry.getValue();
      final Integer target;
      if (instruction.op == Operation.acc || instruction.op == Operation.nop) {
        target = Integer.valueOf(instructionNumber.intValue() + 1);
      }
      else {
        target = Integer.valueOf(instructionNumber.intValue() + instruction.arg);
      }
      mapping.get(target)
             .add(instructionNumber);
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

  /** Get the input data for this solution. */
  private Map<Integer, Instruction> getInput(final PuzzleContext pc) {
    final List<Instruction> instructions = il.linesAsObjects(pc, Instruction::make);
    final Map<Integer, Instruction> map = new HashMap<>();
    for (int i = 0; i < instructions.size(); ++i) {
      map.put(Integer.valueOf(i), instructions.get(i));
    }
    return map;
  }

  /** One instruction corresponds with one line in the input file. */
  private static record Instruction(Operation op, int arg) {

    private static final Pattern SPLIT = Pattern.compile(" ");

    static Instruction make(final String input) {
      final String[] token = SPLIT.split(input);
      return new Instruction(Operation.valueOf(token[0]), Integer.parseInt(token[1]));
    }

  }

  /** Represents the operations supported. */
  private static enum Operation {
    acc,
    jmp,
    nop;
  }

}
