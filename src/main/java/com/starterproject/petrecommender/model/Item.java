/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
