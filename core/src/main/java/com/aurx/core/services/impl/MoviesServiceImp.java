package com.aurx.core.services.impl;

import com.aurx.core.services.MoviesService;
import com.aurx.core.services.config.MoviesConfiguration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.Oneway;

@Designate(ocd = MoviesConfiguration.class)
@Component(service = MoviesService.class, immediate = true)
public class MoviesServiceImp implements MoviesService {

    private String[] movies;
    private String[] productMovies;

    private boolean isEnabled;
    private MoviesConfiguration moviesConfiguration;
    Logger logger= LoggerFactory.getLogger(MoviesServiceImp.class);

    @Activate
    protected void activate(MoviesConfiguration moviesConfiguration) {
        logger.info("activate method start");
        this.moviesConfiguration = moviesConfiguration;
        movies = moviesConfiguration.movies();
        isEnabled = moviesConfiguration.isEnable();
        logger.info("activate method end");
    }

    @Modified
    protected void modified(MoviesConfiguration moviesConfiguration) {
        logger.info("modified method start");
        this.moviesConfiguration = moviesConfiguration;
        isEnabled = moviesConfiguration.isEnable();
        movies = moviesConfiguration.movies();
        logger.info("modified method end");
    }


    @Override
    public String[] fetchAllMoviesName() {
        return movies;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
