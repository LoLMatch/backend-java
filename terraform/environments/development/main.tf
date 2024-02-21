module "kubernetes" {
  source       = "../../modules/kubernetes"
  project      = var.project
  location     = var.location
  cluster_name = var.cluster_name
}

terraform {
  backend "gcs" {
    bucket = "tf-state-lolmatch-bucket"
    prefix = "terraform/state"
  }
}
