    SAVE_FUNCTION_NAME=productsmocksavesvc-lambda
    GET_FUNCTION_NAME=productsmockgetsvc-lambda
    DEPLOY_JAR=./target/productsmocksvc-1.0-SNAPSHOT.jar
    DEPLOY_REGION=us-east-1

    # Build new Jar
    mvn clean package

    cd templates

    terraform init

    terraform apply --auto-approve

    cd ..
    
    # Update the new function and publish a new lambda version
    aws lambda --region $DEPLOY_REGION update-function-code --function-name $SAVE_FUNCTION_NAME --publish --zip-file fileb://$DEPLOY_JAR 

    aws lambda --region $DEPLOY_REGION update-function-code --function-name $GET_FUNCTION_NAME --publish --zip-file fileb://$DEPLOY_JAR 

    