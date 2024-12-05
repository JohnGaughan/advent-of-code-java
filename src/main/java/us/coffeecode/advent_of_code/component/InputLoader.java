/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
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
package us.coffeecode.advent_of_code.component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Component that retrieves input data for a solution. Its methods accept a {@link PuzzleContext} which contains
 * metadata necessary to find the specific input file required. These methods then load the raw data and pre-process it
 * in various ways.
 * <ul>
 * <li>Retrieve the file as a single string.</li>
 * <li>Retrieve the file as a list of strings, one for each line.</li>
 * <li>Retrieve the file as a list of list of strings, where each string is a single line. The file is read into a giant
 * list of lines, then split up into groups separated by empty lines. Each group is its own list, with those lists
 * contained in another list. Thus, the outer list is really a list of groups. Each inner list is a list of lines that
 * form that group. To understand why this is useful, look at a puzzle where there are several actors that interact and
 * each actor has its data spread across multiple lines. Parsing this way means that each inner list is actually data
 * about that actor, and the outer list is all of the actors.</li>
 * </ul>
 * Furthermore, methods of this class have the ability to process further using lambdas and some built-in functions.
 * After reading in the raw string data as outlined above, some methods can perform tasks such as the following:
 * <ul>
 * <li>Parse the entire file as an integer.</li>
 * <li>Parse the entire file using a lambda or method reference, such as a constructor that accepts a string.</li>
 * <li>Parse each line as an integer, returning {@code int[]}.</li>
 * <li>Split each line using a {@link Pattern} defining a regular expression and optionally process each token through a
 * lambda or method reference.</li>
 * <li>As above, but parse the tokens as integers, returning the entire file as {@code int[][]} where the outer array
 * represents the lines and the inner arrays represent each line after splitting using a regular expression.</li>
 * </ul>
 * There are other capabilities and each year brings the potential for more to be added. This is not an exhaustive list,
 * but shows what this class does. The ultimate goal is to move as much parsing logic into this class from the solution
 * classes so they can focus on the algorithms instead of boring text parsing.
 */
@Component
public class InputLoader {

  @Autowired
  private InputLocator il;

  public static final Pattern DIGITS = Pattern.compile("\\-?\\d+");

  /**
   * Get the contents of the file as a boolean array. The provided truth value will be used to determine which code
   * points evaluate to true: all other code points evaluate to false.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param truth code point that indicates true.
   * @return an array of primitive booleans.
   */
  public boolean[] fileAsBooleanArray(final PuzzleContext pc, final int truth) {
    final String line = fileAsString(pc);
    // Too bad Java doesn't have boolean streams.
    final boolean[] array = new boolean[line.length()];
    for (int i = 0; i < array.length; ++i) {
      array[i] = (line.codePointAt(i) == truth);
    }
    return array;
  }

  /**
   * Get the contents of the file as an array of code points.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return an array of code points.
   */
  public int[] fileAsCodePoints(final PuzzleContext pc) {
    return fileAsString(pc).codePoints()
                           .toArray();
  }

  /**
   * Get the contents of the file as a single integer.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return a single primitive integer.
   */
  public int fileAsInt(final PuzzleContext pc) {
    return Integer.parseInt(fileAsString(pc));
  }

  /**
   * Get the contents of the file as an integer array. An internal regular expression is used to extract each group of
   * digits, which is then parsed into a primitive integer that becomes an element in the array.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return an integer array containing each number in the file.
   */
  public int[] fileAsIntsFromDigitGroups(final PuzzleContext pc) {
    return DIGITS.matcher(fileAsString(pc))
                 .results()
                 .map(r -> r.group())
                 .mapToInt(Integer::parseInt)
                 .toArray();
  }

  /**
   * Get the contents of the file as an integer array. Each code point in the file is treated as a textual number, which
   * is translated into a primitive integer between zero and nine.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return an array of integers.
   */
  public int[] fileAsIntsFromDigits(final PuzzleContext pc) {
    return fileAsString(pc).codePoints()
                           .map(i -> i - '0')
                           .toArray();
  }

  /**
   * Get the contents of the file as an integer array. The file is a hexadecimal string. Each digit of that string is
   * converted to its equivalent integer.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return an integer array containing each number in the file.
   */
  public int[] fileAsIntsFromHexDigits(final PuzzleContext pc) {
    return fileAsString(pc).codePoints()
                           .map(i -> (('0' <= i) && (i <= '9')) ? (i - '0') : (i - 'A' + 10))
                           .toArray();
  }

  /**
   * Get the contents of the file as an integer array. The provided regular expression is used to split the file
   * contents into tokens, and each token is parsed into a primitive integer.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param split the regular expression to use to split the file.
   * @return an integer array containing each number in the file.
   */
  public int[] fileAsIntsFromSplit(final PuzzleContext pc, final Pattern split) {
    return Arrays.stream(split.split(fileAsString(pc).trim()))
                 .mapToInt(Integer::parseInt)
                 .toArray();
  }

  /**
   * Get the contents of the file as a single long.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return a single primitive long.
   */
  public long fileAsLong(final PuzzleContext pc) {
    return Long.parseLong(fileAsString(pc));
  }

  /**
   * Get the contents of the file as a long array. The provided regular expression is used to split the file contents
   * into tokens, and each token is parsed into a primitive long.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param split the regular expression to use to split the file.
   * @return a long array containing each number in the file.
   */
  public long[] fileAsLongsFromSplit(final PuzzleContext pc, final Pattern split) {
    return Arrays.stream(split.split(fileAsString(pc).trim()))
                 .mapToLong(Long::parseLong)
                 .toArray();
  }

  /**
   * Get the contents of the file as a single object.
   *
   * @param <T> the type of the object to return.
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f function that converts the string contents of the file into an object.
   * @return the object representing the file contents.
   */
  public <T> T fileAsObject(final PuzzleContext pc, final Function<String, T> f) {
    return f.apply(fileAsString(pc));
  }

  /**
   * Get the contents of the file as multiple objects that do not necessarily correlate with lines.
   *
   * @param <T> the type of the objects to return.
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f function that converts the string contents of the file into multiple objects.
   * @return a list of objects representing the file contents.
   */
  public <T> List<T> fileAsObjectsFromCodePoints(final PuzzleContext pc, final IntFunction<T> f) {
    return IntStream.of(fileAsCodePoints(pc))
                    .mapToObj(f)
                    .toList();
  }

  /**
   * Get the contents of the file as multiple objects. The provided regular expression will be used to split the file
   * contents, and each token from that split will correspond with an object.
   *
   * @param <T> the type of the objects to return.
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param split the regular expression to use to split the file.
   * @param f function that converts a string from the regex split into an object.
   * @return a list of objects representing the file contents.
   */
  public <T> List<T> fileAsObjectsFromSplit(final PuzzleContext pc, final Pattern split, final Function<String, T> f) {
    return Arrays.stream(split.split(fileAsString(pc).trim()))
                 .map(f)
                 .toList();
  }

  /**
   * Get the contents of the file as a sorted integer array. The provided regular expression is used to split the file
   * contents into tokens, and each token is parsed into a primitive integer. The resultant array will be sorted using
   * the integers' natural sort order.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param split the regular expression to use to split the file.
   * @return an integer array containing each number in the file, sorted in natural order.
   */
  public int[] fileAsSortedIntsFromSplit(final PuzzleContext pc, final Pattern split) {
    return Arrays.stream(split.split(fileAsString(pc).trim()))
                 .mapToInt(Integer::parseInt)
                 .sorted()
                 .toArray();
  }

  /**
   * Get the contents of the file as a string.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return a string containing all of the text in the file, minus any leading or trailing whitespace.
   */
  public String fileAsString(final PuzzleContext pc) {
    try {
      return Files.readString(path(pc))
                  .trim();
    }
    catch (final IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Get the lines of the file grouped together such that blank lines separate the groups.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return a list of line groups, which are themselves lists of strings.
   */
  public List<List<String>> groups(final PuzzleContext pc) {
    final List<String> lines = lines(pc);
    // Split lines list into segments based on blank lines separating groups.
    final List<List<String>> lineGroups = new ArrayList<>();
    int start = -1;
    for (int i = 0; i < lines.size(); ++i) {
      final boolean currentLineHasData = !lines.get(i)
                                               .isBlank();
      // Not currently inside a group
      if (start < 0) {
        // Don't combine these if statements!
        if (currentLineHasData) {
          start = i;
        }
      }
      // Current inside a group
      else if (!currentLineHasData) {
        lineGroups.add(lines.subList(start, i));
        start = -1;
      }
    }
    // See if there is dangling data: if so, add it
    if (start > 0) {
      lineGroups.add(lines.subList(start, lines.size()));
    }

    return lineGroups;
  }

  /***
   * Get the contents of the file as grouped lines separated by blank lines, where each group is then transformed into a
   * primitive long. The resultant longs are then returned in an array.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f the function that converts groups of strings to a primitive long.
   * @return a long array where each element represents a group of strings.
   */
  public long[] groupsAsLongs(final PuzzleContext pc, final ToLongFunction<List<String>> f) {
    return groups(pc).stream()
                     .mapToLong(f)
                     .toArray();
  }

  /**
   * Get the contents of the file as grouped lines separated by blank lines, transformed into a single object using a
   * supplied function.
   *
   * @param <T> the type of the object to return.
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f the function that constructs the object to return.
   * @return an object representing all data in the file.
   */
  public <T> T groupsAsObject(final PuzzleContext pc, final Function<List<List<String>>, T> f) {
    return f.apply(groups(pc));
  }

  /**
   * Get the contents of the file as grouped lines separated by blank lines, transformed into multiple objects where
   * each object represents one group of lines.
   *
   * @param <T> the type of the objects to return.
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f the function that constructs the objects to return.
   * @return a list of object representing each group of lines.
   */
  public <T> List<T> groupsAsObjects(final PuzzleContext pc, final Function<List<String>, T> f) {
    return groups(pc).stream()
                     .map(f)
                     .toList();
  }

  /**
   * Get the lines of the file as strings.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return a list of strings.
   */
  public List<String> lines(final PuzzleContext pc) {
    try {
      return Files.readAllLines(path(pc));
    }
    catch (final IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Get the contents of the file by line, where each line is translated into a boolean array.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param truth code point that indicates true.
   * @return a two-dimensional boolean array representing the file.
   */
  public boolean[][] linesAs2dBooleanArray(final PuzzleContext pc, final int truth) {
    return lines(pc).stream()
                    .map(s -> {
                      boolean[] b = new boolean[s.length()];
                      for (int i = 0; i < b.length; ++i) {
                        b[i] = s.codePointAt(i) == truth;
                      }
                      return b;
                    })
                    .toArray(boolean[][]::new);
  }

  /**
   * Get the contents of the file by line, where each line is translated into an integer array.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f function that converts each string line into an integer array.
   * @return a two-dimensional integer array representing the file.
   */
  public int[][] linesAs2dIntArray(final PuzzleContext pc, final Function<String, int[]> f) {
    return lines(pc).stream()
                    .map(f)
                    .toArray(int[][]::new);
  }

  /**
   * Get the contents of the file by line, where each line is translated into an integer array. Each code point on a
   * line is translated into an integer representing its textual digit.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return a two-dimensional integer array representing the file.
   */
  public int[][] linesAs2dIntArrayFromDigits(final PuzzleContext pc) {
    return lines(pc).stream()
                    .map(s -> s.codePoints()
                               .map(i -> i - '0')
                               .toArray())
                    .toArray(int[][]::new);
  }

  /**
   * Get the contents of the file by line, where each line is translated into an integer array.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param split the regular expression to use to split each line.
   * @return a two-dimensional integer array representing the file.
   */
  public int[][] linesAs2dIntArrayFromSplit(final PuzzleContext pc, final Pattern split) {
    return lines(pc).stream()
                    .map(s -> Arrays.stream(split.split(s.trim()))
                                    .mapToInt(Integer::parseInt)
                                    .toArray())
                    .toArray(int[][]::new);
  }

  /**
   * Get the contents of the file by line, where each line is translated into a long array.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f function that converts each string line into a long array.
   * @return a two-dimensional long array representing the file.
   */
  public long[][] linesAs2dLongArray(final PuzzleContext pc, final Function<String, long[]> f) {
    return lines(pc).stream()
                    .map(f)
                    .toArray(long[][]::new);
  }

  /**
   * Get the contents of the file by line, where each line is translated into a long array.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f function that converts each string line into a long array.
   * @return a two-dimensional long array representing the file.
   */
  public long[][] linesAs2dLongArrayFromSplit(final PuzzleContext pc, final Pattern split) {
    return lines(pc).stream()
                    .map(s -> Arrays.stream(split.split(s.trim()))
                                    .mapToLong(Long::parseLong)
                                    .toArray())
                    .toArray(long[][]::new);
  }

  /**
   * Get the contents of the file by line, where each line is translated into the code points representing its string.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return a two-dimensional array of code points representing the file.
   */
  public int[][] linesAsCodePoints(final PuzzleContext pc) {
    return lines(pc).stream()
                    .map(s -> s.codePoints()
                               .toArray())
                    .toArray(int[][]::new);
  }

  /**
   * Get the contents of the file by line, where each line is translated into a single integer.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return an array of integers where each integer represents one line.
   */
  public int[] linesAsInts(final PuzzleContext pc) {
    return lines(pc).stream()
                    .mapToInt(Integer::parseInt)
                    .toArray();
  }

  /**
   * Get the contents of the file by line, where each line is translated into an integer array. Each code point on a
   * line is translated into an integer representing its textual digit.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return a list of integer arrays representing the file.
   */
  public List<int[]> linesAsIntsFromDigits(final PuzzleContext pc) {
    return lines(pc).stream()
                    .map(s -> s.codePoints()
                               .map(i -> i - '0')
                               .toArray())
                    .toList();
  }

  /**
   * Get the contents of the file by line, where each line is translated into a single integer. The final array is
   * sorted using integers' natural ordering.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return an array of integers where each integer represents one line. This array is sorted.
   */
  public int[] linesAsIntsSorted(final PuzzleContext pc) {
    return lines(pc).stream()
                    .mapToInt(Integer::parseInt)
                    .sorted()
                    .toArray();
  }

  /**
   * Get the contents of the file by line, where each line is translated into a single long.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return an array of longs where each integer represents one line.
   */
  public long[] linesAsLongs(final PuzzleContext pc) {
    return lines(pc).stream()
                    .mapToLong(Long::parseLong)
                    .toArray();
  }

  /**
   * Get the contents of the file by line, where each line is translated into a single long using custom parsing logic.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param parser the function which will convert each line to a long.
   * @return an array of longs where each integer represents one line.
   */
  public long[] linesAsLongs(final PuzzleContext pc, final ToLongFunction<String> parser) {
    return lines(pc).stream()
                    .mapToLong(parser)
                    .toArray();
  }

  /**
   * Get the contents of the file by line, where each line is translated into a single long. The final array is sorted
   * using longs' natural ordering.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return an array of longs where each long represents one line. This array is sorted.
   */
  public long[] linesAsLongsSorted(final PuzzleContext pc) {
    return lines(pc).stream()
                    .mapToLong(Long::parseLong)
                    .sorted()
                    .toArray();
  }

  /**
   * Get the contents of the file by line, where each line is translated into an object. That object is then used to
   * construct a map entry.
   *
   * @param <T> the type of object to construct.
   * @param <U> the type of map key.
   * @param <V> the type of map value.
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f function that constructs an object from a string line in the file.
   * @param k function that constructs a map key from an object.
   * @param v function that constructs a map value from an object.
   * @return a map representing the entire file.
   */
  public <T, U, V> Map<U, V> linesAsMap(final PuzzleContext pc, final Function<String, T> f, final Function<T, U> k, final Function<T, V> v) {
    return lines(pc).stream()
                    .map(f)
                    .collect(Collectors.toMap(k, v));
  }

  /**
   * Get the contents of the file by line, and translate those lines into a single object representing the file.
   *
   * @param <T> the type of object to construct.
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f function that converts a list of lines into an object.
   * @return an object representing the entire file.
   */
  public <T> T linesAsObject(final PuzzleContext pc, final Function<List<String>, T> f) {
    return f.apply(lines(pc));
  }

  /**
   * Get the contents of the file by line, and translate those lines into one object per line. There is no guarantee
   * about the mutability of the returned list.
   *
   * @param <T> the type of object to construct.
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f function that converts a string line into an object.
   * @return a list of objects, one per line in the file. This list has no guarantee about mutability.
   */
  public <T> List<T> linesAsObjects(final PuzzleContext pc, final Function<String, T> f) {
    return lines(pc).stream()
                    .map(f)
                    .toList();
  }

  /**
   * Get the contents of the file by line, and translate those lines into one object per line. Those objects will be
   * stored in a navigable set. There is no guarantee about the mutability of the returned set. The object stored in the
   * set needs to implement the <code>{@link Comparable}</code> interface.
   *
   * @param <T> the type of object to construct.
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f function that converts a string line into an object.
   * @param g supplier function that creates the collection containing the generated objects.
   * @return a collection of objects, one per line in the file.
   */
  public <T> SequencedCollection<T> linesAsObjects(final PuzzleContext pc, final Function<String, T> f, final Supplier<? extends SequencedCollection<T>> g) {
    return lines(pc).stream()
                    .map(f)
                    .collect(Collectors.toCollection(g));
  }

  /**
   * Get the contents of the file by line, and translate those lines into an array of objects per line.
   *
   * @param <T> the type of object to construct.
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f function that converts a string line into an array of objects.
   * @param a function that constructs a 2D array of objects.
   * @return a two-dimensional array of objects.
   */
  public <T> T[][] linesAsObjectsArray(final PuzzleContext pc, final Function<String, T[]> f, final IntFunction<T[][]> a) {
    return lines(pc).stream()
                    .map(f)
                    .toArray(a);
  }

  /**
   * Get the contents of the file by line, and translate those lines into an array of objects per line.
   *
   * @param <T> the type of object to construct.
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param split the regular expression to use to split each line.
   * @param f function that converts a string token into an object.
   * @param a1 function that constructs an array of objects.
   * @param a2 function that constructs a 2D array of objects.
   * @return a two-dimensional array of objects.
   */
  public <T> T[][] linesAsObjectsArray(final PuzzleContext pc, final Pattern split, final Function<String, T> f, final IntFunction<T[]> a1, final IntFunction<T[][]> a2) {
    return lines(pc).stream()
                    .map(s -> Arrays.stream(split.split(s))
                                    .map(f)
                                    .toArray(a1))
                    .toArray(a2);
  }

  /**
   * Get the contents of the file by line, and translate those lines into one object per line.
   *
   * @param <T> the type of object to construct.
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f function that converts a string line into an object.
   * @return a list of objects, one per line in the file. This list is mutable.
   */
  public <T> List<T> linesAsObjectsMutable(final PuzzleContext pc, final Function<String, T> f) {
    return lines(pc).stream()
                    .map(f)
                    .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Get the contents of the file by line, and translate those lines into one object per line. Each line will be split
   * by the provided pattern before being handed to the function that builds each object.
   *
   * @param <T> the type of object to construct.
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param f function that converts a string line into an object.
   * @param split pattern that splits each line into a string array.
   * @return a list of objects, one per line in the file. This list is mutable.
   */
  public <T> List<T> linesAsObjectsMutable(final PuzzleContext pc, final Function<String[], T> f, final Pattern split) {
    return lines(pc).stream()
                    .map(s -> split.split(s))
                    .map(f)
                    .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Get the contents of the file by line, and translate those lines into a list of strings per line.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param split the regular expression to use to split each line.
   * @return a list representing the file, with each list entry being another list containing strings for that line.
   */
  public List<List<String>> linesAsStrings(final PuzzleContext pc, final Pattern split) {
    return lines(pc).stream()
                    .map(s -> Arrays.asList(split.split(s.trim())))
                    .toList();
  }

  /**
   * Get the contents of the file by line, and translate those lines into a list of strings per line.
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @param split the regular expression to use to split each line.
   * @param skip the number of tokens to skip at the start of each line after splitting it.
   * @return a list representing the file, with each list entry being another list containing strings for that line.
   */
  public List<List<String>> linesAsStrings(final PuzzleContext pc, final Pattern split, final long skip) {
    return lines(pc).stream()
                    .map(s -> Arrays.asList(split.split(s.trim()))
                                    .stream()
                                    .skip(skip)
                                    .toList())
                    .toList();
  }

  /**
   * Get the full path to the input file to load including all path elements (directories) and the regular file itself
   * (file name).
   *
   * @param pc Puzzle context containing metadata necessary to find the relevant input file.
   * @return full path to the specific file to load.
   */
  private Path path(final PuzzleContext pc) {
    return il.getInputLocation(pc.getYear(), pc.getDay(), pc.getInputId());
  }

}
