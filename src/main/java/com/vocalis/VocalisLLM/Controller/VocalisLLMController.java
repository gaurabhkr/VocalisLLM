package com.vocalis.VocalisLLM.Controller;

import java.util.Arrays; 

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

@Controller
@RequestMapping("/api")
public class VocalisLLMController {

    public final Parser parser;  
	public final HtmlRenderer renderer;
	public MutableDataSet options=new MutableDataSet();
	
	
	private final ChatClient LLMChatClient;
	private String unrendered_response="";
	
	
	public VocalisLLMController(@Qualifier("llmchatmodel") OpenAiChatModel llmchatmodel) {
		this.LLMChatClient=ChatClient.create(llmchatmodel);
		options.set( Parser.EXTENSIONS , Arrays.asList( TablesExtension.create(),EmojiExtension.create() ) );
		this.parser=Parser.builder(options).build();
		this.renderer=HtmlRenderer.builder(options).build();
	}
	
	//response has html render response and unrendered response has markdown response
	//response use as text and unrendered response use by speech text ai
	//	ChatResponse a=LLMChatClient.prompt(message).call().chatResponse(); used for more option
	//	response=a.getResult().getOutput().getText();
	

	
	
	@GetMapping("/llm/{message}")
	public ResponseEntity<String> llmresponse(@PathVariable String message){
		try {
		String response;  response=LLMChatClient.prompt(message).call().content();
		
		this.unrendered_response=response;
		
		response=renderer.render(parser.parse(response));
		
		System.out.println("\n Response by LLM \n" + response);
		
		System.out.println("\n Response by LLM \n" + this.unrendered_response);
		
		return ResponseEntity.ok(response);}
		catch(Exception e) {return ResponseEntity.internalServerError().body(e.getMessage());}
	}
	
	
	//called after llm text for transcribe text
	@GetMapping("/llm/response/audio")
	public ResponseEntity<String> llmresponseaudio() {
		unrendered_response=unrendered_response.replaceAll( "([*_`\\[\\](){}+\\-.!#])"," ");
		return ResponseEntity.ok(this.unrendered_response);
	}
	
	//We had to add clear chat option and add about section
	//There are three problem -
	//For Speech to text and text to speech we use either on client side transcription or Server side transcription
	//
	//1>For server side transcriptio we have to use llm model for it and speech to text - text prompt - text to speech will take 3 req per user request
	//	Using chat model also has month usage limit so they are not free
	//
	//	(solution- I uses client side transcription which is free web speech api by google)
	//
	//2>Text to speech web speech api has limit of fixed character of text will convert to speech
	//
	//	(solution- I divided the text into small chunks in array and load in to utterance(web speech api) 1 chunk contain character limit by web Speech
	//
	//3>speech to text web speech api has limit 60s of audio and it should not pre recorded 
	//
	//	(solution-I make 2 string one is interim result for live transcription and other is result for final transcription response
	//			  When the user will speak the live transcription text get into interim result string 
	//			  On 55s iterim result string get added in result string, iterim result get empty and session will restart and loaded to web speech api
	//			  It will will make live transcription of larger audio )
	
	
	
	
}
