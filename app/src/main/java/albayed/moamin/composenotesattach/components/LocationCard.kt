package albayed.moamin.composenotesattach.components

import albayed.moamin.composenotesattach.models.Location
import albayed.moamin.composenotesattach.utils.fetchThumbnailUri
import albayed.moamin.composenotesattach.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest


@Composable
fun LocationCard(
    modifier: Modifier = Modifier,
    location: Location,
    isDeleteMode: MutableState<Boolean>,
    isNewDeleteProcess: Boolean,
    onClick: (Location)-> Unit,
    checkedDelete: (MutableState<Boolean>) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isSelected = remember {
        mutableStateOf(false)
    }
    if (isNewDeleteProcess) {
        isSelected.value = false
    }

    ConstraintLayout(
        modifier = modifier
            .padding(5.dp)
            .height(200.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (isDeleteMode.value) {
                            checkedDelete(isSelected)
                        } else {
                            onClick(location)
                        }
                    },
                    onLongPress = {
                        isDeleteMode.value = true
                        checkedDelete(isSelected)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                )
            },
    ) {
        val checkRef = createRef()
        Card(
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(2.dp, color = MaterialTheme.colors.primary),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 5.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(5.dp),
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(fetchThumbnailUri(location))
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.location),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Map Thumbnail"
                )
                Divider(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight(0.9f)
                        .align(Alignment.CenterVertically),
                    color = MaterialTheme.colors.onSurface
                )
                Text(modifier = Modifier.padding(5.dp).fillMaxWidth(), textAlign = TextAlign.Left, text = location.description, color = MaterialTheme.colors.onSurface)
            }
        }
        if (isDeleteMode.value) {
            CircleCheckbox(modifier = Modifier.constrainAs(checkRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }, selected = isSelected.value) {
                checkedDelete(isSelected)
            }
        }
    }

}

