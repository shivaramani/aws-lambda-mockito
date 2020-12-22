package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;

import com.google.gson.Gson;

import org.junit.Before;
import org.mockito.*;

import com.example.Handlers.ProductSaveHandler;
import com.example.Handlers.ProductGetHandler;
import com.example.Utils.S3Manager;
import com.example.Utils.DynamoManager;
import com.example.Model.Product;
import com.example.Model.ProductHandlerException;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.any;
import java.io.IOException;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({S3Manager.class, DynamoManager.class})
public class ProductSaveHandlerTest {

  @Mock
  S3Manager s3Manager;

  @Mock
  DynamoManager dynamoManager;
  
  private Gson gson;

  private Product inputProduct;
  private String TEST_OUTPUT = "Success - SAVE Received Input - {\"productId\":\"1\",\"productName\":\"iphone\",\"productVersion\":\"10R\"}";
  private ProductSaveHandler saveHandler;

  @Before
  public void setup() throws Exception{
    gson = new Gson();

    inputProduct = new Product();
    inputProduct.setProductId("1");
    inputProduct.setProductName("iphone");
    inputProduct.setProductVersion("10R");

    MockitoAnnotations.initMocks(this);
    PowerMockito.mockStatic(DynamoManager.class);
    PowerMockito.mockStatic(S3Manager.class);
    PowerMockito.when(S3Manager.upload(any(), any(), any())).thenReturn(true);
    PowerMockito.when(DynamoManager.save(any(), any(), any(), any(), any())).thenReturn(true);

    saveHandler = new ProductSaveHandler(gson);

  }

  @Test
  public void saveS3_validRequest_Success() throws IOException {

    String serviceOutput = saveHandler.handleRequest(inputProduct);

    verify(s3Manager, times(1)).upload(any(), any(), any());
    verify(dynamoManager, times(1)).save(any(), any(), any(), any(), any());
    assertEquals(TEST_OUTPUT, serviceOutput);
  }

  @Test(expected = ProductHandlerException.class)
  public void noProduct_invalidRequest_RaiseException(){
    Product product = new Product();
    saveHandler.handleRequest(product);
  }

  @Test
  public void noProduct_nullRequest_RaiseException() throws ProductHandlerException{
    try{
      saveHandler.handleRequest(null);
    }
    catch(Exception ex){
      assertThat(ex, instanceOf(ProductHandlerException.class));
    }
  }
}