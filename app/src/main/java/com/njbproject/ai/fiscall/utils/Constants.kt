package com.njbproject.ai.fiscall.utils

object Constants {
    // Placeholder for your key
    const val BUILT_IN_API_KEY = "YOUR_API_KEY_HERE"

    const val DEFAULT_SYSTEM_PROMPT = "You are an expert AI tutor specializing in Physics and Calculus. " +
            "Provide clear, step-by-step solutions. Use LaTeX formatting for math formulas (e.g., $$\\int x dx$$ or \\( x^2 \\)). " +
            "Be encouraging and helpful."

    val AVAILABLE_MODELS = listOf(
        "gemini-1.5-flash",
        "gemini-1.5-pro",
        "gemini-pro", // Legacy 1.0 Pro
        "gemini-2.0-flash-exp",
        "gemini-exp-1206" // Experimental newer models if valid
        // Note: "gemini-3" isn't standard yet in public API without specific preview strings.
        // We will stick to known working ones or allow custom input.
    )
}
