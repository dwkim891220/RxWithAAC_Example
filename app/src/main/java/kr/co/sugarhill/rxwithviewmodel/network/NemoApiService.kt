package kr.co.sugarhill.rxwithviewmodel.network

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by dowoo-kim on 2017. 7. 19..
 */

interface NemoApiService {
    @GET(ApiUrl.FRANCHISES_KEYWORDS)
    fun franchisesKeywords(
            @Query("Keyword") keyword: String
    ): Single<List<FranchiseBrand>>
}