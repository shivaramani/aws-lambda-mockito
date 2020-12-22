package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.example.LambdaHandlers;
import java.util.Map;
import com.example.Model.Product;
import com.example.Model.ProductHandlerException;

public class InvocationHandlers {
    private LambdaHandlers handlers = DaggerLambdaHandlers.create();

    public String handleSaveRequest(Product request, final Context context) throws ProductHandlerException {
        return handlers.productSaveHandler().handleRequest(request);
    }

    public String handleGetRequest(Product request, final Context context) throws ProductHandlerException{
        return handlers.productGetHandler().handleRequest(request);
    }
}