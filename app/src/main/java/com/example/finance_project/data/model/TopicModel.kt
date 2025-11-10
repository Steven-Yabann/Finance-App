package com.example.finance_project.data.model

data class TopicModel (
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val sections: List<TopicSection>,
    val videoUrl: String? = null
)

data class TopicSection (
    val title: String,
    val content: String
)