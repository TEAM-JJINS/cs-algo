terraform {
  required_providers {
    cloudflare = {
      source  = "cloudflare/cloudflare"
      version = ">= 4.4.0"
    }
  }
}

locals {
  # ""와 "@"를 동일하게 취급하도록 정규화
  norm_records = [
    for r in var.records : merge(r, {
      name = (r.name == "" ? "@" : r.name)
    })
    if try(r.value, "") != ""
  ]
}

resource "cloudflare_record" "this" {
  for_each = {
    for r in local.norm_records :
    "${r.type}:${r.name}" => r
  }

  zone_id         = var.zone_id
  name            = each.value.name                   # ← 문자열로 바로 넣기
  type            = each.value.type
  value           = each.value.value
  ttl             = each.value.ttl
  proxied         = each.value.proxied
  allow_overwrite = true                              # ← 기존 레코드 있으면 덮어쓰기
}
