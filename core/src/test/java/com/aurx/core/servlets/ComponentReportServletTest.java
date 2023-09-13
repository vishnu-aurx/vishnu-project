package com.aurx.core.servlets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aurx.core.services.QueryBuilderUtil;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.jcr.RepositoryException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ComponentReportServletTest {

  private final AemContext context = new AemContext();

  private ComponentReportServlet componentReportServlet;

  @Mock
  private SlingHttpServletRequest request;

  @Mock
  private SlingHttpServletResponse response;

  @Mock
  private ServletOutputStream servletOutputStream;

  @Captor
  private ArgumentCaptor<byte[]> outputStream;

  @Mock
  transient QueryBuilderUtil queryBuilderUtil;

  @Mock
  private SearchResult result;

  @Mock
  private Hit hit1, hit2;

  @Test
  void doPost() throws ServletException, IOException, RepositoryException {
    context.registerService(QueryBuilderUtil.class, queryBuilderUtil);
    when(queryBuilderUtil.getQueryBuilderResult(eq(request.getResourceResolver()),
        anyMap())).thenReturn(
        result);
    List<Hit> hits = new ArrayList<>();
    hits.add(hit1);
    hits.add(hit2);
    when(result.getHits()).thenReturn(hits);
    Resource indiaPageResource = context.load(true)
        .json(
            "/com/aurx/core/servlets/ComponentReportServlet/ComponentReportServletIndiaPageResource.json",
            "/content/vishnu-project/myfrinds/Ind");
    Resource radhePageResource = context.load(true)
        .json(
            "/com/aurx/core/servlets/ComponentReportServlet/ComponentReportServletRadhePageResource.json",
            "/content/vishnu-project/lm/en/Radhe");
    when(hits.get(0).getResource()).thenReturn(indiaPageResource);
    when(hits.get(1).getResource()).thenReturn(radhePageResource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    componentReportServlet = context.registerInjectActivateService(new ComponentReportServlet());
    componentReportServlet.doPost(request, response);
    assertEquals(200, context.response().getStatus());
    verify(servletOutputStream).write(outputStream.capture());
    String responseJson = new String(outputStream.getValue());
    assertNotNull(responseJson);
    JsonArray responseJsonObject = JsonParser.parseString(responseJson).getAsJsonArray();
    assertEquals(2, responseJsonObject.size());
  }

  @Test
  void ComponentReportServletWithRepositoryException()
      throws RepositoryException, IOException, ServletException {
    context.registerService(QueryBuilderUtil.class, queryBuilderUtil);
    when(queryBuilderUtil.getQueryBuilderResult(eq(request.getResourceResolver()),
        anyMap())).thenReturn(
        result);
    List<Hit> hits = new ArrayList<>();
    hits.add(hit1);
    hits.add(hit2);
    when(result.getHits()).thenReturn(hits);
    when(hits.get(0).getResource()).thenThrow(new RepositoryException("Custom RepositoryException"));
    when(hits.get(1).getResource()).thenThrow(new RepositoryException("Custom RepositoryException"));
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    componentReportServlet = context.registerInjectActivateService(new ComponentReportServlet());
    componentReportServlet.doPost(request, response);
    assertEquals(200, context.response().getStatus());
    verify(servletOutputStream).write(outputStream.capture());
    String responseJson = new String(outputStream.getValue());
    assertNotNull(responseJson);
    JsonArray responseJsonObject = JsonParser.parseString(responseJson).getAsJsonArray();
    assertEquals(0, responseJsonObject.size());
  }
}