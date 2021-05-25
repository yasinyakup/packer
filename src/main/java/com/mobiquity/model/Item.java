package com.mobiquity.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {
    private Long index;
    private Double weight;
    private int cost;

}
