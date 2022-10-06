package com.telenav.kivakit.web.jetty.resources;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.web.jetty.JettyServer;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.language.Classes.simpleName;

/**
 * A {@link JettyServer} plugin that serves static resources
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class AssetsJettyPlugin extends BaseAssetsJettyPlugin
{
    private final ResourceFolder<?> folder;

    public AssetsJettyPlugin(ResourceFolder<?> folder)
    {
        this("[AssetsJettyPlugin " + simpleName(folder.getClass()).toLowerCase() + " = " + folder.path() + "]", folder);
    }

    public AssetsJettyPlugin(String name, ResourceFolder<?> folder)
    {
        super(name);
        this.folder = folder;
    }

    @Override
    public ServletHolder holder()
    {
        var defaultServlet = new DefaultServlet();

        var holder = new ServletHolder(defaultServlet);

        holder.setName("jetty-assets:" + folder.resourceFolderIdentifier());
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
