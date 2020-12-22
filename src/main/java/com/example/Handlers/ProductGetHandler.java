package com.example.Handlers;

import javax.inject.Inject;
import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import com.example.Model.Product;
import com.example.Model.ProductHandlerException;

import com.example.Utils.S3Manager;
import com.example.Utils.DynamoManager;
import java.io.IOException;

public class ProductGetHandler{
  Gson gson;
  S3Manager s3Manager;
  DynamoManager dynamoManager;

  static Logger logger = LoggerFactory.getLogger(ProductGetHandler.class);

  @Inject
  public ProductGetHandler(Gson gson, S3Manager s3Manager, DynamoManager dynamoManager){
      this.gson = gson;
      this.s3Manager = s3Manager;
      this.dynamoManager = dynamoManager;
  }

  public String handleRequest(Product request)  throws ProductHandlerException{
      logger.info("Received request in the GET Service");
      String requestJson = gson.toJson(request, Product.class);
      String output = "";

      String region = System.getenv("REGION");
      String bucketName = System.getenv("S3_BUCKET");
      String dynamoTable = System.getenv("DATABASE_TABLE");
      
      if(request != null && request.getProductId() != null)
      {
        String productId = request.getProductId();
        try {

          Product product = s3Manager.ListObjects(region, bucketName, productId);
          logger.info("S3 fetch done");
          if(product != null && product.getProductId() != null){
              logger.info("getting ddb");
              //product = DynamoManager.get(region, dynamoTable, productId);
              product = getTableData(region, dynamoTable, productId);
              logger.info("ddb fetch done");
              output = gson.toJson(product);
              logger.info("ddb json - " + output);
          }
          else{
            logger.info("S3 ERROR");
            throw new ProductHandlerException("Error getting products");
          }
        }
        catch (IOException e) {
          logger.info(e.getMessage());
          throw new ProductHandlerException("Error getting products");
        } 
        catch (Exception ex) {
          logger.info(ex.getMessage());
          throw new ProductHandlerException("Error getting products");
        }
       
      }
      else{
        throw new ProductHandlerException("Invalid product request.");
      }

      return  output ;
  }

  private Product getTableData(String region, String dynamoTable, String productId){
      Product product = dynamoManager.get(region, dynamoTable, productId);
      return product;
  }
}