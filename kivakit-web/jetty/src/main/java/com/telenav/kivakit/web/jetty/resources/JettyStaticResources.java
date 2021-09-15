package com.telenav.kivakit.web.jetty.resources;

import com.telenav.kivakit.kernel.language.types.Classes;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;

public class JettyStaticResources extends BaseJettyResource
{
    private final Class<?> _package;

    private final String folder;

    public JettyStaticResources(final Class<?> _package, final String folder)
    {
        super(folder);
        this._package = _package;
        this.folder = folder;
    }

    @Override
    public ServletHolder holder()
    {
        final var defaultServlet = new DefaultServlet();

        final var holder = new ServletHolder(defaultServlet);
        holder.setName("jetty-static-resources:" + _package.getSimpleName());
        holder.setInitParameter("resourceBase", Classes.resourceUri(_package, folder).toString());
        holder.setInitParameter("dirAllowed", "false");
        holder.setInitParameter("pathInfoOnly", "true");

        return holder;
    }
}
