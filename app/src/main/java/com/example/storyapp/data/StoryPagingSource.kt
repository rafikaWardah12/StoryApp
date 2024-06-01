package com.example.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.response.ListStoryItem
import com.example.storyapp.response.StoryResponse
import com.example.storyapp.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService): PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
       return try {
           val position = params.key ?: INITIAL_PAGE_INDEX
           val responseData = apiService.getStories(position, params.loadSize)  //Ambil dari All Stories

           LoadResult.Page(
               data = responseData.listStory,
               prevKey = if (position == INITIAL_PAGE_INDEX) null else position -1,
               nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
           )
       } catch (exception: Exception) {
           return LoadResult.Error(exception)
       }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}