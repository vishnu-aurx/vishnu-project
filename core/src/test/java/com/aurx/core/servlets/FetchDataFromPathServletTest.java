package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.ONE;
import static com.aurx.core.constant.ApplicationConstants.PATH;
import static com.aurx.core.constant.ApplicationConstants.THREE;
import static com.aurx.core.constant.ApplicationConstants.TWO;
import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
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
class FetchDataFromPathServletTest {

  private AemContext context = new AemContext();

  @Mock
  private SlingHttpServletResponse response;

  @Mock
  private SlingHttpServletRequest request;

  @Mock
  private ServletOutputStream servletOutputStream;

  @Captor
  private ArgumentCaptor<byte[]> outputStream;

  @Mock
  private ResourceResolver resourceResolver;

  @Mock
  private Resource resource;
  private FetchDataFromPathServlet fetchDataFromPathServlet;

  @Test
  void testDoGet() throws IOException, ServletException {
    when(request.getParameter(PATH)).thenReturn("/content/vishnu-project/ind/en");
    Resource pageResource = context.load(true)
        .json("/com/aurx/core/servlets/FetchDataFromPathServletTestResource/DummyDataOfPath.json",
            "/content/vishnu-project/ind/en");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource("/content/vishnu-project/ind/en")).thenReturn(resource);
    when(resource.getChild(JCR_CONTENT)).thenReturn(pageResource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    fetchDataFromPathServlet = context.registerInjectActivateService(
        new FetchDataFromPathServlet());
    fetchDataFromPathServlet.doGet(request, response);
    assertEquals(200, context.response().getStatus());
    verify(servletOutputStream).write(outputStream.capture());
    String responseJson = new String(outputStream.getValue());
    assertNotNull(responseJson);
    assertTrue(responseJson.contains(ONE));
  }

  @Test
  void testNullValueOfProperty() throws IOException, ServletException {
    when(request.getParameter(PATH)).thenReturn("/content/vishnu-project/ind/en");
    Resource pageResource = context.load(true)
        .json(
            "/com/aurx/core/servlets/FetchDataFromPathServletTestResource/DummyNullValueofPathProperty.json",
            "/content/vishnu-project/ind/en");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource("/content/vishnu-project/ind/en")).thenReturn(resource);
    when(resource.getChild(JCR_CONTENT)).thenReturn(pageResource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    fetchDataFromPathServlet = context.registerInjectActivateService(
        new FetchDataFromPathServlet());
    fetchDataFromPathServlet.doGet(request, response);
    assertEquals(200, context.response().getStatus());
    verify(servletOutputStream).write(outputStream.capture());
    String responseJson = new String(outputStream.getValue());
    assertNotNull(responseJson);
    assertTrue(responseJson.contains(TWO));
  }

  @Test
  void testFetchDataServletWithNullResource() throws IOException, ServletException {
    when(request.getParameter(PATH)).thenReturn("/content/vishnu-project/ind/en");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource("/content/vishnu-project/ind/en")).thenReturn(null);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    fetchDataFromPathServlet = context.registerInjectActivateService(
        new FetchDataFromPathServlet());
    fetchDataFromPathServlet.doGet(request, response);
    assertEquals(200, context.response().getStatus());
    verify(servletOutputStream).write(outputStream.capture());
    String responseJson = new String(outputStream.getValue());
    assertNotNull(responseJson);
    assertTrue(responseJson.contains(THREE));
  }
}