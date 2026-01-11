# 🎙️ VocalisLLM: Voice-Powered AI Assistant

VocalisLLM is a state-of-the-art, end-to-end conversational AI assistant built with **Spring Boot**, **Spring AI**, and the **Web Speech API**. It provides a seamless voice-to-voice interaction experience, allowing users to talk to an LLM naturally in multiple languages.

---

### 🌐 [Live Demo: vocalisllm.onrender.com](https://vocalisllm.onrender.com)

---

## 🚀 Core Features

*   **Voice-to-Voice Interaction**: Seamless speech-to-text (STT) and text-to-speech (TTS) flow.
*   **Multi-Language Support**: Supports over 10 languages including English, Spanish, Hindi, Japanese, and more.
*   **Intelligent STT Session Management**: Handles continuous speech recognition beyond browser-imposed 60-second limits.
*   **Optimized TTS Playback**: Implements custom text chunking logic to bypass Web Speech API character limits.
*   **Markdown Rendering**: AI responses are rendered with support for tables, emojis, and styling using **Flexmark**.
*   **Glassmorphic UI**: A premium, responsive design built with **TailwindCSS**, featuring real-time audio meters and status indicators.

## 🛠️ Tech Stack

| Component | Technology |
| :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.5.9 |
| **AI Framework** | Spring AI (OpenAI / NVIDIA / DeepSeek support) |
| **Frontend** | Vanilla JS, TailwindCSS, Web Speech API |
| **Processing** | Flexmark-java (Markdown Rendering) |

## ⚙️ Architecture Highlights

### 1. Advanced Speech-to-Text (STT)
Standard browser Web Speech API often cuts off after a period of silence or 60 seconds. VocalisLLM manages this by:
*   Monitoring `onresult` for final and interim transcripts.
*   Automatically restarting the recognition engine when it stops while the "Mic" is active.
*   Buffering transcripts to maintain context across session restarts.

### 2. Robust Text-to-Speech (TTS)
To handle long LLM responses (which often exceed the 200-character reliable limit of the browser's TTS):
*   The `speechUtteranceChunker` breaks down the generated HTML/Markdown into sentences and small chunks.
*   It manages a queue of utterances to ensure continuous, natural-sounding playback.

## 🧩 Challenges & Technical Solutions

During development, several technical hurdles were encountered related to browser API limitations and token costs.

### 1. Cost & Usage Limits (Server-side vs. Client-side)
> **Challenge:** Using server-side models for STT and TTS alongside the LLM prompt would require 3 API requests per user interaction, significantly increasing costs.

**Solution:** I implemented **client-side transcription** using the free **Web Speech API**. This offloads processing to the browser, making the voice interaction entirely free and scalable.

---

### 2. TTS Character Limits
> **Challenge:** The Web Speech API has a fixed character limit for single utterances, causing long AI responses to be cut off mid-sentence.

**Solution:** I developed a **text chunking algorithm** that divides the AI's response into smaller arrays. These chunks are loaded sequentially into utterance objects, ensuring the entire response is spoken fluently.

---

### 3. STT 60-Second Audio Limit
> **Challenge:** Most browser STT implementations have a 60-second limit and do not support continuous long-form audio.

**Solution:** I implemented a dynamic session management system:
*   **Interim Results:** Real-time transcription is captured in an interim result string.
*   **Session Cycling:** Every 55 seconds, the interim text is committed to the final result, and the recognition engine is transparently reloaded.
*   **Buffering:** This process allows for infinite continuous transcription of long-form speech.

## 📥 Getting Started

### Prerequisites
*   **JDK 21+**
*   **Maven 3.6+**
*   An OpenAI-compatible API Key (e.g., NVIDIA NIM, OpenRouter, or OpenAI)

### Environment Variables
Set these variables before running:
```bash
SPRING_AI_OPENAI_API_KEY=your_api_key_here
SPRING_AI_OPENAI_BASE_URL=https://integrate.api.nvidia.com/v1 # Or your provider
```

### Running Locally
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/gaurabhkr/VocalisLLM.git
    cd VocalisLLM
    ```
2.  **Build and run:**
    ```bash
    ./mvnw spring-boot:run
    ```
3.  **Access the app:** Open `http://localhost:8080` in your browser.

## 📝 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
*Built with ❤️ for the future of Conversational AI.*
