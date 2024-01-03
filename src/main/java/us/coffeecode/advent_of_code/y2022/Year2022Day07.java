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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2022, day = 7, title = "No Space Left On Device")
@Component
public class Year2022Day07 {

  @Autowired
  private InputLoader il;

  private static final Pattern SPLIT = Pattern.compile(" ");

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Node root = getRootNode(pc);
    return getAllSizes(root).stream().mapToLong(Long::longValue).filter(l -> l <= 100_000).sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Node root = getRootNode(pc);
    final long minToFree = root.totalSize() - 40_000_000;
    return getAllSizes(root).stream().mapToLong(Long::longValue).filter(l -> l >= minToFree).min().getAsLong();
  }

  private List<Long> getAllSizes(final Node root) {
    final List<Long> sizes = new ArrayList<>(100);
    sizes.add(Long.valueOf(root.totalSize()));
    for (final Node child : root.children) {
      sizes.addAll(getAllSizes(child));
    }
    return sizes;
  }

  private Node getRootNode(final PuzzleContext pc) {
    final Node root = new Node("/");
    Node current = root;
    for (final Command command : getDeviceIO(pc)) {
      if (command.command.startsWith("$ cd")) {
        final String loc = SPLIT.split(command.command)[2];
        if ("/".equals(loc)) {
          current = root;
        }
        else if ("..".equals(loc)) {
          current = current.parent;
        }
        else {
          current = current.getChild(loc);
        }
      }
      else {
        for (final String line : command.results) {
          final String[] parts = SPLIT.split(line);
          if ("dir".equals(parts[0])) {
            current.getChild(parts[1]);
          }
          else {
            final Long size = Long.valueOf(parts[0]);
            current.files.put(parts[1], size);
          }
        }
      }
    }
    return root;
  }

  private List<Command> getDeviceIO(final PuzzleContext pc) {
    final List<Command> result = new ArrayList<>();
    List<String> group = new ArrayList<>();
    String command = null;
    for (final String line : il.lines(pc)) {
      // New group
      if (line.startsWith("$")) {
        if (command != null) {
          result.add(new Command(command, group));
        }
        command = line;
        group = new ArrayList<>();
      }
      else {
        group.add(line);
      }
    }
    if ((command != null) && !command.startsWith("$ cd")) {
      result.add(new Command(command, group));
    }

    return result;
  }

  private record Command(String command, List<String> results) {}

  private static final class Node {

    final String name;

    final Node parent;

    final Map<String, Long> files = new HashMap<>();

    final Set<Node> children = new HashSet<>();

    Long size;

    Node(final String n) {
      name = n;
      parent = this;
    }

    Node(final String n, final Node p) {
      name = n;
      parent = p;
    }

    Node getChild(final String _name) {
      final Optional<Node> match = children.stream().filter(n -> n.name.equals(_name)).findFirst();
      if (match.isPresent()) {
        return match.get();
      }
      final Node child = new Node(_name, this);
      children.add(child);
      return child;
    }

    long totalSize() {
      if (size == null) {
        long thisNode = files.values().stream().mapToLong(Long::longValue).sum();
        long childNodes = children.stream().mapToLong(Node::totalSize).sum();
        size = Long.valueOf(thisNode + childNodes);
      }
      return size.longValue();
    }

    @Override
    public boolean equals(final Object obj) {
      return (this == obj);
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }
  }

}
