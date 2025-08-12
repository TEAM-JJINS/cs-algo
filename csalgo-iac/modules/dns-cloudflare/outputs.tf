output "record_ids" {
  description = "Cloudflare DNS 레코드 ID 목록"
  value = { for k, v in cloudflare_record.this : k => v.id }
}
