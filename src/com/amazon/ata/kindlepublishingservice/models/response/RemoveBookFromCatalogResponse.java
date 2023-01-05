package com.amazon.ata.kindlepublishingservice.models.response;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;

import java.util.Objects;

public class RemoveBookFromCatalogResponse {
    private CatalogItemVersion catalogItemVersion;
    public RemoveBookFromCatalogResponse() {
    }

    public RemoveBookFromCatalogResponse(CatalogItemVersion catalogItemVersion) {
        this.catalogItemVersion = catalogItemVersion;
    }

    public CatalogItemVersion getCatalogItemVersion() {
        return catalogItemVersion;
    }

    public void setCatalogItemVersion(CatalogItemVersion catalogItemVersion) {
        this.catalogItemVersion = catalogItemVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoveBookFromCatalogResponse that = (RemoveBookFromCatalogResponse) o;
        return catalogItemVersion.equals(that.catalogItemVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(catalogItemVersion);
    }

    @Override
    public String toString() {
        return "RemoveBookFromCatalogResponse{" +
                "catalogItemVersion=" + catalogItemVersion +
                '}';
    }
}
