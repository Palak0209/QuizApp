package com.example.quizapp

class Questions () {
    companion object {
        val questions: Array<Question> = arrayOf(
            Question("What is the capital of France?", arrayOf("Paris", "Berlin", "Rome", "Madrid"),0),
            Question( "Which planet is closest to the sun?",arrayOf("Venus", "Mercury", "Earth", "Mars"),1),
            Question("What is the largest ocean on Earth?", arrayOf("Atlantic", "Indian", "Arctic", "Pacific"), 3),
            Question("Who wrote the play 'Romeo and Juliet'?", arrayOf("William Shakespeare", "Charles Dickens", "Jane Austen", "Mark Twain"), 0),
            Question("Which element has the chemical symbol 'O'?", arrayOf("Oxygen", "Gold", "Hydrogen", "Carbon"), 0),
            Question("What is the smallest prime number?", arrayOf("1", "2", "3", "5"), 1),
            Question("Which country is known as the Land of the Rising Sun?", arrayOf("China", "Japan", "Thailand", "South Korea"), 1),
            Question("What is the freezing point of water in degrees Celsius?", arrayOf("0", "32", "100", "273"), 0)
        )
            get() = field
    }
}