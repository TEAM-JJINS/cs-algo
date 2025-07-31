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

module "server" {
  source = "./modules/server"

  image = "csalgo.kr.ncr.ntruss.com/csalgo-server:latest"

  mail_host     = var.mail_host
  mail_port     = var.mail_port
  mail_username = var.mail_username
  mail_password = var.mail_password

  db_url      = "jdbc:mysql://${module.mysql.mysql_host}:3306/${module.mysql.mysql_database}"
  db_username = module.mysql.mysql_username
  db_password = module.mysql.mysql_password

  redis_host = module.redis.redis_host
  redis_port = module.redis.redis_port

  sentry_dsn = var.sentry_dsn
}

module "web" {
  source = "./modules/web"

  image = "csalgo.kr.ncr.ntruss.com/csalgo-web:latest"

  external_api_base_url   = module.server.server_cluster_ip
}
