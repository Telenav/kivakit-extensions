package com.telenav.kivakit.web.jetty.resources;

import com.telenav.kivakit.resource.ResourceFolder;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;

public class AssetsJettyResourcePlugin extends BaseJettyResourcePlugin
{
    private final ResourceFolder folder;

    public AssetsJettyResourcePlugin(final ResourceFolder folder)
    {
        super("[AssetsJettyResourcePlugin path = " + folder.toString() + "]");
        this.folder = folder;
    }

    @Override
    public ServletHolder holder()
    {
        final var defaultServlet = new DefaultServlet();

        final var holder = new ServletHolder(defaultServlet);

        holder.setName("jetty-assets:" + folder.identifier());
        final var base = folder.uri();
        if (base != null)
        {
            holder.setInitParameter("resourceBase", base.toString());
        }
        holder.setInitParameter("dirAllowed", "false");
        holder.setInitParameter("pathInfoOnly", "true");

        return holder;
    }
}
