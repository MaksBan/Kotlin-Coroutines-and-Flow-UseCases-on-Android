package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.rx

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SequentialNetworkRequestsRxViewModel(
    private val mockApi: RxMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val disposables = CompositeDisposable()

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        mockApi.getRecentAndroidVersions()
            .flatMap { androidVersions ->
                val recentAndroidVersion = androidVersions.last()
                mockApi.getAndroidVersionFeatures(recentAndroidVersion.apiLevel)

            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { uiState.value = UiState.Error("Network call failed!")},
                onSuccess = { versionFeatures ->
                    uiState.value = UiState.Success(versionFeatures)
                }
            )
            .addTo(disposables)
    }
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}