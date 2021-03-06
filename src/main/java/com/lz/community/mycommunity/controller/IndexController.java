package com.lz.community.mycommunity.controller;

import com.lz.community.mycommunity.dto.PageDTO;
import com.lz.community.mycommunity.dto.QuestionDTO;
import com.lz.community.mycommunity.mapper.QuestionMapper;
import com.lz.community.mycommunity.mapper.UserMapper;
import com.lz.community.mycommunity.model.Question;
import com.lz.community.mycommunity.model.User;
import com.lz.community.mycommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size,
                        @RequestParam(name = "serch",required = false) String search
                        ){


        PageDTO pagination = questionService.list(search,page,size);
        model.addAttribute("pagination",pagination);
        model.addAttribute("search",search);
        return "index";
    }
}
