package com.skyyo.expandabledraggablelist

import androidx.compose.runtime.Immutable


sealed class CardItemType

@Immutable
data class CardHeader(
    val cardId: Int,
    var isExpanded: Boolean = false
) : CardItemType()

object ProgressBarItem : CardItemType()