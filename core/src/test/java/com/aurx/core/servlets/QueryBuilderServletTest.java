package com.aurx.core.servlets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aurx.core.services.QueryBuilderUtil;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
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
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class QueryBuilderServletTest {

  private final AemContext context = new AemContext();

  @Mock
  private SlingHttpServletRequest request;

  @Mock
  private SlingHttpServletResponse response;

  @Mock
  private ResourceResolver resourceResolver;

  @Captor
  private ArgumentCaptor<byte[]> argumentCaptor;

  @Mock
  private ServletOutputStream servletOutputStream;

  @Mock
  private QueryBuilderUtil queryBuilderUtil;

  @Mock
  private SearchResult result;

  @Mock
  private Hit hit1;

  private QueryBuilderServlet queryBuilderServlet;

  @Test
  void testDoGet() throws IOException, ServletException, RepositoryException {
    context.registerService(QueryBuilderUtil.class, queryBuilderUtil);
    Resource resource = context.load(true).json(
        "/com/aurx/core/servlets/QueryBuilderServletTestResource/QueryBuilderServletResource.json",
        "/content/we-retail/language-masters/en");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(queryBuilderUtil.getQueryBuilderResult(eq(resourceResolver), anyMap())).thenReturn(result);
    List<Hit> hits = new ArrayList<>();
    hits.add(hit1);
    when(result.getHits()).thenReturn(hits);
    when(hit1.getResource()).thenReturn(resource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    queryBuilderServlet = context.registerInjectActivateService(new QueryBuilderServlet());
    queryBuilderServlet.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains("/content/we-retail/language-masters/en"));
  }

  @Test
  void testQueryBuilderServletWithRepositoryException()
      throws IOException, ServletException, RepositoryException {
    context.registerService(QueryBuilderUtil.class, queryBuilderUtil);
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(queryBuilderUtil.getQueryBuilderResult(eq(resourceResolver), anyMap())).thenReturn(result);
    List<Hit> hits = new ArrayList<>();
    hits.add(hit1);
    when(result.getHits()).thenReturn(hits);
    when(hit1.getResource()).thenThrow(new RepositoryException("Custom RepositoryException"));
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    queryBuilderServlet = context.registerInjectActivateService(new QueryBuilderServlet());
    queryBuilderServlet.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
  }
}