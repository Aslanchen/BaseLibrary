package com.aslan.baselibrary.http;

import com.aslan.baselibrary.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络基类，主要创建Okhttp Client对象
 *
 * @author Aslan
 * @date 2018/4/20
 */
public class CustomHttpClient {
    public static final String TAG_LOG = "OkHttp";

    private volatile static CustomHttpClient mInstance;

    public static CustomHttpClient getInstance() {
        if (null == mInstance) {
            synchronized (CustomHttpClient.class) {
                if (null == mInstance) {
                    mInstance = new CustomHttpClient();
                }
            }
        }
        return mInstance;
    }

    private CustomHttpClient() {

    }

    public <T> T create(@NonNull String url, Class<T> service) {
        HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.d(TAG_LOG, message);
            }
        };
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(logger);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(service);
    }
}
