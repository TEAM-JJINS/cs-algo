output "server_cluster_ip" {
  description = "Server 클러스터 IP"
  value = kubernetes_service.csalgo_server.spec[0].cluster_ip
}
