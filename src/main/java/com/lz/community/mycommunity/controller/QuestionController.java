package com.lz.community.mycommunity.controller;

import com.lz.community.mycommunity.dto.CommentDTO;
import com.lz.community.mycommunity.dto.QuestionDTO;
import com.lz.community.mycommunity.enums.CommentTypeEnum;
import com.lz.community.mycommunity.service.CommentService;
import com.lz.community.mycommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name ="id") Long id,
                           Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.seleceRelated(questionDTO);
        List<CommentDTO> comments =commentService.listByTargetId(id, CommentTypeEnum.QUESTIOIN);
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
