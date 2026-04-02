package com.mlmasters.airguard.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mlmasters.airguard.data.repository.AirGuardRepository
import com.mlmasters.airguard.ui.i18n.S
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
)

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val isSending: Boolean = false,
)

class ChatViewModel(private val repository: AirGuardRepository) : ViewModel() {
    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    init {
        _state.value = ChatState(
            messages = listOf(ChatMessage(S.chatGreeting, false))
        )
    }

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
                ChatMessage(S.chatUnavailable, false)
            }
            _state.value = _state.value.copy(
                messages = _state.value.messages + botMsg,
                isSending = false,
            )
        }
    }
}
