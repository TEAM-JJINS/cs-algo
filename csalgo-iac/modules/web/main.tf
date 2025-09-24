resource "kubernetes_secret" "csalgo_web_env" {
  metadata {
    name = "csalgo-web-env"
  }

  data = {
    EXTERNAL_API_BASE_URL = var.external_api_base_url
    JWT_SECRET            = var.jwt_secret
  }

  type = "Opaque"
}

resource "kubernetes_deployment" "csalgo_web" {
  metadata {
    name = "csalgo-web"
    labels = {
      app = "csalgo-web"
    }
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "csalgo-web"
      }
    }

    template {
      metadata {
        labels = {
          app = "csalgo-web"
        }
      }

      spec {
        image_pull_secrets {
          name = "ncr-secret"
        }

        container {
          name  = "csalgo-web"
          image = var.image

          port {
            container_port = 80
          }

          resources {
            requests = {
              cpu    = "100m"
              memory = "128Mi"
            }
            limits = {
              cpu    = "200m"
              memory = "192Mi"
            }
          }

          env {
            name  = "EXTERNAL_API_BASE_URL"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_web_env.metadata[0].name
                key  = "EXTERNAL_API_BASE_URL"
              }
            }
          }

          env {
            name  = "JWT_SECRET"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.csalgo_web_env.metadata[0].name
                key  = "JWT_SECRET"
              }
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "csalgo_web" {
  metadata {
    name = "csalgo-web-service"
  }

  spec {
    selector = {
      app = "csalgo-web"
    }

    port {
      port        = 80
      target_port = 3000
    }

    type = "LoadBalancer"
  }
}
