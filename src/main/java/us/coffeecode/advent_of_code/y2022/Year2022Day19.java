/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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
package us.coffeecode.advent_of_code.y2022;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2022, day = 19, title = "Not Enough Minerals")
@Component
public class Year2022Day19 {

  /*
   * Note: this code assumes the input is formatted like the "real" input. The problem splits the example input across
   * lines on the web page. If you want to run this against the example input, you must first put each blueprint on a
   * single line with no gaps between blueprints. Otherwise, input will fail to parse and the unit tests will throw an
   * exception. In other words, make sure the example input is formatted like your real input before running it.
   */

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Collection<Blueprint> blueprints = il.linesAsObjects(pc, Blueprint::valueOf);
    return blueprints.parallelStream()
                     .mapToLong(bp -> bp.id * score(bp, 24))
                     .sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    List<Blueprint> blueprints = il.linesAsObjects(pc, Blueprint::valueOf);
    // This prevents the example input from throwing an exception.
    if (blueprints.size() > 3) {
      blueprints = blueprints.subList(0, 3);
    }
    return blueprints.parallelStream()
                     .mapToLong(bp -> score(bp, 32))
                     .reduce(1, (a, b) -> a * b);
  }

  private long score(final Blueprint bp, final int timeLimit) {
    final Queue<State> queue = new LinkedList<>();
    final Set<State> seen = new HashSet<>((timeLimit < 30) ? (1 << 15) : (1 << 19));
    queue.add(START);
    seen.add(START);
    long bestScore = 0;

    while (!queue.isEmpty()) {
      final State current = queue.remove();

      // Skip this state if it is mathematically impossible for it to beat the best score so far.
      final int timeRemaining = timeLimit - current.time;
      final int maxPotential = current.score + (timeRemaining - 1) * (timeRemaining >> 1);
      if (maxPotential <= bestScore) {
        continue;
      }

      // Try to build each type of robot and branch out assuming we build it.
      for (int newRobot = ORE; newRobot < GEODE; ++newRobot) {
        final int timeLeft = timeLimit - current.time;
        final int curRbt = current.robots[newRobot];
        final int curRes = current.resources[newRobot];
        final int maxRes = IntStream
                                    .of(bp.resourceCosts[ORE][newRobot], bp.resourceCosts[CLAY][newRobot],
                                      bp.resourceCosts[OBSIDIAN][newRobot], bp.resourceCosts[GEODE][newRobot])
                                    .max()
                                    .getAsInt();
        if ((curRbt * timeLeft + curRes < timeLeft * maxRes) && bp.shouldBuild(newRobot, current.robots)
          && bp.hasPrerequisites(newRobot, current.robots)) {
          final int[] resources = Arrays.copyOf(current.resources, current.resources.length);
          int turns = 1;
          while (!bp.canAfford(newRobot, resources)) {
            for (int resType = ORE; resType < GEODE; ++resType) {
              resources[resType] += current.robots[resType];
            }
            ++turns;
          }
          if (current.time + turns < timeLimit) {
            for (int resType = ORE; resType < GEODE; ++resType) {
              resources[resType] += current.robots[resType];
            }
            bp.build(newRobot, resources);
            final int[] robots = Arrays.copyOf(current.robots, current.robots.length);
            ++robots[newRobot];
            final State next = new State(current.time + turns, robots, resources, current.score);
            if (!seen.contains(next)) {
              queue.add(next);
              seen.add(next);
            }
          }
        }
      }

      // Build a geode robot.
      if (bp.hasPrerequisites(GEODE, current.robots)) {
        final int[] resources = Arrays.copyOf(current.resources, current.resources.length);
        int turns = 1;
        while (!bp.canAfford(GEODE, resources)) {
          for (int resType = ORE; resType < GEODE; ++resType) {
            resources[resType] += current.robots[resType];
          }
          ++turns;
        }
        if (current.time + turns <= timeLimit) {
          for (int resType = ORE; resType < GEODE; ++resType) {
            resources[resType] += current.robots[resType];
          }
          bp.build(GEODE, resources);
          final int newTime = current.time + turns;
          final int newGeodes = current.score + timeLimit - newTime;
          bestScore = Math.max(bestScore, newGeodes);
          final State next = new State(newTime, current.robots, resources, newGeodes);
          if (!seen.contains(next)) {
            queue.add(next);
            seen.add(next);
          }
        }
      }
    }
    return bestScore;
  }

  /**
   * One puzzle state. Exposes guts, including mutable arrays, for speed. That's ok though because solution code does
   * not modify it.
   */
  private static final class State {

    final int time;

    final int[] robots;

    final int[] resources;

    final int score;

    final Integer hashCode;

    State(final int _time, final int[] _robots, final int[] _resources, final int _score) {
      time = _time;
      robots = _robots;
      resources = _resources;
      score = _score;
      // Objects.hash methods generate too many collisions for this limited data set. Using several large primes spreads
      // the bits out more evenly across the whole 32 bit range.
      int hash = 15508331 * time;
      hash = 19717813 * hash + score;
      hash = 29651693 * hash + Arrays.hashCode(robots);
      hash = 15488723 * hash + Arrays.hashCode(resources);
      hashCode = Integer.valueOf(hash);
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof State o) {
        return (time == o.time) && (score == o.score) && Arrays.equals(robots, o.robots) && Arrays.equals(resources, o.resources);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return hashCode.intValue();
    }
  }

  private static final class Blueprint {

    private static final Pattern SPLIT = Pattern.compile(" ");

    static Blueprint valueOf(final String line) {
      final String[] tokens = SPLIT.split(line);
      final int id = Integer.parseInt(tokens[1].substring(0, tokens[1].length() - 1));
      final int[][] resourceCosts = new int[4][3];
      resourceCosts[ORE][ORE] = Integer.parseInt(tokens[6]);
      resourceCosts[CLAY][ORE] = Integer.parseInt(tokens[12]);
      resourceCosts[OBSIDIAN][ORE] = Integer.parseInt(tokens[18]);
      resourceCosts[OBSIDIAN][CLAY] = Integer.parseInt(tokens[21]);
      resourceCosts[GEODE][ORE] = Integer.parseInt(tokens[27]);
      resourceCosts[GEODE][OBSIDIAN] = Integer.parseInt(tokens[30]);
      return new Blueprint(id, resourceCosts);
    }

    final int id;

    /** First dimension is the robot type, second dimension is the resource to spend on it. */
    final int[][] resourceCosts;

    /** Maximum number of each robot type potentially worth building. */
    final int[] maxRobots;

    private Blueprint(final int _id, final int[][] rc) {
      id = _id;
      resourceCosts = rc;
      maxRobots = new int[3];
      for (int i = 0; i < resourceCosts.length; ++i) {
        for (int j = 0; j < resourceCosts[i].length; ++j) {
          // Don't build more ore robots only so we can build more... ore robots.
          if ((i != ORE) || (j != ORE)) {
            maxRobots[j] = Math.max(maxRobots[j], resourceCosts[i][j]);
          }
        }
      }
    }

    boolean hasPrerequisites(final int robotType, final int[] robots) {
      for (int i = ORE; i < GEODE; ++i) {
        // If this robot costs resources but nothing is harvesting it, it is impossible to build.
        if ((resourceCosts[robotType][i] > 0) && (robots[i] == 0)) {
          return false;
        }
      }
      return true;
    }

    boolean shouldBuild(final int robotType, final int[] robots) {
      return robots[robotType] < maxRobots[robotType];
    }

    /** Determine if we can afford to purchase the robot type. */
    boolean canAfford(final int robotType, final int[] resources) {
      for (int i = 0; i < resourceCosts[robotType].length; ++i) {
        if (resources[i] < resourceCosts[robotType][i]) {
          return false;
        }
      }
      return true;
    }

    /** Deduct the cost of a robot from the resource array. */
    void build(final int robotType, final int[] resources) {
      for (int i = 0; i < resourceCosts[robotType].length; ++i) {
        resources[i] -= resourceCosts[robotType][i];
      }
    }
  }

  private static final int ORE = 0;

  private static final int CLAY = 1;

  private static final int OBSIDIAN = 2;

  private static final int GEODE = 3;

  private static final State START = new State(1, new int[] { 1, 0, 0 }, new int[] { 1, 0, 0 }, 0);
}
