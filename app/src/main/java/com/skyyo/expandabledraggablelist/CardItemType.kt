package com.skyyo.expandabledraggablelist

import androidx.compose.runtime.Immutable


sealed class CardItemType

@Immutable
data class CardHeader(
    val cardId: Int,
    val cardState: CardState = CardState.COLLAPSED
) : CardItemType()

object ProgressBarItem : CardItemType()


