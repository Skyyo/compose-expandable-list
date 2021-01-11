package com.skyyo.expandabledraggablelist.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardsViewModel : ViewModel() {

    private val _cards = MutableStateFlow(listOf<ExpandableCardModel>())
    val cards: StateFlow<List<ExpandableCardModel>> get() = _cards

    init {
        getFakeData()
    }

    private fun getFakeData() {
        viewModelScope.launch(Dispatchers.Default) {
            val testList = arrayListOf<ExpandableCardModel>()
            repeat(20) { testList += ExpandableCardModel(title = "Card $it") }
            _cards.emit(testList)
        }
    }

    fun onCardArrowClicked(cardIndex: Int) {
        _cards.value = _cards.value.toMutableList().also {
            val card = it[cardIndex]
            val newState =
                if (card.state == CardState.EXPANDED) CardState.COLLAPSED else CardState.EXPANDED
            it[cardIndex] = card.copy(state = newState)
        }
    }

}
