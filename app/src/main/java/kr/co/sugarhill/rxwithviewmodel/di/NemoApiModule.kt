package kr.co.sugarhill.rxwithviewmodel.di

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import kr.co.sugarhill.rxwithviewmodel.network.ApiUrl.BASE_URL_API
import kr.co.sugarhill.rxwithviewmodel.network.NemoApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

val nemoApiModule = applicationContext {
    // provided web components
    bean { createOkHttpClient() }

    // Fill property
    bean { createApiService<NemoApiService>(get(), BASE_URL_API) }
}

fun createOkHttpClient(): OkHttpClient {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        @Throws(java.security.cert.CertificateException::class)
        override fun checkClientTrusted(x509Certificates: Array<X509Certificate>, s: String) {

        }

        @Throws(java.security.cert.CertificateException::class)
        override fun checkServerTrusted(x509Certificates: Array<X509Certificate>, s: String) {

        }

        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })

    return OkHttpClient.Builder().apply {
        sslSocketFactory(
            SSLContext.getInstance("SSL") .apply {
                init(null, trustAllCerts, java.security.SecureRandom())
            }.socketFactory)
        hostnameVerifier { _, _ ->  true }
        connectTimeout(60L, TimeUnit.SECONDS)
        readTimeout(60L, TimeUnit.SECONDS)
        addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
    }.build()
}

inline fun <reified T> createApiService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}
