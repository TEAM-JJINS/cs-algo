terraform {
  required_providers {
    ncloud = {
      source  = "NaverCloudPlatform/ncloud"
      version = "4.0.0"
    }
  }
}

provider "ncloud" {
  access_key  = var.access_key
  secret_key  = var.secret_key
  region      = "KR"
  site        = "public"
  support_vpc = true
}

data "ncloud_nks_clusters" "all" {}

data "ncloud_nks_cluster" "all" {
  for_each = toset(data.ncloud_nks_clusters.all.cluster_uuids)
  uuid     = each.value
}

locals {
  target_uuid = [
    for k, v in data.ncloud_nks_cluster.all : v.id
    if v.name == var.cluster_name
  ][0]
}

data "ncloud_nks_cluster" "target" {
  uuid = local.target_uuid
}

provider "kubernetes" {
  config_path = "./kubeconfig.yaml"
}
