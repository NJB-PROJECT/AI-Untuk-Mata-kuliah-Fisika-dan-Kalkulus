# AI Fiscall

Aplikasi Android untuk membantu mahasiswa Fisika dan Kalkulus menggunakan Google Gemini AI.

## Fitur
- **Chat dengan AI**: Bertanya soal Fisika dan Kalkulus.
- **Dukungan LaTeX**: Rumus matematika ditampilkan dengan rapi menggunakan MathJax.
- **Multimodal**: Bisa upload gambar soal.
- **Customizable**: Ganti Model AI (Gemini 2.5/3 Pro/Flash) dan System Prompt (Personality).
- **Manajemen API Key**: Gunakan built-in key atau key pribadi.

## Cara Menggunakan
1. Buka file `app/src/main/java/com/njbproject/ai/fiscall/utils/Constants.kt`.
2. Ganti `YOUR_API_KEY_HERE` dengan API Key Google Gemini Anda.
3. Build dan Run aplikasi di Android Studio.

## Struktur
- `ui/`: Tampilan (Activities, ViewModels, Adapters).
- `data/`: Database Room dan API Client.
- `utils/`: Helper dan Constants.

## Teknologi
- Kotlin
- MVVM Architecture
- Room Database
- Google Generative AI SDK (Gemini)
- MathJax (via WebView)
