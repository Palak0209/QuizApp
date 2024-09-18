package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.ui.theme.QuizAppTheme
val quiz = Quiz()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                // Start with the topic selection screen
                TopicSelectionScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview(modifier: Modifier = Modifier) {
    QuizAppTheme {
        TopicSelectionScreen()
    }
}

@Composable
fun TopicSelectionScreen(){

    var selectedQuiz by remember { mutableStateOf<String?>(null) }

    if(selectedQuiz == null){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
            .background(Color(0xFFFFDFE0)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(text = "Select a Quiz Topic: ",
                fontSize = 24.sp)

            // Button for General Knowledge Quiz
            Button(
                onClick = { selectedQuiz = "general" },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("General Knowledge")
            }

            // Button for Science Quiz
            Button(
                onClick = { selectedQuiz = "science" },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Science")
            }
        }
    }
    else{
        // Start the appropriate quiz based on the selection
        if (selectedQuiz == "general") {
            QuizScreen(
                questions = quiz.GetGKQuestions(),
                options = quiz.GetGKOptions(),
                correctAnswers = quiz.GetGKAnswers()
            )
        } else {
            QuizScreen(
                questions = quiz.GetScienceQuestions(),
                options = quiz.GetScienceOptions(),
                correctAnswers = quiz.GetScienceAnswers()
            )
        }
    }
}

@Composable
fun QuizScreen(modifier: Modifier = Modifier,
               questions: List<String>,
               options: List<List<String>>,
               correctAnswers: List<String>) {

    var score by remember { mutableStateOf(0) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedRadioOption by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        if (currentQuestionIndex < questions.size) {
            Text(text = "Question ${currentQuestionIndex + 1}",
                fontSize = 24.sp)

            Text(text = questions[currentQuestionIndex],
                fontSize = 24.sp)

            // Display the question and radio buttons
            RadioButtonGroup(
                modifier = modifier,
                options = options[currentQuestionIndex],
                selectedOption = selectedRadioOption,
                onOptionSelected = { selectedRadioOption = it }
            )

            // Submit button
            Button(
                onClick = {
                    if (checkAnswer(selectedRadioOption, correctAnswers, currentQuestionIndex)) {
                        score++
                    }
                    currentQuestionIndex++ // Move to next question
                },
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text("Submit",
                    fontSize = 24.sp)
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                // Show the score once all questions are done
                ShowScore(score, questions.size)

                //Return to home screen button
                Button(
                    onClick = {
                        if (checkAnswer(selectedRadioOption, correctAnswers, currentQuestionIndex)) {
                            score++
                        }
                        currentQuestionIndex++ // Move to next question
                    },
                    modifier = Modifier.padding(top = 20.dp)
                ){
                    Text(text = "Return to Home",
                        fontSize = 24.sp)
                }
            }
        }
    }
}

@Composable
fun RadioButtonGroup(
    modifier: Modifier,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier.padding(16.dp)) {
        options.forEach { option ->
            Row(modifier.padding(vertical = 8.dp)) {
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

private fun checkAnswer(selectedOption: String, correctAnswers: List<String>, currentQuestionIndex: Int): Boolean {
    return selectedOption == correctAnswers[currentQuestionIndex]
}

@Composable
private fun ShowScore(score: Int, numberOfQuestions: Int) {

    var homeReturnClicked by remember { mutableStateOf(false) }

    if(homeReturnClicked){
        TopicSelectionScreen()
    }
    else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Quiz finished!",
                fontSize = 24.sp
            )
            Text(
                text = "Your final score: $score / $numberOfQuestions",
                fontSize = 24.sp
            )

            //Return to home screen button
            Button(
                onClick = { homeReturnClicked = true },
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(
                    text = "Return to Home",
                    fontSize = 24.sp
                )
            }
        }
    }
}
