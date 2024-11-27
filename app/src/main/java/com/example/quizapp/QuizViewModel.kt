package com.example.quizapp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.ui.theme.ThemeSettings
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {

    var isLightTheme = mutableStateOf(ThemeSettings.isLightTheme)
        private set

    private val allQuestions = Questions.questions
    var currentQuestionIndex = mutableStateOf(0)
    var currentScore = mutableStateOf(0)
        private set
    var highScore = mutableStateOf(0)
        private set
    val currentQuestion
        get() = allQuestions[currentQuestionIndex.value]
    val hasMoreQuestions: Boolean
        get() = currentQuestionIndex.value < allQuestions.size - 1

    fun toggleTheme() {
        val newTheme = !isLightTheme.value
        isLightTheme.value = newTheme
        ThemeSettings.isLightTheme = newTheme // Update ThemeSettings
    }

    fun checkAnswer(selectedAnswer: String) {
        if(selectedAnswer == currentQuestion.answer)
            currentScore.value += 1
    }

    fun nextQuestion() {
        if (hasMoreQuestions) {
            currentQuestionIndex.value += 1
        }
    }

    fun finishQuiz() {
        if (currentScore.value > highScore.value) {
            highScore.value = currentScore.value
        }
    }

    fun resetQuiz() {
        currentQuestionIndex.value = 0
        currentScore.value = 0
    }
}
