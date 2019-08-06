package com.lz.community.mycommunity.dto;

import com.lz.community.mycommunity.model.Question;
import com.lz.community.mycommunity.model.User;
import lombok.Data;

@Data
public class QuestionDTO extends Question {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;

}
