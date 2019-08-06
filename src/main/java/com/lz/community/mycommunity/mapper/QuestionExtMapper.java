package com.lz.community.mycommunity.mapper;

import com.lz.community.mycommunity.dto.QuestionQueryDTO;
import com.lz.community.mycommunity.model.Question;
import com.lz.community.mycommunity.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
    int intCommentCount(Question record);
    List<Question> selectRelated(Question record);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}