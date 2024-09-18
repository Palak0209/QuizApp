package com.example.quizapp

class Quiz {

    //Quiz 1: General Knowledge Quiz
    private val generalKnowledgeQuestions: List<String> = listOf(
        "What is the hardest natural substance on Earth?",
        "Which scientist proposed the theory of relativity?",
        "What is the largest desert in the world?",
        "Which metal is liquid at room temperature?",
        "Which country has the largest population in the world?",
        "Who painted the 'Mona Lisa'?",
        "Which gas is most abundant in Earth's atmosphere?",
        "What is the longest bone in the human body?",
        "Which ancient civilization built the Pyramids?"
    )


    private val generalKnowledgeOptions: List<List<String>> = listOf(
        listOf("Diamond", "Quartz", "Granite", "Gold"),
        listOf("Isaac Newton", "Albert Einstein", "Galileo Galilei", "Marie Curie"),
        listOf("Sahara", "Gobi", "Arabian", "Antarctic"),
        listOf("Mercury", "Gold", "Aluminum", "Lead"),
        listOf("India", "United States", "China", "Brazil"),
        listOf("Vincent van Gogh", "Leonardo da Vinci", "Pablo Picasso", "Claude Monet"),
        listOf("Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"),
        listOf("Tibia", "Femur", "Spine", "Skull"),
        listOf("Mayans", "Romans", "Egyptians", "Greeks")
    )

    private val generalKnowledgeCorrectAnswers: List<String> = listOf(
        "Diamond",
        "Albert Einstein",
        "Antarctic",
        "Mercury",
        "China",
        "Leonardo da Vinci",
        "Nitrogen",
        "Femur",
        "Egyptians"
    )

    // Quiz 2: Science Quiz
    private val scienceQuestions: List<String> = listOf(
        "What is the hardest natural substance on Earth?",
        "Which gas is most abundant in Earth's atmosphere?",
        "Which of the following enzymes is not present in the human stomach?",
        "Name the gland which is present above our kidneys?"
    )

    private val scienceOptions:List<List<String>> = listOf(
        listOf("Diamond", "Quartz", "Granite", "Gold"),
        listOf("Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"),
        listOf("Pepsin", "Hydrochloric Acid", "Mucus", "Trypsin"),
        listOf("Adrenal", "Pituitary", "Gonads", "Salivary")
    )

    private val scienceCorrectAnswers: List<String> = listOf("Diamond", "Nitrogen", "Trypsin", "Adrenal")

    fun GetGKQuestions(): List<String>{
        return generalKnowledgeQuestions
    }

    fun GetGKOptions(): List<List<String>>{
        return generalKnowledgeOptions
    }

    fun GetGKAnswers(): List<String>{
        return generalKnowledgeCorrectAnswers
    }

    fun GetScienceQuestions(): List<String>{
        return scienceQuestions
    }

    fun GetScienceOptions(): List<List<String>>{
        return scienceOptions
    }

    fun GetScienceAnswers(): List<String>{
        return scienceCorrectAnswers
    }
}