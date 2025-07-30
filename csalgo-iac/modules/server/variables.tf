variable "image" {
  description = "Docker 이미지명"
  type        = string
}

variable "access_key" {
  description = "NCP Access Key"
  type        = string
  sensitive   = true
}

variable "secret_key" {
  description = "NCP Secret Key"
  type        = string
  sensitive   = true
}

variable "mail_host" {
  description = "메일 서버 호스트"
  type        = string
  sensitive   = true
}

variable "mail_port" {
  description = "메일 서버 포트"
  type        = string
  sensitive   = true
}

variable "mail_username" {
  description = "메일 서버 사용자명"
  type        = string
  sensitive   = true
}

variable "mail_password" {
  description = "메일 서버 비밀번호"
  type        = string
  sensitive   = true
}

variable "db_url" {
  description = "데이터베이스 URL"
  type        = string
  sensitive   = true
}

variable "db_username" {
  description = "데이터베이스 사용자명"
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "데이터베이스 비밀번호"
  type        = string
  sensitive   = true
}

variable "sentry_dsn" {
  description = "Sentry DSN"
  type        = string
  sensitive   = true
}

variable "redis_host" {
  description = "Redis 호스트"
  type        = string
  sensitive = true
}

variable "redis_port" {
  description = "Redis 포트"
  type        = string
  sensitive = true
}
