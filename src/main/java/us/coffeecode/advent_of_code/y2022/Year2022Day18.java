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

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point3D;

@AdventOfCodeSolution(year = 2022, day = 18, title = "Boiling Boulders")
@Component
public class Year2022Day18 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculateSurfaceArea(il.linesAsObjects(pc, Point3D::valueOf));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Collection<Point3D> points = il.linesAsObjects(pc, Point3D::valueOf);

    // Flood-fill the outside.
    final Boundaries b = getBoundaries(points);
    final Collection<Point3D> filled = floodFill(b, points);

    // Invert the filled volume to get the interior points.
    final Set<Point3D> interior = new HashSet<>();
    for (int x = b.minX; x < b.maxX; ++x) {
      for (int y = b.minY; y < b.maxY; ++y) {
        for (int z = b.minZ; z < b.maxZ; ++z) {
          final Point3D p = new Point3D(x, y, z);
          if (!filled.contains(p)) {
            interior.add(p);
          }
        }
      }
    }

    // Answer is the difference between the total surface area and the interior surface area.
    return calculateSurfaceArea(points) - calculateSurfaceArea(interior);
  }

  private long calculateSurfaceArea(final Collection<Point3D> points) {
    long answer = 0;
    for (final Point3D point : points) {
      for (final Point3D neighbor : point.getNeighbors()) {
        if (!points.contains(neighbor)) {
          ++answer;
        }
      }
    }
    return answer;
  }

  private Set<Point3D> floodFill(final Boundaries b, final Collection<Point3D> points) {
    final Set<Point3D> filled = new HashSet<>(points);
    final Queue<Point3D> queue = new LinkedList<>();
    queue.add(new Point3D(b.minX, b.minY, b.minZ));
    while (!queue.isEmpty()) {
      final Point3D p = queue.remove();
      if (!filled.contains(p)) {
        filled.add(p);
        for (final Point3D neighbor : p.getNeighbors()) {
          if (!filled.contains(neighbor) && (b.minX <= neighbor.getX()) && (neighbor.getX() <= b.maxX)
            && (b.minY <= neighbor.getY()) && (neighbor.getY() <= b.maxY) && (b.minZ <= neighbor.getZ())
            && (neighbor.getZ() <= b.maxZ)) {
            queue.add(neighbor);
          }
        }
      }
    }
    return filled;
  }

  private Boundaries getBoundaries(final Collection<Point3D> points) {
    int minX = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    int minZ = Integer.MAX_VALUE;
    int maxZ = Integer.MIN_VALUE;
    for (final Point3D p : points) {
      minX = Math.min(minX, p.getX());
      maxX = Math.max(maxX, p.getX());
      minY = Math.min(minY, p.getY());
      maxY = Math.max(maxY, p.getY());
      minZ = Math.min(minZ, p.getZ());
      maxZ = Math.max(maxZ, p.getZ());
    }
    // Not technically correct, but this problem actually needs an extra unit on each side to ensure flood fill works.
    return new Boundaries(minX - 1, maxX + 1, minY - 1, maxY + 1, minZ - 1, maxZ + 1);
  }

  private record Boundaries(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {}
}
