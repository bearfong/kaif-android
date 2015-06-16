package io.kaif.mobile.service;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import android.app.Application;
import android.net.ConnectivityManager;
import dagger.Module;
import dagger.Provides;
import io.kaif.mobile.BuildConfig;
import io.kaif.mobile.R;
import io.kaif.mobile.config.ApiConfiguration;
import io.kaif.mobile.json.ApiResponseDeserializer;
import io.kaif.mobile.json.AutoParcelAdapterFactory;
import io.kaif.mobile.model.oauth.AccessTokenInfo;
import io.kaif.mobile.model.oauth.AccessTokenManager;
import io.kaif.mobile.retrofit.RetrofitRetryStaleProxy;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module
public class ServiceModule {

  private final Application application;

  private static final int CACHE_SIZE = 10 * 1024 * 1024;

  public ServiceModule(Application application) {
    this.application = application;
  }

  @Provides
  @Singleton
  public FeedService provideFeedService(
      @Named("apiRestAdapter") RetrofitRetryStaleProxy restAdapter) {
    return restAdapter.create(FeedService.class);
  }

  @Provides
  @Singleton
  public AccountService provideAccountService(
      @Named("apiRestAdapter") RetrofitRetryStaleProxy restAdapter) {
    return restAdapter.create(AccountService.class);
  }

  @Provides
  @Singleton
  public ZoneService provideZoneService(
      @Named("apiRestAdapter") RetrofitRetryStaleProxy restAdapter) {
    return restAdapter.create(ZoneService.class);
  }

  @Provides
  @Singleton
  public VoteService provideVoteService(
      @Named("apiRestAdapter") RetrofitRetryStaleProxy restAdapter) {
    return restAdapter.create(VoteService.class);
  }

  @Provides
  @Singleton
  public DebateService provideDebateService(
      @Named("apiRestAdapter") RetrofitRetryStaleProxy restAdapter) {
    return restAdapter.create(DebateService.class);
  }

  @Provides
  @Singleton
  public ArticleService provideArticleService(
      @Named("apiRestAdapter") RetrofitRetryStaleProxy restAdapter) {
    return restAdapter.create(ArticleService.class);
  }

  @Provides
  @Singleton
  public OauthService provideOauthService(@Named("oauthRestAdapter") RestAdapter restAdapter) {
    return restAdapter.create(OauthService.class);
  }

  @Provides
  @Singleton
  public RequestInterceptor provideHeaderRequestInterceptor(AccessTokenManager accessTokenManager,
      ConnectivityManager connectivityManager) {
    return request -> {
      final AccessTokenInfo accountTokenInfo = accessTokenManager.findAccount();
      if (accountTokenInfo != null) {
        request.addHeader("Authorization", accountTokenInfo.getAuthorization());
        request.addHeader("Content-Type", "application/json;charset=UTF-8");
      }
    };
  }

  @Provides
  @Singleton
  public ApiConfiguration provideOauthConfiguration() {
    return new ApiConfiguration(application.getString(R.string.endpoint),
        BuildConfig.CLIENT_ID,
        BuildConfig.CLIENT_SECRET,
        application.getString(R.string.redirect_uri));
  }

  @Provides
  @Named("apiRestAdapter")
  @Singleton
  RetrofitRetryStaleProxy provideApiRestAdapter(RequestInterceptor interceptor,
      ApiConfiguration apiConfiguration,
      OkHttpClient okHttpClient) {

    //custom gson for api access
    final Gson autoParcelGson = new GsonBuilder().registerTypeAdapterFactory(new AutoParcelAdapterFactory())
        .create();
    final Gson restApiGson = new GsonBuilder().registerTypeHierarchyAdapter(Object.class,
        new ApiResponseDeserializer(autoParcelGson)).create();

    return new RetrofitRetryStaleProxy(new RestAdapter.Builder().setRequestInterceptor(interceptor)
        .setEndpoint(apiConfiguration.getEndPoint())
        .setClient(new OkClient(okHttpClient))
        .setConverter(new GsonConverter(restApiGson))
        .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
        .build());
  }

  @Provides
  @Named("oauthRestAdapter")
  @Singleton
  RestAdapter provideOauthRestAdapter(ApiConfiguration apiConfiguration) {
    return new RestAdapter.Builder().setEndpoint(apiConfiguration.getEndPoint())
        .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
        .build();
  }

  @Provides
  @Singleton
  OkHttpClient provideOkClient(Cache cache) {
    final OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
    okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
    okHttpClient.setCache(cache);
    return okHttpClient;
  }

  @Provides
  @Singleton
  Cache provideOkHttpCache() {
    return new Cache(application.getExternalCacheDir(), CACHE_SIZE);
  }
}
