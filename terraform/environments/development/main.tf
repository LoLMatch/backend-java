module "kubernetes" {
  source       = "../../modules/kubernetes"
  project_id   = var.project_id
  location     = var.location
  cluster_name = var.cluster_name
}
