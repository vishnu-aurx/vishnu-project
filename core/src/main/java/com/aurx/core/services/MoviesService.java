package com.aurx.core.services;

/**
 * Interface MoviesService
 */
public interface MoviesService {

  /**
   * This method return all movies name.
   *
   * @return - All Movies Name.
   */
  String[] fetchAllMoviesName();

  /**
   * This method returns a boolean value based on the Movies Configuration Enabled checkbox.
   *
   * @return - The boolean value
   */
  boolean isEnabled();
}
