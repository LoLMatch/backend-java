variable "project_id" {}
variable "location" {}
variable "cluster_name" {}
variable "creds_path" {}

provider "google" {
  credentials = file(var.creds_path)
  project     = var.project_id
  region      = var.location
}

resource "google_container_cluster" "primary" {
  name     = var.cluster_name
  location = var.location

  remove_default_node_pool = true
  initial_node_count       = 1
  deletion_protection      = false

}

resource "google_container_node_pool" "primary_preemptible_nodes" {
  name     = "node-pool"
  location = var.location

  cluster    = google_container_cluster.primary.name
  node_count = 1

  node_config {
    disk_size_gb = 100
    disk_type    = "pd-standard"

    preemptible  = true
    machine_type = "e2-small"

  }
}
