package de.berlindroid.zeapp.zeui.zehome

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.LibraryDefaults.libraryColors
import de.berlindroid.zeapp.zeui.zetheme.ZeBlack
import de.berlindroid.zeapp.zeui.zetheme.ZeWhite

@Composable
internal fun ZeOpenSource(
    paddingValues: PaddingValues,
) {
    Surface {
        LibrariesContainer(
            Modifier.fillMaxSize(),
            contentPadding = paddingValues,
            colors = libraryColors(
                backgroundColor = ZeBlack,
                badgeBackgroundColor = ZeWhite,
            ),
        )
    }
}
