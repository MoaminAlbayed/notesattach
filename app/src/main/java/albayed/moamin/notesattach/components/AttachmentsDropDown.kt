package albayed.moamin.notesattach.components

import albayed.moamin.notesattach.R
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun AttachmentsDropDown(isVisible: MutableState<Boolean>, vararg attachmentCount: Int) {
    val attachmentIconScale = 2.2f
    val attachmentIconPadding = 2.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
            .absolutePadding(top = 45.dp, right = 20.dp)
    ) {
        DropdownMenu(
            expanded = isVisible.value,
            onDismissRequest = { isVisible.value = false }
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
            DropdownMenuItem(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(5.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {

                }) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    //contentAlignment = Alignment.Center
//                )
//                {
                    AttachmentIcon(
                        icon = R.drawable.photo,
                        scale = attachmentIconScale,
                        padding = attachmentIconPadding,
                        contentDescription = "Camera Button",
                        tint = MaterialTheme.colors.primary,
                        count = attachmentCount[0]
                    )
//                }
            }
            DropdownMenuItem(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(5.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {

            }) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                ) {
                    AttachmentIcon(
                        icon = R.drawable.location,
                        scale = attachmentIconScale,
                        padding = attachmentIconPadding,
                        contentDescription = "Locations Button",
                        tint = MaterialTheme.colors.primary,
                        count = attachmentCount[1]
                    )
//                }
            }
        }

            Row(modifier = Modifier.fillMaxSize()) {
                DropdownMenuItem(
                    modifier = Modifier.weight(1f).aspectRatio(1f).padding(5.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {

                }) {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                    ) {
                        AttachmentIcon(
                            icon = R.drawable.video,
                            scale = attachmentIconScale,
                            padding = attachmentIconPadding,
                            contentDescription = "Video Button",
                            tint = MaterialTheme.colors.primary,
                            count = attachmentCount[2]
                        )
//                    }
                }
                DropdownMenuItem(
                    modifier = Modifier.weight(1f).aspectRatio(1f).padding(5.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {

                    }) {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                    ) {
                        AttachmentIcon(
                            icon = R.drawable.alarm,
                            scale = attachmentIconScale,
                            padding = attachmentIconPadding,
                            contentDescription = "Alarms Button",
                            tint = MaterialTheme.colors.primary,
                            count = attachmentCount[2]
                        )
//                    }
                }
            }
            Row(modifier = Modifier.fillMaxSize(0.5f)) {
                DropdownMenuItem(
                    modifier = Modifier.weight(1f).aspectRatio(1f).padding(5.dp),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {

                    }) {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                    ) {
                        AttachmentIcon(
                            icon = R.drawable.mic,
                            scale = attachmentIconScale,
                            padding = attachmentIconPadding,
                            contentDescription = "Mic Button",
                            tint = MaterialTheme.colors.primary,
                            count = attachmentCount[2]
                        )
//                    }
                }

            }
        }
    }
}
