package de.berlindroid.zeapp.zeui.zehome

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import de.berlindroid.zeapp.ZeDimen
import de.berlindroid.zeapp.zevm.ZeBadgeViewModel

@Composable
internal fun ZePages(
    paddingValues: PaddingValues,
    vm: ZeBadgeViewModel,
    lazyListState: LazyListState,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        val uiState by vm.uiState.collectAsState() // should be replace with 'collectAsStateWithLifecycle'

        val editor = uiState.currentSlotEditor
        val templateChooser = uiState.currentTemplateChooser
        val message = uiState.message
        val messageProgress = uiState.messageProgress
        val slots = uiState.slots
        val badgeConfiguration = uiState.currentBadgeConfig

        if (badgeConfiguration != null) {
            BadgeConfigEditor(
                config = badgeConfiguration,
                onDismissRequest = vm::closeConfiguration,
                onConfirmed = vm::updateConfiguration,
            )
        }

        if (editor != null) {
            SelectedEditor(editor, vm)
        }

        if (templateChooser != null) {
            TemplateChooserDialog(vm, templateChooser)
        }

        // column surrounding a lazycolumn: so the message stays ontop.
        Column {
            if (message.isNotEmpty()) {
                InfoBar(message, messageProgress, vm::copyInfoToClipboard)
            }

            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(
                    start = ZeDimen.One,
                    end = ZeDimen.One,
                    top = ZeDimen.Half,
                    bottom = ZeDimen.One,
                ),
            ) {
                items(
                    slots.keys.toList(),
                ) { slot ->
                    var isVisible by remember { mutableStateOf(false) }
                    val alpha: Float by animateFloatAsState(
                        targetValue = if (isVisible) 1f else 0f,
                        label = "alpha",
                        animationSpec = tween(durationMillis = 750),
                    )
                    LaunchedEffect(slot) {
                        isVisible = true
                    }

                    PagePreview(
                        modifier = Modifier.graphicsLayer { this.alpha = alpha },
                        name = slot::class.simpleName ?: "WTF",
                        bitmap = vm.slotToBitmap(slot),
                        customizeThisPage = if (slot.isSponsor) {
                            { vm.customizeSponsorSlot(slot) }
                        } else {
                            { vm.customizeSlot(slot) }
                        },
                        resetThisPage = if (slot.isSponsor) {
                            null
                        } else {
                            { vm.resetSlot(slot) }
                        },
                        sendToDevice = {
                            vm.sendPageToBadgeAndDisplay(slot)
                        },
                    )

                    Spacer(modifier = Modifier.height(ZeDimen.One))
                }
            }
        }
    }
}
