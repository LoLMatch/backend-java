variable "project" {}
variable "location" {}
variable "cluster_name" {}
variable "machine_type" {}

provider "google" {
  project = var.project
  region  = var.location
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
    machine_type = var.machine_type

  }
}
