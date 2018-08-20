package kr.co.sugarhill.rxwithviewmodel.network

import android.content.Context
import android.util.Log
import okhttp3.ResponseBody

class CustomCallback<T>(private val mContext: Context?) : retrofit2.Callback<T> {
    companion object {
        private const val SUCCESS = 200
        private const val INVALID_PARAMETER = 400
        private const val NEED_LOGIN = 401
        private const val UNAUTHORIZED = 403
        private const val NOT_FOUND = 404
        private const val INTERNAL_SERVER_ERROR = 500
    }

    lateinit var successResponse: (response: T?) -> Unit
    lateinit var errorResponse: (response: ResponseBody?) -> Unit

    override fun onResponse(call: retrofit2.Call<T>, response: retrofit2.Response<T>) {
        val code = response.code()
        val body = response.body()
        val errorBody = response.errorBody()

        when (code) {
            SUCCESS ->{
                if(::successResponse.isInitialized) successResponse(body)
            }
            INVALID_PARAMETER,
            INTERNAL_SERVER_ERROR,
            NOT_FOUND -> {
                if(::errorResponse.isInitialized){
                    errorResponse(errorBody)
                }else{
                    NemoClient.showErrorMessage(
                        mContext,
                        errorBody
                    )
                }
            }
            UNAUTHORIZED,
            NEED_LOGIN -> { }
            else -> Log.d("CustomCallBack", "error code = $code")
        }
    }

    override fun onFailure(call: retrofit2.Call<T>, t: Throwable) {
        t.printStackTrace()
    }
}