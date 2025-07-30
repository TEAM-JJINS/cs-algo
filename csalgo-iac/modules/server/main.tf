resource "kubernetes_secret" "csalgo_server_env" {
  metadata {
    name = "csalgo-server-env"
  }

  data = {
    MAIL_HOST        = base64encode(var.mail_host)
    MAIL_PORT        = base64encode(var.mail_port)
    MAIL_USERNAME    = base64encode(var.mail_username)
    MAIL_PASSWORD    = base64encode(var.mail_password)
    DB_URL           = base64encode(var.db_url)
    DB_USERNAME      = base64encode(var.db_username)
    DB_PASSWORD      = base64encode(var.db_password)
    SENTRY_DSN       = base64encode(var.sentry_dsn)
  }
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
        container {
          name  = "csalgo-server"
          image = var.image

          env {
            name = "MAIL_HOST"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_server_env.metadata[0].name
                key  = "MAIL_HOST"
              }
            }
          }

          env {
            name = "MAIL_PORT"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_server_env.metadata[0].name
                key  = "MAIL_PORT"
              }
            }
          }

          env {
            name = "MAIL_USERNAME"
            value_from {
              secret_key_ref {
                  name = kubernetes_secret.csalgo_server_env.metadata[0].name
                  key  = "MAIL_USERNAME"
              }
            }
          }

          env {
            name = "MAIL_PASSWORD"
            value_from {
              secret_key_ref {
                  name = kubernetes_secret.csalgo_server_env.metadata[0].name
                  key  = "MAIL_PASSWORD"
              }
            }
          }

          env {
            name = "DB_URL"
            value_from {
              secret_key_ref {
                  name = kubernetes_secret.csalgo_server_env.metadata[0].name
                  key  = "DB_URL"
              }
            }
          }

          env {
            name = "DB_USERNAME"
            value_from {
              secret_key_ref {
                  name = kubernetes_secret.csalgo_server_env.metadata[0].name
                  key  = "DB_USERNAME"
              }
            }
          }

          env {
            name = "DB_PASSWORD"
            value_from {
              secret_key_ref {
                  name = kubernetes_secret.csalgo_server_env.metadata[0].name
                  key  = "DB_PASSWORD"
              }
            }
          }

          env {
            name = "SENTRY_DSN"
            value_from {
              secret_key_ref {
                  name = kubernetes_secret.csalgo_server_env.metadata[0].name
                  key  = "SENTRY_DSN"
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
