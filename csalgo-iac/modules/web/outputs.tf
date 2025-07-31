output "csalgo_web_url" {
  description = "Web 클러스터 IP"
  value       = kubernetes_service.csalgo_web.spec[0].cluster_ip
}
