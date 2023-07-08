package com.chengjunjie.web.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

@Component
public class CustomOpenAiService extends OpenAiService {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);

    public CustomOpenAiService() {
        super(getAPI());
    }

    private static OpenAiApi getAPI() {
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) throw new RuntimeException("OPENAI_API_KEY is missing.");

        ObjectMapper mapper = defaultObjectMapper();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.8.104", 7890));
        OkHttpClient client = defaultClient(apiKey, DEFAULT_TIMEOUT)
                .newBuilder()
                .proxy(proxy)
                .build();

        Retrofit retrofit = defaultRetrofit(client, mapper);
        return retrofit.create(OpenAiApi.class);
    }
}
