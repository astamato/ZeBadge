package de.berlindroid.zeapp.zeui.zehome

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import de.berlindroid.zeapp.zeui.ZeNavigationPad
import de.berlindroid.zeapp.zeui.zeabout.ZeAbout
import de.berlindroid.zeapp.zeui.zetheme.ZeBadgeAppTheme
import de.berlindroid.zeapp.zevm.ZeBadgeViewModel
import kotlinx.coroutines.launch

@Composable
internal fun ZeScreen(vm: ZeBadgeViewModel, modifier: Modifier = Modifier) {
    val lazyListState = rememberLazyListState()
    var isShowingAbout by remember { mutableStateOf(false) }
    var isShowingOpenSource by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val goToReleases: () -> Unit = remember {
        {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gdg-berlin-android/ZeBadge/releases")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }
    val goToGithubPage: () -> Unit = remember {
        {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gdg-berlin-android/ZeBadge")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    BackHandler(isShowingOpenSource || isShowingAbout) {
        isShowingOpenSource = false
        isShowingAbout = false
    }

    ZeBadgeAppTheme(
        content = {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ZeDrawerContent(
                        drawerState,
                        onGetStoredPages = vm::getStoredPages,
                        onSaveAllClick = vm::saveAll,
                        onGotoReleaseClick = goToReleases,
                        onGotoOpenSourceClick = { isShowingOpenSource = !isShowingOpenSource },
                        onUpdateConfig = vm::listConfiguration,
                        onCloseDrawer = {
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        onTitleClick = goToGithubPage,
                    )
                },
            ) {
                Scaffold(
                    modifier = modifier,
                    floatingActionButton = {
                        if (!isShowingAbout) {
                            ZeNavigationPad(
                                lazyListState,
                            )
                        }
                    },
                    topBar = {
                        ZeTopBar(
                            isShowingAbout = isShowingAbout,
                            onAboutClick = { isShowingAbout = !isShowingAbout },
                            isNavDrawerOpen = drawerState.isOpen,
                            onOpenMenuClicked = { scope.launch { drawerState.open() } },
                            onCloseMenuClicked = { scope.launch { drawerState.close() } },
                            onTitleClick = {
                                scope.launch { drawerState.close() }
                                goToGithubPage()
                            },
                        )
                    },
                    content = { paddingValues ->
                        if (isShowingAbout) {
                            ZeAbout(paddingValues)
                        } else if (isShowingOpenSource) {
                            ZeOpenSource(paddingValues)
                        } else {
                            ZePages(
                                paddingValues = paddingValues,
                                lazyListState = lazyListState,
                                vm = vm,
                            )
                        }
                    },
                )
            }
        },

        )
}
