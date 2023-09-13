package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.ERROR;
import static com.aurx.core.constant.ApplicationConstants.SUCCESSFUL;
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
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class PageLoadCountServletTest {

  private final AemContext context =  new AemContext();

  private PageLoadCountServlet pageLoadCountServlet;

  @Mock
  private SlingHttpServletRequest request;

  @Mock
  private SlingHttpServletResponse response;

  @Mock
  private ServletOutputStream servletOutputStream;

  @Captor
  private ArgumentCaptor<byte[]> outputStream;

  @Test
  void testDoGet() throws ServletException, IOException {
    Resource resource = context.load(true).json("/com/aurx/core/servlets/PageLoadCountServletResource/PageLoadCountServletResource.json","/content/vishnu-project/myfrinds/testdummypage");
    when(request.getResource()).thenReturn(resource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    pageLoadCountServlet = context.registerInjectActivateService(new PageLoadCountServlet());
    pageLoadCountServlet.doGet(request, response);
    assertEquals(200, context.response().getStatus());
    verify(servletOutputStream).write(outputStream.capture());
    String responseJson = new String(outputStream.getValue());
    assertNotNull(responseJson);
    assertTrue(responseJson.contains(SUCCESSFUL));
  }

  @Test
  void pageLoadCountServletWithNullResource() throws ServletException, IOException, LoginException {
    when(request.getResource()).thenReturn(null);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    pageLoadCountServlet = context.registerInjectActivateService(new PageLoadCountServlet());
    pageLoadCountServlet.doGet(request, response);
    assertEquals(200, context.response().getStatus());
    verify(servletOutputStream).write(outputStream.capture());
    String responseJson = new String(outputStream.getValue());
    assertNotNull(responseJson);
    assertTrue(responseJson.contains(ERROR));
  }
}