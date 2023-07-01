package com.chengjunjie.web.presentation.controller;

import com.chengjunjie.web.application.CustomOpenAiService;
import com.chengjunjie.web.presentation.ControllerConstant;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.model.Model;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping(value = ControllerConstant.openAI)
public class OpenAIController {

    @Autowired
    CustomOpenAiService service;

    @GetMapping("/test")
    public List<CompletionChoice> test() {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Somebody once told me the world is gonna roll me")
                .model("ada")
                .echo(true)
                .build();
        return service.createCompletion(completionRequest).getChoices();
    }

    @GetMapping(value = "/listModels",
            produces = "application/json;charset=UTF-8")
    public List<Model> listModels() {
        return service.listModels();
    }

    @PostMapping(value = "/chat/completions")
    public SseEmitter chat() {
        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(new ChatMessage("system", "You are a senior Java Engineer"));
        messageList.add(new ChatMessage("user", "Please give a Java concurrency thread promgram example."));

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
            .model("gpt-3.5-turbo-0613")
            .temperature(0.0)
            .topP(0.0)
            .maxTokens(100)
            .frequencyPenalty(0.0)
            .presencePenalty(0.0)
            .messages(messageList).build();

        SseEmitter emitter = new SseEmitter();
        Flowable<ChatCompletionChunk> resultStream = service.streamChatCompletion(completionRequest);
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                resultStream.doOnComplete(emitter::complete).forEach(chatCompletionChunk -> {
                    SseEmitter.SseEventBuilder event = SseEmitter.event().data(chatCompletionChunk);
                    emitter.send(event);
                });
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        sseMvcExecutor.shutdown();
        return emitter;
    }
}
