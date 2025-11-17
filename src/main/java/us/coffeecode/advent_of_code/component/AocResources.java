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
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class responsible for loading files from the resource directory.
 */
@Component
public class AocResources {

  private static final String YEAR_TITLE_PROPERTIES = "YearTitles.properties";

  /** Directory containing resources on the file system. */
  @Value("${resources}")
  private Path resources;

  /** Titles used to describe each year's test in the output. */
  private Map<Integer, String> yearTitles;

  /** Get the titles for each year, lazy loading as necessary. */
  public Map<Integer, String> getYearTitle(final Integer year) {
    if (yearTitles == null) {
      try (Reader in = new FileReader(resources.resolve(YEAR_TITLE_PROPERTIES)
                                               .toFile())) {
        Properties p = new Properties();
        p.load(in);
        yearTitles = Collections.unmodifiableMap(p.entrySet()
                                                  .stream()
                                                  .collect(Collectors.toMap(e -> Integer.valueOf(e.getKey()
                                                                                                  .toString()),
                                                    e -> e.getValue()
                                                          .toString())));
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    return yearTitles;
  }

}
