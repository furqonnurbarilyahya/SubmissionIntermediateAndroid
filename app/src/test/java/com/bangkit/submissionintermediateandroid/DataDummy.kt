package com.bangkit.submissionintermediateandroid

import com.bangkit.submissionintermediateandroid.data.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val storyItem = ListStoryItem(
                i.toString(),
                "photoUrl + $i",
                "createdAt $i",
                "name $i",
                "created $i",
                null,
                null,
            )
            items.add(storyItem)
        }
        return items
    }
}