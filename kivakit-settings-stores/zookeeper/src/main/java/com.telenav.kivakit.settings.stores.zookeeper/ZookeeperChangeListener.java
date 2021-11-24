package com.telenav.kivakit.settings.stores.zookeeper;

import com.telenav.kivakit.kernel.language.paths.StringPath;

public interface ZookeeperChangeListener
{
    void onNodeCreated(StringPath path);

    void onNodeDataChanged(StringPath path);

    void onNodeDeleted(StringPath path);
}