variable "access_key" {
  description = "네이버 클라우드 플랫폼 API Access Key"
  type        = string
}

variable "secret_key" {
  description = "네이버 클라우드 플랫폼 API Secret Key"
  type        = string
}

variable "cluster_name" {
  description = "Naver Cloud Platform NKS 클러스터 이름"
  type        = string
}

variable "mysql_root_password" {
  description = "MySQL root 계정 비밀번호"
  type      = string
  sensitive = true
}

variable "mysql_db_name" {
  description = "생성할 MySQL 데이터베이스 이름"
  type = string
}

variable "mysql_user" {
  description = "MySQL 사용자 이름"
  type = string
}

variable "mysql_password" {
  description = "MySQL 사용자 비밀번호"
  type      = string
  sensitive = true
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

variable "sentry_dsn"    {
  description = "Sentry DSN"
  type = string
}
