module "mysql" {
  source        = "./modules/mysql"
  root_password = var.mysql_root_password
  db_name       = var.mysql_db_name
  username      = var.mysql_user
  password      = var.mysql_password
}

module "redis" {
  source = "./modules/redis"
}
