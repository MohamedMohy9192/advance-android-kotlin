package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StatisticsViewModelTest {

    // This will swap the background executor used by Architecture Components (which ViewModels are part of)
    // for an executor which will execute each task synchronously. This ensures your tests are deterministic.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    // Subject under test
    private lateinit var statisticsViewModel: StatisticsViewModel

    // Use a fake repository to be injected into the view model.
    private lateinit var tasksRepository: FakeTestRepository

    @Before
    fun setupStatisticsViewModel() {
        // Initialise the repository with no tasks.
        tasksRepository = FakeTestRepository()

        statisticsViewModel = StatisticsViewModel(tasksRepository)
    }

    @Test
    fun loadTasks_loading() = runTest {
        // Creates an instance of a TestDispatcher whose tasks are run inside calls to the scheduler.
        // This TestDispatcher instance does not itself execute any of the tasks.
        // Instead, it always sends them to its scheduler,
        // which can then be accessed via TestCoroutineScheduler.runCurrent,
        // TestCoroutineScheduler.advanceUntilIdle, or TestCoroutineScheduler.advanceTimeBy,
        // which will then execute these tasks in a blocking manner.
        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        // Load the task in the view model.
        statisticsViewModel.refresh()

        // Then assert that the progress indicator is shown.
        assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(true))

        // Execute pending coroutines actions
        advanceUntilIdle()

        // Then assert that the progress indicator is hidden.
        assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(false))
    }
}