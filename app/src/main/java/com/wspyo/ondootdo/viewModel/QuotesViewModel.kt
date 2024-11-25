package com.wspyo.ondootdo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random


class QuotesViewModel : ViewModel() {
    private val _randomQuote = MutableLiveData<String>()
    val randomQuote: LiveData<String> get() = _randomQuote

    private val quotes = listOf(
        "내일은 오늘보다 더 나은 날이 될 것이다.",
        "성공은 최선을 다하고, 실패는 경험에서 배운 것이다.",
        "힘든 순간이 지나고 나면, 그 다음엔 밝은 날이 올 것이다.",
        "가장 어두운 순간이 가장 밝은 미래를 위한 전환점이다.",
        "성공은 실패를 거듭하면서도 포기하지 않는 사람에게 주어진다.",
        "어려운 길이 끝나면 더 멋진 풍경이 기다린다.",
        "넌 할 수 있다! 끝까지 시도해라.",
        "끝까지 가면, 그 길은 반드시 보람 있을 것이다.",
        "고난은 지나가고, 그 후엔 더 강해진 자신이 있을 것이다.",
        "실패를 두려워하지 말고, 다시 일어나는 것을 두려워하라."
    )

    fun loadRandomQuote() {
        _randomQuote.value = quotes[Random.nextInt(quotes.size)]
    }
}
