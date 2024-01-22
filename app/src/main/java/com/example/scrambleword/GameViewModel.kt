package com.example.scrambleword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.scrambleword.data.allWords
import com.example.scrambleword.data.maxWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    private var usedwords: MutableSet<String> = mutableSetOf()
    private lateinit var currentWord: String

    init {
        resetGame()
    }

    fun resetGame() {
        usedwords.clear()
        _uiState.value = GameUiState(currentScrambledWord = randomPickAndShuffle())
    }

    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    fun skipWord() {
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val updatedScore = _uiState.value.score.plus(20)
            updateGameState(updatedScore)
        } else {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateUserGuess("")
    }

    private fun updateGameState(updatedScore: Int) {
        if (usedwords.size == maxWords) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    isGameOver = true,
                    score = updatedScore
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isGameOver = false,
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    currentScrambledWord = randomPickAndShuffle(),
                    currentWordCount = currentState.currentWordCount.inc()
                )
            }
        }

    }

    private fun randomPickAndShuffle(): String {
        currentWord = allWords.random()
        return if (usedwords.contains(currentWord)) {
            randomPickAndShuffle()
        } else {
            usedwords.add(currentWord)
            shuffleCurrentWord(currentWord)
        }
    }


    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        tempWord.shuffle()

        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)

    }
}