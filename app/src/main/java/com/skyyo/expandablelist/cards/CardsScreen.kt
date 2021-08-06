package com.skyyo.expandablelist.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.skyyo.expandablelist.R
import com.skyyo.expandablelist.theme.cardCollapsedBackgroundColor
import com.skyyo.expandablelist.theme.cardExpandedBackgroundColor

@Composable
fun CardsScreen(viewModel: CardsViewModel) {
    val cards = viewModel.cards.collectAsState()
    val expandedCardIds = viewModel.expandedCardIdsList.collectAsState()
    Scaffold(
        backgroundColor = Color(
            ContextCompat.getColor(
                LocalContext.current,
                R.color.colorDayNightWhite
            )
        )
    ) {
        LazyColumn {
            itemsIndexed(cards.value) { _, card ->
                ExpandableCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    title = { CardTitle(card.title) },
                    toggle = { viewModel.onCardArrowClicked(card.id) },
                    expanded = expandedCardIds.value.contains(card.id),
                    expandedBackgroundColor = cardExpandedBackgroundColor,
                    collapsedBackgroundColor = cardCollapsedBackgroundColor,
                    expandedElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Spacer(modifier = Modifier.heightIn(50.dp))
                        Text(
                            text = "Expandable content here",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
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