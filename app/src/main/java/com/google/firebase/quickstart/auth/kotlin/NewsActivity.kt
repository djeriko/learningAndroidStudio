package com.google.firebase.quickstart.auth.kotlin

import android.support.v7.app.AppCompatActivity
import com.google.firebase.quickstart.auth.R
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    companion object{
        const val NEWS_TEXT = "news_text" //Статическая переменная для использования в качестве ключа
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news) //Указание на файл разметки
        showNews()
    }

    fun showNews() {
        val news = intent.getStringExtra(NEWS_TEXT)
        textViewLabel.text = news
    }
}
