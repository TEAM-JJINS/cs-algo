output "web_load_balancer_ip" {
  description = "Web 로드 밸런서 IP"
  value       = kubernetes_service.csalgo_web.status[0].load_balancer[0].ingress[0].hostname
}
