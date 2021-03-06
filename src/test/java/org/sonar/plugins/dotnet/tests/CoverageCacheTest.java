/*
 * SonarQube .NET Tests Library
 * Copyright (C) 2014-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plugins.dotnet.tests;

import java.io.File;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CoverageCacheTest {

  @Test
  public void test() {
    CoverageCache cache = new CoverageCache();
    CoverageParser parser = mock(CoverageParser.class);
    File reportFile = mock(File.class);
    when(reportFile.getAbsolutePath()).thenReturn("foo.txt");

    Coverage coverage = cache.readCoverageFromCacheOrParse(parser, reportFile);
    verify(parser, Mockito.times(1)).parse(reportFile, coverage);

    cache.readCoverageFromCacheOrParse(parser, reportFile);
    verify(parser, Mockito.times(1)).parse(Mockito.eq(reportFile), Mockito.any(Coverage.class));
  }

}
