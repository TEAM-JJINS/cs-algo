resource "kubernetes_secret" "csalgo_server_env" {
  metadata {
    name = "csalgo-server-env"
  }

  data = {
    MAIL_HOST     = var.mail_host
    MAIL_PORT     = var.mail_port
    MAIL_USERNAME = var.mail_username
    MAIL_PASSWORD = var.mail_password
    DB_HOST       = var.db_host
    DB_NAME       = var.db_name
    DB_USERNAME   = var.db_username
    DB_PASSWORD   = var.db_password
    SENTRY_DSN    = var.sentry_dsn
    REDIS_HOST    = var.redis_host
    REDIS_PORT    = var.redis_port
    JWT_SECRET    = var.jwt_secret
    HUGGINGFACE_API_TOKEN = var.huggingface_api_token
    OPENAI_API_TOKEN = var.openai_api_token
  }

  type = "Opaque"
}

resource "kubernetes_deployment" "csalgo_server" {
  metadata {
    name = "csalgo-server"
    labels = {
      app = "csalgo-server"
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "csalgo-server"
      }
    }

    template {
      metadata {
        labels = {
          app = "csalgo-server"
        }
      }

      spec {
        image_pull_secrets {
          name = "ncr-secret"
        }

        container {
          name  = "csalgo-server"
          image = var.image

          env_from {
            secret_ref {
              name = kubernetes_secret.csalgo_server_env.metadata[0].name
            }
          }

          port {
            container_port = 8080
          }

          resources {
            requests = {
              cpu    = "250m"
              memory = "256Mi"
            }
            limits = {
              cpu    = "500m"
              memory = "512Mi"
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "csalgo_server" {
  metadata {
    name = "csalgo-server-service"
  }

  spec {
    selector = {
      app = "csalgo-server"
    }

    type = "LoadBalancer"

    port {
      port        = 80
      target_port = 8080
    }
  }
}
