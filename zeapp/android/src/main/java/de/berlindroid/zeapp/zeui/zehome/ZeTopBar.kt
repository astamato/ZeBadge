package de.berlindroid.zeapp.zeui.zehome

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ZeTopBar(
    isShowingAbout: Boolean,
    onAboutClick: () -> Unit,
    isNavDrawerOpen: Boolean,
    onOpenMenuClicked: () -> Unit,
    onCloseMenuClicked: () -> Unit,
    onTitleClick: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = if (isNavDrawerOpen) onCloseMenuClicked else onOpenMenuClicked) {
                Icon(
                    imageVector = if (isNavDrawerOpen) {
                        Icons.AutoMirrored.Filled.ArrowBack
                    } else {
                        Icons.Filled.Menu
                    },
                    contentDescription = "Menu button",
                )
            }
        },
        title = {
            ZeTitle {
                onTitleClick()
            }
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
        ),
        actions = {
            IconButton(onClick = onAboutClick) {
                if (isShowingAbout) {
                    Icon(Icons.Default.Close, contentDescription = "About")
                } else {
                    Icon(Icons.Default.Info, contentDescription = "Close About screen")
                }
            }
        },
    )
}
