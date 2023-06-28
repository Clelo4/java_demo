package com.chengjunjie.web.presentation.controller;

import com.chengjunjie.web.application.CustomOpenAiService;
import com.chengjunjie.web.presentation.ControllerConstant;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = ControllerConstant.openAI)
public class OpenAIController {

    @Autowired
    CustomOpenAiService service;

    @GetMapping("/test")
    public String test() {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Somebody once told me the world is gonna roll me")
                .model("ada")
                .echo(true)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
        return "";
    }

    @GetMapping(value = "/listModels",
            produces = "application/json;charset=UTF-8")
    public Model listModels() {
        List<Model> models = service.listModels();
        return models.get(0);
    }
}
