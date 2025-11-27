package com.njbproject.ai.fiscall.ui.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.njbproject.ai.fiscall.R
import com.njbproject.ai.fiscall.data.model.ChatMessage
import java.io.File

class ChatAdapter : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        const val VIEW_TYPE_USER = 1
        const val VIEW_TYPE_AI = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isUser) VIEW_TYPE_USER else VIEW_TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_USER) {
            val view = inflater.inflate(R.layout.item_message_user, parent, false)
            UserViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_message_ai, parent, false)
            AiViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        if (holder is UserViewHolder) {
            holder.bind(message)
        } else if (holder is AiViewHolder) {
            holder.bind(message)
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvUserMessage)
        private val ivImage: ImageView = itemView.findViewById(R.id.ivUserImage)

        fun bind(message: ChatMessage) {
            tvMessage.text = message.text
            if (message.imagePath != null) {
                ivImage.visibility = View.VISIBLE
                ivImage.load(File(message.imagePath))
            } else {
                ivImage.visibility = View.GONE
            }
        }
    }

    class AiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val webView: WebView = itemView.findViewById(R.id.webViewContent)

        init {
            webView.settings.javaScriptEnabled = true
            webView.webViewClient = WebViewClient()
            webView.setBackgroundColor(0) // Transparent
        }

        fun bind(message: ChatMessage) {
            // Basic HTML wrapper with MathJax
            val htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <script src="https://polyfill.io/v3/polyfill.min.js?features=es6"></script>
                    <script id="MathJax-script" async src="https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js"></script>
                    <style>
                        body { font-family: sans-serif; color: #212121; margin: 0; padding: 0; font-size: 14px; white-space: pre-wrap; }
                        p { margin: 0 0 8px 0; }
                        img { max-width: 100%; height: auto; }
                    </style>
                </head>
                <body>
                    ${renderMarkdown(message.text)}
                </body>
                </html>
            """.trimIndent()

            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
        }

        private fun renderMarkdown(text: String): String {
            // Very basic markdown parser to HTML. Ideally, use a library like Flexmark-java and convert to HTML.
            // We use white-space: pre-wrap in CSS to handle newlines, so we don't need <br/> which breaks MathJax blocks.
            // We escape HTML entities to prevent injection.

            var html = text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace(Regex("\\*\\*(.*?)\\*\\*"), "<b>$1</b>") // Bold
                .replace(Regex("\\*(.*?)\\*"), "<i>$1</i>") // Italic

            return html
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }
    }
}
