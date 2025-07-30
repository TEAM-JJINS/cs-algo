output "mysql_host" {
  value = kubernetes_service.mysql.metadata[0].name
}

output "mysql_username" {
  value = var.username
  sensitive = true
}

output "mysql_password" {
  value     = var.password
  sensitive = true
}

output "mysql_database" {
  value = var.db_name
}
