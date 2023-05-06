/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.dao;

import com.aw.MovieDatabase.dto.CastMember;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Alex Wood
 */
public interface CastMemberDao {
    CastMember getCastMemberById(int castMemberId);
    List<CastMember> getCastMembers();
    CastMember addCastMember(CastMember castMember);
    void updateCastMember(CastMember castMember);
    void removeCastMemberById(int castMemberId); 
    
    List<CastMember> searchCastMembersByName(String searchText);
    CastMember getCastMemberByImdbId(String imdbId);
    CastMember getCastMemberByNameAndBirthdate(String castName, LocalDate birthdate);
}
