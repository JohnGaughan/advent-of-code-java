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
package us.coffeecode.advent_of_code.component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Locates input files. This is used prior to {@link PuzzleContext} being constructed, since its output is used by the
 * context itself. This class can return the input files present, which can then be used to split an execution of a
 * solution part into several, one for each input. This is where that ability starts.
 */
@Component
public class InputLocator {

  @Value("${inputRoot}")
  private Path inputRoot;

  private final Map<AnswerKey, Map<String, String>> answers = new HashMap<>();

  private final Map<ParameterKey, Map<String, String>> parameters = new HashMap<>();

  /**
   * Get the path to the input for the given year, day, and input ID.
   */
  public Path getInputLocation(final int year, final int day, final String inputId) {
    final Path base = getInputBase(year, day);
    return base.resolve(inputId);
  }

  public int getParts(final int year, final int day) {
    int parts = 0;
    for (int i = 1; i < 3; ++i) {
      final AnswerKey key = new AnswerKey(year, day, i);
      ensureLoaded(key);
      if (answers.containsKey(key)) {
        parts = i;
      }
    }
    return parts;
  }

  /**
   * Get the available input IDs for the given year, day, and part. Part is included here because some puzzles have
   * different example inputs for parts one and two.
   */
  public String[] getInputIds(final int year, final int day, final int part) {
    final AnswerKey key = new AnswerKey(year, day, part);
    ensureLoaded(key);
    if (answers.containsKey(key)) {
      return answers.get(key).keySet().stream().filter(s -> s.endsWith(".txt")).toArray(String[]::new);
    }
    return new String[0];
  }

  public String getAnswer(final int year, final int day, final int part, final String inputId) {
    final AnswerKey key = new AnswerKey(year, day, part);
    ensureLoaded(key);
    if (answers.containsKey(key)) {
      final Map<String, String> value = answers.get(key);
      if (value.containsKey(inputId)) {
        return value.get(inputId);
      }
    }
    return null;
  }

  /**
   * If data for the given answer cache key is not loaded, load it if possible.
   */
  private void ensureLoaded(final AnswerKey key) {
    if (!answers.containsKey(key)) {
      final File file = getInputBase(key.year, key.day).resolve("part" + key.part + ".answers.properties").toFile();
      final Map<String, String> value = loadMap(file);
      answers.put(key, value);
    }
  }

  public Map<String, String> getParameters(final int year, final int day) {
    final ParameterKey key = new ParameterKey(year, day);
    ensureLoaded(key);
    return parameters.getOrDefault(key, Collections.emptyMap());
  }

  /**
   * If data for the given parameter cache key is not loaded, load it if possible.
   */
  private void ensureLoaded(final ParameterKey key) {
    if (!parameters.containsKey(key)) {
      final File file = getInputBase(key.year, key.day).resolve("parameters.properties").toFile();
      final Map<String, String> value = loadMap(file);
      parameters.put(key, value);
    }
  }

  private Map<String, String> loadMap(final File file) {
    final Map<String, String> map = new HashMap<>();
    if (file.exists() && file.canRead()) {
      final Properties props = new Properties();
      try (final Reader in = new FileReader(file)) {
        if (in.ready()) {
          props.load(in);
          props.forEach((k, v) -> map.put(k.toString(), v.toString()));
        }
      }
      catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    return Collections.unmodifiableMap(map);
  }

  /**
   * Get the path to the directory containing inputs for the given year and day.
   */
  private Path getInputBase(final int year, final int day) {
    final Path relative = Path.of("year" + year, "day" + pad(day));
    return inputRoot.resolve(relative);
  }

  private String pad(final int day) {
    if (day < 10) {
      return "0" + day;
    }
    return Integer.toString(day);
  }

  private record AnswerKey(int year, int day, int part) {}

  private record ParameterKey(int year, int day) {}
}
