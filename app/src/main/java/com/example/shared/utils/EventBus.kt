package com.example.shared.utils

import com.example.shared.models.MyUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object EventBus {
    private val _events = Channel<Any>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: Any) {
        _events.send(event)
    }
}


sealed interface UiEvent {
    data class Toast(val message: String) : UiEvent
    object NavigateToAuthSystem : UiEvent
    object NavigateToPrivateSystem : UiEvent
    object RefetchSession : UiEvent

}

sealed interface UiPrivateEvent {
    object NavigateBack : UiPrivateEvent
    object Logout : UiPrivateEvent
    data class Navigate(val path: Paths.PrivateSystem) : UiPrivateEvent
    data class ChangeTitles(
        val title: String,
        val subtitle: String,
        val backButton: Boolean = false,
    ) : UiPrivateEvent

    data class SetUser(val myUser: MyUser) : UiPrivateEvent
}