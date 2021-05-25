package com.mobiquity.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class Record {
    private int capacity;
    private List<Item> items;
}
