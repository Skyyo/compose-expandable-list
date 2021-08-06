package com.skyyo.expandablelist.cards

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.skyyo.expandablelist.R

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ExpandableCard(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    toggle: () -> Unit,
    expanded: Boolean,
    expandedBackgroundColor: Color = MaterialTheme.colors.background,
    collapsedBackgroundColor: Color = MaterialTheme.colors.background,
    expandedElevation: Dp = 1.dp,
    collapsedElevation: Dp = 1.dp,
    shape: Shape = MaterialTheme.shapes.medium,
    content: @Composable () -> Unit
) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "transition")
    val cardBgColor by transition.animateColor({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "bgColorTransition") {
        if (expanded) expandedBackgroundColor else collapsedBackgroundColor
    }
    val cardElevation by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "elevationTransition") {
        if (expanded) expandedElevation else collapsedElevation
    }
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0f else 180f
    }

    Card(
        backgroundColor = cardBgColor,
        contentColor = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.colorDayNightPurple
            )
        ),
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
            ExpandableContent(visible = expanded, content = content)
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableContent(
    visible: Boolean = true,
    content: @Composable () -> Unit,
) {
    val enterFadeIn = remember {
        fadeIn(
            animationSpec = TweenSpec(
                durationMillis = FADE_IN_ANIMATION_DURATION,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember {
        expandVertically(animationSpec = tween(EXPAND_ANIMATION_DURATION))
    }
    val exitFadeOut = remember {
        fadeOut(
            animationSpec = TweenSpec(
                durationMillis = FADE_OUT_ANIMATION_DURATION,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(COLLAPSE_ANIMATION_DURATION))
    }
    AnimatedVisibility(
        modifier = Modifier.padding(8.dp),
        visible = visible,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut,
    ) {
        content()
    }
}