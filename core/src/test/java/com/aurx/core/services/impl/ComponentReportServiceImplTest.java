package com.aurx.core.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
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
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ComponentReportServiceImplTest {

  private final AemContext context = new AemContext();

  private ComponentReportServiceImpl componentReportService;

  @Mock
  private QueryBuilderUtil queryBuilderUtil;
  @Mock
  private ResourceResolverFactory resourceResolverFactory;

  @Mock
  private SearchResult result;

  @Mock
  private Hit hit1, hit2;

  @Test
  void fetchComponents() throws RepositoryException, IOException {
    configureQueryBuilder();
    componentReportService = context.registerInjectActivateService(
        new ComponentReportServiceImpl());
    assertNotNull(componentReportService);
    assertNotNull(componentReportService.fetchComponents());
    assertEquals(2, componentReportService.fetchComponents().size());
  }

  @Test
  void componentList() throws RepositoryException {
    configureQueryBuilder();
    componentReportService = context.registerInjectActivateService(
        new ComponentReportServiceImpl());
    assertNotNull(componentReportService);
    assertNotNull(componentReportService.comopnentList());
    assertEquals(2, componentReportService.comopnentList().size());
  }

  @Test
  void componentReportServiceWithRepositoryException() throws RepositoryException {
    context.registerService(ResourceResolverFactory.class, resourceResolverFactory);
    context.registerService(QueryBuilderUtil.class, queryBuilderUtil);
    when(queryBuilderUtil.getQueryBuilderResult(any(ResourceResolver.class), anyMap())).thenReturn(
        result);
    List<Hit> hits = new ArrayList<>();
    hits.add(hit1);
    hits.add(hit2);
    when(result.getHits()).thenReturn(hits);
    Resource productDetails = context.load(true)
        .json(
            "/com/aurx/core/services/ComponentReportServiceResource/ProductDetailsComponentsResource.json",
            "/apps/vishnu-project/components/product-details");
    Resource resource2 = context.load(true)
        .json(
            "/com/aurx/core/services/ComponentReportServiceResource/StudentsDetailsComponentsResource.json",
            "/apps/vishnu-project/components/Student-details");
    when(hits.get(0).getResource()).thenThrow(new RepositoryException("Custom RepositoryException"));
    when(hits.get(1).getResource()).thenThrow(new RepositoryException("Custom RepositoryException"));
    componentReportService = context.registerInjectActivateService(
        new ComponentReportServiceImpl());
    assertNotNull(componentReportService);
    assertNotNull(componentReportService.comopnentList());
    assertEquals(0, componentReportService.comopnentList().size());
  }


  private void configureQueryBuilder() throws RepositoryException {
    context.registerService(ResourceResolverFactory.class, resourceResolverFactory);
    context.registerService(QueryBuilderUtil.class, queryBuilderUtil);
    when(queryBuilderUtil.getQueryBuilderResult(any(ResourceResolver.class), anyMap())).thenReturn(
        result);
    List<Hit> hits = new ArrayList<>();
    hits.add(hit1);
    hits.add(hit2);
    when(result.getHits()).thenReturn(hits);
    Resource productDetailsResource = context.load(true)
        .json(
            "/com/aurx/core/services/ComponentReportServiceResource/ProductDetailsComponentsResource.json",
            "/apps/vishnu-project/components/product-details");
    Resource studentDetailsResource = context.load(true)
        .json(
            "/com/aurx/core/services/ComponentReportServiceResource/StudentsDetailsComponentsResource.json",
            "/apps/vishnu-project/components/Student-details");
    when(hits.get(0).getResource()).thenReturn(productDetailsResource);
    when(hits.get(1).getResource()).thenReturn(studentDetailsResource);
  }
}