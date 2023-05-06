/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.controller.dto;

import com.aw.MovieDatabase.dao.CastMemberDao;
import com.aw.MovieDatabase.dao.MovieDao;
import com.aw.MovieDatabase.dto.CastMember;
import com.aw.MovieDatabase.dto.Movie;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Alex Wood
 */
@Controller
public class CastMemberController {
    @Autowired
    CastMemberDao castDao;
    
    @Autowired
    MovieDao movieDao;
    
    Set<ConstraintViolation<CastMember>> violations = new HashSet<>();
    
    @GetMapping("castMembers")
    public String displayCast(Model model){
        List<CastMember> castMembers = castDao.getCastMembers();
        model.addAttribute("castMembers",castMembers);
        model.addAttribute("errors",violations);
        return "castMembers";
    }
    
    @GetMapping("castSearch")
    public String displaySearch(String searchText, Model model){
        List<CastMember> members = castDao.searchCastMembersByName(searchText);
        model.addAttribute("castMembers",members);
        return "castMembers";
    }
    
    
    @GetMapping("addCastMember")
    public String addCast(Model model){
        violations = new HashSet<>();
        model.addAttribute("errors",violations);
        return "addCastMember";
    }
    
    @PostMapping("addCastMember")
    public String addCast(CastMember castMember, Model model){
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(castMember);
        
        if(!violations.isEmpty()){
            model.addAttribute("errors",violations);
            violations = new HashSet<>();
            return "addCastMember";
        }
        
        castDao.addCastMember(castMember);
        return "redirect:/castMembers";
    }
    
    @GetMapping("castMemberInfo")
    public String displayCastInfo(Integer id, Model model){
        CastMember member = castDao.getCastMemberById(id);
        List<Movie> movies = movieDao.getMoviesCastMemberIn(member.getId());
        model.addAttribute("movies",movies);
        model.addAttribute("castMember",member);
        return "castMemberInfo";
    }
    
    @PostMapping("removeCastMember")
    public String removeCast(Integer id){
        castDao.removeCastMemberById(id);
        return "redirect:/castMembers";
    }
    
    @GetMapping("editCastMember")
    public String editCast(Integer id, Model model){
        CastMember member = castDao.getCastMemberById(id);
        model.addAttribute("castMember",member);
        
        return "editCastMember";
    }
    
    @PostMapping("editCastMember")
    public String editCast(@Valid CastMember castMember, BindingResult result, Model model){
        
        if(result.hasErrors()){
            model.addAttribute("castMember", castMember);
            return "editCastmember";
        }
        
        castDao.updateCastMember(castMember);
        return "redirect:/castMembers";
    }
}
