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
 * This Service get The Movies name from MoviesConfiguration
 */
@Designate(ocd = MoviesConfiguration.class)
@Component(service = MoviesService.class, immediate = true)
public class MoviesServiceImp implements MoviesService {

  /**
   * movies -String[] object
   */
  private String[] movies;

  /**
   * isEnabled - isEnabled object
   */
  private boolean isEnabled;
  /**
   * moviesConfiguration -  MoviesConfiguration object
   */
  MoviesConfiguration moviesConfiguration;
  /**
   * logger - Logger object
   */
  private static final Logger logger = LoggerFactory.getLogger(MoviesServiceImp.class);

  /**
   * this method used to fetch data to activation of bundle
   *
   * @param moviesConfiguration
   */
  @Activate
  protected void activate(MoviesConfiguration moviesConfiguration) {
    logger.info("=================activate method start===========");
    this.moviesConfiguration = moviesConfiguration;
    movies = moviesConfiguration.movies();
    isEnabled = moviesConfiguration.isEnable();
    logger.info("activate method end=========isEnabled : {} ,movies :{}", isEnabled, movies);
  }

  /**
   * this method used to fetch data to modification of bundle
   *
   * @param moviesConfiguration
   */
  @Modified
  protected void modified(MoviesConfiguration moviesConfiguration) {
    logger.info("==========modified method start===========");
    this.moviesConfiguration = moviesConfiguration;
    isEnabled = moviesConfiguration.isEnable();
    movies = moviesConfiguration.movies();
    logger.info("========modified method end=======isEnabled : {} ,movies :{}", isEnabled, movies);
  }

  /**
   * fetchAllMoviesName method return movies name from configuration
   *
   * @return movies
   */
  @Override
  public String[] fetchAllMoviesName() {
    return movies;
  }

  /**
   * isEnabled method return true or false  name from configuration checkbox
   *
   * @return isEnabled
   */
  @Override
  public boolean isEnabled() {
    return isEnabled;
  }
}
