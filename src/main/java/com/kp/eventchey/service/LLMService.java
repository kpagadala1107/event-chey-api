package com.kp.eventchey.service;


import com.kp.eventchey.domain.Event;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class LLMService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.model:gpt-4}")
    private String model;

    public String summarizeEvent(Event event) {

        OpenAiService service = new OpenAiService(openAiApiKey, Duration.ofSeconds(60));

        String systemPrompt = """
                You are an expert event analyst and summarizer. Your task is to analyze event data and create comprehensive,
                well-structured summaries. Focus on extracting key information including:
                - Event title and description
                - Date, time, and location details
                - Attendee names and their status with counts (for example, invited, confirmed, pending, declined) with no email or phone details
                - Agenda items in the order they occurred
                - Questions and polls (if any)
                - Important highlights or action items
                
                Provide clear, concise summaries that are easy to understand and actionable.
                """;

        String userPrompt = """
                Please summarize the following event details in a clear and organized format:
                
                Event Data:
                """ + event.toString();

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(List.of(
                        new ChatMessage("system", systemPrompt),
                        new ChatMessage("user", userPrompt)
                ))
                .temperature(0.7)
                .maxTokens(1500)
                .build();

        ChatCompletionResult result = service.createChatCompletion(request);
        return result.getChoices().get(0).getMessage().getContent();
    }

    public String generateAgenda(String eventName, String eventDescription,
                                  String startDate, String endDate) {
        OpenAiService service = new OpenAiService(openAiApiKey, Duration.ofSeconds(60));

        String systemPrompt = """
                You are an expert event planner and agenda designer. Your task is to create a comprehensive,
                realistic agenda for events based on the provided details. Consider:
                - The nature and purpose of the event based on its name and description
                - The total duration of the event (single day or multi-day)
                - Standard event practices (registration, breaks, meals, networking, closing)
                - Appropriate time allocations for different activities
                - Logical flow and pacing throughout the event
                
                CRITICAL: All agenda item times MUST be in the SAME TIMEZONE as the event start and end times.
                Use the EXACT date-time format provided in the event times, maintaining the same timezone context.
                Do NOT convert times or change timezones. Keep all times consistent with the event's timezone.
                
                Return the agenda as a JSON array of objects. Each object should have:
                - "title": string (agenda item title)
                - "startTime": string (ISO 8601 format: yyyy-MM-dd'T'HH:mm:ss - SAME timezone as event)
                - "endTime": string (ISO 8601 format: yyyy-MM-dd'T'HH:mm:ss - SAME timezone as event)
                - "description": string (brief description of the agenda item)
                - "speaker": string (suggested speaker role or "TBD" if not applicable)
                
                Return ONLY the JSON array, no additional text or explanation.
                """;

        String userPrompt = String.format("""
                Create a detailed agenda for the following event:
                
                Event Name: %s
                Event Description: %s
                Start Date & Time: %s
                End Date & Time: %s
                
                IMPORTANT: Generate all agenda item times using the SAME timezone as the event times above.
                All startTime and endTime values must be between the event start and end times shown above.
                Maintain the exact date-time format (yyyy-MM-dd'T'HH:mm:ss) without any timezone conversions.
                
                Generate a realistic, comprehensive agenda that covers the entire event duration.
                """, eventName, eventDescription, startDate, endDate);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(List.of(
                        new ChatMessage("system", systemPrompt),
                        new ChatMessage("user", userPrompt)
                ))
                .temperature(0.7)
                .maxTokens(2000)
                .build();

        ChatCompletionResult result = service.createChatCompletion(request);
        return result.getChoices().get(0).getMessage().getContent();
    }

}