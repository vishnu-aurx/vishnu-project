package com.aurx.core.servlets;

import com.aurx.core.services.ProductDetailService;
import com.day.cq.wcm.api.Page;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
@Component(service = Servlet.class,immediate = true,property = {
        "sling.servlet.methods=GET",
        "sling.servlet.resourceTypes=cq:Page",
        "sling.servlet.selectors=details",
        "sling.servlet.extensions=json",
})
public class TestServlet extends SlingSafeMethodsServlet {

    @Reference
    ProductDetailService productDetailService;
    @Override
    protected void doGet(SlingHttpServletRequest request,  SlingHttpServletResponse response) throws ServletException, IOException {

        /*Page page = request.getResource().adaptTo(Page.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", page.getTitle());
        jsonObject.addProperty("name", page.getName());
        jsonObject.addProperty("lastModified", page.getLastModified().toString());*/

        ValueMap valueMap = request.getResource().getChild("jcr:content").getValueMap();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", valueMap.get("jcr:title", ""));
        jsonObject.addProperty("name", request.getResource().getName());
        jsonObject.addProperty("lastModified", valueMap.get("jcr:lastModified", ""));

//        String count = request.getParameter("count");
//        JsonObject jsonObject = new JsonObject();
//        JsonArray responseArray = new JsonArray();
//        Resource resource = request.getResource();
//        int productsCount =10;
//        if(count != null) {
//            productsCount = Integer.parseInt(count);
//        } else {
//            productsCount = resource.getValueMap().get("numberOfProducts", 10);
//        }
//        JsonArray products = productDetailService.fetchAllProducts();
//        if(products != null && !products.isEmpty()) {
//            for(int i=0; i<productsCount; i++) {
//                if(products.size() > i) {
//                    responseArray.add(products.get(i));
//                } else {
//                    break;
//                }
//            }
//        }
//        response.getWriter().write(responseArray.toString());
        response.getWriter().write(jsonObject.toString());

    }
}
