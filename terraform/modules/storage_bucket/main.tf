provider "google" {
  project = var.project
  region  = var.location
}

resource "google_storage_bucket" "primary" {
  name     = "lolmatch-tfstate-storage"
  location = var.location
  project  = var.project
}
