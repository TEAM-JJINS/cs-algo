terraform {
  required_providers {
    ncloud = {
      source  = "NaverCloudPlatform/ncloud"   # Ncloud provider 소스 지정
      version = "4.0.0"                       # 버전 고정
    }
  }
}

provider "ncloud" {
  access_key  = var.access_key               # 인증용 Access Key (외부 변수로 주입)
  secret_key  = var.secret_key               # 인증용 Secret Key
  region      = "KR"                         # 리전 설정
  site        = "public"                     # 퍼블릭 클라우드 사용
  support_vpc = true                         # VPC 기반 리소스 사용
}

data "ncloud_nks_clusters" "all" {}          # 현재 프로젝트의 모든 NKS 클러스터 목록 조회

data "ncloud_nks_cluster" "all" {
  for_each = toset(data.ncloud_nks_clusters.all.cluster_uuids)  # 모든 UUID에 대해 순회
  uuid     = each.value                                         # 개별 클러스터 상세 정보 조회
}

locals {
  target_uuid = [                                               # 원하는 클러스터 이름과 일치하는 UUID만 필터링
    for k, v in data.ncloud_nks_cluster.all : v.id
    if v.name == var.cluster_name
  ][0]                                                          # 첫 번째 일치 항목 선택
}

data "ncloud_nks_cluster" "target" {
  uuid = local.target_uuid                                      # 최종 대상 클러스터 정보 바인딩
}

provider "kubernetes" {
  config_path = "./kubeconfig.yaml"                             # 선택한 클러스터용 kubeconfig 사용
}
