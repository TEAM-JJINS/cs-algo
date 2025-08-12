terraform {
  required_providers {
    cloudflare = {
      source  = "cloudflare/cloudflare"       # Cloudflare provider 소스 지정
      version = "4.4.0"                       # 버전 고정
    }
  }
}

resource "cloudflare_record" "this" {
  for_each = {
    for r in var.records :
    "${r.type}:${r.name != "" ? r.name : var.root_domain}" => r
    if r.value != null && r.value != ""
  }

  zone_id = var.zone_id
  name    = each.value.name == "" ? var.root_domain : each.value.name
  type    = each.value.type
  value   = each.value.value
  ttl     = each.value.ttl
  proxied = each.value.proxied
}
