/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author Alex Wood
 */
public class Movie {
    int id;
    String imdbId;
    @NotBlank(message = "Title must not be empty")
    @Size(max = 50, message = "Title must be less than 50 characters")
    String title;
    @Size(max = 1000, message = "Description must be less that 1000 characters")
    String description;
    @Size(max = 200, message = "Plot must be less that 200 characters")
    String plot;
    LocalDate releaseDate;
    String contentRating;
    int length;
    float userRating;
    int popularity;
    @Size(max = 200, message = "Image URL must be less than 200 characters")
    String imageUrl;
    @Size(max = 200, message = "Video URL must be less than 100 characters")
    String videoUrl;
    List<Genre> genres;
    List<MovieCastMember> directors; 
    List<MovieCastMember> writers;
    List<MovieCastMember> actors;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<MovieCastMember> getDirectors() {
        return directors;
    }

    public void setDirectors(List<MovieCastMember> directors) {
        this.directors = directors;
    }

    public List<MovieCastMember> getWriters() {
        return writers;
    }

    public void setWriters(List<MovieCastMember> writers) {
        this.writers = writers;
    }

    public List<MovieCastMember> getActors() {
        return actors;
    }

    public void setActors(List<MovieCastMember> actors) {
        this.actors = actors;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id;
        hash = 79 * hash + Objects.hashCode(this.imdbId);
        hash = 79 * hash + Objects.hashCode(this.title);
        hash = 79 * hash + Objects.hashCode(this.description);
        hash = 79 * hash + Objects.hashCode(this.plot);
        hash = 79 * hash + Objects.hashCode(this.releaseDate);
        hash = 79 * hash + Objects.hashCode(this.contentRating);
        hash = 79 * hash + this.length;
        hash = 79 * hash + Float.floatToIntBits(this.userRating);
        hash = 79 * hash + this.popularity;
        hash = 79 * hash + Objects.hashCode(this.imageUrl);
        hash = 79 * hash + Objects.hashCode(this.videoUrl);
        hash = 79 * hash + Objects.hashCode(this.genres);
        hash = 79 * hash + Objects.hashCode(this.directors);
        hash = 79 * hash + Objects.hashCode(this.writers);
        hash = 79 * hash + Objects.hashCode(this.actors);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Movie other = (Movie) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.length != other.length) {
            return false;
        }
        if (Float.floatToIntBits(this.userRating) != Float.floatToIntBits(other.userRating)) {
            return false;
        }
        if (this.popularity != other.popularity) {
            return false;
        }
        if (!Objects.equals(this.imdbId, other.imdbId)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.plot, other.plot)) {
            return false;
        }
        if (!Objects.equals(this.contentRating, other.contentRating)) {
            return false;
        }
        if (!Objects.equals(this.imageUrl, other.imageUrl)) {
            return false;
        }
        if (!Objects.equals(this.videoUrl, other.videoUrl)) {
            return false;
        }
        if (!Objects.equals(this.releaseDate, other.releaseDate)) {
            return false;
        }
        if (!Objects.equals(this.genres, other.genres)) {
            return false;
        }
        if (!Objects.equals(this.directors, other.directors)) {
            return false;
        }
        if (!Objects.equals(this.writers, other.writers)) {
            return false;
        }
        if (!Objects.equals(this.actors, other.actors)) {
            return false;
        }
        return true;
    }

    
    
}
