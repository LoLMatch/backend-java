# environments/development/variables.tf

variable "project_id" {
  description = "The project ID in GCP."
}

variable "location" {
  description = "The location (region or zone) for the cluster."
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

variable "creds_path" {
  description = "The path to the GCP credentials file."
  default     = "../../credentials.json"
}
