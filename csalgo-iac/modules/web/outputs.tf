output "web_load_balancer_hostname" {
  description = "Web 로드 밸런서 호스트네임"
  value       = kubernetes_service.csalgo_web.status[0].load_balancer[0].ingress[0].hostname
}
