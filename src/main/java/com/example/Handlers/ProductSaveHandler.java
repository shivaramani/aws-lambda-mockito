package com.example.Handlers;

import javax.inject.Inject;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Model.Product;
import com.example.Model.ProductHandlerException;
import com.example.Utils.S3Manager;
import com.example.Utils.DynamoManager;
import java.io.IOException;

public class ProductSaveHandler{
  Gson gson;
  S3Manager s3Manager;
  DynamoManager dynamoManager;
  static Logger logger = LoggerFactory.getLogger(ProductSaveHandler.class);

  @Inject
  public ProductSaveHandler(Gson gson, S3Manager s3Manager, DynamoManager dynamoManager){

    this.gson = gson;
    this.s3Manager = s3Manager;
    this.dynamoManager = dynamoManager;
  }

  public String handleRequest(Product request) throws ProductHandlerException {
      logger.info("Received request in the SAVE Service");
      String requestJson = gson.toJson(request, Product.class);
      String output = String.format("Success - SAVE Received Input - %s", requestJson);

      String region = System.getenv("REGION");
      String bucketName = System.getenv("S3_BUCKET");
      String dynamoTable = System.getenv("DATABASE_TABLE");
      
      if(request != null && request.getProductId() != null &&
                            request.getProductName() != null &&
                            request.getProductVersion() != null)
      {
        String productId = request.getProductId();
        String productName = request.getProductName();
        String productVersion = request.getProductVersion();

        try {
          Boolean s3UploadStatus = s3Manager.upload(region, bucketName, request);
          if(s3UploadStatus){
            dynamoManager.save(region, dynamoTable, productId, productName, productVersion);
          }
          else{
            throw new ProductHandlerException("Error saving products");
          }
        }
        catch (IOException e) {
          throw new ProductHandlerException("Error saving products");
        } 
        catch (Exception ex) {
          throw new ProductHandlerException("Error saving products");
        }
       
      }
      else{
        throw new ProductHandlerException("Invalid product request.");
      }

      return  output;
  }
}