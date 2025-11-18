/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2017;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MutablePoint3D;

@AdventOfCodeSolution(year = 2017, day = 20)
@Component
public final class Year2017Day20 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Map<Integer, Particle> particles = getInput(pc);
    int closest = 0;
    for (int iterations = 0; iterations < 322; ++iterations) {
      for (int i = 0; i < particles.size(); ++i) {
        final Particle particle = particles.get(Integer.valueOf(i));
        particle.update();
        if (particle.compareTo(particles.get(Integer.valueOf(closest))) < 0) {
          closest = i;
        }
      }
    }
    return closest;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<Integer, Particle> particles = getInput(pc);
    for (int iterations = 0; iterations < 39; ++iterations) {
      final Map<MutablePoint3D, Set<Integer>> collisions = new HashMap<>();
      // Update each particle and save its position in the map of collisions.
      for (final Map.Entry<Integer, Particle> entry : particles.entrySet()) {
        final Particle particle = entry.getValue();
        particle.update();
        if (!collisions.containsKey(particle.p)) {
          collisions.put(particle.p, new HashSet<>());
        }
        collisions.get(particle.p)
                  .add(entry.getKey());
      }
      // Check each location to see if two or more particles exist there. If so, remove them all.
      collisions.values()
                .stream()
                .filter(s -> s.size() > 1);
      for (final Collection<Integer> collision : collisions.values()) {
        if (collision.size() > 1) {
          for (final Integer key : collision) {
            particles.remove(key);
          }
        }
      }
    }
    return particles.size();
  }

  /** Get the input data for this solution. */
  private Map<Integer, Particle> getInput(final PuzzleContext pc) {
    final Map<Integer, Particle> particles = new HashMap<>();
    int id = 0;
    for (final String line : il.lines(pc)) {
      particles.put(Integer.valueOf(id), new Particle(line));
      ++id;
    }
    return particles;
  }

  private static final class Particle
  implements Comparable<Particle> {

    private static final Pattern SEPARATOR = Pattern.compile(",");

    final MutablePoint3D p;

    final MutablePoint3D v;

    final MutablePoint3D a;

    Particle(final String input) {
      int start = input.indexOf('<') + 1;
      int end = input.indexOf('>', start);
      p = new MutablePoint3D(Arrays.stream(SEPARATOR.split(input.substring(start, end)))
                                   .map(String::strip)
                                   .mapToInt(Integer::parseInt)
                                   .toArray());
      start = input.indexOf('<', end) + 1;
      end = input.indexOf('>', start);
      v = new MutablePoint3D(Arrays.stream(SEPARATOR.split(input.substring(start, end)))
                                   .map(String::strip)
                                   .mapToInt(Integer::parseInt)
                                   .toArray());
      start = input.indexOf('<', end) + 1;
      end = input.indexOf('>', start);
      a = new MutablePoint3D(Arrays.stream(SEPARATOR.split(input.substring(start, end)))
                                   .map(String::strip)
                                   .mapToInt(Integer::parseInt)
                                   .toArray());
    }

    void update() {
      v.add(a);
      p.add(v);
    }

    @Override
    public int compareTo(final Particle o) {
      return Integer.compare(p.getManhattanDistance(), o.p.getManhattanDistance());
    }

  }

}
