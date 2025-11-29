package com.kp.eventchey.mapper;

import com.kp.eventchey.domain.Question;
import com.kp.eventchey.dto.response.QuestionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    QuestionResponse toResponse(Question question);
}

