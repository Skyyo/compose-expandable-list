package com.skyyo.expandabledraggablelist.cardsList

import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.core.*
import com.skyyo.expandabledraggablelist.theme.cardBg
import com.skyyo.expandabledraggablelist.theme.greenish

val roundedCorners = IntPropKey(label = "CardCornersTransitionKey")
val paddingHorizontal = IntPropKey(label = "CardPaddingHorizontalTransitionKey")
val elevation = IntPropKey(label = "CardElevationTransitionKey")
val bgColor = ColorPropKey(label = "CardBgColorTransitionKey")
val rotationDegree = FloatPropKey(label = "CardArrowRotationTransitionKey")

val transitionDefinition by lazy {
    transitionDefinition<CardState> {
        state(CardState.COLLAPSED) {
            this[roundedCorners] = 16
            this[paddingHorizontal] = 24
            this[elevation] = 4
            this[bgColor] = cardBg
            this[rotationDegree] = 180f
        }
        state(CardState.EXPANDED) {
            this[roundedCorners] = 0
            this[paddingHorizontal] = 48
            this[elevation] = 24
            this[bgColor] = greenish
            this[rotationDegree] = 0f
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
            rotationDegree using tween(
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
            rotationDegree using tween(
                durationMillis = EXPAND_ANIMATION_DURATION,
            )
        }
    }
}
