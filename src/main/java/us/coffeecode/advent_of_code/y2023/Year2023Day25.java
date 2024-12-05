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
import java.util.regex.Pattern;

import org.jgrapht.Graph;
import org.jgrapht.alg.StoerWagnerMinimumCut;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2023, day = 25, title = "Snowverload")
@Component
public class Year2023Day25 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Graph<String, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);

    // Parse the input directly into the graph.
    for (final String line : il.lines(pc)) {
      final String[] tokens = SPLIT.split(line);
      Arrays.stream(tokens)
            .forEach(s -> graph.addVertex(s));
      Arrays.stream(tokens)
            .skip(1)
            .forEach(s -> graph.addEdge(tokens[0], s));
    }

    // Delegate to JGraphT to do the hard work.
    var cutter = new StoerWagnerMinimumCut<>(graph);
    final long cut1 = cutter.minCut()
                            .size();
    return (graph.vertexSet()
                 .size()
      - cut1) * cut1;
  }

  /** Split each line into vertex names. */
  private static final Pattern SPLIT = Pattern.compile(":? ");
}
