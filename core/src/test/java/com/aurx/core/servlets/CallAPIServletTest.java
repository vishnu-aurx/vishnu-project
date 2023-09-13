package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.ERROR;
import static com.aurx.core.constant.ApplicationConstants.PASSWORD;
import static com.aurx.core.constant.ApplicationConstants.TOKEN_API_URL;
import static com.aurx.core.constant.ApplicationConstants.USERNAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aurx.core.services.PopulateDataFromAPI;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.service.component.annotations.Reference;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class CallAPIServletTest {

  private final AemContext context = new AemContext();

  @Mock
  private PopulateDataFromAPI populateDataFromAPI;

  @Mock
  private SlingHttpServletResponse response;

  @Mock
  private SlingHttpServletRequest request;

  @Mock
  private ServletOutputStream servletOutputStream;

  @Captor
  private ArgumentCaptor<byte[]> outputStream;

  @Mock
  private HttpResponse httpResponse;

  @Mock
  private HttpEntity httpEntity;

  private CallAPIServlet callAPIServlet;

  @Test
  void testDoGet() throws IOException, ServletException {
    context.registerService(PopulateDataFromAPI.class, populateDataFromAPI);
    InputStream apiResponse = getClass().getResourceAsStream(
        "/com/aurx/core/servlets/CallAPIServletResource/CallAPIServletDummyData.json");
    when(populateDataFromAPI.getAPIResponseWithUserPassword(TOKEN_API_URL, USERNAME,
        PASSWORD)).thenReturn(httpResponse);
    when(httpResponse.getEntity()).thenReturn(httpEntity);
    when(httpResponse.getEntity().getContent()).thenReturn(apiResponse);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    callAPIServlet = context.registerInjectActivateService(new CallAPIServlet());
    callAPIServlet.doGet(request, response);
    assertEquals(200, context.response().getStatus());
    verify(servletOutputStream).write(outputStream.capture());
    String responseJson = new String(outputStream.getValue());
    assertNotNull(responseJson);
    assertTrue(responseJson.contains("token"));
  }

  @Test
  void testCallAPIServletWithNullResponse() throws IOException, ServletException {
    context.registerService(PopulateDataFromAPI.class, populateDataFromAPI);
    when(populateDataFromAPI.getAPIResponseWithUserPassword(TOKEN_API_URL, USERNAME,
        PASSWORD)).thenReturn(null);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    callAPIServlet = context.registerInjectActivateService(new CallAPIServlet());
    callAPIServlet.doGet(request, response);
    assertEquals(200, context.response().getStatus());
    verify(servletOutputStream).write(outputStream.capture());
    String responseJson = new String(outputStream.getValue());
    assertNotNull(responseJson);
    assertTrue(responseJson.contains(ERROR));
  }

}