package com.lz.community.mycommunity.mapper;

import com.lz.community.mycommunity.model.Comment;
import com.lz.community.mycommunity.model.CommentExample;
import com.lz.community.mycommunity.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int incCommentCount(Comment record);
}