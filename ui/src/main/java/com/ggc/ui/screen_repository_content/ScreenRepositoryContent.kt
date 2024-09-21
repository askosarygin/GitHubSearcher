package com.ggc.ui.screen_repository_content

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ggc.ui.R.drawable.icon_file
import com.ggc.ui.R.drawable.icon_folder
import com.ggc.ui.theme.RepositoryContentFile
import com.ggc.ui.theme.RepositoryContentFolder
import com.ggc.ui.theme.RepositoryContentText

@Composable
fun ScreenRepositoryContent() {

}

@Composable
private fun MenuItemFolder(
    name: String,
    onClick: () -> Unit
) {
    MenuItem(
        imageResource = icon_folder,
        color = RepositoryContentFolder,
        name = name,
        onClick = onClick
    )
}

@Composable
private fun MenuItemFile(
    name: String,
    onClick: () -> Unit
) {
    MenuItem(
        imageResource = icon_file,
        color = RepositoryContentFile,
        name = name,
        onClick = onClick
    )
}

@Composable
private fun MenuItem(
    imageResource: Int,
    color: Color,
    name: String,
    onClick: () -> Unit
) {
    Layout(
        modifier = Modifier.clickable(onClick = onClick).padding(start = 10.dp),
        content = {
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .layoutId("MENU_ITEM_ICON"),
                colorFilter = ColorFilter.tint(color = color),
                painter = painterResource(id = imageResource),
                contentScale = ContentScale.FillHeight,
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 10.dp)
                    .layoutId("MENU_ITEM_NAME"),
                text = name,
                color = RepositoryContentText,
                fontSize = 18.sp
            )
        },
        measurePolicy = MeasurePolicy { measurables, constraints ->
            val menuItemNameMeasurable = measurables.find { it.layoutId == "MENU_ITEM_NAME" }!!
            val menuItemName = menuItemNameMeasurable.measure(constraints)

            val newConstraints = constraints.copy(
                minHeight = menuItemName.height,
                maxHeight = menuItemName.height,
                minWidth = menuItemName.height,
                maxWidth = menuItemName.height
            )

            val menuItemImageMeasurable = measurables.find { it.layoutId == "MENU_ITEM_ICON" }!!
            val menuItemImage = menuItemImageMeasurable.measure(newConstraints)

            layout(width = constraints.maxWidth, height = menuItemName.height) {
                menuItemImage.placeRelative(x = 0, y = 0)
                menuItemName.placeRelative(x = menuItemImage.width, y = 0)
            }
        }

    )
}