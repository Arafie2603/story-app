package com.dicoding.picodiploma.loginwithanimation

import com.dicoding.picodiploma.loginwithanimation.data.story.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse() : List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = mutableListOf()
        for (i in 0..25) {
            val storyItem = ListStoryItem(
                "url $i",
                "created $i",
                "name $i",
                "description $i",
                i.toDouble(),
                i.toString(),
                i.toDouble()
            )
            items.add(storyItem)
        }
        return items
    }
}
