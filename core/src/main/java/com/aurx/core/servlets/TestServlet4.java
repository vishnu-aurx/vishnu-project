package com.aurx.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class, immediate = true, property = {"sling.servlet.methods=GET",
        "sling.servlet.paths=/bin/property",
        "sling.servlet.selectors=getMethode",
        "sling.servlet.extensions=json"
})
public class TestServlet4 extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        String nodeName = request.getParameter("nodeName");
        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource resource;
        if (path != null && resourceResolver != null) {
            resource = resourceResolver.getResource(path);
            if (resource != null) {
                ValueMap valueMap = resource.getValueMap();
                ModifiableValueMap modifiableValueMap = resource.adaptTo(ModifiableValueMap.class);
                modifiableValueMap.put(key, value);
                resourceResolver.commit();
            }
        }
    }
}







