package com.chengjunjie.web.presentation.controller;

import com.chengjunjie.web.application.CustomOpenAiService;
import com.chengjunjie.web.domain.model.ChatCompletionDto;
import com.chengjunjie.web.domain.model.Result;
import com.chengjunjie.web.presentation.ControllerConstant;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.model.Model;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping(value = ControllerConstant.openAI)
public class OpenAIController {

    private CustomOpenAiService customOpenAiService;

    @Autowired
    public void customOpenAiService(CustomOpenAiService customOpenAiService) {
        this.customOpenAiService = customOpenAiService;
    }

    @GetMapping(value = "/listModels",
            produces = "application/json;charset=UTF-8")
    public Result<List<Model>> listModels() {
        Result<List<Model>> result = new Result<>();
        result.setResultSuccess("SUCCESS", customOpenAiService.listModels());
        return result;
    }

    @PostMapping(
            value = "/chat/completions",
            produces = { MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )
    public Object chatCompletions(@RequestBody ChatCompletionDto chatCompletionDto) {
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model(chatCompletionDto.getModel())
                .temperature(chatCompletionDto.getTemperature())
                .topP(0.0)
                .maxTokens(1000)
                .frequencyPenalty(chatCompletionDto.getFrequencyPenalty())
                .presencePenalty(chatCompletionDto.getPresencePenalty())
                .messages(chatCompletionDto.getMessages()).build();

        Boolean isStream = chatCompletionDto.getStream();
        if (isStream != null && isStream) {
            Flowable<ChatCompletionChunk> resultStream = customOpenAiService.streamChatCompletion(completionRequest);

            SseEmitter emitter = new SseEmitter();
            ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
            sseMvcExecutor.execute(() -> resultStream
                    .doOnComplete(emitter::complete)
                    .doOnError(exception -> {
                        Result<?> result = new Result<>();
                        result.setResultFailed(-1, exception.getMessage());
                        emitter.send(result, MediaType.APPLICATION_JSON);
                        emitter.complete();
                    })
                    .forEach(chatCompletionChunk -> {
                        Result<ChatCompletionChunk> result = new Result<>();
                        result.setResultSuccess("SUCCESS", chatCompletionChunk);
                        SseEmitter.SseEventBuilder event = SseEmitter.event().data(result);
                        emitter.send(event);
                    }));
            sseMvcExecutor.shutdown();

            return emitter;
        } else {
            Result<ChatCompletionRequest> result = new Result<>();
            result.setResultSuccess("SUCCESS", completionRequest);
            return result;
        }
    }
}
