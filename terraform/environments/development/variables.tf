# environments/development/variables.tf

variable "project_id" {
  description = "The project ID in GCP."
}

variable "location" {
  description = "The location (region or zone) for the cluster."
  default     = "europe-central2"
}

variable "cluster_name" {
  description = "The name of the cluster."
  default     = "dev-cluster"
}

variable "node_count" {
  description = "The number of nodes to create in the cluster."
  default     = 1
}

variable "machine_type" {
  description = "The machine type for the cluster nodes."
  default     = "e2-small"
}
