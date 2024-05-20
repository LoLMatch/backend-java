provider "google" {
  project = var.project
  region  = var.location
}

resource "google_container_cluster" "primary" {
  name       = var.cluster_name
  location   = var.location
  network    = google_compute_network.vpc_network.name
  subnetwork = google_compute_subnetwork.default_subnetwork.name

  remove_default_node_pool = true
  initial_node_count       = 1
  deletion_protection      = false
}

resource "google_container_node_pool" "node_pool" {
  name     = "node-pool"
  location = var.location

  cluster    = google_container_cluster.primary.name
  node_count = var.node_count

  node_config {
    disk_size_gb = 10
    disk_type    = "pd-standard"

    preemptible  = true
    machine_type = var.machine_type
  }
}

resource "google_container_node_pool" "database_node_pool" {
  name     = "node-pool-db"
  location = var.location

  cluster    = google_container_cluster.primary.name
  node_count = 1

  node_config {
    disk_size_gb = 10
    disk_type    = "pd-standard"

    preemptible  = true
    machine_type = var.machine_type
    labels = {
      role = "database"
    }
    taint {
      key    = "database"
      value  = "true"
      effect = "NO_SCHEDULE"
    }
  }
}

resource "google_compute_network" "vpc_network" {
  name                    = "main"
  auto_create_subnetworks = false
}

resource "google_compute_subnetwork" "default_subnetwork" {
  ip_cidr_range = "10.5.0.0/16"
  name          = "test-subnetwork"
  region        = "europe-central2"
  network       = google_compute_network.vpc_network.id
}
