package com.aurx.core.services;

/**
 * this is MoviesService interface
 */
public interface MoviesService {

    /**
     * this method used to fetchAllMoviesName
     * @return String array
     */
    String [] fetchAllMoviesName();

    /**
     * this method used to check is service enable or not
     * @return boolean
     */
    boolean isEnabled();
}
