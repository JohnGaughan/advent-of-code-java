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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 21)
@Component
public class Year2024Day21 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  /** Perform the problem's calculation for either part. */
  private long calculate(final PuzzleContext pc) {
    final int depth = pc.getInt("depth");
    final Map<CacheKey, Long> cache = new HashMap<>(1 << 9);
    return il.linesAsObjects(pc, this::parse)
             .stream()
             .mapToLong(o -> score(o, depth, cache))
             .sum();
  }

  /** Get the score for a line of input. */
  private long score(final InputRecord record, final int depth, final Map<CacheKey, Long> cache) {
    return record.score * getShortestNumpadSequence(record.buttons, depth, cache);
  }

  /** Get the shortest button sequence for number pad entry. */
  private long getShortestNumpadSequence(final String buttons, final int depth, final Map<CacheKey, Long> cache) {
    long result = 0;
    for (int i = 0; i < buttons.length(); ++i) {
      final String numpadKey = (i == 0) ? "A" + buttons.substring(i, i + 1) : buttons.substring(i - 1, i + 1);
      long subResult = Long.MAX_VALUE;
      for (final String nextButtons : NUMBER_PAD_MAPPING.get(numpadKey)) {
        long candidate = getShortestDirpadSequence(nextButtons, depth, cache);
        subResult = Math.min(candidate, subResult);
      }
      result += subResult;
    }
    return result;
  }

  /** Get the shortest button sequence for direction pad entry. */
  private long getShortestDirpadSequence(final String buttons, final int depth, final Map<CacheKey, Long> cache) {
    if (depth == 0) {
      return buttons.length();
    }
    final CacheKey cacheKey = new CacheKey(buttons, depth);
    if (cache.containsKey(cacheKey)) {
      return cache.get(cacheKey)
                  .longValue();
    }
    long result = 0;
    for (int i = 0; i < buttons.length(); ++i) {
      final String dirpadKey = (i == 0) ? "A" + buttons.substring(i, i + 1) : buttons.substring(i - 1, i + 1);
      long subResult = Long.MAX_VALUE;
      for (final String nextButtons : DIRECTION_PAD_MAPPING.get(dirpadKey)) {
        long candidate = getShortestDirpadSequence(nextButtons, depth - 1, cache);
        subResult = Math.min(candidate, subResult);
      }
      result += subResult;
    }
    cache.put(cacheKey, Long.valueOf(result));
    return result;
  }

  /** Parse a row of input into a record. */
  private InputRecord parse(final String line) {
    return new InputRecord(line, Long.parseLong(line.substring(0, line.length() - 1)));
  }

  /**
   * Used to store program state in the cache based on which buttons are being pressed and the depth in the stack of
   * direction pad presses.
   */
  private record CacheKey(String buttons, int depth) {}

  /** Represents one record of input, that is, one line in the input file. */
  private record InputRecord(String buttons, long score) {}

  /** Maps start and end buttons on the number pad to all paths between them that can be the shortest. */
  private static final Map<String, List<String>> NUMBER_PAD_MAPPING = new HashMap<>();

  /** Maps start and end buttons on the direction pad to all paths between them that can be the shortest. */
  private static final Map<String, List<String>> DIRECTION_PAD_MAPPING = new HashMap<>();

  static {
    // Cache all of the shortest mappings ahead of time because they never change regardless of input.
    NUMBER_PAD_MAPPING.put("AA", List.of("A"));
    NUMBER_PAD_MAPPING.put("A0", List.of("<A"));
    NUMBER_PAD_MAPPING.put("A1", List.of("^<<A"));
    NUMBER_PAD_MAPPING.put("A2", List.of("^<A", "<^A"));
    NUMBER_PAD_MAPPING.put("A3", List.of("^A"));
    NUMBER_PAD_MAPPING.put("A4", List.of("^^<<A"));
    NUMBER_PAD_MAPPING.put("A5", List.of("<^^A", "^^<A"));
    NUMBER_PAD_MAPPING.put("A6", List.of("^^A"));
    NUMBER_PAD_MAPPING.put("A7", List.of("^^^<<A"));
    NUMBER_PAD_MAPPING.put("A8", List.of("^^^<A", "<^^^A"));
    NUMBER_PAD_MAPPING.put("A9", List.of("^^^A"));

    NUMBER_PAD_MAPPING.put("0A", List.of(">A"));
    NUMBER_PAD_MAPPING.put("00", List.of("A"));
    NUMBER_PAD_MAPPING.put("01", List.of("^<A"));
    NUMBER_PAD_MAPPING.put("02", List.of("^A"));
    NUMBER_PAD_MAPPING.put("03", List.of("^>A", ">^A"));
    NUMBER_PAD_MAPPING.put("04", List.of("^^<A"));
    NUMBER_PAD_MAPPING.put("05", List.of("^^A"));
    NUMBER_PAD_MAPPING.put("06", List.of("^^>A", ">^^A"));
    NUMBER_PAD_MAPPING.put("07", List.of("^^^<A"));
    NUMBER_PAD_MAPPING.put("08", List.of("^^^A"));
    NUMBER_PAD_MAPPING.put("09", List.of("^^^>A", ">^^^A"));

    NUMBER_PAD_MAPPING.put("1A", List.of(">>vA"));
    NUMBER_PAD_MAPPING.put("10", List.of(">vA"));
    NUMBER_PAD_MAPPING.put("11", List.of("A"));
    NUMBER_PAD_MAPPING.put("12", List.of(">A"));
    NUMBER_PAD_MAPPING.put("13", List.of(">>A"));
    NUMBER_PAD_MAPPING.put("14", List.of("^A"));
    NUMBER_PAD_MAPPING.put("15", List.of("^>A", "<^A"));
    NUMBER_PAD_MAPPING.put("16", List.of(">>^A", "^>>A"));
    NUMBER_PAD_MAPPING.put("17", List.of("^^A"));
    NUMBER_PAD_MAPPING.put("18", List.of("^^>A", ">^^A"));
    NUMBER_PAD_MAPPING.put("19", List.of("^^>>A", ">>^^A"));

    NUMBER_PAD_MAPPING.put("2A", List.of(">vA", "v>A"));
    NUMBER_PAD_MAPPING.put("20", List.of("vA"));
    NUMBER_PAD_MAPPING.put("21", List.of("<A"));
    NUMBER_PAD_MAPPING.put("22", List.of("A"));
    NUMBER_PAD_MAPPING.put("23", List.of(">A"));
    NUMBER_PAD_MAPPING.put("24", List.of("<^A", "^<A"));
    NUMBER_PAD_MAPPING.put("25", List.of("^A"));
    NUMBER_PAD_MAPPING.put("26", List.of("^>A", ">^A"));
    NUMBER_PAD_MAPPING.put("27", List.of("^^<A", "<^^A"));
    NUMBER_PAD_MAPPING.put("28", List.of("^^A"));
    NUMBER_PAD_MAPPING.put("29", List.of("^^>A", ">^^A"));

    NUMBER_PAD_MAPPING.put("3A", List.of("vA"));
    NUMBER_PAD_MAPPING.put("30", List.of("v<A", "<vA"));
    NUMBER_PAD_MAPPING.put("31", List.of("<<A"));
    NUMBER_PAD_MAPPING.put("32", List.of("<A"));
    NUMBER_PAD_MAPPING.put("33", List.of("A"));
    NUMBER_PAD_MAPPING.put("34", List.of("<<^A", "^<<A"));
    NUMBER_PAD_MAPPING.put("35", List.of("<^A", "^<A"));
    NUMBER_PAD_MAPPING.put("36", List.of("^A"));
    NUMBER_PAD_MAPPING.put("37", List.of("^^<<A", "<<^^A"));
    NUMBER_PAD_MAPPING.put("38", List.of("^^<A", "<^^A"));
    NUMBER_PAD_MAPPING.put("39", List.of("^^A"));

    NUMBER_PAD_MAPPING.put("4A", List.of(">>vvA"));
    NUMBER_PAD_MAPPING.put("40", List.of(">vvA"));
    NUMBER_PAD_MAPPING.put("41", List.of("vA"));
    NUMBER_PAD_MAPPING.put("42", List.of("v>A", ">vA"));
    NUMBER_PAD_MAPPING.put("43", List.of(">>vA", "v>>A"));
    NUMBER_PAD_MAPPING.put("44", List.of("A"));
    NUMBER_PAD_MAPPING.put("45", List.of(">A"));
    NUMBER_PAD_MAPPING.put("46", List.of(">>A"));
    NUMBER_PAD_MAPPING.put("47", List.of("^A"));
    NUMBER_PAD_MAPPING.put("48", List.of("^>A", ">^A"));
    NUMBER_PAD_MAPPING.put("49", List.of("^>>A", ">>^A"));

    NUMBER_PAD_MAPPING.put("5A", List.of("vv>A", ">vvA"));
    NUMBER_PAD_MAPPING.put("50", List.of("vvA"));
    NUMBER_PAD_MAPPING.put("51", List.of("v<A", "<vA"));
    NUMBER_PAD_MAPPING.put("52", List.of("vA"));
    NUMBER_PAD_MAPPING.put("53", List.of("v>A", ">vA"));
    NUMBER_PAD_MAPPING.put("54", List.of("<A"));
    NUMBER_PAD_MAPPING.put("55", List.of("A"));
    NUMBER_PAD_MAPPING.put("56", List.of(">A"));
    NUMBER_PAD_MAPPING.put("57", List.of("^<A", "<^A"));
    NUMBER_PAD_MAPPING.put("58", List.of("^A"));
    NUMBER_PAD_MAPPING.put("59", List.of("^>A", ">^A"));

    NUMBER_PAD_MAPPING.put("6A", List.of("vvA"));
    NUMBER_PAD_MAPPING.put("60", List.of("vv<A", "<vvA"));
    NUMBER_PAD_MAPPING.put("61", List.of("<<vA", "v<<A"));
    NUMBER_PAD_MAPPING.put("62", List.of("<vA", "v<A"));
    NUMBER_PAD_MAPPING.put("63", List.of("vA"));
    NUMBER_PAD_MAPPING.put("64", List.of("<<A"));
    NUMBER_PAD_MAPPING.put("65", List.of("<A"));
    NUMBER_PAD_MAPPING.put("66", List.of("A"));
    NUMBER_PAD_MAPPING.put("67", List.of("<<^A", "^<<A"));
    NUMBER_PAD_MAPPING.put("68", List.of("<^A", "^<A"));
    NUMBER_PAD_MAPPING.put("69", List.of("^A"));

    NUMBER_PAD_MAPPING.put("7A", List.of(">>vvvA"));
    NUMBER_PAD_MAPPING.put("70", List.of(">vvvA"));
    NUMBER_PAD_MAPPING.put("71", List.of("vvA"));
    NUMBER_PAD_MAPPING.put("72", List.of("vv>A", ">vvA"));
    NUMBER_PAD_MAPPING.put("73", List.of("vv>>A", ">>vvA"));
    NUMBER_PAD_MAPPING.put("74", List.of("vA"));
    NUMBER_PAD_MAPPING.put("75", List.of("v>A", ">vA"));
    NUMBER_PAD_MAPPING.put("76", List.of("v>>A", ">>vA"));
    NUMBER_PAD_MAPPING.put("77", List.of("A"));
    NUMBER_PAD_MAPPING.put("78", List.of(">A"));
    NUMBER_PAD_MAPPING.put("79", List.of(">>A"));

    NUMBER_PAD_MAPPING.put("8A", List.of("vvv>A", ">vvvA"));
    NUMBER_PAD_MAPPING.put("80", List.of("vvvA"));
    NUMBER_PAD_MAPPING.put("81", List.of("vv<A", "<vvA"));
    NUMBER_PAD_MAPPING.put("82", List.of("vvA"));
    NUMBER_PAD_MAPPING.put("83", List.of("vv>A", ">vvA"));
    NUMBER_PAD_MAPPING.put("84", List.of("v<A", "<vA"));
    NUMBER_PAD_MAPPING.put("85", List.of("vA"));
    NUMBER_PAD_MAPPING.put("86", List.of("v>A", ">vA"));
    NUMBER_PAD_MAPPING.put("87", List.of("<A"));
    NUMBER_PAD_MAPPING.put("88", List.of("A"));
    NUMBER_PAD_MAPPING.put("89", List.of(">A"));

    NUMBER_PAD_MAPPING.put("9A", List.of("vvvA"));
    NUMBER_PAD_MAPPING.put("90", List.of("vvv<A", "<vvvA"));
    NUMBER_PAD_MAPPING.put("91", List.of("<<vvA", "vv<<A"));
    NUMBER_PAD_MAPPING.put("92", List.of("<vvA", "vv<A"));
    NUMBER_PAD_MAPPING.put("93", List.of("vvA"));
    NUMBER_PAD_MAPPING.put("94", List.of("<<vA", "v<<A"));
    NUMBER_PAD_MAPPING.put("95", List.of("<vA", "v<A"));
    NUMBER_PAD_MAPPING.put("96", List.of("vA"));
    NUMBER_PAD_MAPPING.put("97", List.of("<<A"));
    NUMBER_PAD_MAPPING.put("98", List.of("<A"));
    NUMBER_PAD_MAPPING.put("99", List.of("A"));

    DIRECTION_PAD_MAPPING.put("^^", List.of("A"));
    DIRECTION_PAD_MAPPING.put("^A", List.of(">A"));
    DIRECTION_PAD_MAPPING.put("^<", List.of("v<A"));
    DIRECTION_PAD_MAPPING.put("^v", List.of("vA"));
    DIRECTION_PAD_MAPPING.put("^>", List.of("v>A", ">vA"));

    DIRECTION_PAD_MAPPING.put("A^", List.of("<A"));
    DIRECTION_PAD_MAPPING.put("AA", List.of("A"));
    DIRECTION_PAD_MAPPING.put("A<", List.of("v<<A"));
    DIRECTION_PAD_MAPPING.put("Av", List.of("v<A", "<vA"));
    DIRECTION_PAD_MAPPING.put("A>", List.of("vA"));

    DIRECTION_PAD_MAPPING.put("<^", List.of(">^A"));
    DIRECTION_PAD_MAPPING.put("<A", List.of(">>^A"));
    DIRECTION_PAD_MAPPING.put("<<", List.of("A"));
    DIRECTION_PAD_MAPPING.put("<v", List.of(">A"));
    DIRECTION_PAD_MAPPING.put("<>", List.of(">>A"));

    DIRECTION_PAD_MAPPING.put("v^", List.of("^A"));
    DIRECTION_PAD_MAPPING.put("vA", List.of("^>A", ">^A"));
    DIRECTION_PAD_MAPPING.put("v<", List.of("<A"));
    DIRECTION_PAD_MAPPING.put("vv", List.of("A"));
    DIRECTION_PAD_MAPPING.put("v>", List.of(">A"));

    DIRECTION_PAD_MAPPING.put(">^", List.of("^<A", "<^A"));
    DIRECTION_PAD_MAPPING.put(">A", List.of("^A"));
    DIRECTION_PAD_MAPPING.put("><", List.of("<<A"));
    DIRECTION_PAD_MAPPING.put(">v", List.of("<A"));
    DIRECTION_PAD_MAPPING.put(">>", List.of("A"));
  }
}
