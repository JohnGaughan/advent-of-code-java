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

import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/20">Year 2017, day 20</a>. This problem asks us to perform vector
 * arithmetic. Given a collection of particles, calculate their positions based on an initial position, velocity, and
 * acceleration. Part one asks us to find the particle that accelerates away from the origin the slowest, being the
 * closest particle in the long term. Part two introduces collisions, and asks us how many particles are present after
 * all possible collisions occur.
 * </p>
 * <p>
 * The simulation is straightforward, except for knowing when to end it. Trial and error shows how many iterations it
 * takes until the answer stabilizes.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day20 {

  public long calculatePart1() {
    final Map<Integer, Particle> particles = getInput();
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

  public long calculatePart2() {
    final Map<Integer, Particle> particles = getInput();
    for (int iterations = 0; iterations < 39; ++iterations) {
      final Map<Vector, Set<Integer>> collisions = new HashMap<>();
      // Update each particle and save its position in the map of collisions.
      for (final Map.Entry<Integer, Particle> entry : particles.entrySet()) {
        final Particle particle = entry.getValue();
        particle.update();
        if (!collisions.containsKey(particle.p)) {
          collisions.put(particle.p, new HashSet<>());
        }
        collisions.get(particle.p).add(entry.getKey());
      }
      // Check each location to see if two or more particles exist there. If so, remove them all.
      for (final Set<Integer> collision : collisions.values()) {
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
  private Map<Integer, Particle> getInput() {
    try {
      final Map<Integer, Particle> particles = new HashMap<>();
      int id = 0;
      for (final String line : Files.readAllLines(Utils.getInput(2017, 20))) {
        particles.put(Integer.valueOf(id), new Particle(line));
        ++id;
      }
      return particles;
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Vector {

    int x;

    int y;

    int z;

    Vector(final int[] values) {
      x = values[0];
      y = values[1];
      z = values[2];
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (!(obj instanceof Vector)) {
        return false;
      }
      final Vector v = (Vector) obj;
      return x == v.x && y == v.y && z == v.z;
    }

    @Override
    public int hashCode() {
      return 9677 * (x + 2749 * (y + 2161 * z));
    }

  }

  private static final class Particle
  implements Comparable<Particle> {

    private static final Pattern SEPARATOR = Pattern.compile(",");

    final Vector p;

    final Vector v;

    final Vector a;

    int distance;

    Particle(final String input) {
      int start = input.indexOf('<') + 1;
      int end = input.indexOf('>', start);
      p = new Vector(Arrays.stream(SEPARATOR.split(input.substring(start, end))).mapToInt(Integer::parseInt).toArray());
      start = input.indexOf('<', end) + 1;
      end = input.indexOf('>', start);
      v = new Vector(Arrays.stream(SEPARATOR.split(input.substring(start, end))).mapToInt(Integer::parseInt).toArray());
      start = input.indexOf('<', end) + 1;
      end = input.indexOf('>', start);
      a = new Vector(Arrays.stream(SEPARATOR.split(input.substring(start, end))).mapToInt(Integer::parseInt).toArray());
      distance = getDistance();
    }

    void update() {
      v.x += a.x;
      v.y += a.y;
      v.z += a.z;
      p.x += v.x;
      p.y += v.y;
      p.z += v.z;
      distance = getDistance();
    }

    private int getDistance() {
      return Math.abs(p.x) + Math.abs(p.y) + Math.abs(p.z);
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(final Particle o) {
      return Integer.compare(distance, o.distance);
    }

  }

}
