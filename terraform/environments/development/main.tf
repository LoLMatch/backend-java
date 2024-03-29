variable "project" {}
variable "location" {}

module "kubernetes" {
  source       = "../../modules/kubernetes"
  project      = var.project
  location     = var.location
  cluster_name = format("%s%s", basename(abspath(path.cwd)), "-main")
  node_count   = 2
  machine_type = "e2-small"
}

terraform {
  backend "gcs" {
    bucket = "lolmatch-tfstate-storage"
    prefix = "terraform/state"
  }
}
