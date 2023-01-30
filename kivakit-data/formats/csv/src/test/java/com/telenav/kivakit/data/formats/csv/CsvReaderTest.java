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

import com.telenav.kivakit.conversion.core.language.primitive.DoubleConverter;
import com.telenav.kivakit.conversion.core.language.primitive.IntegerConverter;
import com.telenav.kivakit.resource.packages.PackageTrait;
import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import static com.telenav.kivakit.data.formats.csv.CsvColumn.csvColumn;

public class CsvReaderTest extends UnitTest implements PackageTrait
{
    @Test
    public void test()
    {
        var year = csvColumn("year", new IntegerConverter(this));
        var make = csvColumn("make");
        var model = csvColumn("model");
        var description = csvColumn("description");
        var price = csvColumn("price", new DoubleConverter(this));
        var schema = new CsvSchema(year, make, model, description, price);

        var resource = packageResource("SampleCsv.csv");
        try (var in = new CsvReader(resource, schema))
        {
            in.skipLines(1);

            ensure(in.hasNext());

            {
                var line = in.next();
                ensureEqual(line.string(model), "\"K\" Ct");
            }

            {
                var line = in.next();
                ensureEqual(line.string(model), "\"Venture Extended Edition, Very Large\"");
            }

            {
                var line = in.next();
                ensureEqual(line.get(year), 1997);
                ensureEqual(line.string(description), "ac, abs, moon");
                ensureEqual(line.get(price), 3000.0);
            }

            in.next();

            {
                var line = in.next();
                ensureEqual(line.string(model), "Venture \"Extended Edition, Very Large\"");
            }

            {
                var line = in.next();
                ensure(line.string(description).indexOf('!') >= 0);
            }
        }
    }
}
