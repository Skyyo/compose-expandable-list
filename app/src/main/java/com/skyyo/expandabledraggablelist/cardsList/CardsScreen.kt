package com.skyyo.expandabledraggablelist.cardsList

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.skyyo.expandabledraggablelist.*
import com.skyyo.expandabledraggablelist.R

@Composable
fun CardsScreen(vm: CardsViewModel) {
    val cardsState = vm.cards.collectAsState()
    Scaffold(
        backgroundColor = Color(
            ContextCompat.getColor(
                AmbientContext.current,
                R.color.colorDayNightWhite
            )
        )
    ) {
        LazyColumn {
            itemsIndexed(cardsState.value) { index, card ->
                when (card) {
                    is CardHeader -> ExpandableCard(
                        card = card,
                        onCardArrowClicked = { vm.onCardArrowClicked(index) },
                    )
                    is ProgressBarItem -> PaginationIndicator()
                }
            }
        }
    }
}

@Composable
fun ExpandableCard(
    card: CardHeader,
    onCardArrowClicked: () -> Unit,
    isExpanded: Boolean = card.cardState == CardState.EXPANDED,
) {
    val cardState = remember { mutableStateOf(card.cardState) }
    val state = transition(
        definition = transitionDefinition,
        initState = cardState.value,
        toState = if (isExpanded) CardState.EXPANDED else CardState.COLLAPSED,
    )
    Card(
        backgroundColor = state[bgColor],
        contentColor = Color(
            ContextCompat.getColor(
                AmbientContext.current,
                R.color.colorDayNightPurple
            )
        ),
        elevation = state[elevation].dp,
        shape = RoundedCornerShape(state[roundedCorners].dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = state[paddingHorizontal].dp, vertical = 8.dp)
    ) {
        Column {
            Box {
                Log.d("vovk", "composing IconButton: ${card.cardTitle}:${state[rotationDegree]}")
                CardArrow(
                    degrees = state[rotationDegree],
                    onClicked = onCardArrowClicked
                )
                CardTitle(title = card.cardTitle)
            }
            AnimatedExpandableContent(visible = isExpanded, initialVisibility = isExpanded)
        }
    }
}

@Composable
fun CardArrow(
    degrees: Float,
    onClicked: () -> Unit
) {
    IconButton(
        onClick = onClicked,
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

@Composable
fun PaginationIndicator() {
    CircularProgressIndicator(
        color = Color(
            ContextCompat.getColor(
                AmbientContext.current,
                R.color.colorDayNightPurple
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .size(36.dp)
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedExpandableContent(
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
