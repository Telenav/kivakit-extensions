package com.telenav.kivakit.data.formats.yaml.model;

import com.telenav.kivakit.core.collections.list.ObjectList;

public interface YamlNodeContainer
{
    ObjectList<YamlNode> elements();

    YamlNode prepending(YamlNode element);

    int size();
}
