/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.dao;

import com.aw.MovieDatabase.dto.CastMember;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Alex Wood
 */
@Repository
public class CastMemberDaoDB implements CastMemberDao{
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public CastMember getCastMemberById(int castMemberId) {
        String sql = "SELECT * FROM CastMember WHERE id = ?;";
        try{
            return jdbc.queryForObject(sql, new CastMemberMapper(), castMemberId);
        }
        catch(DataAccessException e){
            return null;
        }
    }

    @Override
    public List<CastMember> getCastMembers() {
        String sql = "SELECT * FROM CastMember";
        return jdbc.query(sql, new CastMemberMapper());    
    }

    @Override
    @Transactional
    public CastMember addCastMember(CastMember castMember) {
        String sql = "INSERT INTO CastMember(imdbId, name, imageUrl, birthdate, birthplace, bio, bioFull) "
                + "VALUES(?,?,?,?,?,?,?);";
        jdbc.update(sql, castMember.getImdbId(), castMember.getName(), castMember.getImageUrl(),
                castMember.getBirthdate(), castMember.getBirthplace(), castMember.getBio(), castMember.getBioFull());
        int castMemberId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        castMember.setId(castMemberId);
        return castMember;
    }

    @Override
    public void updateCastMember(CastMember castMember) {
        String sql = "UPDATE CastMember SET imdbId = ?, name = ?, imageUrl = ?, birthdate = ?, birthplace = ?, bio = ?, bioFull = ? WHERE id = ?;";
        jdbc.update(sql, castMember.getImdbId(), castMember.getName(), castMember.getImageUrl(),
                castMember.getBirthdate(), castMember.getBirthplace(), castMember.getBio(), 
                castMember.getBioFull(), castMember.getId());
    }

    @Override
    @Transactional
    public void removeCastMemberById(int castMemberId) {
        String sql = "DELETE FROM CastMember WHERE id = ?;";
        // Delete from MovieCastMember Bridge first
        removeMovieCastMember(castMemberId);
        jdbc.update(sql, castMemberId);
    }
    
    @Override
    public CastMember getCastMemberByImdbId(String imdbId){
       String sql = "SELECT * FROM CastMember WHERE imdbId= ?;";
        try{
            return jdbc.queryForObject(sql, new CastMemberMapper(), imdbId);
        }
        catch(DataAccessException e){
            return null;
        } 
    }
    
    @Override
    public CastMember getCastMemberByNameAndBirthdate(String castName, LocalDate birthdate) {
        String sql = "SELECT * FROM CastMember WHERE name = ? AND birthdate = ?;";
        try{
            return jdbc.queryForObject(sql, new CastMemberMapper(), castName, birthdate);
        }
        catch(DataAccessException e){
            return null;
        }
    }
    
    @Override
    public List<CastMember> searchCastMembersByName(String searchText){
        String sql = "SELECT * FROM CastMember WHERE name LIKE ?;";
        List<CastMember> members = jdbc.query(sql, new CastMemberMapper(),"%" + searchText + "%");
        return members;
    }
    
    private void removeMovieCastMember(int castMemberId){
        String sql = "DELETE FROM MovieCastMember WHERE castMemberId = ?;";
        jdbc.update(sql, castMemberId);
    }

    
    
    public static class CastMemberMapper implements RowMapper<CastMember>{

        @Override
        public CastMember mapRow(ResultSet rs, int rowNum) throws SQLException {
            CastMember member = new CastMember();
            member.setId(rs.getInt("id"));
            member.setImdbId(rs.getString("imdbId"));
            member.setName(rs.getString("name"));
            member.setImageUrl(rs.getString("imageUrl"));
            Date date = rs.getDate("birthdate");
            if(date!=null){
                LocalDate localDate = date.toLocalDate();
                member.setBirthdate(rs.getDate("birthdate").toLocalDate());
            }
            else{
                member.setBirthdate(null);
            }
            member.setBirthplace(rs.getString("birthplace"));
            member.setBio(rs.getString("bio"));
            member.setBioFull(rs.getString("bioFull"));
            return member;
        }
    } 
}
