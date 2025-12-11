/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2025  John Gaughan
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
package us.coffeecode.advent_of_code.y2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MyArrays;
import us.coffeecode.advent_of_code.util.MyCollections;
import us.coffeecode.advent_of_code.util.Point3D;

@AdventOfCodeSolution(year = 2025, day = 8)
@Component
public class Year2025Day08 {

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

  /**
   * Perform the calculation, relying on puzzle parameters to define the edge limit (if any). It returns values for
   * parts one and two dynamically: if it ends up with a single graph then it must be running part two because there are
   * nowhere near enough edges added to get one graph in part one. If it does not end up with a single graph then it
   * must be running part one because due to how edges are created the graph must be fully connected.
   */
  private long calculate(final PuzzleContext pc) {
    // Start by getting all vertices and all potential edges between them.
    final List<Point3D> vertices = il.linesAsObjects(pc, Point3D::valueOf);
    final List<Edge> edges = getSortedEdges(vertices);

    /*
     * Add each vertex to its own graph. Record the connection in two maps to make it easy and efficient to look up and
     * merge graphs in the next step. We don't need the complexity of using a real graph object since we never need to
     * traverse the graph. All we need to track is which vertices belong to which graph. From that we can get the graph
     * sizes for part one, or the count of graphs for part two with trivial effort.
     */
    final Map<Integer, Set<Point3D>> graphs = new HashMap<>();
    final Map<Point3D, Integer> graphIds = new HashMap<>();
    {
      int i = 0;
      for (final Point3D vertex : vertices) {
        final Integer id = Integer.valueOf(i);
        graphs.put(id, MyCollections.mutableSetOf(vertex));
        graphIds.put(vertex, id);
        ++i;
      }
    }

    /*
     * Add each edge to the graph until either there is only one graph remaining (part two) or we reach the edge limit
     * (part one).
     */
    final int edgeLimit = ((pc.getPart() == 1) ? pc.getInt("edgeLimit") : Integer.MAX_VALUE);
    int i = 0;
    for (final Edge edge : edges) {
      if (i == edgeLimit) {
        break;
      }
      // If this potential edge connects two disjoint graphs, merge those graphs. Otherwise, ignore it.
      if (!graphIds.get(edge.p1)
                   .equals(graphIds.get(edge.p2))) {
        final Integer oldGraphId = graphIds.get(edge.p2);
        final Integer newGraphId = graphIds.get(edge.p1);
        final Set<Point3D> oldGraph = graphs.get(oldGraphId);
        graphs.remove(oldGraphId);
        graphs.get(newGraphId)
              .addAll(oldGraph);
        for (final Point3D vertex : oldGraph) {
          graphIds.put(vertex, newGraphId);
        }
      }

      // Part two: if there is only one graph remaining, return the answer.
      if (graphs.size() == 1) {
        return (long) edge.p1.getX() * edge.p2.getX();
      }
      ++i;
    }

    // Part one: there are multiple graphs remaining. Find the three largest and multiply their sizes together.
    return Arrays.stream(MyArrays.reverse(graphs.entrySet()
                                                .stream()
                                                .mapToLong(e -> e.getValue()
                                                                 .size())
                                                .sorted()
                                                .toArray()))
                 .limit(3)
                 .reduce((a, b) -> a * b)
                 .getAsLong();
  }

  /** Get all of the potential edge definitions sorted by cost from low to high. */
  private List<Edge> getSortedEdges(final List<Point3D> vertices) {
    final List<Edge> edges = new ArrayList<>((vertices.size() * vertices.size()) >> 1);
    for (int i = 0; i < vertices.size() - 1; ++i) {
      for (int j = i + 1; j < vertices.size(); ++j) {
        edges.add(new Edge(vertices.get(i), vertices.get(j)));
      }
    }
    Collections.sort(edges);
    return edges;
  }

  /** Represents an edge that may or may not be added to the graph. */
  private final class Edge
  implements Comparable<Edge> {

    public final Point3D p1;

    public final Point3D p2;

    public final long distance;

    public Edge(final Point3D a, final Point3D b) {
      p1 = a;
      p2 = b;
      /*
       * This is not the real distance formula, but that does not matter. It preserves relative ordering which is
       * important. It also avoids floating-point math and square roots which squeezes out a small but measurable amount
       * of performance.
       */
      final long x = p1.getX() - p2.getX();
      final long y = p1.getY() - p2.getY();
      final long z = p1.getZ() - p2.getZ();
      distance = x * x + y * y + z * z;
    }

    @Override
    public int compareTo(final Edge o) {
      return Long.compare(distance, o.distance);
    }

  }
}
