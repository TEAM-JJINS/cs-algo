variable "image" {
  description = "Docker 이미지명"
  type        = string
}

variable "mail_host"     {
  description = "메일 서버 호스트"
  type = string
}

variable "mail_port"     {
  description = "메일 서버 포트"
  type = string
}

variable "mail_username" {
  description = "메일 서버 사용자명"
  type = string
}

variable "mail_password" {
  description = "메일 서버 비밀번호"
  type = string
}

variable "db_url"        {
  description = "데이터베이스 URL"
  type = string
}

variable "db_username"   {
  description = "데이터베이스 사용자명"
  type = string
}

variable "db_password"   {
  description = "데이터베이스 비밀번호"
  type = string
}

variable "sentry_dsn"    {
  description = "Sentry DSN"
  type = string
}
