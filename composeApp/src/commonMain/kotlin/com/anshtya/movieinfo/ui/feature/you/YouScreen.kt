package com.anshtya.movieinfo.ui.feature.you

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anshtya.movieinfo.common.data.local.datastore.preferences.SelectedDarkMode
import com.anshtya.movieinfo.common.data.model.AccountDetails
import com.anshtya.movieinfo.common.data.model.LibraryType
import com.anshtya.movieinfo.ui.component.PersonImage
import kotlinx.coroutines.launch
import movieinfo.composeapp.generated.resources.Res
import movieinfo.composeapp.generated.resources.attribution_info
import movieinfo.composeapp.generated.resources.attribution_text
import movieinfo.composeapp.generated.resources.favorites
import movieinfo.composeapp.generated.resources.log_in
import movieinfo.composeapp.generated.resources.log_in_description
import movieinfo.composeapp.generated.resources.log_out
import movieinfo.composeapp.generated.resources.reload_account_details
import movieinfo.composeapp.generated.resources.settings_dialog_adult
import movieinfo.composeapp.generated.resources.settings_dialog_dark_default
import movieinfo.composeapp.generated.resources.settings_dialog_dark_mode
import movieinfo.composeapp.generated.resources.settings_dialog_dark_no
import movieinfo.composeapp.generated.resources.settings_dialog_dark_yes
import movieinfo.composeapp.generated.resources.settings_dialog_dismiss_text
import movieinfo.composeapp.generated.resources.settings_dialog_theme
import movieinfo.composeapp.generated.resources.settings_dialog_theme_default
import movieinfo.composeapp.generated.resources.settings_dialog_theme_dynamic
import movieinfo.composeapp.generated.resources.settings_dialog_title
import movieinfo.composeapp.generated.resources.tmdb_logo
import movieinfo.composeapp.generated.resources.watchlist
import movieinfo.composeapp.generated.resources.your_library
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun YouRoute(
    navigateToAuth: () -> Unit,
    navigateToLibraryItem: (LibraryType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: YouViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userSettings by viewModel.userSettings.collectAsStateWithLifecycle()
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    YouScreen(
        uiState = uiState,
        isLoggedIn = isLoggedIn,
        userSettings = userSettings,
        onChangeTheme = viewModel::setDynamicColorPreference,
        onChangeDarkMode = viewModel::setDarkModePreference,
        onChangeIncludeAdult = viewModel::setAdultResultPreference,
        onNavigateToAuth = navigateToAuth,
        onLibraryItemClick = navigateToLibraryItem,
        onReloadAccountDetailsClick = viewModel::getAccountDetails,
        onRefresh = viewModel::onRefresh,
        onLogOutClick = viewModel::logOut,
        onErrorShown = viewModel::onErrorShown,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun YouScreen(
    uiState: YouUiState,
    isLoggedIn: Boolean?,
    userSettings: UserSettings?,
    onChangeTheme: (Boolean) -> Unit,
    onChangeDarkMode: (SelectedDarkMode) -> Unit,
    onChangeIncludeAdult: (Boolean) -> Unit,
    onNavigateToAuth: () -> Unit,
    onLibraryItemClick: (LibraryType) -> Unit,
    onReloadAccountDetailsClick: () -> Unit,
    onLogOutClick: () -> Unit,
    onRefresh: () -> Unit,
    onErrorShown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    uiState.errorMessage?.let {
        scope.launch { snackbarHostState.showSnackbar(it) }
        onErrorShown()
    }

    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    if (showSettingsDialog) {
        SettingsDialog(
            userSettings = userSettings,
            onChangeTheme = onChangeTheme,
            onChangeDarkMode = onChangeDarkMode,
            onChangeIncludeAdult = onChangeIncludeAdult,
            onDismissRequest = { showSettingsDialog = !showSettingsDialog },
            modifier = modifier
        )
    }

    var showAttributionInfoDialog by rememberSaveable { mutableStateOf(false) }
    if (showAttributionInfoDialog) {
        AttributionInfoDialog(
            onDismissRequest = { showAttributionInfoDialog = !showAttributionInfoDialog },
            modifier = modifier
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(
                        onClick = { showAttributionInfoDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = stringResource(Res.string.attribution_info)
                        )
                    }

                    userSettings?.let {
                        IconButton(
                            onClick = { showSettingsDialog = true }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = stringResource(Res.string.settings_dialog_title)
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh
            ) {
                Column(Modifier.fillMaxSize()) {
                    isLoggedIn?.let {
                        if (isLoggedIn) {
                            uiState.accountDetails?.let {
                                LoggedInView(
                                    accountDetails = it,
                                    isLoggingOut = uiState.isLoggingOut,
                                    onLibraryItemClick = onLibraryItemClick,
                                    onLogOutClick = onLogOutClick
                                )
                            } ?: LoadAccountDetails(
                                isLoading = uiState.isLoading,
                                onReloadAccountDetailsClick = onReloadAccountDetailsClick
                            )
                        } else {
                            LoggedOutView(
                                onNavigateToAuth = onNavigateToAuth
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoggedInView(
    accountDetails: AccountDetails,
    isLoggingOut: Boolean,
    onLibraryItemClick: (LibraryType) -> Unit,
    onLogOutClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        PersonImage(
            imageUrl = accountDetails.avatar ?: "",
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = accountDetails.username,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = accountDetails.name,
            style = MaterialTheme.typography.titleMedium,
        )
        LibrarySection(onLibraryItemClick = onLibraryItemClick)

        if (isLoggingOut) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = onLogOutClick,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(stringResource(Res.string.log_out))
            }
        }
    }
}

@Composable
private fun LoggedOutView(
    onNavigateToAuth: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = stringResource(Res.string.log_in_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            Button(
                onClick = onNavigateToAuth,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.log_in))
            }
        }
    }
}

@Composable
private fun LoadAccountDetails(
    isLoading: Boolean,
    onReloadAccountDetailsClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = onReloadAccountDetailsClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.reload_account_details))
            }
        }
    }
}

@Composable
private fun LibrarySection(
    onLibraryItemClick: (LibraryType) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(Res.string.your_library),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        LibraryItemOption(
            optionName = stringResource(Res.string.favorites),
            onClick = { onLibraryItemClick(LibraryType.FAVORITE) }
        )
        LibraryItemOption(
            optionName = stringResource(Res.string.watchlist),
            onClick = { onLibraryItemClick(LibraryType.WATCHLIST) }
        )
    }
}

@Composable
private fun LibraryItemOption(
    optionName: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .height(42.dp)
    ) {
        Text(
            text = optionName,
            fontSize = 18.sp,
        )
    }
}

@Composable
private fun AttributionInfoDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Text(
                text = stringResource(Res.string.settings_dialog_dismiss_text),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismissRequest() },
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(Res.drawable.tmdb_logo),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.attribution_text),
                    textAlign = TextAlign.Center
                )
            }
        },
        modifier = modifier
    )
}

@Composable
private fun SettingsDialog(
    userSettings: UserSettings?,
    onChangeTheme: (Boolean) -> Unit,
    onChangeDarkMode: (SelectedDarkMode) -> Unit,
    onChangeIncludeAdult: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    userSettings?.let {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = stringResource(Res.string.settings_dialog_title),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            text = {
                HorizontalDivider()
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    SettingsPanel(
                        settings = userSettings,
                        onChangeTheme = onChangeTheme,
                        onChangeDarkMode = onChangeDarkMode,
                        onChangeIncludeAdult = onChangeIncludeAdult
                    )
                }
            },
            confirmButton = {
                Text(
                    text = stringResource(Res.string.settings_dialog_dismiss_text),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { onDismissRequest() },
                )
            },
            modifier = modifier
        )
    }
}

@Composable
private fun SettingsPanel(
    settings: UserSettings,
    onChangeTheme: (Boolean) -> Unit,
    onChangeDarkMode: (SelectedDarkMode) -> Unit,
    onChangeIncludeAdult: (Boolean) -> Unit,
) {
    val supportsDynamicTheme = SupportsDynamicTheme.current
    if (supportsDynamicTheme) {
        SettingsDialogSectionTitle(text = stringResource(Res.string.settings_dialog_theme))
        Column(Modifier.selectableGroup()) {
            SettingsDialogChooserRow(
                text = stringResource(Res.string.settings_dialog_theme_default),
                selected = !settings.useDynamicColor,
                onClick = { onChangeTheme(false) }
            )
            SettingsDialogChooserRow(
                text = stringResource(Res.string.settings_dialog_theme_dynamic),
                selected = settings.useDynamicColor,
                onClick = { onChangeTheme(true) }
            )
        }
    }
    SettingsDialogSectionTitle(text = stringResource(Res.string.settings_dialog_dark_mode))
    Column(Modifier.selectableGroup()) {
        SettingsDialogChooserRow(
            text = stringResource(Res.string.settings_dialog_dark_default),
            selected = settings.darkMode == SelectedDarkMode.SYSTEM,
            onClick = { onChangeDarkMode(SelectedDarkMode.SYSTEM) }
        )
        SettingsDialogChooserRow(
            text = stringResource(Res.string.settings_dialog_dark_yes),
            selected = settings.darkMode == SelectedDarkMode.DARK,
            onClick = { onChangeDarkMode(SelectedDarkMode.DARK) }
        )
        SettingsDialogChooserRow(
            text = stringResource(Res.string.settings_dialog_dark_no),
            selected = settings.darkMode == SelectedDarkMode.LIGHT,
            onClick = { onChangeDarkMode(SelectedDarkMode.LIGHT) }
        )
    }
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SettingsDialogSectionTitle(text = stringResource(Res.string.settings_dialog_adult))
        Switch(
            checked = settings.includeAdultResults,
            onCheckedChange = onChangeIncludeAdult
        )
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
private fun SettingsDialogChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsDialogPreview() {
    SettingsDialog(
        userSettings = UserSettings(
            useDynamicColor = true,
            includeAdultResults = true,
            darkMode = SelectedDarkMode.SYSTEM
        ),
        onChangeTheme = {},
        onChangeDarkMode = {},
        onChangeIncludeAdult = {},
        onDismissRequest = {}
    )
}