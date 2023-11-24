package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch

class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading


        viewModelScope.launch {
            try {
                val mostRecentVersion = mockApi.getRecentAndroidVersions().last()
                val versionFeatures = mockApi.getAndroidVersionFeatures(mostRecentVersion.apiLevel)
                uiState.value = UiState.Success(versionFeatures)
            }catch (e: Exception){
                uiState.value = UiState.Error(e.message ?: "Network request failed")
            }

        }
        // TODO: Exercise 1
        // switch to branch "coroutine_course_full" to see solution
    }
}