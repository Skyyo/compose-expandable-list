package com.skyyo.expandabledraggablelist.cardsList

import androidx.compose.runtime.Immutable


sealed class CardItemType

@Immutable
data class CardHeader(
    val cardTitle: String,
    val cardState: CardState = CardState.COLLAPSED
) : CardItemType()

object ProgressBarItem : CardItemType()


