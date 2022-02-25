package com.telenav.kivakit.web.jetty.resources;

import com.telenav.kivakit.resource.ResourceFolder;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;

public class AssetsJettyPlugin extends BaseAssetsJettyPlugin
{
    private final ResourceFolder folder;

    public AssetsJettyPlugin(ResourceFolder folder)
    {
        this("[AssetsJettyPlugin folder = " + folder.toString() + "]", folder);
    }

    public AssetsJettyPlugin(String name, ResourceFolder folder)
    {
        super(name);
        this.folder = folder;
    }

    @Override
    public ServletHolder holder()
    {
        var defaultServlet = new DefaultServlet();

        var holder = new ServletHolder(defaultServlet);

        holder.setName("jetty-assets:" + folder.identifier());
        var base = folder.uri();
        if (base != null)
        {
            holder.setInitParameter("resourceBase", base.toString());
        }
        holder.setInitParameter("dirAllowed", "false");
        holder.setInitParameter("pathInfoOnly", "true");

        return holder;
    }
}
