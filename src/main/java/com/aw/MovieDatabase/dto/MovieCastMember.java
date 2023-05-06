/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.dto;

import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author Alex Wood
 */
public class MovieCastMember {
    Movie movie;
    CastMember castMember;
    @NotBlank(message = "Role can not be empty")
    @Size(max = 100, message = "Role must be less than 100 characters")
    String role;
    int roleRelevance;   

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public CastMember getCastMember() {
        return castMember;
    }

    public void setCastMember(CastMember castMember) {
        this.castMember = castMember;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getRoleRelevance() {
        return roleRelevance;
    }

    public void setRoleRelevance(int roleRelevance) {
        this.roleRelevance = roleRelevance;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.movie);
        hash = 11 * hash + Objects.hashCode(this.castMember);
        hash = 11 * hash + Objects.hashCode(this.role);
        hash = 11 * hash + this.roleRelevance;
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
        final MovieCastMember other = (MovieCastMember) obj;
        if (this.roleRelevance != other.roleRelevance) {
            return false;
        }
        if (!Objects.equals(this.role, other.role)) {
            return false;
        }
        if (!Objects.equals(this.movie, other.movie)) {
            return false;
        }
        if (!Objects.equals(this.castMember, other.castMember)) {
            return false;
        }
        return true;
    }
    
    
    
}
