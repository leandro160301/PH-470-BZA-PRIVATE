package com.jws.jwsapi.di;

import android.app.Application;
import android.content.Context;
import androidx.room.Room;

import com.jws.jwsapi.core.data.local.PreferencesManagerBase;
import com.jws.jwsapi.core.storage.StorageService;
import com.jws.jwsapi.core.user.UserManager;
import com.jws.jwsapi.AppDatabase;
import com.jws.jwsapi.pallet.PalletApi;
import com.jws.jwsapi.pallet.PalletDao;
import com.jws.jwsapi.pallet.PalletService;
import com.jws.jwsapi.core.label.LabelManager;
import com.jws.jwsapi.shared.PalletRepository;
import com.jws.jwsapi.core.data.local.PreferencesManager;
import com.jws.jwsapi.shared.WeighRepository;
import com.jws.jwsapi.weighing.WeighingApi;
import com.jws.jwsapi.weighing.WeighingDao;
import com.jws.jwsapi.weighing.WeighingService;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    private static final String DATABASE_NAME = "bza-database";
    private static final String BASE_URL = "http://10.41.0.78:8080/";

    @Provides
    @Singleton
    public UserManager provideUserManager(Application application){
        return new UserManager(application);
    }

    @Provides
    @Singleton
    public PreferencesManager providePreferencesManager(Application application) {
        return new PreferencesManager(application);
    }

    @Provides
    @Singleton
    public PreferencesManagerBase providePreferencesManagerBase(Application application) {
        return new PreferencesManagerBase(application);
    }

    @Provides
    @Singleton
    public LabelManager provideLabelManager(PreferencesManager preferencesManager){
        return new LabelManager(preferencesManager);
    }

    @Provides
    @Singleton
    public static StorageService provideStorageService(@ApplicationContext Context context) {
        return new StorageService(context);
    }

    @Provides
    @Singleton
    public PalletRepository providePalletRepository(PalletDao palletDao){
        return new PalletRepository(palletDao);
    }



    @Provides
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    public WeighingApi provideWeighingApi(Retrofit retrofit) {
        return retrofit.create(WeighingApi.class);
    }

    @Provides
    public PalletApi providePalletApi(Retrofit retrofit) {
        return retrofit.create(PalletApi.class);
    }

    @Provides
    @Singleton
    public WeighingDao provideWeighingDao(AppDatabase appDatabase) {
        return appDatabase.weighingDao();
    }

    @Provides
    @Singleton
    public PalletDao providePalletDao(AppDatabase appDatabase) {
        return appDatabase.palletDao();
    }

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    @Provides
    public PalletService providePalletService(PalletApi palletApi, PalletDao palletDao) {
        return new PalletService(palletApi, palletDao);
    }

    @Provides
    public WeighingService provideWeighingService(WeighingApi weighingApi, WeighingDao weighingDao,PalletDao palletDao) {
        return new WeighingService(weighingApi, weighingDao,palletDao);
    }

    @Provides
    @Singleton
    public WeighRepository provideWeighRepository() {
        return new WeighRepository();
    }

}