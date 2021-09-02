package com.telenav.kivakit.microservice.rest.servlet;

import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.MicroserviceRestRequest;
import org.jetbrains.annotations.NotNull;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

public class JettyMicroserviceFilter implements Filter, ComponentMixin
{
    /** Map from relative paths to requests */
    private final Map<String, Class<? extends MicroserviceRestRequest<?>>> requests = new HashMap<>();

    /** The microservice rest application */
    private final MicroserviceRestApplication application;

    public JettyMicroserviceFilter(final MicroserviceRestApplication application)
    {
        this.application = application;
    }

    @Override
    public void destroy()
    {
    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain)
    {
        // Cast HTTP request and response,
        var httpRequest = (HttpServletRequest) servletRequest;
        var httpResponse = (HttpServletResponse) servletResponse;

        // get the request type from the relative path of the request,
        var requestType = requests.get(relativePath(httpRequest));
        if (requestType != null)
        {
            try
            {
                // and handle each HTTP method.
                switch (httpRequest.getMethod())
                {
                    case "POST":
                    {
                        var in = httpRequest.getInputStream();
                        var out = httpResponse.getOutputStream();
                        var jsonIn = IO.string(in);
                        var gson = application.gsonFactory().newInstance();
                        var request = gson.fromJson(jsonIn, requestType);
                        var response = request.respond(this);
                        var jsonOut = gson.toJson(response);
                        out.println(jsonOut);
                    }
                    break;

                    case "GET":
                        unsupported();
                        break;

                    case "PUT":
                        unsupported();
                        break;

                    case "DELETE":
                        unsupported();
                        break;
                }
            }
            catch (Exception e)
            {
                problem("Cannot create request of type: $", requestType);
            }
        }
        else
        {
            try
            {
                filterChain.doFilter(servletRequest, servletResponse);
            }
            catch (Exception e)
            {
                warning(e, "Exception thrown by filter chain");
            }
        }
    }

    @Override
    public void init(final FilterConfig filterConfig)
    {
    }

    public void mount(final String relativePath, final Class<? extends MicroserviceRestRequest<?>> request)
    {
        requests.put(relativePath, request);
    }

    @NotNull
    private String relativePath(final HttpServletRequest httpRequest)
    {
        // Get the full request URI,
        var uri = httpRequest.getRequestURI();

        // and the context path,
        String contextPath = httpRequest.getContextPath();
        ensure(uri.startsWith(contextPath));

        // then return the URI without the context path
        return uri.substring(contextPath.length());
    }
}
