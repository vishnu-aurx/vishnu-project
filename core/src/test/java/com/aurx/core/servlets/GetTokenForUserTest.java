package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.API_KEY;
import static com.aurx.core.constant.ApplicationConstants.APP_ID_PATH;
import static com.aurx.core.constant.ApplicationConstants.APP_ID_TIME_PATH;
import static com.aurx.core.constant.ApplicationConstants.APP_KEY;
import static com.aurx.core.constant.ApplicationConstants.INVALID_API_KEY;
import static com.aurx.core.constant.ApplicationConstants.TOKEN;
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
class GetTokenForUserTest {

  private final AemContext context = new AemContext();

  @Mock
  private SlingHttpServletResponse response;

  @Mock
  private SlingHttpServletRequest request;

  @Mock
  private ServletOutputStream servletOutputStream;

  @Captor
  private ArgumentCaptor<byte[]> argumentCaptor;

  @Mock
  private ResourceResolver resourceResolver;

  @Mock
  private Resource resource;

  private GetTokenForUser getTokenForUser;


  @Test
  void testDoGet() throws IOException, ServletException {
    Resource apiIdResource = context.load(true)
        .json("/com/aurx/core/servlets/GetTokenForUserTestResource/API_IDResource.json",
            "/etc/api-data/app-id");
    Resource apiKeyResource = context.load(true)
        .json("/com/aurx/core/servlets/GenerateUserAPIKeyTestResource/APIKeysResource.json",
            "/etc/api-data/api-key");
    Resource apiTimeResource = context.load(true)
        .json("/com/aurx/core/servlets/GetTokenForUserTestResource/API_ID_TimeResource.json",
            "/etc/api-data/app-id-time");
    when(request.getParameter(APP_KEY)).thenReturn("user3864235");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource(APP_ID_PATH)).thenReturn(apiIdResource);
    when(resourceResolver.getResource(API_KEY)).thenReturn(apiKeyResource);
    when(resourceResolver.getResource(APP_ID_TIME_PATH)).thenReturn(apiTimeResource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    getTokenForUser = context.registerInjectActivateService(new GetTokenForUser());
    getTokenForUser.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains(TOKEN));
  }

  @Test
  void testAppKeyNull() throws IOException, ServletException {
    when(request.getParameter(APP_KEY)).thenReturn(null);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    getTokenForUser = context.registerInjectActivateService(new GetTokenForUser());
    getTokenForUser.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains(INVALID_API_KEY));
  }

  @Test
  void testInvalidAppKey() throws IOException, ServletException {
    Resource apiIdResource = context.load(true)
        .json("/com/aurx/core/servlets/GetTokenForUserTestResource/API_IDResource.json",
            "/etc/api-data/app-id");
    Resource apiKeyResource = context.load(true)
        .json("/com/aurx/core/servlets/GenerateUserAPIKeyTestResource/APIKeysResource.json",
            "/etc/api-data/api-key");
    when(request.getParameter(APP_KEY)).thenReturn("user3864235904090");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource(APP_ID_PATH)).thenReturn(apiIdResource);
    when(resourceResolver.getResource(API_KEY)).thenReturn(apiKeyResource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    getTokenForUser = context.registerInjectActivateService(new GetTokenForUser());
    getTokenForUser.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains(INVALID_API_KEY));
  }
}