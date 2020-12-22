
variable "app_prefix" {
  description = "Application prefix for the AWS services that are built"
  default = "productsmock"
}

variable "stage_name" {
  default = "dev"
  type    = "string"
}

variable "lambda_source_zip_path" {
  description = "Java lambda zip"
  default = "..//target//productsmocksvc-1.0-SNAPSHOT.jar"
}