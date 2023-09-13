package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.API_KEY;
import static com.aurx.core.constant.ApplicationConstants.APP_ID;
import static com.aurx.core.constant.ApplicationConstants.APP_ID_PATH;
import static com.aurx.core.constant.ApplicationConstants.APP_ID_TIME_PATH;
import static com.aurx.core.constant.ApplicationConstants.INVALID_API_KEY;
import static com.aurx.core.constant.ApplicationConstants.TOKEN_EXPIRE_MSG;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class GetUserFromTokenTest {

  private final AemContext context = new AemContext();

  @Mock
  private SlingHttpServletRequest request;

  @Mock
  private SlingHttpServletResponse response;

  @Mock
  private ServletOutputStream servletOutputStream;

  @Mock
  private ResourceResolver resourceResolver;

  @Captor
  private ArgumentCaptor<byte[]> argumentCaptor;

  private GetUserFromToken getUserFromToken;

  @Test
  void testDoGet() throws IOException, ServletException {
    Resource apiIdResource = context.load(true)
        .json("/com/aurx/core/servlets/GetTokenForUserTestResource/API_IDResource.json",
            "/etc/api-data/app-id");
    Resource apiTimeResource = context.load(true)
        .json("/com/aurx/core/servlets/GetUserFromTokenResource/API_ID_TimeNotExpireResource.json",
            "/etc/api-data/app-id-time");
    Resource apiKeyResource = context.load(true)
        .json("/com/aurx/core/servlets/GenerateUserAPIKeyTestResource/APIKeysResource.json",
            "/etc/api-data/api-key");
    when(request.getParameter(APP_ID)).thenReturn("token221");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource(APP_ID_PATH)).thenReturn(apiIdResource);
    when(resourceResolver.getResource(APP_ID_TIME_PATH)).thenReturn(apiTimeResource);
    when(resourceResolver.getResource(API_KEY)).thenReturn(apiKeyResource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    getUserFromToken = context.registerInjectActivateService(new GetUserFromToken());
    getUserFromToken.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains("Vishnu"));
  }

  @Test
  void testTokenExpire() throws IOException, ServletException {
    Resource apiIdResource = context.load(true)
        .json("/com/aurx/core/servlets/GetTokenForUserTestResource/API_IDResource.json",
            "/etc/api-data/app-id");
    Resource apiTimeResource = context.load(true)
        .json("/com/aurx/core/servlets/GetTokenForUserTestResource/API_ID_TimeResource.json",
            "/etc/api-data/app-id-time");
    when(request.getParameter(APP_ID)).thenReturn("token221");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource(APP_ID_PATH)).thenReturn(apiIdResource);
    when(resourceResolver.getResource(APP_ID_TIME_PATH)).thenReturn(apiTimeResource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    getUserFromToken = context.registerInjectActivateService(new GetUserFromToken());
    getUserFromToken.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains(TOKEN_EXPIRE_MSG));
  }

  @Test
  void testAppIdNull() throws IOException, ServletException {
    when(request.getParameter(APP_ID)).thenReturn(null);
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    getUserFromToken = context.registerInjectActivateService(new GetUserFromToken());
    getUserFromToken.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains(INVALID_API_KEY));
  }

  @Test
  void testInvalidToken() throws IOException, ServletException {
    Resource apiIdResource = context.load(true)
        .json("/com/aurx/core/servlets/GetTokenForUserTestResource/API_IDResource.json",
            "/etc/api-data/app-id");
    Resource apiTimeResource = context.load(true)
        .json("/com/aurx/core/servlets/GetUserFromTokenResource/API_ID_TimeNotExpireResource.json",
            "/etc/api-data/app-id-time");
    when(request.getParameter(APP_ID)).thenReturn("token2210943209432");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource(APP_ID_PATH)).thenReturn(apiIdResource);
    when(resourceResolver.getResource(APP_ID_TIME_PATH)).thenReturn(apiTimeResource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    getUserFromToken = context.registerInjectActivateService(new GetUserFromToken());
    getUserFromToken.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains(TOKEN_EXPIRE_MSG));
  }

  @Test
  void testGetUserFromTokenWithZeroUser() throws IOException, ServletException {
    Resource apiIdResource = context.load(true)
        .json("/com/aurx/core/servlets/GetTokenForUserTestResource/API_IDResource.json",
            "/etc/api-data/app-id");
    Resource apiTimeResource = context.load(true)
        .json("/com/aurx/core/servlets/GetUserFromTokenResource/API_ID_TimeNotExpireResource.json",
            "/etc/api-data/app-id-time");
    Resource apiKeyResource = context.load(true)
        .json("/com/aurx/core/servlets/GenerateUserAPIKeyTestResource/APIKeysResource.json",
            "/etc/api-data/api-key");
    when(request.getParameter(APP_ID)).thenReturn("token977");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource(APP_ID_PATH)).thenReturn(apiIdResource);
    when(resourceResolver.getResource(APP_ID_TIME_PATH)).thenReturn(apiTimeResource);
    when(resourceResolver.getResource(API_KEY)).thenReturn(apiKeyResource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    getUserFromToken = context.registerInjectActivateService(new GetUserFromToken());
    getUserFromToken.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains(INVALID_API_KEY));
  }
}