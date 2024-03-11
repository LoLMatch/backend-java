module "kubernetes" {
  source       = "../../modules/kubernetes"
  project      = var.project
  location     = var.location
  cluster_name = var.cluster_name
  machine_type = var.machine_type
}

terraform {
  backend "gcs" {
    bucket = "tf-state-lolmatch-bucket"
    prefix = "terraform/state"
  }
}
