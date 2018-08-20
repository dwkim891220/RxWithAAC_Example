package kr.co.sugarhill.rxwithviewmodel.network

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.gson.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import kr.co.sugarhill.rxwithviewmodel.BuildConfig


/**
 * Created by dowoo-kim on 2018. 1. 10..
 */
object NemoClient {
    var API_CLIENT: NemoApiService? = null

    fun buildClients(context: Context){
        val sizeOfCache = (10 * 1024 * 1024).toLong() // 10 MiB
        val cache = Cache(context.cacheDir, sizeOfCache)

        API_CLIENT =
                getBuilder(ApiUrl.BASE_URL_API, cache)
                    .build()
                    .create(NemoApiService::class.java)
    }

    private fun getBuilder(url: String, cache: Cache) =
            Retrofit.Builder()
                    .baseUrl(url)
                    .client(getOkHttpClient(cache))
                    .addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder().create()
                        )
                    )

    private fun getOkHttpClient(cache: Cache): OkHttpClient {
        try {
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
                cache(cache)
                readTimeout(30, TimeUnit.SECONDS)
                connectTimeout(30, TimeUnit.SECONDS)
                addInterceptor{ chain ->
                    chain.proceed(
                        chain.request().newBuilder().build()
                    )
                }

                if(BuildConfig.DEBUG) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.HEADERS
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                }
            }.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun parseErrorMessage(response: ResponseBody?): String{
        var msg = ""

        try {
            JsonParser().parse(response?.string())?.run {
                when (this) {
                    is JsonObject -> {
                        this.run {
                            this.keySet()?.forEach { key ->
                                if(this.has(key)){
                                    this.getAsJsonArray(key).forEach { arrayElement ->
                                        if(msg.isNotEmpty()) msg += "\n"
                                        msg += arrayElement.asString
                                    }
                                }
                            }
                        }
                    }
                    is JsonArray -> {
                        this.forEach { arrayElement ->
                            if(msg.isNotEmpty()) msg += "\n"
                            msg += arrayElement.asString
                        }
                    }
                    else -> {}
                }
            }
        }catch (e: Exception){
            e.message?.run {
                msg = this
            }
        }

        return msg
    }

    fun showErrorMessage(pContext: Context?, responseBody: ResponseBody?){
        pContext?.let { context ->
            NemoClient.parseErrorMessage(responseBody).let { errString ->
                if (errString.isNotEmpty()) {
                    (context as? Activity)?.run {
                        if(!this.isFinishing){
                            Toast.makeText(this, errString, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}