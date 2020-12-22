package com.example.modules;

import javax.inject.Singleton;

import com.example.Utils.DynamoManager;
import com.example.Utils.S3Manager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import dagger.Module;
import dagger.Provides;

import java.util.logging.Logger;

@Module
public class UtilityModule {
    @Provides
    @Singleton
    public Gson providesGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    // Having static methods, Otherwise we can DI these also!
    /*
    @Provides
    @Singleton
    public S3Manager providesS3Manager() {
        return new S3Manager();
    }

    @Provides
    @Singleton
    public DynamoManager providesDynamoManager() {
        return new DynamoManager();
    }
    */

}
