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
package us.coffeecode.advent_of_code.component;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

/**
 * Class responsible for loading files from the resource directory. This class uses caching and lazy loading.
 */
@Component
public class AocResources {

  private static final String YEAR_TITLE_PROPERTIES = "YearTitles.properties";

  private static final String DAY_TITLE_PROPERTIES = "DayTitles.properties";

  /** Directory containing resources on the file system. */
  @Value("${resources}")
  private Path resources;

  /** Titles used to describe each year's test in the output. */
  private Map<Integer, String> yearTitles;

  /** Get the title for a specific year. */
  public String getYearTitle(final Integer year) {
    if (yearTitles == null) {
      yearTitles = loadMap(YEAR_TITLE_PROPERTIES, Integer::valueOf, s -> s);
    }
    return yearTitles.get(year);
  }

  /** Titles used to describe each year's test in the output. */
  private Map<YearDay, String> dayTitles;

  /** Get the title for a specific year and day. */
  public String getDayTitle(final int year, final int day) {
    if (dayTitles == null) {
      dayTitles = loadMap(DAY_TITLE_PROPERTIES, YearDay::valueOf, s -> s);
    }
    return dayTitles.get(new YearDay(year, day));
  }

  /** Load a properties file into a map of arbitrary generic type. */
  private <K, V> Map<K, V> loadMap(final String filename, final Function<String, K> km, final Function<String, V> vm) {
    try (Reader in = new FileReader(resources.resolve(filename)
                                             .toFile())) {
      Properties p = new Properties();
      p.load(in);
      return Maps.fromProperties(p)
                 .entrySet()
                 .stream()
                 .collect(Collectors.toMap(e -> km.apply(e.getKey()), e -> vm.apply(e.getValue())));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private record YearDay(int year, int day) {

    static YearDay valueOf(final String line) {
      return new YearDay(Integer.parseInt(line.substring(0, 4)), Integer.parseInt(line.substring(5)));
    }
  }
}
