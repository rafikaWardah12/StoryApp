package com.example.storyapp

import com.example.storyapp.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem (
                "photoUrl $i",
                "createdAt + $i",
                "name $i",
                "description $i",
                12.0,
                i.toString(),
                11.0,
            )
            items.add(story)
        }
        return items
    }
}