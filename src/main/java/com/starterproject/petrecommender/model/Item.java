package com.starterproject.petrecommender.model;

import com.google.cloud.recommendationengine.v1beta1.CatalogItem;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class Item {

    @NotNull(message = "id cannot be null")
    private final String id;

    @NotNull(message = "catalogItem cannot be null")
    private final CatalogItem catalogItem;

    @Builder
    public Item(String id, CatalogItem catalogItem) {
        this.id = id;
        this.catalogItem = catalogItem;
    }
}
