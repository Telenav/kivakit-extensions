////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.kernel.data.conversion.string.primitive.DoubleConverter;
import com.telenav.kivakit.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.resource.resources.packaged.PackageResource;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class UnquotedCsvReaderTest extends UnitTest
{
    @Test
    public void test()
    {
        var year = CsvColumn.of("year", new IntegerConverter(this));
        var make = CsvColumn.of("make");
        var model = CsvColumn.of("model");
        var description = CsvColumn.of("description");
        var price = CsvColumn.of("price", new DoubleConverter(this));
        var schema = new CsvSchema(year, make, model, description, price);

        var resource = PackageResource.of(UnquotedCsvReaderTest.class, "SampleUnquotedCsv.csv");
        try (var reader = new UnquotedCsvReader(resource, schema, ';', ProgressReporter.NULL))
        {
            reader.skipLines(1);
            ensure(reader.hasNext());
            var firstDataLine = reader.next();
            // simple test
            ensureEqual(firstDataLine.get(year), 1997);
            // comma-within-column test
            ensureEqual(firstDataLine.string(description), "\"ac, abs, moon\"");
            reader.next();
            var thirdLine = reader.next();
            // quote-within-column test
            ensureEqual(thirdLine.string(model), "\"Venture \"\"Extended Edition, Very Large\"\"\"");
        }
    }
}
