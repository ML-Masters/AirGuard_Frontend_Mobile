package com.mlmasters.airguard.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlmasters.airguard.data.repository.AirGuardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
)

data class ChatState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage("Bonjour ! Je suis AirGuard Bot. Posez-moi vos questions sur la qualité de l'air au Cameroun.", false)
    ),
    val isSending: Boolean = false,
)

class ChatViewModel(private val repository: AirGuardRepository) : ViewModel() {
    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    fun sendMessage(text: String) {
        if (text.isBlank() || _state.value.isSending) return

        val userMsg = ChatMessage(text, true)
        _state.value = _state.value.copy(
            messages = _state.value.messages + userMsg,
            isSending = true,
        )

        viewModelScope.launch {
            val result = repository.chat(text)
            val botMsg = if (result.isSuccess) {
                ChatMessage(result.getOrThrow().response, false)
            } else {
                ChatMessage("Désolé, je suis temporairement indisponible.", false)
            }
            _state.value = _state.value.copy(
                messages = _state.value.messages + botMsg,
                isSending = false,
            )
        }
    }
}
