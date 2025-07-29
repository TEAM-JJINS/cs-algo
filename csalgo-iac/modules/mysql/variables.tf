variable "root_password" {
  description = "MySQL root 계정 비밀번호"
  type      = string
  sensitive = true
}

variable "db_name" {
  description = "생성할 MySQL 데이터베이스 이름"
  type = string
}

variable "username" {
  description = "MySQL 사용자 이름"
  type = string
}

variable "password" {
  description = "MySQL 사용자 비밀번호"
  type      = string
  sensitive = true
}
