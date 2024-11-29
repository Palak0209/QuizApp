package com.example.quizapp
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.ui.theme.ThemeSettings
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class QuizViewModel(private val settingsDataStore: SettingsDataStore) : ViewModel() {

    var isLightTheme = mutableStateOf(ThemeSettings.isLightTheme)
        private set
    var isTimerEnabled = mutableStateOf(false) // Tracks whether the timer is enabled
        private set

    val timerDuration = 10
    var remainingTime = mutableStateOf(timerDuration)
    private var timerJob: Job? = null // Coroutine job for the timer

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

    init {
        viewModelScope.launch {
            settingsDataStore.isLightTheme.collectLatest { theme ->
                isLightTheme.value = theme
                ThemeSettings.isLightTheme = isLightTheme.value
            }
        }
        viewModelScope.launch {
            settingsDataStore.highScore.collectLatest { score ->
                highScore.value = score
            }
        }
        viewModelScope.launch {
            settingsDataStore.isTimerEnabled.collectLatest { enabled ->
                isTimerEnabled.value = enabled
            }
        }
    }

    fun changeTheme() {
        val newTheme = !isLightTheme.value
        isLightTheme.value = newTheme
        ThemeSettings.isLightTheme = newTheme
        viewModelScope.launch {
            settingsDataStore.saveLightTheme(newTheme)
        }// Update ThemeSettings
    }

    fun checkAnswer(selectedAnswer: String) {
        if(selectedAnswer == currentQuestion.answer)
            currentScore.value += 1
    }

    fun toggleTimer() {
        val newState = !isTimerEnabled.value
        isTimerEnabled.value = newState
        viewModelScope.launch {
            settingsDataStore.saveTimerState(newState)
        }
    }

    fun startQuizTimer(onTimeUp: () -> Unit) {
        if (!isTimerEnabled.value) return
        timerJob?.cancel() // Cancel any existing timer
        timerJob = viewModelScope.launch {
            for (time in timerDuration downTo 0) {
                remainingTime.value = time
                delay(1000L)
            }

            Log.d("24601", "text")
            onTimeUp() // Trigger the navigation when time is up
        }
    }

    fun cancelQuizTimer() {
        timerJob?.cancel()
        remainingTime.value = timerDuration
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
        viewModelScope.launch {
            settingsDataStore.saveHighScore(highScore.value)
        }
    }

    fun resetQuiz() {
        currentQuestionIndex.value = 0
        currentScore.value = 0
    }
}
