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
package us.coffeecode.advent_of_code.y2021;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point3D;

@AdventOfCodeSolution(year = 2021, day = 19, title = "Beacon Scanner")
@Component
public final class Year2021Day19 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc).beacons;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc).distance;
  }

  /** Do the calculations to get the answers to the puzzle. */
  private Answer calculate(final PuzzleContext pc) {
    final Map<UnmatchedScanner, Collection<UnmatchedScanner>> mapping = getScannerMapping(pc);
    final Collection<Long> distances = new HashSet<>(1 << 10);
    final Map<UnmatchedScanner, Scanner> processed = new HashMap<>(mapping.size() << 1);

    // Add the first scanner.
    {
      final UnmatchedScanner first = mapping.keySet().iterator().next();
      processed.put(first, new Scanner(Point3D.ORIGIN, first.beacons));
      mapping.remove(first);
    }

    // Remove items from mapping after processing. This means the keys are always unprocessed, and the values contain
    // scanners that may or may not already be processed.
    while (!mapping.isEmpty()) {
      // Track mapping removals outside of the map's loop to avoid ConcurrentModificationException.
      final Collection<UnmatchedScanner> remove = new HashSet<>();
      // Get the first scanner that maps to a processed scanner.
      for (final var entry : mapping.entrySet()) {
        for (final UnmatchedScanner us : entry.getValue()) {
          if (processed.containsKey(us)) {
            // Mapping's key is not processed, and overlaps with processed's key.
            final UnmatchedScanner unmatched = entry.getKey();
            final Scanner matched = processed.get(us);
            final Scanner newMatch = match(matched, unmatched);
            processed.put(unmatched, newMatch);
            // Add distances to everything in processed before marking this as processed.
            for (final Scanner s : processed.values()) {
              distances.add(Long.valueOf(s.location.getManhattanDistance(newMatch.location)));
            }
            remove.add(unmatched);
          }
        }
      }
      remove.stream().forEach(mapping::remove);
    }

    // All scanner and beacon locations are now known. Extract the answers for both parts.
    final Collection<Point3D> beacons = new HashSet<>(1 << 10);
    processed.values().stream().map(Scanner::beacons).forEach(beacons::addAll);

    return new Answer(beacons.size(), distances.stream().mapToLong(Long::longValue).max().getAsLong());
  }

  /** Get a mapping of scanners to other scanners that overlap it. */
  private Map<UnmatchedScanner, Collection<UnmatchedScanner>> getScannerMapping(final PuzzleContext pc) {
    final SequencedCollection<UnmatchedScanner> unprocessed = new ArrayList<>(il.groupsAsObjects(pc, this::parse));
    final Collection<UnmatchedScanner> processed = new ArrayList<>(unprocessed.size());
    final Map<UnmatchedScanner, Collection<UnmatchedScanner>> mapping = new HashMap<>(64);

    // First scanner is rooted at origin and we orient everything else to it.
    processed.add(unprocessed.removeFirst());

    // Match up scanners to each other. Orientation and location will not be known yet.
    while (!unprocessed.isEmpty()) {
      final var iter = unprocessed.iterator();
      while (iter.hasNext()) {
        final UnmatchedScanner testing = iter.next();
        boolean update = false;
        for (final UnmatchedScanner known : processed) {
          final Set<BigDecimal> distances = new HashSet<>(testing.distances);
          distances.retainAll(known.distances);
          if (distances.size() >= 66) {
            mapping.putIfAbsent(known, new ArrayList<>());
            mapping.get(known).add(testing);
            mapping.putIfAbsent(testing, new ArrayList<>());
            mapping.get(testing).add(known);
            iter.remove();
            update = true;
            break;
          }
        }
        if (update) {
          processed.add(testing);
        }
      }
    }
    return mapping;
  }

  /** Try to match the given points against what is known, returning the scanner if possible. */
  private Scanner match(final Scanner matched, final UnmatchedScanner unmatched) {
    // Apply each transformation to the input points and see if they can be shifted to match the known points.
    for (final Function<Point3D, Point3D> transformation : TRANSFORMATIONS) {
      final Collection<Point3D> transformed = unmatched.beacons.stream().map(transformation).toList();
      for (final Point3D t1 : transformed) {
        final Collection<Point3D> checked = new HashSet<>();
        for (final Point3D r1 : matched.beacons) {
          final Point3D shift = r1.subtract(t1);
          if (checked.contains(shift)) {
            continue;
          }
          checked.add(shift);
          final Collection<Point3D> shifted = transformed.stream().map(p -> p.add(shift)).toList();
          int matches = 0;
          for (final Point3D p : shifted) {
            if (matched.beacons.contains(p)) {
              ++matches;
            }
            if (matches == 12) {
              return new Scanner(shift.invert(), shifted);
            }
          }
        }
      }
    }
    throw new IllegalStateException("No match found, but one was detected prior to calling this method");
  }

  /** Contains the number of beacons and the maximum distance between any two scanners. */
  private record Answer(long beacons, long distance) {}

  /** Contains the correct locations of one scanner and its beacons. */
  private record Scanner(Point3D location, Collection<Point3D> beacons) {}

  /** One scanner in the input with unknown orientation and location. */
  private record UnmatchedScanner(Collection<Point3D> beacons, Collection<BigDecimal> distances) {}

  private UnmatchedScanner parse(final List<String> lines) {
    final List<Point3D> points = new ArrayList<>(lines.size() - 1);
    lines.stream().skip(1).forEach(line -> {
      final int c1 = line.indexOf(',');
      final int c2 = line.indexOf(',', c1 + 1);
      final int x = Integer.parseInt(line.substring(0, c1));
      final int y = Integer.parseInt(line.substring(c1 + 1, c2));
      final int z = Integer.parseInt(line.substring(c2 + 1));
      points.add(new Point3D(x, y, z));
    });
    final Collection<BigDecimal> distances = new HashSet<>(1 << 9);
    for (int i = 0; i < points.size() - 1; ++i) {
      for (int j = i + 1; j < points.size(); ++j) {
        final Point3D p1 = points.get(i);
        final Point3D p2 = points.get(j);
        final int x = (p2.getX() - p1.getX()) * (p2.getX() - p1.getX());
        final int y = (p2.getY() - p1.getY()) * (p2.getY() - p1.getY());
        final int z = (p2.getZ() - p1.getZ()) * (p2.getZ() - p1.getZ());
        final BigDecimal d = BigDecimal.valueOf(x + y + z).sqrt(MathContext.DECIMAL64);
        distances.add(d.setScale(2, RoundingMode.HALF_DOWN));
      }
    }
    return new UnmatchedScanner(points, distances);
  }

  private static final Collection<Function<Point3D, Point3D>> TRANSFORMATIONS;

  static {
    TRANSFORMATIONS = new ArrayList<>(24);
    TRANSFORMATIONS.add(p -> p);
    TRANSFORMATIONS.add(p -> new Point3D(p.getX(), -p.getZ(), p.getY()));
    TRANSFORMATIONS.add(p -> new Point3D(p.getX(), -p.getY(), -p.getZ()));
    TRANSFORMATIONS.add(p -> new Point3D(p.getX(), p.getZ(), -p.getY()));
    TRANSFORMATIONS.add(p -> new Point3D(-p.getY(), p.getX(), p.getZ()));
    TRANSFORMATIONS.add(p -> new Point3D(p.getZ(), p.getX(), p.getY()));
    TRANSFORMATIONS.add(p -> new Point3D(p.getY(), p.getX(), -p.getZ()));
    TRANSFORMATIONS.add(p -> new Point3D(-p.getZ(), p.getX(), -p.getY()));
    TRANSFORMATIONS.add(p -> new Point3D(-p.getX(), -p.getY(), p.getZ()));
    TRANSFORMATIONS.add(p -> new Point3D(-p.getX(), -p.getZ(), -p.getY()));
    TRANSFORMATIONS.add(p -> new Point3D(-p.getX(), p.getY(), -p.getZ()));
    TRANSFORMATIONS.add(p -> new Point3D(-p.getX(), p.getZ(), p.getY()));
    TRANSFORMATIONS.add(p -> new Point3D(p.getY(), -p.getX(), p.getZ()));
    TRANSFORMATIONS.add(p -> new Point3D(p.getZ(), -p.getX(), -p.getY()));
    TRANSFORMATIONS.add(p -> new Point3D(-p.getY(), -p.getX(), -p.getZ()));
    TRANSFORMATIONS.add(p -> new Point3D(-p.getZ(), -p.getX(), p.getY()));
    TRANSFORMATIONS.add(p -> new Point3D(-p.getZ(), p.getY(), p.getX()));
    TRANSFORMATIONS.add(p -> new Point3D(p.getY(), p.getZ(), p.getX()));
    TRANSFORMATIONS.add(p -> new Point3D(p.getZ(), -p.getY(), p.getX()));
    TRANSFORMATIONS.add(p -> new Point3D(-p.getY(), -p.getZ(), p.getX()));
    TRANSFORMATIONS.add(p -> new Point3D(-p.getZ(), -p.getY(), -p.getX()));
    TRANSFORMATIONS.add(p -> new Point3D(-p.getY(), p.getZ(), -p.getX()));
    TRANSFORMATIONS.add(p -> new Point3D(p.getZ(), p.getY(), -p.getX()));
    TRANSFORMATIONS.add(p -> new Point3D(p.getY(), -p.getZ(), -p.getX()));
  }

}
