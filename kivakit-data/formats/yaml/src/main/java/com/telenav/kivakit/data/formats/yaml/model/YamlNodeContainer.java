package com.telenav.kivakit.data.formats.yaml.model;

import com.telenav.kivakit.core.collections.list.ObjectList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface YamlNodeContainer extends Iterable<YamlNode>
{
    ObjectList<YamlNode> elements();

    @NotNull
    @Override
    default Iterator<YamlNode> iterator()
    {
        return elements().iterator();
    }

    YamlNode prepending(YamlNode element);

    int size();
}
