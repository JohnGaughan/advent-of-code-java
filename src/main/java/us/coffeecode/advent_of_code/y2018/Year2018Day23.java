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
package us.coffeecode.advent_of_code.y2018;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point3D;

@AdventOfCodeSolution(year = 2018, day = 23, title = "Experimental Emergency Teleportation")
@Component
public final class Year2018Day23 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Collection<Nanobot> bots = il.linesAsObjects(pc, Nanobot::new);
    final Nanobot strongest = bots.stream().max((a, b) -> a.radius - b.radius).get();
    final Collection<?> inRange = bots.stream().filter(b -> strongest.isInRange(b)).toList();
    return inRange.size();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Collection<Nanobot> bots = il.linesAsObjects(pc, Nanobot::new);
    for (final Nanobot bot : bots) {
      bot.addNeighbors(bots);
    }
    final Set<Nanobot> clique = new HashSet<>();
    for (final Nanobot bot : bots) {
      boolean add = true;
      for (final Nanobot bot2 : clique) {
        if (!bot2.getNeighbors().contains(bot)) {
          add = false;
        }
      }
      if (add) {
        clique.add(bot);
      }
    }
    return clique.stream().max((a, b) -> a.distance() - b.distance()).get().distance();
  }

  private static final class Nanobot {

    private static final Pattern SPLIT = Pattern.compile(",");

    final Point3D location;

    final int radius;

    private final Set<Nanobot> neighbors = new HashSet<>(2048);

    private final int hashCode;

    private final String toString;

    Nanobot(final String input) {
      final int coordinateStart = input.indexOf('<') + 1;
      final int coordinateEnd = input.indexOf('>');
      final int radiusStart = input.indexOf('=', coordinateEnd) + 1;
      final String[] coordinates = SPLIT.split(input.substring(coordinateStart, coordinateEnd));
      location =
        new Point3D(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]));
      radius = Integer.parseInt(input.substring(radiusStart));
      hashCode = input.hashCode();
      toString = input;
    }

    void addNeighbors(final Collection<Nanobot> candidates) {
      for (final Nanobot candidate : candidates) {
        if (candidate != this && hasMutualPointInRange(candidate)) {
          neighbors.add(candidate);
        }
      }
    }

    int distance() {
      return location.getX() + location.getY() + location.getZ() - radius;
    }

    Set<Nanobot> getNeighbors() {
      return neighbors;
    }

    boolean hasMutualPointInRange(final Nanobot other) {
      final int distance = location.getManhattanDistance(other.location);
      return distance <= radius + other.radius;
    }

    boolean isInRange(final Nanobot other) {
      final int distance = location.getManhattanDistance(other.location);
      return distance <= radius;
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof Nanobot o) {
        return (radius == o.radius) && Objects.equals(location, o.location);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

    @Override
    public String toString() {
      return toString;
    }
  }

}
