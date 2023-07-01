package com.chengjunjie.web.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.Data;

import java.util.List;

@Data
public class ChatCompletionDto {
    @JsonProperty("frequency_penalty")
    double frequencyPenalty;

    String model;

    List<ChatMessage> messages;

    @JsonProperty("presence_penalty")
    double presencePenalty;

    Boolean stream;

    double temperature;
}
