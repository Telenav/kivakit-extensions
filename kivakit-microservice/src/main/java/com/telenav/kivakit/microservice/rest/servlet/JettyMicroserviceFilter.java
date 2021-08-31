package com.telenav.kivakit.microservice.rest.servlet;

import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.kernel.language.io.IO;
import com.telenav.kivakit.microservice.rest.MicroserviceRestApplication;
import com.telenav.kivakit.microservice.rest.MicroserviceRestRequest;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class JettyMicroserviceFilter implements Filter, ComponentMixin
{
    /** Map from paths to requests */
    private final Map<String, Class<? extends MicroserviceRestRequest>> requests = new HashMap<>();

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
        var httpRequest = (HttpServletRequest) servletRequest;
        var httpResponse = (HttpServletResponse) servletResponse;
        var method = httpRequest.getMethod();
        var path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        path = path.substring(contextPath.length());
        var requestType = requests.get(path);
        if (requestType != null)
        {
            try
            {
                switch (method)
                {
                    case "POST":
                        var in = httpRequest.getInputStream();
                        var out = httpResponse.getOutputStream();
                        var json = IO.string(in);
                        var gson = application.gsonFactory().newInstance();
                        var request = gson.fromJson(json, requestType);
                        var response = request.respond();
                        var
                        response.
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

    public void mount(final String path, final Class<? extends MicroserviceRestRequest> request)
    {
        requests.put(path, request);
    }
}
