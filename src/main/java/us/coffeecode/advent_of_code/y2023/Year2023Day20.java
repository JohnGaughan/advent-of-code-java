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
package us.coffeecode.advent_of_code.y2023;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MyLongMath;

@AdventOfCodeSolution(year = 2023, day = 20, title = "Pulse Propagation")
@Component
public class Year2023Day20 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Map<String, Module> modules = getInput(pc);
    final Queue<Action> actions = new LinkedList<>();
    long low = 0;
    long high = 0;
    for (int i = 0; i < 1_000; ++i) {
      actions.offer(new Action(BUTTON, modules.get(BROADCASTER_ID), Pulse.LOW));
      while (!actions.isEmpty()) {
        final Action action = actions.poll();
        if (action.pulse == Pulse.LOW) {
          ++low;
        }
        else {
          ++high;
        }
        switch (action.destination.type) {
          case ModuleType.OTHER:
            continue;
          case ModuleType.BROADCASTER:
            for (final String id : action.destination.downstream) {
              actions.offer(new Action(action.destination, modules.get(id), action.pulse));
            }
            break;
          case ModuleType.FLIP_FLOP:
            if (action.pulse == Pulse.LOW) {
              action.destination.on = !action.destination.on;
              for (final String id : action.destination.downstream) {
                actions.offer(new Action(action.destination, modules.get(id), action.destination.on ? Pulse.HIGH : Pulse.LOW));
              }
            }
            break;
          case ModuleType.CONJUNCTION:
            action.destination.upstream.put(action.source.id, action.pulse);
            Pulse pulse = Pulse.HIGH;
            if (action.destination.upstream.values().stream().allMatch(p -> p == Pulse.HIGH)) {
              pulse = Pulse.LOW;
            }
            for (final String id : action.destination.downstream) {
              actions.offer(new Action(action.destination, modules.get(id), pulse));
            }
            break;
        }
      }
    }
    return low * high;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<String, Module> modules = getInput(pc);
    // For each module directly downstream from the broadcaster, inspect the module chain to get its period. The real
    // inputs all have periods that are prime numbers, which means LCM is not necessary. I include it here in case
    // anyone runs it with custom data where that is not the case.
    return modules.get(BROADCASTER_ID).downstream.stream().mapToLong(m -> getValue(modules, m)).reduce(1, MyLongMath::lcm);
  }

  /**
   * Broadcaster's child flip flop modules are each the one's bit of a larger register. The register counts up until it
   * hits a target prime number, triggering another conjunction module to send a signal down the line eventually hitting
   * RX. Then the flip flops in this chain reset and start counting again. Given that pattern, we can decode the target
   * prime number by setting the bits equal to one where the flip flop has a conjunction downstream. Eventually, there
   * is no flip flop downstream and we reach the end of the register. More details in the README.md file.
   */
  private long getValue(final Map<String, Module> modules, final String firstId) {
    long result = 0;
    long bit = 1;
    Module current = modules.get(firstId);
    // Each flip flip module has one or two downstream modules. One other flip flop, and possibly one conjunction. If a
    // flip flop has a conjunction downstream, then it represents a one.
    while (current != null) {
      // Has a conjunction downstream: flip its bit.
      if (current.downstream.stream().map(s -> modules.get(s).type).anyMatch(t -> t == ModuleType.CONJUNCTION)) {
        result += bit;
      }
      // Replace the current module reference with the downstream flip flop, or null if this is the last in the chain.
      current =
        current.downstream.stream().map(s -> modules.get(s)).filter(m -> m.type == ModuleType.FLIP_FLOP).findFirst().orElseGet(
          () -> null);
      bit <<= 1;
    }
    return result;
  }

  /** Get the input as a map of module IDs mapped to the matching modules. */
  private Map<String, Module> getInput(final PuzzleContext pc) {
    final Map<String, Module> input = new HashMap<>(128);
    final Map<String, Set<String>> conjunctions = new HashMap<>();
    for (final String line : il.lines(pc)) {
      final String[] tokens = SPLIT1.split(line);
      final List<String> downstream = Arrays.stream(SPLIT2.split(tokens[1])).toList();
      final String id;
      final ModuleType type;
      if (tokens[0].codePointAt(0) == '%') {
        id = tokens[0].substring(1);
        type = ModuleType.FLIP_FLOP;
      }
      else if (tokens[0].codePointAt(0) == '&') {
        id = tokens[0].substring(1);
        type = ModuleType.CONJUNCTION;
        conjunctions.put(id, new HashSet<>());
      }
      else {
        id = tokens[0];
        type = ModuleType.BROADCASTER;
      }
      input.put(id, new Module(id, type, downstream));
    }
    input.put(BUTTON.id, BUTTON);
    input.put(OUTPUT.id, OUTPUT);
    input.put(RX.id, RX);
    // Wire up conjunction modules
    for (final var entry : input.entrySet()) {
      entry.getValue().downstream.stream().filter(id -> conjunctions.containsKey(id)).forEach(
        id -> conjunctions.get(id).add(entry.getKey()));
    }
    conjunctions.entrySet().stream().forEach(e -> {
      final Module conjunction = input.get(e.getKey());
      e.getValue().stream().forEach(up -> conjunction.upstream.put(up, Pulse.LOW));
    });
    return input;
  }

  /** Module ID for the broadcaster that is the root of the hierarchy. */
  private static final String BROADCASTER_ID = "broadcaster";

  /** Button module that initiates the broadcaster to do its work. This is not specified in input files. */
  private static final Module BUTTON = new Module("button", ModuleType.OTHER, List.of(BROADCASTER_ID));

  /** Dummy output module that is used in examples but not specified in any input file. */
  private static final Module OUTPUT = new Module("output", ModuleType.OTHER, List.of());

  /** RX register that receives signals but is not specified in any input file. */
  private static final Module RX = new Module("rx", ModuleType.OTHER, List.of());

  /** Separates a module ID from its downstream modules. */
  private static final Pattern SPLIT1 = Pattern.compile(" -> ");

  /** List separator between downstream modules. */
  private static final Pattern SPLIT2 = Pattern.compile(", ");

  /** One action between a source and destination module. */
  private record Action(Module source, Module destination, Pulse pulse) {}

  /** One pulse sent across the wire between modules, representing a one or zero bit. */
  private static enum Pulse {
    LOW,
    HIGH;
  }

  /** Type of a module which controls how it behaves when receiving input. */
  private static enum ModuleType {
    BROADCASTER,
    FLIP_FLOP,
    CONJUNCTION,
    OTHER;
  }

  /**
   * One module in the program input. This contains fields common to all module types rather than dirtying up the code
   * with unnecessary inheritance.
   */
  private static class Module {

    /** Identifier or name of this module. */
    final String id;

    /** The type of this module. */
    final ModuleType type;

    /** IDs of all downstream modules. */
    final List<String> downstream;

    /** Conjunction modules remember the previous input from each upstream module. */
    final Map<String, Pulse> upstream = new HashMap<>();

    /** Flip flop modules remember their current state. */
    boolean on = false;

    Module(final String _id, final ModuleType _type, final List<String> _downstream) {
      id = _id;
      type = _type;
      downstream = _downstream;
    }
  }
}
