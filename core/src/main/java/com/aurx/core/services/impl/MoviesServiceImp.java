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
//        movies=this.moviesConfiguration.movies();
        movies = moviesConfiguration.movies();
       // movies = toUpperCaseMovieName();
        isEnabled = moviesConfiguration.isEnable();
       // productMovies = getMovieName();
    }

    @Modified
    protected void modified(MoviesConfiguration moviesConfiguration) {
        this.moviesConfiguration = moviesConfiguration;
//        movies=this.moviesConfiguration.movies();
//        movies = toUpperCaseMovieName();
        isEnabled = moviesConfiguration.isEnable();
        movies = moviesConfiguration.movies();
//        productMovies = getMovieName();
    }


    private String[] toUpperCaseMovieName() {

        if (this.moviesConfiguration.isEnable()) {
            String[] upperCaseMovieName = this.moviesConfiguration.movies();
            int i = 0;
            while (i < upperCaseMovieName.length) {
                if (upperCaseMovieName[i] != null) upperCaseMovieName[i] = upperCaseMovieName[i].toUpperCase();
                i++;
            }

            return upperCaseMovieName;
        }
        return null;
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
