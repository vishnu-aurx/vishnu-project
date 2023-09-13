package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.PATH;
import static com.aurx.core.constant.ApplicationConstants.TWO;
import static com.aurx.core.constant.ApplicationConstants.VALUE;
import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;
import static org.junit.jupiter.api.Assertions.*;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SaveDataFromPathServletTest {

  private final AemContext context = new AemContext();

  @Mock
  private SlingHttpServletResponse response;

  @Mock
  private SlingHttpServletRequest request;

  @Mock
  private ServletOutputStream servletOutputStream;

  @Mock
  private ResourceResolver resourceResolver;

  @Mock
  private Resource resource;

  @Captor
  private ArgumentCaptor<byte[]> argumentCaptor;

  private SaveDataFromPathServlet saveDataFromPathServlet;

  @Test
  void doGet() throws IOException, ServletException {
    Resource pageResource = context.load(true)
        .json("/com/aurx/core/servlets/FetchDataFromPathServletTestResource/DummyDataOfPath.json",
            "/content/vishnu-project/ind/en");
    when(request.getParameter(PATH)).thenReturn("/content/vishnu-project/ind/en");
    when(request.getParameter(VALUE)).thenReturn("11");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource("/content/vishnu-project/ind/en")).thenReturn(resource);
    when(resource.getChild(JCR_CONTENT)).thenReturn(pageResource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    saveDataFromPathServlet = context.registerInjectActivateService(
        new SaveDataFromPathServlet());
    saveDataFromPathServlet.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains("11"));
  }

  @Test
  void testNullResource() throws IOException, ServletException {
    when(request.getParameter(PATH)).thenReturn("/content/vishnu-project/ind/en");
    when(request.getParameter(VALUE)).thenReturn("11");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource("/content/vishnu-project/ind/en")).thenReturn(null);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    saveDataFromPathServlet = context.registerInjectActivateService(
        new SaveDataFromPathServlet());
    saveDataFromPathServlet.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains(TWO));
  }
}