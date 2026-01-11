# 🎙️ VocalisLLM: Voice-Powered AI Assistant

VocalisLLM is a state-of-the-art, end-to-end conversational AI assistant built with **Spring Boot**, **Spring AI**, and the **Web Speech API**. It provides a seamless voice-to-voice interaction experience, allowing users to talk to an LLM naturally in multiple languages.

## 🚀 Core Features

- **Voice-to-Voice Interaction**: Seamless speech-to-text (STT) and text-to-speech (TTS) flow.
- **Multi-Language Support**: Supports over 10 languages including English, Spanish, Hindi, Japanese, and more.
- **Intelligent STT Session Management**: Handles continuous speech recognition beyond browser-imposed 60-second limits through automated session restarts and interim result buffering.
- **Optimized TTS Playback**: Implements custom text chunking logic to bypass Web Speech API character limits, ensuring smooth playback of long responses.
- **Markdown Rendering**: AI responses are rendered with support for tables, emojis, and styling using Flexmark.
- **Glassmorphic UI**: A premium, responsive design built with TailwindCSS, featuring real-time audio meters and status indicators.

## 🛠️ Tech Stack

- **Backend**: Java 21, Spring Boot 3.5.9
- **AI Framework**: Spring AI (OpenAI/NVIDIA/DeepSeek models support)
- **Frontend**: Vanilla JS, TailwindCSS, Web Speech API
- **Markdown Processing**: Flexmark-java

## ⚙️ Project Architecture Highlights

### 1. Advanced Speech-to-Text (STT)
Standard browser Web Speech API often cuts off after a period of silence or 60 seconds. VocalisLLM manages this by:
- Monitoring `onresult` for final and interim transcripts.
- Automatically restarting the recognition engine when it stops while the "Mic" is active.
- Buffering transcripts to maintain context across session restarts.

### 2. Robust Text-to-Speech (TTS)
To handle long LLM responses (which often exceed the 200-character reliable limit of the browser's TTS):
- The `speechUtteranceChunker` breaks down the generated HTML/Markdown into sentences and small chunks.
- It manages a queue of utterances to ensure continuous, natural-sounding playback.

### 3. Spring AI Integration
The backend leverages Spring AI to connect to various LLM providers. It currently features integration with **NVIDIA Nemotron** (via OpenAI-compatible API) for high-performance reasoning.

## 🧩 Challenges & Technical Solutions

During the development of VocalisLLM, several technical hurdles were encountered, primarily related to the limitations of browser-based APIs and the costs associated with server-side AI processing.

### 1. Cost & Usage Limits (Server-side vs. Client-side)
**Challenge:** Using server-side models for STT and TTS alongside the LLM prompt would require 3 API requests per user interaction. This significantly increases costs and consumes monthly usage quotas quickly.

**Solution:** I implemented **client-side transcription** using the free **Web Speech API** by Google. This offloads the processing to the browser, making the voice interaction entirely free and scalable.

### 2. TTS Character Limits
**Challenge:** The Web Speech API has a fixed character limit for single utterances, causing long AI responses to be cut off mid-sentence.

**Solution:** I developed a **text chunking algorithm** that divides the AI's response into smaller arrays. These chunks are loaded sequentially into utterance objects, ensuring the entire response is spoken fluently without hitting the cap.

### 3. STT 60-Second Audio Limit
**Challenge:** Most browser STT implementations have a 60-second limit and do not support pre-recorded or long-form continuous audio naturally.

**Solution:** I implemented a dynamic session management system:
- **Interim Results:** Real-time transcription is captured in an interim result string.
- **Session Cycling:** At the 55-second mark, the interim text is committed to the final result string, the current session is transparently restarted, and the recognition engine is reloaded.
- **Buffering:** This process allows for infinite continuous transcription of long-form speech.

## 📥 Getting Started

### Prerequisites
- JDK 21+
- Maven 3.6+
- An OpenAI-compatible API Key (e.g., from NVIDIA NIM, OpenRouter, or OpenAI)

### Environment Variables
Set the following environment variables before running:
```bash
SPRING_AI_OPENAI_API_KEY=your_api_key_here
SPRING_AI_OPENAI_BASE_URL=https://integrate.api.nvidia.com/v1 # Or your provider
```

### Running Locally
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/VocalisLLM.git
   cd VocalisLLM
   ```
2. Build and run:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Open `http://localhost:8080` in your browser.

## 📝 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
*Built with ❤️ for the future of Conversational AI.*
