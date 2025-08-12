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

  image = var.server_image

  mail_host     = var.mail_host
  mail_port     = var.mail_port
  mail_username = var.mail_username
  mail_password = var.mail_password

  db_host     = module.mysql.mysql_host
  db_name     = module.mysql.mysql_database
  db_username = module.mysql.mysql_username
  db_password = module.mysql.mysql_password

  redis_host = module.redis.redis_host
  redis_port = module.redis.redis_port

  sentry_dsn = var.sentry_dsn
}

module "web" {
  source = "./modules/web"

  image = var.web_image

  external_api_base_url   = module.server.server_cluster_ip
}

module "dns" {
  source = "./modules/dns-cloudflare"
  zone_id = var.cloudflare_zone_id
  root_domain = var.root_domain
  records = [
    // 루트 도메인 -> 웹 LB IP (A 레코드)
    {
      name    = ""                           # "" = 루트
      type    = "A"
      value   = coalesce(module.web.web_cluster_ip, "")  # IP가 null이면 생성 안 함
      ttl     = 3600
      proxied = false
    },
    // www -> 루트 (CNAME)
    {
      name    = "www"
      type    = "CNAME"
      value   = var.root_domain
      ttl     = 3600
      proxied = false
    },
    // api -> 서버 LB IP (A 레코드)
    {
      name    = "api"
      type    = "A"
      value   = coalesce(module.server.server_cluster_ip, "")
      ttl     = 3600
      proxied = false
    },
  ]
}
