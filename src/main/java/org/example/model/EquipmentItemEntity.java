package org.example.model;

public record EquipmentItemEntity(
        Integer id,
        Integer vikingId,
        String name,
        String quality
) {
}