package com.example;

import javax.inject.Singleton;

import com.example.Handlers.ProductSaveHandler;
import com.example.Handlers.ProductGetHandler;
import com.example.modules.UtilityModule;

import dagger.Component;

@Singleton
@Component(modules={
    UtilityModule.class
})

public interface LambdaHandlers {
    ProductSaveHandler productSaveHandler();
    ProductGetHandler productGetHandler();
}