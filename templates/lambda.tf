resource "aws_lambda_function" "productsmocksavesvc" {
  filename      = "${var.lambda_source_zip_path}"
  function_name = "${var.app_prefix}savesvc-lambda"
  role          = "${aws_iam_role.productsmocksvc_lambda_role.arn}"
  handler       = "com.example.InvocationHandlers::handleSaveRequest"
  runtime       = "java8"
  memory_size   = 2048
  timeout       = 300
  
  source_code_hash = "${filebase64sha256(var.lambda_source_zip_path)}"
  depends_on = ["aws_iam_role.productsmocksvc_lambda_role"]

  environment {
    variables = {
      Env = "Dev"
      S3_BUCKET = "${aws_s3_bucket.s3_productsmocksvc_bucket.bucket}"
      DATABASE_TABLE = "${aws_dynamodb_table.powermocksvc_table.name}"
      REGION = "${data.aws_region.current.name}"
    }
  }
}

resource "aws_lambda_function" "productsmockgetsvc" {
  filename      = "${var.lambda_source_zip_path}"
  function_name = "${var.app_prefix}getsvc-lambda"
  role          = "${aws_iam_role.productsmocksvc_lambda_role.arn}"
  handler       = "com.example.InvocationHandlers::handleGetRequest"
  runtime       = "java8"
  memory_size   = 2048
  timeout       = 300
  
  source_code_hash = "${filebase64sha256(var.lambda_source_zip_path)}"
  depends_on = ["aws_iam_role.productsmocksvc_lambda_role"]

  environment {
    variables = {
      Env = "Dev"
      S3_BUCKET = "${aws_s3_bucket.s3_productsmocksvc_bucket.bucket}"
      DATABASE_TABLE = "${aws_dynamodb_table.powermocksvc_table.name}"
      REGION = "${data.aws_region.current.name}"
    }
  }
}

output "productsmocksavesvc" {
  value = "${aws_lambda_function.productsmocksavesvc}"
}

output "productsmockgetsvc" {
  value = "${aws_lambda_function.productsmockgetsvc}"
}

