package com.lz.community.mycommunity.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/error")
public class CustomizeErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(Model model,
                                  HttpServletRequest request){
        HttpStatus status = getStatus(request);
        if(status.is4xxClientError()){
            model.addAttribute("message","你的请求方式有误");
        }
        if(status.is5xxServerError()){
            model.addAttribute("message","服务器又摸鱼了");
        }
        return new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest request){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == null){
            return  HttpStatus.INTERNAL_SERVER_ERROR;
        }

        try {
            return  HttpStatus.valueOf(statusCode);
        } catch (Exception ex){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }


    }
}
