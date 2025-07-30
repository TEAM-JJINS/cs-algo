output "redis_host" {
  description = "Redis 서비스의 내부 접근 주소"
  value       = kubernetes_service.redis.metadata[0].name
}

output "redis_port" {
  description = "Redis 서비스의 포트"
  value       = kubernetes_service.redis.spec[0].port[0].port
}
