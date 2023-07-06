package com.aurx.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.aurx.core.pojo.ComponentReport;
import com.aurx.core.services.ComponentReportService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ComponentReportModelTest {

  private final AemContext context = new AemContext();

  private ComponentReportModel componentReportModel;

  @Mock
  private ComponentReportService componentReportService;

  private ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    context.registerService(ComponentReportService.class, componentReportService);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testGetComponentReportList() throws IOException {

    String response = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream(
            "/com/aurx/core/models/ComponentReportModel/ComponentReportServiceData.json")),
        StandardCharsets.UTF_8);
    List<ComponentReport> componentPropertyList = mapper.readValue(response,
        new TypeReference<List<ComponentReport>>() {
        });
    when(componentReportService.comopnentList()).thenReturn(componentPropertyList);
    componentReportModel = context.request().adaptTo(ComponentReportModel.class);
    assertNotNull(componentReportModel);
    assertNotNull(componentReportModel.getComponentReportList());
    assertEquals(2, componentReportModel.getComponentReportList().size());
  }

  @Test
  void testGetComponentReportListWithNullServiceResponse() throws IOException {
    when(componentReportService.comopnentList()).thenReturn(null);
    componentReportModel = context.request().adaptTo(ComponentReportModel.class);
    assertNotNull(componentReportModel);
    assertNull(componentReportModel.getComponentReportList());

  }
}