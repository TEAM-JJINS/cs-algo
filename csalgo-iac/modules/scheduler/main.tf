resource "kubernetes_secret" "csalgo_scheduler_env" {
  metadata {
    name = "csalgo-scheduler-env"
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
  }

  type = "Opaque"
}

resource "kubernetes_deployment" "csalgo_scheduler" {
  metadata {
    name = "csalgo-scheduler"
    labels = {
      app = "csalgo-scheduler"
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "csalgo-scheduler"
      }
    }

    template {
      metadata {
        labels = {
          app = "csalgo-scheduler"
        }
      }

      spec {
        image_pull_secrets {
          name = "ncr-secret"
        }

        container {
          name  = "csalgo-scheduler"
          image = var.image

          env {
            name = "MAIL_HOST"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_scheduler_env.metadata[0].name
                key  = "MAIL_HOST"
              }
            }
          }

          env {
            name = "MAIL_PORT"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_scheduler_env.metadata[0].name
                key  = "MAIL_PORT"
              }
            }
          }

          env {
            name = "MAIL_USERNAME"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_scheduler_env.metadata[0].name
                key  = "MAIL_USERNAME"
              }
            }
          }

          env {
            name = "MAIL_PASSWORD"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_scheduler_env.metadata[0].name
                key  = "MAIL_PASSWORD"
              }
            }
          }

          env {
            name = "DB_HOST"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_scheduler_env.metadata[0].name
                key  = "DB_HOST"
              }
            }
          }

          env {
            name = "DB_NAME"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_scheduler_env.metadata[0].name
                key  = "DB_NAME"
              }
            }
          }

          env {
            name = "DB_USERNAME"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_scheduler_env.metadata[0].name
                key  = "DB_USERNAME"
              }
            }
          }

          env {
            name = "DB_PASSWORD"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_scheduler_env.metadata[0].name
                key  = "DB_PASSWORD"
              }
            }
          }

          env {
            name = "SENTRY_DSN"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_scheduler_env.metadata[0].name
                key  = "SENTRY_DSN"
              }
            }
          }

          env {
            name = "REDIS_HOST"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_scheduler_env.metadata[0].name
                key  = "REDIS_HOST"
              }
            }
          }

          env {
            name = "REDIS_PORT"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_scheduler_env.metadata[0].name
                key  = "REDIS_PORT"
              }
            }
          }

          env {
            name = "JWT_SECRET"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_scheduler_env.metadata[0].name
                key  = "JWT_SECRET"
              }
            }
          }

          port {
            container_port = 8080
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "csalgo_scheduler" {
  metadata {
    name = "csalgo-scheduler-service"
  }

  spec {
    selector = {
      app = "csalgo-scheduler"
    }

    type = "LoadBalancer"

    port {
      port        = 80
      target_port = 8080
    }
  }
}
