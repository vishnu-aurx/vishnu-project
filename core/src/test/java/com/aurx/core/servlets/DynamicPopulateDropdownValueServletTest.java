package com.aurx.core.servlets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
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
class DynamicPopulateDropdownValueServletTest {

  private final AemContext context = new AemContext();

  @Mock
  private SlingHttpServletRequest request;

  @Mock
  private SlingHttpServletResponse response;

  @Mock
  private ServletOutputStream servletOutputStream;

  @Captor
  private ArgumentCaptor<byte[]> outputStream;

  private DynamicPopulateDropdownValueServlet dynamicPopulateDropdownValueServlet;
  @Mock
  private ResourceResolver resourceResolver;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testDoGet() throws IOException, ServletException {
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    dynamicPopulateDropdownValueServlet = context.registerInjectActivateService(new DynamicPopulateDropdownValueServlet());
    dynamicPopulateDropdownValueServlet.doGet(request, response);
    assertEquals(200, context.response().getStatus());
    verify(servletOutputStream).write(outputStream.capture());
    String responseJson = new String(outputStream.getValue());
    assertNotNull(responseJson);
  }
}