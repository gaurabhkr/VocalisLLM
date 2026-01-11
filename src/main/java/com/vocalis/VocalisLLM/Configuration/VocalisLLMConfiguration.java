package com.vocalis.VocalisLLM.Configuration;

import org.springframework.ai.model.openai.autoconfigure.OpenAiConnectionProperties;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VocalisLLMConfiguration {
	
	//OpenAiProperties will retrieve apikey and base url 
	
	private final String API_KEY;
	private final String BASE_URL;
	
	public VocalisLLMConfiguration(OpenAiConnectionProperties con) {
		this.API_KEY=con.getApiKey();
		this.BASE_URL=con.getBaseUrl();
		System.out.println("API_KEY:"+API_KEY+"  "+"BASE_URL:"+BASE_URL);
	}
	
	
	@Bean
	@Qualifier("chatmodel")
	public OpenAiChatModel llmchatmodel() {
		
		var option=OpenAiChatOptions.builder().model("nvidia/nemotron-3-nano-30b-a3b:free").build();
		var openaiapi=OpenAiApi.builder().apiKey(API_KEY).baseUrl(BASE_URL).build();
		
		return OpenAiChatModel
				.builder()
				.openAiApi(openaiapi).defaultOptions(option)
				.build();
	}
}
