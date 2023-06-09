package com.fedorinov.tpumobile.ui.start.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fedorinov.tpumobile.App
import com.fedorinov.tpumobile.R
import com.fedorinov.tpumobile.data.repositories.AuthRepository
import com.fedorinov.tpumobile.data.rest.model.response.AuthResponse
import com.fedorinov.tpumobile.logic.utils.DATE_PATTERN
import com.fedorinov.tpumobile.logic.utils.toString
import com.fedorinov.tpumobile.ui.start.auth.AuthState.*
import com.fedorinov.tpumobile.ui.start.auth.AuthorizationUiEvent.LoginChanged
import com.fedorinov.tpumobile.ui.start.auth.AuthorizationUiEvent.PasswordChanged
import com.fedorinov.tpumobile.ui.start.auth.AuthorizationUiEvent.RememberChanged
import com.fedorinov.tpumobile.ui.start.auth.AuthorizationUiEvent.SignIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class AuthorizationViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // - Состояние экрана
    private val _uiState: MutableStateFlow<AuthorizationUiState> = MutableStateFlow(AuthorizationUiState())
    val uiState: StateFlow<AuthorizationUiState> = _uiState

    // - Обработчик событий экрана
    fun receiveUiEvent(currentEvent: AuthorizationUiEvent) = viewModelScope.launch {
        _uiState.update { prevState ->
            when(currentEvent) {
                is LoginChanged -> changeLogin(prevState, currentEvent.newLogin)
                is PasswordChanged -> changePassword(prevState, currentEvent.newPassword)
                is RememberChanged -> changeRemember(prevState, currentEvent.isRemember)
                is SignIn -> {
                    signIn()
                    uiState.value
                }
            }
        }
    }

    /**
     * Изменяет поле логина.
     * @param [prevState]   - предыдущее состояние.
     * @param [newLogin]    - новая строка логина.
     */
    private fun changeLogin(prevState: AuthorizationUiState, newLogin: String): AuthorizationUiState  {
        // TODO: Добавить валидацию данных
        return prevState.copy(
            login = newLogin
        )
    }

    /**
     * Изменяет поле логина.
     * @param [prevState]   - предыдущее состояние.
     * @param [newPassword] - новая строка пароля.
     */
    private fun changePassword(prevState: AuthorizationUiState, newPassword: String): AuthorizationUiState  {
        // TODO: Добавить валидацию данных
        return prevState.copy(
            password = newPassword
        )
    }

    /**
     * Авторизация в системе.
     */
    private fun signIn() = viewModelScope.launch {
        try {
            // 1. Начинаем крутить баранку, о том, что процесс авторизации стартовал
            _uiState.update { prevState ->
                prevState.copy(
                    authState = Loading
                )
            }
            // 2. Отправляем запрос на авторизацию и получаем результат
            val response = authRepository.authorization(
                email = uiState.value.login,
                password = uiState.value.password,
                rememberMe = uiState.value.isRemember
            )
            // 3.1. Если авторизация прошла успешно
            if (response.user != null) {
                _uiState.update { prevState ->
                    prevState.copy(
                        authState = Success(response)
                    )
                }
                // 3.1.1 Сохраняем токен в хранилище
               authRepository.updateAllDataInDateStore(
                    token = response.token ?: throw Exception(App.getAppResources()?.getString(R.string.exception_failed_save_token)),
                    email = response.user.email ?: "",
                    firstName = response.user.firstName ?: "",
                    lastName = response.user.lastName ?: "",
                    phoneNumber = response.user.phoneNumber ?: "",
                    groupName = response.user.groupName ?: "",
                    languageId = response.user.languageId ?: "",
                    languageName = response.user.languageName ?: ""
               )
            }
            // 3.2 В противном случае вывести информацию по неудачной попытке авторизации.
            else {
                _uiState.update { prevState ->
                    Log.i(TAG, "signIn: response = $response")
                    prevState.copy(
                        authState = Error(response)
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.update { prevState ->
                prevState.copy(
                    authState = Error(
                        authResponse = AuthResponse(
                            type = e::class.simpleName,
                            date = Date().toString(DATE_PATTERN),
                            message = e.message
                        )
                    )
                )
            }
        }
    }

    private fun changeRemember(prevState: AuthorizationUiState, isRemember: Boolean): AuthorizationUiState {
        return prevState.copy(
            isRemember = isRemember
        )
    }

    companion object {
        private val TAG = AuthorizationViewModel::class.simpleName
    }
}