package com.aurx.core.servlets;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = Servlet.class, immediate = true, property = {
        "sling.servlet.methods=GET",
        "sling.servlet.paths=/bin/node",

})
public class TestServlet3 extends SlingSafeMethodsServlet {
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        String nodeName = request.getParameter("nodeName");
        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource resource;
        if (StringUtils.isNotBlank(nodeName)) {
            if (path != null && resourceResolver != null) {
                resource = resourceResolver.getResource(path);
                Map<String, Object> map = new HashMap<>();
                map.put("jcr:primaryType", "nt:unstructured");
                if (resource != null && resource.getChild(nodeName) == null) {
                    ValueMap valueMap = resource.getValueMap();
                    resourceResolver.create(resource, nodeName, map);
                    resourceResolver.commit();
                    response.getWriter().println("node is created");
                } else {
                    response.getWriter().println("somthing went wrong ");
                }
            } else {
                response.getWriter().println("invaild path");
            }
        }else {response.getWriter().println("nodeName IS blank");}
    }
}
