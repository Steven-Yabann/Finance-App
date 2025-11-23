package com.example.finance_project.data.model

import com.google.gson.annotations.SerializedName

data class CryptoNewsResponse(
    @SerializedName("Data")
    val data: List<CryptoNewsArticle>,
    @SerializedName("HasWarning")
    val hasWarning: Boolean
)

data class CryptoNewsArticle(
    @SerializedName("id")
    val id: String,
    @SerializedName("guid")
    val guid: String,
    @SerializedName("published_on")
    val publishedOn: Long,
    @SerializedName("imageurl")
    val imageUrl: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("tags")
    val tags: String,
    @SerializedName("categories")
    val categories: String,
    @SerializedName("source_info")
    val sourceInfo: SourceInfo,
    @SerializedName("lang")
    val language: String
)

data class SourceInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("img")
    val imageUrl: String?,
    @SerializedName("lang")
    val language: String
)