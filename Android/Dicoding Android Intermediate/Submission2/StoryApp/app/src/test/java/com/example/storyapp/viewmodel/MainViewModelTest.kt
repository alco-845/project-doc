package com.example.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.DataDummy
import com.example.storyapp.MainDispatcherRule
import com.example.storyapp.getOrAwaitValue
import com.example.storyapp.data.AppRepository
import com.example.storyapp.data.remote.response.ListStoryResponse
import com.example.storyapp.ui.main.MainAdapter
import com.example.storyapp.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Mock
    private lateinit var appRepository: AppRepository

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<ListStoryResponse> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<ListStoryResponse>>()
        expectedStory.value = data
        Mockito.`when`(appRepository.getListStory()).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(appRepository)
        val actualStory: PagingData<ListStoryResponse> = mainViewModel.getListStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MainAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryResponse> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryResponse>>()
        expectedStory.value = data
        Mockito.`when`(appRepository.getListStory()).thenReturn(expectedStory)
        val mainViewModel = MainViewModel(appRepository)
        val actualStory: PagingData<ListStoryResponse> = mainViewModel.getListStory().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = MainAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
        Assert.assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryResponse>>>() {
    companion object {
        fun snapshot(items: List<ListStoryResponse>): PagingData<ListStoryResponse> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryResponse>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryResponse>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
