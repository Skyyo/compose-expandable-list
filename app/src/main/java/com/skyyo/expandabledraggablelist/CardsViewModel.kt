package com.skyyo.expandabledraggablelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardsViewModel : ViewModel() {

    private val _cards = MutableStateFlow(listOf<CardItemType>())
    val cards: StateFlow<List<CardItemType>> get() = _cards

    init {
        getFakeData()
    }

    fun getFakeData() {
        viewModelScope.launch(Dispatchers.Default) {
            val testList = arrayListOf<CardItemType>()
            repeat(20) { testList += (CardHeader(cardId = it)) }
            testList += ProgressBarItem
            _cards.value += testList
        }
    }

    fun onCardArrowClicked(cardIndex: Int) {
        _cards.value = _cards.value.toMutableList().also {
            val currentCard = (it[cardIndex] as CardHeader)
            val newState = if (currentCard.cardState == CardState.EXPANDED) {
                CardState.COLLAPSED
            } else {
                CardState.EXPANDED
            }
            it[cardIndex] = currentCard.copy(cardState = newState)
        }
    }

}
