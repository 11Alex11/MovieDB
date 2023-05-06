/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aw.MovieDatabase.controller.dto;

import com.aw.MovieDatabase.dao.MovieDao;
import com.aw.MovieDatabase.dto.Movie;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Alex Wood
 */
@Controller
public class HomeController {
    @Autowired
    private MovieDao movieDao;
    
    @RequestMapping("/")
    public String displayHome(Model model){
        List<Movie> movies = movieDao.getTopUserRatedMovies(10);
        model.addAttribute("movies",movies);
        return "movies";
    }
    
    @GetMapping("search")
    public String displaySearch(int searchFor, String searchText, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("searchText",searchText);
        if(searchFor == 1){
            return "redirect:/movieSearch";
        }else{
            return "redirect:/castSearch";
        }
    }
}
