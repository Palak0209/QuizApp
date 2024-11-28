package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.ui.theme.Quiz_app_composeTheme
import com.example.quizapp.ui.theme.ThemeSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStoreManager = SettingsDataStore(applicationContext)

        setContent {
            // Pass DataStoreManager to the ViewModel
            val viewModel: QuizViewModel = QuizViewModel(
                dataStoreManager
            )

            Quiz_app_composeTheme {
                Router(viewModel)
            }
        }
    }
}

val fontSize = 24.sp
val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }
@Composable
fun Router(viewModel : QuizViewModel) {
    val navController = rememberNavController()
    //val viewModel : QuizViewModel = viewModel()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = "MainScreenRoute") {
            composable("MainScreenRoute") {
                MainScreen({viewModel.resetQuiz()},
                    viewModel.highScore.value)
            }
            composable("ScoreScreenRoute") {
                ScoreScreen(viewModel.currentScore.value, viewModel.highScore.value)
            }
            composable("QuizScreenRoute") {
                QuizScreen(
                    {
                        viewModel.finishQuiz()
                        navController.navigate("ScoreScreenRoute")
                    },
                    viewModel.currentQuestionIndex.value,
                    viewModel.currentQuestion,
                    { viewModel.hasMoreQuestions },
                    { selectedAnswer -> viewModel.checkAnswer(selectedAnswer) },
                    { viewModel.nextQuestion() }
                )
            }
            composable("SettingsScreenRoute") {
                SettingsScreen( {viewModel.changeTheme()},
                    viewModel.isLightTheme
                )
            }
        }
    }
}

@Composable
fun MainScreen(resetQuiz: () -> Unit, highScore: Int) {
    val navController = LocalNavController.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Highest Score: ${highScore} /8", fontSize = fontSize)
            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { resetQuiz()
                navController.navigate("QuizScreenRoute") }, modifier = Modifier.padding(8.dp)) {
                Text("Start", fontSize = fontSize)
            }
            Button(onClick = { navController.navigate("ScoreScreenRoute") }, modifier = Modifier.padding(8.dp)) {
                Text("Score", fontSize = fontSize)
            }
            Button(onClick = { navController.navigate("SettingsScreenRoute") }, modifier = Modifier.padding(8.dp)) {
                Text("Settings", fontSize = fontSize)
            }
        }
    }
}

@Composable
fun ScoreScreen(quizScores: Int, highScore: Int) {
    val navController = LocalNavController.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(vertical = 32.dp)
        ) {
            Text(text = "Quiz Scores")
            Row {
                Text(
                    text = "Your Score: $quizScores",
                    fontSize = fontSize,
                    lineHeight = fontSize * 1.2
                )
            }
            // Display high score if needed
            Row {
                Text(

                    text = "Highest Score: ${highScore} / 8",
                    fontSize = fontSize,
                    lineHeight = fontSize * 1.2
                )
            }
            Button(
                onClick = { navController.navigate("MainScreenRoute") }
            ) {
                Text(
                    text = "Back",
                    modifier = Modifier.padding(0.dp),
                    fontSize = fontSize
                )
            }
        }
    }
}

@Composable
fun SettingsScreen(changeTheme: () -> Unit, lightTheme: MutableState<Boolean>) {
    val navController = LocalNavController.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(26.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Settings",
                    fontSize = fontSize,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { changeTheme() }) {
                    Text(if (lightTheme.value) "Switch to Dark Theme" else "Switch to Light Theme")
                }
                Button(
                    onClick = {navController.popBackStack()}
                ) {
                    Text(
                        text = "Back",
                        modifier = Modifier.padding(0.dp),
                        fontSize = fontSize
                    )
                }
            }
        }
    }
}

@Composable
fun QuizScreen(
    onQuizCompleted: () -> Unit,
    questionId: Int,
    currentQuestion: Question,
    hasMoreQuestions: () -> Boolean,
    checkAnswer: (String) -> Unit,
    nextQuestion: () -> Unit
) {
    val navController = LocalNavController.current
    var selectedRadioOption by remember { mutableStateOf("") }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            if (hasMoreQuestions()) {
                Text(
                    text = "Question ${questionId + 1}",
                    fontSize = 24.sp
                )

                Text(
                    text = currentQuestion.text,
                    fontSize = 24.sp
                )

                // Display the question and radio buttons
                RadioButtonGroup(
                    options = currentQuestion.answers,
                    selectedOption = selectedRadioOption,
                    onOptionSelected = { selectedRadioOption = it }
                )

                // Submit button
                Button(
                    onClick = {
                        checkAnswer(selectedRadioOption)
                        nextQuestion()
                    },
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text(
                        "Next",
                        fontSize = fontSize
                    )
                }
            } else {
                SubmitButton(onQuizCompleted)
            }
            Button(
                onClick = { navController.popBackStack() }
            ) {
                Text(
                    text = "Back",
                    modifier = Modifier.padding(0.dp),
                    fontSize = fontSize
                )

            }
        }

    }
}

@Composable
fun RadioButtonGroup(
    options: Array<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        options.forEach { option ->
            Row(Modifier.padding(vertical = 2.dp)) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = { onOptionSelected(option) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(option)
            }
        }
    }
}

@Composable
fun SubmitButton(onQuizCompleted: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { onQuizCompleted() }
        ) {
            Text(
                text = "Submit",
                modifier = Modifier.padding(10.dp),
                fontSize = fontSize
            )
        }
    }
}
