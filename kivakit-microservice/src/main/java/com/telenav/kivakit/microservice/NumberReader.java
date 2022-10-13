package com.telenav.kivakit.microservice;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.resource.packages.PackageTrait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.core.ensure.Ensure.illegalState;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.os.Console.console;

public class NumberReader extends BaseComponent implements PackageTrait
{
    public ObjectList<Integer> readNumbers()
    {
        return packageResource("numbers.txt")
                .reader()
                .readLines()
                .map(Integer::parseInt);
    }

    public Double averageNumber()
    {
        var numbers = listenTo(new NumberReader()).readNumbers();
        if (numbers.isNonEmpty())
        {
            return 0.0;
        }
        else
        {
            problem("Couldn't create average");
            return null;
        }
    }
    public List<Integer> readNumbers22() throws IOException
    {
        var values = new ArrayList<Integer>();
        try (var in = getClass().getResourceAsStream("numbers.txt"))
        {
            var reader = new BufferedReader(new InputStreamReader(in));
            while (reader.ready())
            {
                values.add(Integer.parseInt(reader.readLine()));
            }
        }
        return values;
    }
    public double averageNumber2()
    {
        var numbers = throwingListener().listenTo(new NumberReader()).readNumbers();
        if (numbers.isNonEmpty())
        {
            var sum = 0.0;
            for (var number : numbers)
            {
                sum += number;
            }
            return sum / numbers.size();
        }

        return illegalState("No numbers to average");
    }

    public static void main(String[] args)
    {
        console().println("average: " + new NumberReader().averageNumber());
    }
}
