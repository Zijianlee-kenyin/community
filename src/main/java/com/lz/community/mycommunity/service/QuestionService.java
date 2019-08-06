package com.lz.community.mycommunity.service;

import com.lz.community.mycommunity.dto.PageDTO;
import com.lz.community.mycommunity.dto.QuestionDTO;
import com.lz.community.mycommunity.dto.QuestionQueryDTO;
import com.lz.community.mycommunity.exception.CustomizeErrorCode;
import com.lz.community.mycommunity.exception.CustomizeException;
import com.lz.community.mycommunity.mapper.QuestionExtMapper;
import com.lz.community.mycommunity.mapper.QuestionMapper;
import com.lz.community.mycommunity.mapper.UserMapper;
import com.lz.community.mycommunity.model.Question;
import com.lz.community.mycommunity.model.QuestionExample;
import com.lz.community.mycommunity.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PageDTO list(String serch,Integer page, Integer size) {
        if (StringUtils.isNotBlank(serch)){
            String[] tags = StringUtils.split(serch, " ");
            Arrays.stream(tags).collect(Collectors.joining("|"));
        }



        Integer totalPage;
        Integer offset = size*(page-1);
        PageDTO paginationDTO = new PageDTO();
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(serch);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);

        if(totalCount%size==0){
            totalPage = totalCount/size;
        }else{
            totalPage = totalCount/size+1;
        }

        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }

        paginationDTO.setPagination(totalPage,page);
        //List<Question> questions=questionMapper.list(page,size);
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();

        for(Question question:questions){
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public PageDTO list(Long userId, Integer page, Integer size) {
         Integer totalPage;
        Integer offset = size*(page-1);
        PageDTO paginationDTO = new PageDTO();
        QuestionExample questionExample =new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);

        if(totalCount%size==0){
            totalPage = totalCount/size;
        }else{
            totalPage = totalCount/size+1;
        }

        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }

        paginationDTO.setPagination(totalPage,page);

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds((page-1)*size, size));
        //List<Question> questions=questionMapper.listByUserId(userId,page,size);
        List<QuestionDTO> questionDTOList = new ArrayList<QuestionDTO>();

        for(Question question:questions){
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question=questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user=userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }else {
            question.setGmtModified(System.currentTimeMillis());
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
//        Question question=questionMapper.selectByPrimaryKey(id);
//        Question updateQuestion = new Question();
//        updateQuestion.setViewCount(question.getViewCount()+1);
//        QuestionExample questionExample = new QuestionExample();
//        questionExample.createCriteria().andIdEqualTo(id);
//
//        questionMapper.updateByExampleSelective(updateQuestion,questionExample);
          Question updateQuestion = new Question();
          updateQuestion.setId(id);
          updateQuestion.setViewCount(1);
          questionExtMapper.incView(updateQuestion);
    }

    public List<QuestionDTO> seleceRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }

        String[] tags = StringUtils.split(questionDTO.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(q,dto);
            return dto;
        }).collect(Collectors.toList());
        return  questionDTOS;
    }
}
