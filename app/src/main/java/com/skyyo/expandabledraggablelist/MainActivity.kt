package com.skyyo.expandabledraggablelist

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.skyyo.expandabledraggablelist.theme.ExpandableDraggableListTheme


const val EXPAND_ANIMATION_DURATION = 300
const val COLLAPSE_ANIMATION_DURATION = 300
const val FADE_IN_ANIMATION_DURATION = 350
const val FADE_OUT_ANIMATION_DURATION = 300


class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<CardsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpandableDraggableListTheme {
                Surface(color = MaterialTheme.colors.background) { CardsScreen(viewModel) }
            }
        }
    }
}

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
    cardId: Int = remember { card.cardId },
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
                CardArrow(isExpanded = isExpanded, onClicked = onCardArrowClicked)
                CardTitle(title = "$cardId")
            }
            AnimatedExpandableContent(visible = isExpanded, initialVisibility = isExpanded)
        }
    }
}

@Composable
fun CardArrow(
    isExpanded: Boolean,
    onClicked: () -> Unit
) {
    val imageId: Int =
        if (isExpanded) R.drawable.ic_expand_less_24 else R.drawable.ic_expand_more_24
    IconButton(
        onClick = onClicked,
        content = { Icon(vectorResource(id = imageId)) }
    )
}

@Composable
fun CardTitle(title: String) {
    val cardTitle = remember { "Card $title" }
    Text(
        text = AnnotatedString(cardTitle),
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
