package com.skyyo.expandabledraggablelist

import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.core.*
import com.skyyo.expandabledraggablelist.theme.cardBg
import com.skyyo.expandabledraggablelist.theme.greenish

val roundedCorners = IntPropKey(label = "CardCornersTransitionKey")
val paddingHorizontal = IntPropKey(label = "CardPaddingHorizontalTransitionKey")
val elevation = IntPropKey(label = "CardElevationTransitionKey")
val bgColor = ColorPropKey(label = "CardBgColorTransitionKey")

val transitionDefinition by lazy {
    transitionDefinition<CardState> {
        state(CardState.COLLAPSED) {
            this[roundedCorners] = 16
            this[paddingHorizontal] = 24
            this[elevation] = 4
            this[bgColor] = cardBg
        }
        state(CardState.EXPANDED) {
            this[roundedCorners] = 0
            this[paddingHorizontal] = 40
            this[elevation] = 24
            this[bgColor] = greenish
        }
        transition(fromState = CardState.COLLAPSED, toState = CardState.EXPANDED) {
            roundedCorners using tween(
                durationMillis = EXPAND_ANIMATION_DURATION,
                easing = FastOutLinearInEasing
            )
            paddingHorizontal using tween(
                durationMillis = EXPAND_ANIMATION_DURATION,
                easing = FastOutLinearInEasing
            )
            elevation using tween(
                durationMillis = EXPAND_ANIMATION_DURATION,
            )
            bgColor using tween(
                durationMillis = EXPAND_ANIMATION_DURATION,
            )
        }
        transition(fromState = CardState.EXPANDED, toState = CardState.COLLAPSED) {
            roundedCorners using tween(
                durationMillis = COLLAPSE_ANIMATION_DURATION,
                easing = LinearEasing
            )
            paddingHorizontal using tween(
                durationMillis = COLLAPSE_ANIMATION_DURATION,
            )
            elevation using tween(
                durationMillis = COLLAPSE_ANIMATION_DURATION,
            )
            bgColor using tween(
                durationMillis = EXPAND_ANIMATION_DURATION,
            )
        }
    }
}
