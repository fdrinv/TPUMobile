package com.fedorinov.tpumobile.ui.start.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.fedorinov.tpumobile.R
import com.fedorinov.tpumobile.ui.common.CheckBoxWithText
import com.fedorinov.tpumobile.ui.start.LoginState
import com.fedorinov.tpumobile.ui.start.auth.AuthorizationUiEvent.*
import com.fedorinov.tpumobile.ui.start.components.AuthResultWindow
import com.fedorinov.tpumobile.ui.start.components.LogoCompanyBrand
import com.fedorinov.tpumobile.ui.theme.BUTTON_SHAPE
import com.fedorinov.tpumobile.ui.theme.ICON_MEDIUM_SIZE
import com.fedorinov.tpumobile.ui.theme.PADDING_BIG
import com.fedorinov.tpumobile.ui.theme.PADDING_MEDIUM
import com.fedorinov.tpumobile.ui.theme.TPUMobileTheme
import org.koin.androidx.compose.getViewModel

private const val WEIGHT_FOR_DIVIDER = 1f
private const val WEIGHT_FOR_TEXT = 0.5f

@Composable
fun AuthorizationScreen(signUp: () -> Unit) {
    val viewModel: AuthorizationViewModel = getViewModel()
    val uiState by viewModel.uiState.collectAsState()

    AuthorizationScreenStateless(
        // - Состояние окна с результатом авторизации
        loginState = uiState.loginState,
        // - Поле логина
        login = uiState.login,
        onLoginChanged = { newLogin -> viewModel.receiveUiEvent(LoginChanged(newLogin)) },
        // Поле пароля
        password = uiState.password,
        isHidePassword = uiState.isHidePassword,
        onPasswordChanged = { newPassword -> viewModel.receiveUiEvent(PasswordChanged(newPassword)) },
        onHideChanged = { viewModel.receiveUiEvent(PasswordVisibilityChanged) },
        // - Запомнить пользователя
        isRemember = uiState.isRemember,
        onRememberClicked = { isRemember -> viewModel.receiveUiEvent(RememberChanged(isRemember)) },
        // - Забыл пароль
        onForgotPasswordClicked = { },
        // - Войти
        signIn = { viewModel.receiveUiEvent(SignIn) },
        // - Зарегистрироваться
        signUp = signUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthorizationScreenStateless(
    // - Состояние окна с результатом авторизации
    loginState: LoginState = LoginState.Loading,
    // - Поле логина
    login: String = "fedorinovea",
    onLoginChanged: (String) -> Unit = {},
    // - Поле пароля
    password: String = "qwerty",
    isHidePassword: Boolean = false,
    onPasswordChanged: (String) -> Unit = {},
    onHideChanged: () -> Unit = {},
    // - Запомнить пользователя
    isRemember: Boolean = true,
    onRememberClicked: (Boolean) -> Unit = {},
    // - Забыл пароль
    onForgotPasswordClicked: () -> Unit = {},
    // - Войти
    signIn: () -> Unit = {},
    // - Зарегистрироваться
    signUp: () -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AuthorizationTopBar() },
        content = { paddingValues ->
            AuthorizationContent(
                paddingValues = paddingValues,
                // - Состояние окна с результатом авторизации
                loginState = loginState,
                // - Поле логина
                login = login,
                onLoginChanged = onLoginChanged,
                // - Поле пароля
                password = password,
                isHidePassword = isHidePassword,
                onPasswordChanged = onPasswordChanged,
                onHideChanged = onHideChanged,
                // - Запомнить пользователя
                isRemember = isRemember,
                onRememberClicked = onRememberClicked,
                // - Забыл пароль
                onForgotPasswordClicked = onForgotPasswordClicked,
                // - Войти
                signIn = signIn,
                // - Зарегистрироваться
                signUp = signUp,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthorizationTopBar() {
    CenterAlignedTopAppBar(title = { Text(stringResource(R.string.title_authorization)) })
}

@Composable
private fun AuthorizationContent(
    paddingValues: PaddingValues,
    // - Состояние окна с результатом авторизации
    loginState: LoginState,
    // - Поле логина
    login: String,
    onLoginChanged: (String) -> Unit,
    // Поле пароля
    password: String,
    isHidePassword: Boolean,
    onPasswordChanged: (String) -> Unit,
    onHideChanged: () -> Unit,
    // - Запомнить пользователя
    isRemember: Boolean,
    onRememberClicked: (Boolean) -> Unit,
    // - Забыл пароль
    onForgotPasswordClicked: () -> Unit,
    // - Войти
    signIn: () -> Unit,
    // - Зарегистрироваться
    signUp: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = PADDING_BIG,
                end = PADDING_BIG
            )
    ) {
        // - Компонент бренда и логотипа бренда
        LogoCompanyBrand()
        // - Окно с информацией (результатом) авторизации
        AuthResultWindow(
            loginState = loginState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = PADDING_BIG)
        )
        // - Логин
        LoginTextField(
            login = login,
            onLoginChanged = onLoginChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PADDING_BIG)
        )
        // - Пароль
        PasswordTextField(
            password = password,
            isHidePassword = isHidePassword,
            onPasswordChanged = onPasswordChanged,
            onHideChanged = onHideChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PADDING_BIG)
        )
        // - Войти
        Button(
            onClick = signIn,
            enabled = login.isNotEmpty() && password.isNotEmpty(),
            shape = RoundedCornerShape(BUTTON_SHAPE),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PADDING_MEDIUM),
        ) {
            Text(stringResource(R.string.button_sign_in))
        }
        // - Запомнить пользователя + Забыл пароль?
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PADDING_MEDIUM),
        ) {
            CheckBoxWithText(
                isChecked = isRemember,
                text = stringResource(R.string.checkbox_remember_me),
                onCheckedChange = onRememberClicked
            )
            TextButton(onClick = onForgotPasswordClicked) {
                Text(stringResource(R.string.button_forgot_password))
            }
        }
        // - Зарегистрироваться
        OutlinedButton(
            onClick = signUp,
            enabled = loginState != LoginState.Loading,
            shape = RoundedCornerShape(BUTTON_SHAPE),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PADDING_BIG),
        ) {
            Text(stringResource(R.string.button_sign_up))
        }
        // - Альтернативные варианты входа
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(WEIGHT_FOR_DIVIDER))
                Text(
                    text = stringResource(R.string.text_or),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = PADDING_MEDIUM)
                        .weight(WEIGHT_FOR_TEXT)
                )
                Divider(modifier = Modifier.weight(WEIGHT_FOR_DIVIDER))
            }
            Spacer(modifier = Modifier.height(PADDING_BIG))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.tpu_logo),
                        contentDescription = stringResource(R.string.tpu),
                        modifier = Modifier.size(ICON_MEDIUM_SIZE),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginTextField(
    modifier: Modifier = Modifier,
    login: String,
    onLoginChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = login,
        onValueChange = onLoginChanged,
        singleLine = true,
        placeholder = {
            Text(stringResource(R.string.text_field_login))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (login.isNotEmpty()) {
                IconButton(onClick = { onLoginChanged("") }) {
                    Icon(
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PasswordTextField(
    modifier: Modifier = Modifier,
    password: String,
    isHidePassword: Boolean,
    onPasswordChanged: (String) -> Unit,
    onHideChanged: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = password,
        onValueChange = onPasswordChanged,
        singleLine = true,
        visualTransformation =
            if (isHidePassword) PasswordVisualTransformation()
            else VisualTransformation.None,
        placeholder = {
            Text(stringResource(R.string.text_field_password))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Key,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = onHideChanged) {
                Icon(
                    imageVector =
                        if (isHidePassword) Icons.Outlined.VisibilityOff
                        else Icons.Outlined.Visibility,
                    contentDescription = null,
                    tint =
                        if (isHidePassword) Color.Unspecified
                        else MaterialTheme.colorScheme.primary,
                )
            }
        }
    )
}

@Preview(device = "id:pixel_4", locale = "ru")
@Composable
private fun AuthorizationScreenPreview() {
    TPUMobileTheme {
        AuthorizationScreenStateless()
    }
}