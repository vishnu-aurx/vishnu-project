package com.aurx.core.services.impl;

import com.aurx.core.services.MoviesService;
import com.aurx.core.services.config.MoviesConfiguration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to fetch movie names from the Movies Configuration.
 */
@Designate(ocd = MoviesConfiguration.class)
@Component(service = MoviesService.class, immediate = true)
public class MoviesServiceImp implements MoviesService {

  /**
   * movies - The movies.
   */
  private String[] movies;

  /**
   * isEnabled -  The isEnabled.
   */
  private boolean isEnabled;

  /**
   * moviesConfiguration - MoviesConfiguration Object.
   */
  protected MoviesConfiguration moviesConfiguration;

  /**
   * logger - Logger object.
   */
  private static final Logger logger = LoggerFactory.getLogger(MoviesServiceImp.class);

  /**
   * This method is invoked when the service is activated.
   *
   * @param moviesConfiguration - The moviesConfiguration.
   */
  @Activate
  protected void activate(MoviesConfiguration moviesConfiguration) {
    logger.info("activate method start");
    this.moviesConfiguration = moviesConfiguration;
    movies = moviesConfiguration.movies();
    isEnabled = moviesConfiguration.isEnable();
    logger.info("activate method end");
  }

  /**
   * This method is invoked when the Configuration is modified.
   *
   * @param moviesConfiguration - The moviesConfiguration.
   */
  @Modified
  protected void modified(MoviesConfiguration moviesConfiguration) {
    logger.info("modified method start");
    this.moviesConfiguration = moviesConfiguration;
    isEnabled = moviesConfiguration.isEnable();
    movies = moviesConfiguration.movies();
    logger.info("modified method end");
  }


  /**
   * This method returns the movies.
   *
   * @return - The movies.
   */
  @Override
  public String[] fetchAllMoviesName() {
    return movies;
  }

  /**
   * This method returns a boolean value based on the Movies Configuration Enabled checkbox.
   *
   * @return - The boolean value.
   */
  @Override
  public boolean isEnabled() {
    return isEnabled;
  }
}
