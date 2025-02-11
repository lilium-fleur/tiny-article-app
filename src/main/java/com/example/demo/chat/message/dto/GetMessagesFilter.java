package com.example.demo.chat.message.dto;

import com.example.demo.chat.message.LoadStrategy;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@ParameterObject
public class GetMessagesFilter {

    @Parameter(description = "Current message id", example = "2")
    private Long cursor;

    @Parameter(description = "Message count", example = "20")
    @Positive
    private Integer limit;

    @Parameter(description = "Load message strategy", example = "BEFORE_CURSOR")
    private LoadStrategy strategy;
}
