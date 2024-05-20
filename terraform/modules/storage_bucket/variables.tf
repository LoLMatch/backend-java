variable "project" {
  description = "The project ID in GCP."
}

variable "location" {
  description = "The location (region or zone) for the cluster."
  default     = "europe-central2"
}
