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

  db_host     = var.mysql_host
  db_name     = var.mysql_db_name
  db_username = var.mysql_user
  db_password = var.mysql_password

  redis_host = module.redis.redis_host
  redis_port = module.redis.redis_port

  sentry_dsn = var.sentry_dsn
  jwt_secret = var.jwt_secret

  huggingface_api_token = var.huggingface_api_token
  openai_api_token = var.openai_api_token
}

module "web" {
  source = "./modules/web"

  image = var.web_image

  external_api_base_url   = "https://api.${var.root_domain}/api"
  jwt_secret              = var.jwt_secret
}

module "scheduler" {
  source = "./modules/scheduler"

  image = var.scheduler_image

  mail_host     = var.mail_host
  mail_port     = var.mail_port
  mail_username = var.mail_username
  mail_password = var.mail_password

  db_host     = var.mysql_host
  db_name     = var.mysql_db_name
  db_username = var.mysql_user
  db_password = var.mysql_password

  redis_host = module.redis.redis_host
  redis_port = module.redis.redis_port

  sentry_dsn = var.sentry_dsn
  jwt_secret = var.jwt_secret

  huggingface_api_token = var.huggingface_api_token
  openai_api_token = var.openai_api_token
}

module "dns" {
  source      = "./modules/dns-cloudflare"
  zone_id     = var.cloudflare_zone_id
  root_domain = var.root_domain

  records = [
    # 루트 → 웹 LB hostname (CNAME, Cloudflare가 루트는 자동 flatten)
    {
      name    = "@"                # apex
      type    = "CNAME"
      value   = module.web.web_load_balancer_hostname
      proxied = true
      ttl     = 1
    },
    # www → 루트
    {
      name    = "www"
      type    = "CNAME"
      value   = var.root_domain
      proxied = true
      ttl     = 1
    },
    # api → 서버 LB hostname
    {
      name    = "api"
      type    = "CNAME"
      value   = module.server.server_load_balancer_hostname
      proxied = true
      ttl     = 1
    },
  ]
}
