package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.APIKEY;
import static com.aurx.core.constant.ApplicationConstants.API_KEY;
import static com.aurx.core.constant.ApplicationConstants.EMAIL;
import static com.aurx.core.constant.ApplicationConstants.INVALID_EMAIL;
import static com.aurx.core.constant.ApplicationConstants.USER_NAME;
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
class GenerateUserAPIKeyTest {

  private final AemContext context = new AemContext();

  @Mock
  private SlingHttpServletRequest request;

  @Mock
  private SlingHttpServletResponse response;

  @Mock
  private ServletOutputStream servletOutputStream;

  @Captor
  private ArgumentCaptor<byte[]> argumentCaptor;

  private GenerateUserAPIKey generateUserAPIKey;

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
    Resource apiKeysResource = context.load(true)
        .json("/com/aurx/core/servlets/GenerateUserAPIKeyTestResource/APIKeysResource.json",
            "/etc/api-data/api-key");
    when(request.getParameter(USER_NAME)).thenReturn("Vishnu");
    when(request.getParameter(EMAIL)).thenReturn("vishnu@g.co");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource(API_KEY)).thenReturn(apiKeysResource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    generateUserAPIKey = context.registerInjectActivateService(new GenerateUserAPIKey());
    generateUserAPIKey.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains("user3864235"));
  }

  @Test
  void testGenerateWithNullUserAndEmail() throws IOException, ServletException {
    Resource apiKeysResource = context.load(true)
        .json("/com/aurx/core/servlets/GenerateUserAPIKeyTestResource/APIKeysResource.json",
            "/etc/api-data/api-key");
    when(request.getParameter(USER_NAME)).thenReturn(null);
    when(request.getParameter(EMAIL)).thenReturn(null);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    generateUserAPIKey = context.registerInjectActivateService(new GenerateUserAPIKey());
    generateUserAPIKey.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains(INVALID_EMAIL));
  }

  @Test
  void testGenerateUserAPIKeyWithZeroEntries() throws IOException, ServletException {
    Resource apiKeysResource = context.load(true)
        .json(
            "/com/aurx/core/servlets/GenerateUserAPIKeyTestResource/APIKeysResourceWithZeroEntries.json",
            "/etc/api-data/api-key");
    when(request.getParameter(USER_NAME)).thenReturn("Vishnu");
    when(request.getParameter(EMAIL)).thenReturn("vishnu@g.co");
    when(request.getResourceResolver()).thenReturn(resourceResolver);
    when(resourceResolver.getResource(API_KEY)).thenReturn(apiKeysResource);
    when(response.getOutputStream()).thenReturn(servletOutputStream);
    generateUserAPIKey = context.registerInjectActivateService(new GenerateUserAPIKey());
    generateUserAPIKey.doGet(request, response);
    verify(servletOutputStream).write(argumentCaptor.capture());
    String jsonResponse = new String(argumentCaptor.getValue());
    assertNotNull(jsonResponse);
    assertTrue(jsonResponse.contains(APIKEY));
  }
}