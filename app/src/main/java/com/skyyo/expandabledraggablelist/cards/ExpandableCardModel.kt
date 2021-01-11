package com.skyyo.expandabledraggablelist.cards

import androidx.compose.runtime.Immutable

@Immutable
data class ExpandableCardModel(
    val title: String,
    val state: CardState = CardState.COLLAPSED
)