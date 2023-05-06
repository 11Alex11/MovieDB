/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.controller.dto;

import com.aw.MovieDatabase.dao.CastMemberDao;
import com.aw.MovieDatabase.dao.GenreDao;
import com.aw.MovieDatabase.dao.MovieDao;
import com.aw.MovieDatabase.dto.CastMember;
import com.aw.MovieDatabase.dto.Genre;
import com.aw.MovieDatabase.dto.Movie;
import com.aw.MovieDatabase.dto.MovieCastMember;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Alex Wood
 */
@Controller
public class APIController {
    @Autowired
    MovieDao movieDao;
    
    @Autowired
    GenreDao genreDao;
    
    @Autowired
    CastMemberDao castDao;
    
    
    @GetMapping("api")
    public String displayApi(){
        return "api";
    }

    @PostMapping("addApiMovie")
    public String addMovie(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes){
        Movie movie = new Movie();
        List<Genre> genres = new ArrayList<>();
        
        List<MovieCastMember> directors = new ArrayList<>();
        List<MovieCastMember> writers = new ArrayList<>();
        List<MovieCastMember> actors = new ArrayList<>();
        
        String[] genreNames = request.getParameterValues("genreName");
        String[] castImdbIds = request.getParameterValues("castImdbId");
        String[] roles = request.getParameterValues("castMemberRole");        
        
        
        // Import genres
        if(genreNames!=null){
            for(String genreName: genreNames){
                Genre genre = genreDao.getGenreByName(genreName);
                // Only add if doesn't exist
                if(genre == null){
                    genre = new Genre();
                    genre.setName(genreName);
                    genre = genreDao.addGenre(genre);
                }
                genres.add(genre);
            }
        }
        
        
        
        // Create directors, writers, actors list for movie
        if(castImdbIds!=null){
        for(int i =0; i < castImdbIds.length; i++){
            
            MovieCastMember movieCastMember = new MovieCastMember();
            if(castImdbIds[i].isEmpty()){
                continue;
            }
            CastMember member = castDao.getCastMemberByImdbId(castImdbIds[i]);
            if(member==null){
                continue;
            }
            System.out.println(roles[i]);
            movieCastMember.setRole(roles[i]);
            movieCastMember.setRoleRelevance(i);
            movieCastMember.setMovie(movie);
            movieCastMember.setCastMember(member);
            if(roles[i].toLowerCase().equals("director")){
                directors.add(movieCastMember);
                
            }
            else if(roles[i].toLowerCase().equals("writer")){
                writers.add(movieCastMember);
            }
            else{
                actors.add(movieCastMember);
            }
        }
        }
        movie.setGenres(genres);
        movie.setActors(actors);
        movie.setDirectors(directors);
        movie.setWriters(writers);


        
        movie.setImdbId(request.getParameter("movieImdbId"));
        movie.setTitle(request.getParameter("movieTitle"));
        movie.setDescription(request.getParameter("movieDescription"));
        movie.setPlot(request.getParameter("moviePlot"));
        movie.setImageUrl(request.getParameter("movieImageUrl"));
        String dateString = request.getParameter("movieReleaseDate");
        LocalDate date = null;
        if(dateString!=null &&!dateString.isEmpty()){
            date = LocalDate.parse(dateString);
        }
        movie.setReleaseDate(date);
        movie.setContentRating(request.getParameter("movieContentRating"));
        String lengthString = request.getParameter("movieLength");
        int length = 0;
        if(lengthString!=null){
            length = Integer.parseInt(lengthString);
        }
        movie.setLength(length);
        String popularityString = request.getParameter("moviePopularity");
        int popularity = 0;
        if(popularityString!=null){
            popularity = Integer.parseInt(popularityString);
        }
        movie.setPopularity(popularity);
        String userRatingString = request.getParameter("movieUserRating");
        float userRating = 1;
        if(userRatingString!=null){
            userRating = Float.parseFloat(userRatingString);
        }
        movie.setUserRating(userRating);

        
        Movie daoMovie = movieDao.getMovieByNameAndReleaseDate(movie.getTitle(),movie.getReleaseDate());
        if(daoMovie==null){
            movieDao.addMovie(movie);
        }
        else{
            movie.setId(daoMovie.getId());
            movieDao.updateMovie(movie);
            
        }
        redirectAttributes.addAttribute("id",movie.getId());
        return "redirect:/movieInfo";
        
    }
    @PostMapping("addApiCastMember")
    public String addCastmember(CastMember castMember,HttpServletRequest request, Model model, RedirectAttributes redirectAttributes){
        castMember.setImdbId(request.getParameter("imdbId"));
        castMember.setImageUrl(request.getParameter("imageUrl"));
        castMember.setBio(request.getParameter("bio"));
        castMember.setBioFull(request.getParameter("bioFull"));
        castMember.setName(request.getParameter("name"));
        String dateString = request.getParameter("birthdate");
        LocalDate date = null;
        if(dateString!=null&&!dateString.isEmpty()){
            date = LocalDate.parse(dateString);
        }
        castMember.setBirthdate(date);
        castMember.setBirthplace(request.getParameter("birthplace"));
        
        CastMember daoCast = castDao.getCastMemberByImdbId(castMember.getImdbId());
        if(daoCast!=null){
            castMember.setId(daoCast.getId());
            castDao.updateCastMember(castMember);
        }
        else{
            castDao.addCastMember(castMember);
        }
        redirectAttributes.addAttribute("id",castMember.getId());
        return "redirect:/castMemberInfo";
    }
}
