package com.chengjunjie.web.presentation.controller;

import com.chengjunjie.web.application.CustomOpenAiService;
import com.chengjunjie.web.domain.model.ChatCompletionDto;
import com.chengjunjie.web.presentation.ControllerConstant;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.model.Model;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping(value = ControllerConstant.openAI)
public class OpenAIController {

    @Autowired
    CustomOpenAiService service;

    @GetMapping(value = "/listModels",
            produces = "application/json;charset=UTF-8")
    public List<Model> listModels() {
        return service.listModels();
    }

    @PostMapping(
            value = "/chat/completions",
            produces = { MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )
    public Object chatCompletions(@RequestBody ChatCompletionDto chatCompletionDto) {
        try {
            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                    .model(chatCompletionDto.getModel())
                    .temperature(chatCompletionDto.getTemperature())
                    .topP(0.0)
                    .maxTokens(1000)
                    .frequencyPenalty(chatCompletionDto.getFrequencyPenalty())
                    .presencePenalty(chatCompletionDto.getPresencePenalty())
                    .messages(chatCompletionDto.getMessages()).build();

            if (chatCompletionDto.getStream()) {
                Flowable<ChatCompletionChunk> resultStream = service.streamChatCompletion(completionRequest);

                SseEmitter emitter = new SseEmitter();
                ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
                sseMvcExecutor.execute(() -> {
                    try {
                        resultStream.doOnComplete(emitter::complete).doOnError(ex -> {
                                    System.err.println("doOnError: " + ex);
                                    ex.printStackTrace();
                                    emitter.completeWithError(ex);
                                })
                                .forEach(chatCompletionChunk -> {
                            SseEmitter.SseEventBuilder event = SseEmitter.event().data(chatCompletionChunk);
                            emitter.send(event);
                        });
                    } catch (Exception ex) {
                        emitter.completeWithError(ex);
                    }
                });
                sseMvcExecutor.shutdown();

                return emitter;
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.createChatCompletion(completionRequest));
            }
        } catch (OpenAiHttpException exception) {
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }
}
