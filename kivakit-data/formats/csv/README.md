# data-formats csv &nbsp;&nbsp; <img src="https://www.lexakai.org/images/https://www.kivakit.org/images/gears-40.png.png" srcset="https://www.lexakai.org/images/https://www.kivakit.org/images/gears-40.png-2x.png 2x"/>

This module reads and writes CSV data.

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Index

[**Summary**](#summary)  
[**Schemas**](#schemas)  
[**Reading and Writing**](#reading-and-writing)  

[**Dependencies**](#dependencies) | [**Class Diagrams**](#class-diagrams) | [**Package Diagrams**](#package-diagrams) | [**Javadoc**](#javadoc)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

### Dependencies <a name="dependencies"></a> &nbsp;&nbsp; <img src="https://www.lexakai.org/images/dependencies-32.png" srcset="https://www.lexakai.org/images/dependencies-32-2x.png 2x"/>

[*Dependency Diagram*](https://www.kivakit.org/lexakai/kivakit-extensions/kivakit-data/formats/csv/documentation/diagrams/dependencies.svg)

#### Maven Dependency

    <dependency>
        <groupId>com.telenav.kivakit</groupId>
        <artifactId>kivakit-data-formats-csv</artifactId>
        <version>0.9.0-SNAPSHOT</version>
    </dependency>


<img src="https://www.kivakit.org/images/short-horizontal-line-128.png" srcset="https://www.kivakit.org/images/short-horizontal-line-128-2x.png 2x"/>

[//]: # (start-user-text)

### Summary <a name = "summary"></a>

This module provides an easy way to read and write CSV files in an object-oriented fashion.

### Schemas <a name = "schemas"></a>

A *CsvSchema* maps column names in a CSV file to *CsvColumn* objects and optionally
associates a *StringConverter* with each column, producing an object-oriented model
of the CSV data.

For example:

    CsvColumn YEAR  = new CsvColumn("year", new IntegerConverter(this));
    CsvColumn MODEL = new CsvColumn("model");
    CsvColumn MAKE  = new CsvColumn("make");
    CsvColumn PRICE = new CsvColumn("price", new DoubleConverter(this));

    CsvSchema SCHEMA = new CsvSchema(YEAR, MAKE, MODEL, PRICE);

### Reading and Writing <a name = "reading-and-writing"></a>

The *CsvLine* class provides a model of rows of CSV data as they are read and written with
*CsvReader* and *CsvWriter*. Data in the columns of a *CsvLine* can be accessed as strings
or as converted objects.

For example (using the schema defined above):

    var progressReporter = Progress.create(this);

    try (var input = new CsvReader(resource, progressReporter, SCHEMA, ','))
    {
        while (input.hasNext())
        {
            var line = input.next();

            var year = line.get(YEAR)
            var model = line.get(MODEL);
            var make = line.get(MAKE);
            var price = line.get(PRICE);

            assert year == 1997;
            assert price == 3000.0;

                [...]

        }
    }

[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/short-horizontal-line-128.png" srcset="https://www.kivakit.org/images/short-horizontal-line-128-2x.png 2x"/>

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; <img src="https://www.lexakai.org/images/diagram-32.png" srcset="https://www.lexakai.org/images/diagram-32-2x.png 2x"/>

[*CSV Data Format*](https://www.kivakit.org/lexakai/kivakit-extensions/kivakit-data/formats/csv/documentation/diagrams/diagram-csv.svg)

<img src="https://www.kivakit.org/images/short-horizontal-line-128.png" srcset="https://www.kivakit.org/images/short-horizontal-line-128-2x.png 2x"/>

### Package Diagrams <a name="package-diagrams"></a> &nbsp;&nbsp; <img src="https://www.lexakai.org/images/box-32.png" srcset="https://www.lexakai.org/images/box-32-2x.png 2x"/>

[*com.telenav.kivakit.data.formats.csv*](https://www.kivakit.org/lexakai/kivakit-extensions/kivakit-data/formats/csv/documentation/diagrams/com.telenav.kivakit.data.formats.csv.svg)  
[*com.telenav.kivakit.data.formats.csv.project*](https://www.kivakit.org/lexakai/kivakit-extensions/kivakit-data/formats/csv/documentation/diagrams/com.telenav.kivakit.data.formats.csv.project.svg)

<img src="https://www.kivakit.org/images/short-horizontal-line-128.png" srcset="https://www.kivakit.org/images/short-horizontal-line-128-2x.png 2x"/>

### Javadoc <a name="javadoc"></a> &nbsp;&nbsp; <img src="https://www.lexakai.org/images/books-32.png" srcset="https://www.lexakai.org/images/books-32-2x.png 2x"/>

Javadoc coverage for this project is 92.9%.  
  
&nbsp; &nbsp;  ![](https://www.kivakit.org/images/meter-90-12.png)



| Class | Documentation Sections |
|---|---|
| [*CsvColumn*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.formats.csv/com/telenav/kivakit/data/formats/csv/CsvColumn.html) |  |  
| [*CsvLine*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.formats.csv/com/telenav/kivakit/data/formats/csv/CsvLine.html) | Properties |  
| | Converting a Line to an Object |  
| [*CsvPropertyFilter*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.formats.csv/com/telenav/kivakit/data/formats/csv/CsvPropertyFilter.html) |  |  
| [*CsvReader*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.formats.csv/com/telenav/kivakit/data/formats/csv/CsvReader.html) | Processing CSV Lines |  
| [*CsvSchema*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.formats.csv/com/telenav/kivakit/data/formats/csv/CsvSchema.html) |  |  
| [*CsvWriter*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.formats.csv/com/telenav/kivakit/data/formats/csv/CsvWriter.html) |  |  
| [*DataFormatsCsvProject*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.formats.csv/com/telenav/kivakit/data/formats/csv/project/DataFormatsCsvProject.html) |  |  
| [*UnquotedCsvReader*](https://www.kivakit.org/javadoc/kivakit-extensions/kivakit.data.formats.csv/com/telenav/kivakit/data/formats/csv/UnquotedCsvReader.html) |  |  

[//]: # (start-user-text)



[//]: # (end-user-text)

<img src="https://www.kivakit.org/images/horizontal-line-512.png" srcset="https://www.kivakit.org/images/horizontal-line-512-2x.png 2x"/>

<sub>Copyright &#169; 2011-2021 [Telenav](http://telenav.com), Inc. Distributed under [Apache License, Version 2.0](LICENSE)</sub>  
<sub>This documentation was generated by [Lexakai](https://github.com/Telenav/lexakai). UML diagrams courtesy
of [PlantUML](http://plantuml.com).</sub>

