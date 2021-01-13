package com.skyyo.expandablelist.cards

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.skyyo.expandablelist.R

@Composable
fun CardsScreen(viewModel: CardsViewModel) {
    val cards = viewModel.cards.collectAsState()
    Scaffold(
        backgroundColor = Color(
            ContextCompat.getColor(
                AmbientContext.current,
                R.color.colorDayNightWhite
            )
        )
    ) {
        LazyColumn {
            itemsIndexed(cards.value) { index, card ->
                ExpandableCard(
                    card = card,
                    onCardArrowClick = { viewModel.onCardArrowClicked(index) },
                )
            }
        }
    }
}

@Composable
fun ExpandableCard(
    card: ExpandableCardModel,
    onCardArrowClick: () -> Unit,
) {
    val isExpanded = card.state == CardState.EXPANDED
//    initialState is always  doing anything since we want to animate item only on tap
//    val initialState = card.state
//    val nextState = if (isExpanded) CardState.EXPANDED else CardState.COLLAPSED
    val transitionState = transition(
        definition = cardTransitionDefinition,
        initState = card.state,
        toState = card.state
    )
    Card(
        backgroundColor = transitionState[bgColor],
        contentColor = Color(
            ContextCompat.getColor(
                AmbientContext.current,
                R.color.colorDayNightPurple
            )
        ),
        elevation = transitionState[elevation].dp,
        shape = RoundedCornerShape(transitionState[roundedCorners].dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = transitionState[paddingHorizontal].dp,
                vertical = 8.dp
            )
    ) {
        Column {
            Box {
                CardArrow(
                    degrees = transitionState[rotationDegree],
                    onClick = onCardArrowClick
                )
                CardTitle(title = card.title)
            }
            ExpandableContent(visible = isExpanded, initialVisibility = isExpanded)
        }
    }
}

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                imageVector = vectorResource(id = R.drawable.ic_expand_less_24),
                modifier = Modifier.rotate(degrees),
            )
        }
    )
}

@Composable
fun CardTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableContent(
    visible: Boolean = true,
    initialVisibility: Boolean = false
) {
    // remember these specs so they don't restart if recomposing during the animation
    // this is required since TweenSpec restarts on interruption
    val enterFadeIn = remember {
        fadeIn(
            animSpec = TweenSpec(
                durationMillis = FADE_IN_ANIMATION_DURATION,
                easing = FastOutLinearInEasing
            )
        )
    }
    val enterExpand = remember {
        expandVertically(animSpec = tween(EXPAND_ANIMATION_DURATION))
    }
    val exitFadeOut = remember {
        fadeOut(
            animSpec = TweenSpec(
                durationMillis = FADE_OUT_ANIMATION_DURATION,
                easing = LinearOutSlowInEasing
            )
        )
    }
    val exitCollapse = remember {
        shrinkVertically(animSpec = tween(COLLAPSE_ANIMATION_DURATION))
    }
    AnimatedVisibility(
        visible = visible,
        initiallyVisible = initialVisibility,
        enter = enterExpand + enterFadeIn,
        exit = exitCollapse + exitFadeOut
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Spacer(modifier = Modifier.heightIn(100.dp))
            Text(
                text = "Expandable content here",
                textAlign = TextAlign.Center
            )
        }

    }
}
