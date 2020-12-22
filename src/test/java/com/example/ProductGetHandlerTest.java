package com.example;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import com.example.Handlers.ProductGetHandler;
import com.example.Model.Product;
import com.example.Model.ProductHandlerException;
import com.example.Utils.DynamoManager;
import com.example.Utils.S3Manager;
import com.google.gson.Gson;

@RunWith(PowerMockRunner.class)
@PrepareForTest({S3Manager.class, DynamoManager.class})
public class ProductGetHandlerTest {

  @Mock
  S3Manager s3Manager;

  @Mock
  DynamoManager dynamoManager;

  Gson gson;

  private Product inputProduct;
  private String TEST_OUTPUT = "{\"productId\":\"1\",\"productName\":\"iphone\",\"productVersion\":\"10R\"}";

  ProductGetHandler getHandler;

  @Before
  public void setup() throws Exception{
    gson = new Gson();

    inputProduct = new Product();
    inputProduct.setProductId("1");


    Product product = new Product();
    product.setProductId("1");
    product.setProductName("iphone");
    product.setProductVersion("10R");

    MockitoAnnotations.initMocks(this);
    PowerMockito.mockStatic(DynamoManager.class);
    PowerMockito.mockStatic(S3Manager.class);
    PowerMockito.when(S3Manager.ListObjects(any(), any(), any())).thenReturn(product);
    PowerMockito.when(DynamoManager.get(any(), any(), any())).thenReturn(product);
    getHandler = new ProductGetHandler(gson);
  }

  @Test
  public void saveS3_validRequest_Success() throws IOException {

    String serviceOutput = getHandler.handleRequest(inputProduct);

    verify(s3Manager, times(1)).ListObjects(any(), any(), any());
    verify(dynamoManager, times(1)).get(any(), any(), any());
    assertEquals(TEST_OUTPUT, serviceOutput);
  }

  @Test(expected = ProductHandlerException.class)
  public void noProduct_invalidRequest_RaiseException(){
    Product product = new Product();
    getHandler.handleRequest(product);
  }

  @Test
  public void noProduct_nullRequest_RaiseException() throws ProductHandlerException{
    try{
      getHandler.handleRequest(null);
    }
    catch(Exception ex){
      assertThat(ex, instanceOf(ProductHandlerException.class));
    }
  }
}