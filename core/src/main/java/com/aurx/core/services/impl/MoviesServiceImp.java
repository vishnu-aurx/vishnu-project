package com.aurx.core.services.impl;

import com.aurx.core.services.MoviesService;
import com.aurx.core.services.config.MoviesConfiguration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

import javax.jws.Oneway;

@Designate(ocd = MoviesConfiguration.class)
@Component(service = MoviesService.class, immediate = true)
public class MoviesServiceImp implements MoviesService {

    private String[] movies;
    private String[] productMovies;

    private boolean isEnabled;
    private MoviesConfiguration moviesConfiguration;

    @Activate
    protected void activate(MoviesConfiguration moviesConfiguration) {
        this.moviesConfiguration = moviesConfiguration;
        movies = moviesConfiguration.movies();
        isEnabled = moviesConfiguration.isEnable();
    }

    @Modified
    protected void modified(MoviesConfiguration moviesConfiguration) {
        this.moviesConfiguration = moviesConfiguration;
        isEnabled = moviesConfiguration.isEnable();
        movies = moviesConfiguration.movies();
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
