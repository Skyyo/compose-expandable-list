package com.skyyo.expandabledraggablelist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.skyyo.expandabledraggablelist.ui.ExpandableDraggableListTheme


const val EXPAND_ANIMATION_DURATION = 325
const val COLLAPSE_ANIMATION_DURATION = 100

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
                        onCardArrowClicked = { vm.onCardArrowClicked(index) }
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
    cardId: Int = card.cardId,
    cardIsExpanded: Boolean = card.isExpanded,
) {
    Card(
        backgroundColor = Color(
            ContextCompat.getColor(
                AmbientContext.current,
                R.color.colorDayNighCardBg
            )
        ),
        contentColor = Color(
            ContextCompat.getColor(
                AmbientContext.current,
                R.color.colorDayNightPurple
            )
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
//            .animateContentSize(animSpec = TweenSpec(EXPAND_ANIMATION_DURATION), clip = false)
    ) {
        Column {
            Box {
                CardArrow(isExpanded = cardIsExpanded, onClicked = onCardArrowClicked)
                CardTitle(title = "$cardId")
            }
            AnimatedExpandableContent(visible = cardIsExpanded, initialVisibility = cardIsExpanded)
        }
    }
}

@Composable
fun CardArrow(
    isExpanded: Boolean,
    onClicked: () -> Unit,
    imageId: Int = if (isExpanded) R.drawable.ic_expand_less_24 else R.drawable.ic_expand_more_24
) {
    IconButton(
        onClick = onClicked,
        content = { Icon(vectorResource(id = imageId)) }
    )
}

@Composable
fun CardTitle(title: String) {
    Text(
        text = AnnotatedString("Card $title"),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
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
    val enter = remember {
        fadeIn(
            animSpec = TweenSpec(
                durationMillis = EXPAND_ANIMATION_DURATION,
                easing = FastOutLinearInEasing
            )
        )
    }
    val exit = remember {
        fadeOut(
            animSpec = TweenSpec(
                durationMillis = COLLAPSE_ANIMATION_DURATION,
                easing = FastOutSlowInEasing
            )
        )
    }
    AnimatedVisibility(
        visible = visible,
        initiallyVisible = initialVisibility,
//        enter = enter,
//        exit = exit,
        enter = expandVertically() + enter,
        exit = shrinkVertically(
            animSpec = tween(
                durationMillis = COLLAPSE_ANIMATION_DURATION,
            )
        ) + exit
    ) {
        Spacer(modifier = Modifier.heightIn(100.dp))
        Text(
            text = AnnotatedString("Expandable content here"),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}
