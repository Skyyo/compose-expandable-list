package com.skyyo.expandablelist.cards

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableCard(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    toggle: () -> Unit,
    expanded: Boolean,
    contentColor: Color? = null,
    expandedBackgroundColor: Color = MaterialTheme.colors.background,
    collapsedBackgroundColor: Color = MaterialTheme.colors.background,
    expandedElevation: Dp = 1.dp,
    collapsedElevation: Dp = 1.dp,
    shape: Shape = MaterialTheme.shapes.medium,
    animationDurationMillis: Int = AnimationConstants.DefaultDurationMillis,
    content: @Composable () -> Unit
) {
    val transitionState = remember { MutableTransitionState(expanded) }
    transitionState.targetState = expanded
    val transition = updateTransition(transitionState, label = "transition")
    val cardBgColor by transition.animateColor({
        tween(durationMillis = animationDurationMillis)
    }, label = "bgColorTransition") {
        if (it) expandedBackgroundColor else collapsedBackgroundColor
    }
    val cardElevation by transition.animateDp({
        tween(durationMillis = animationDurationMillis)
    }, label = "elevationTransition") {
        if (it) expandedElevation else collapsedElevation
    }
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = animationDurationMillis)
    }, label = "rotationDegreeTransition") {
        if (it) 0f else -180f
    }

    Card(
        backgroundColor = cardBgColor,
        contentColor = contentColor ?: contentColorFor(backgroundColor = cardBgColor),
        elevation = cardElevation,
        shape = shape,
        modifier = modifier,
    ) {
        Column {
            val interactionSource = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier.clickable(
                    onClick = toggle,
                    interactionSource = interactionSource,
                    indication = null,
                )
            ) {
                CardArrow(degrees = arrowRotationDegree)
                title()
            }
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut(),
            ) {
                content()
            }
        }
    }
}

@Composable
fun CardArrow(
    degrees: Float,
) {
    Box(modifier = Modifier.size(48.dp)) {
        Icon(
            imageVector = Icons.Default.ExpandMore,
            contentDescription = "Expandable Arrow",
            modifier = Modifier
                .rotate(degrees)
                .align(Alignment.Center),
        )
    }
}
