variable "image" {
  description = "웹 애플리케이션의 Docker 이미지"
  type        = string
}

variable "external_api_base_url" {
  description = "API 호출을 위한 외부 API의 기본 URL"
  type        = string
}

variable "jwt_secret" {
  description = "JWT Secret Key"
  type        = string
  sensitive   = true
}
