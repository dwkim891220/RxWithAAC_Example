package kr.co.sugarhill.rxwithviewmodel.di

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import kr.co.sugarhill.rxwithviewmodel.network.ApiUrl.BASE_URL_API
import kr.co.sugarhill.rxwithviewmodel.network.NemoApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val nemoApiModule = applicationContext {
    // provided web components
    bean { createOkHttpClient() }

    // Fill property
    bean { createApiService<NemoApiService>(get(), BASE_URL_API) }
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor).build()
}

inline fun <reified T> createApiService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}
