variable "zone_id" {
  description = "Cloudflare에서 관리하는 도메인의 Zone ID"
  type = string
}

variable "root_domain" {
  description = "Cloudflare에서 관리하는 루트 도메인"
  type = string
}

variable "records" {
  description = "Cloudflare DNS 레코드 설정"
  type = list(object({
    name    : string
    type    : string         # "A" | "CNAME" | ...
    value   : string         # IPv4 or hostname
    ttl     : number
    proxied : bool
  }))
  default = []
}
