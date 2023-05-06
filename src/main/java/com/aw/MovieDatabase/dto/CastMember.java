/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.dto;

import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author Alex Wood
 */
public class CastMember {
    int id;
    String imdbId;
    @NotBlank(message = "Cast Member name must not be empty")
    @Size(max = 50, message = "Cast Member name must be less than 50 characters")
    String name;
    @Size(max = 200, message = "Image URL must be less than 200 characters")
    String imageUrl;
    LocalDate birthdate;
    @Size(max = 50, message = "Birthplace must be less than 50 characters")
    String birthplace;
    
    @Size(max = 1000, message = "Bio must be less than 1000 characters")
    String bio;
    @Size(max = 10000, message = "Full Bio must be less than 10,000 characters")
    String bioFull;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getBioFull() {
        return bioFull;
    }

    public void setBioFull(String bioFull) {
        this.bioFull = bioFull;
    }
    
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.id;
        hash = 37 * hash + Objects.hashCode(this.imdbId);
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.imageUrl);
        hash = 37 * hash + Objects.hashCode(this.birthdate);
        hash = 37 * hash + Objects.hashCode(this.birthplace);
        hash = 37 * hash + Objects.hashCode(this.bio);
        hash = 37 * hash + Objects.hashCode(this.bioFull);
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
        final CastMember other = (CastMember) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.imdbId, other.imdbId)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.imageUrl, other.imageUrl)) {
            return false;
        }
        if (!Objects.equals(this.birthplace, other.birthplace)) {
            return false;
        }
        if (!Objects.equals(this.bio, other.bio)) {
            return false;
        }
        if (!Objects.equals(this.bioFull, other.bioFull)) {
            return false;
        }
        if (!Objects.equals(this.birthdate, other.birthdate)) {
            return false;
        }
        return true;
    }
    
    
    
}
