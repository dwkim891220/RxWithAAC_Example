package kr.co.sugarhill.rxwithviewmodel.view.list

import android.arch.lifecycle.MutableLiveData
import kr.co.sugarhill.rxwithviewmodel.network.FranchiseBrand
import kr.co.sugarhill.rxwithviewmodel.network.NemoApiService
import kr.co.sugarhill.rxwithviewmodel.util.ext.with
import kr.co.sugarhill.rxwithviewmodel.util.livedata.SingleLiveEvent
import kr.co.sugarhill.rxwithviewmodel.util.rx.SchedulerProvider
import kr.co.sugarhill.rxwithviewmodel.util.viewmodel.AbstractViewModel

class ListViewModel(
    private val nemoRepository: NemoApiService,
    private val scheduler: SchedulerProvider
) : AbstractViewModel() {

    val searchResult = SingleLiveEvent<ResultUIModel>()
    val searchKeyword = MutableLiveData<String>()

    fun getFranchiseBrands(keyword : String){
        launch {
            nemoRepository.franchisesKeywords(keyword)
                .with(scheduler)
                .subscribe(
                    { list ->
                        searchResult.postValue(
                            ResultUIModel(list, keyword = keyword)
                        )
                    },
                    { e ->
                        searchResult.postValue(
                            ResultUIModel(error = e)
                        )
                    }
                )
        }
    }

    fun setKeyword(keyword: String){
        searchKeyword.value = keyword
    }
}

data class ResultUIModel(
    val list: List<FranchiseBrand> = emptyList(),
    val error: Throwable? = null,
    val keyword: String = ""
)