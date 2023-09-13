package com.aurx.core.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.aurx.core.models.ProductDetailsModel;
import com.aurx.core.services.MoviesService;
import com.aurx.core.services.config.MoviesConfiguration;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class MoviesServiceImpTest {

  private final AemContext context = new AemContext();

  private MoviesServiceImp moviesServiceImp;

  @Mock
  private MoviesConfiguration moviesConfiguration;

  @Test
  void fetchAllMoviesName() {
    when(moviesConfiguration.movies()).thenReturn(new String[]{"vishnu","rahul"});
    moviesServiceImp = context.registerInjectActivateService(new MoviesServiceImp());
    moviesServiceImp.activate(moviesConfiguration);
    assertNotNull(moviesServiceImp);
    assertNotNull(moviesServiceImp.fetchAllMoviesName());
  }

  @Test
  void fetchAllMoviesNameWithNullMoviesName() {
    moviesServiceImp = context.registerInjectActivateService(new MoviesServiceImp());
    moviesServiceImp.activate(moviesConfiguration);
    assertNotNull(moviesServiceImp);
    assertNull(moviesServiceImp.fetchAllMoviesName());
  }
  @Test
  void isEnabled() {
    when(moviesConfiguration.isEnable()).thenReturn(true);
    moviesServiceImp = context.registerInjectActivateService(new MoviesServiceImp());
    moviesServiceImp.activate(moviesConfiguration);
    assertNotNull(moviesServiceImp);
    assertTrue(moviesServiceImp.isEnabled());
  }
  @Test
  void isDisabled() {
    when(moviesConfiguration.isEnable()).thenReturn(false);
    moviesServiceImp = context.registerInjectActivateService(new MoviesServiceImp());
    moviesServiceImp.activate(moviesConfiguration);
    assertNotNull(moviesServiceImp);
    assertFalse(moviesServiceImp.isEnabled());
  }
}