package org.example.repository;

import org.example.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VikingMapper {

    public VikingEntity toEntity(Viking v) {
        return new VikingEntity(v.id(), v.name(), v.age(), v.heightCm(), v.hairColor(), v.beardStyle(), "");
    }

    public VikingEntity toEntity(int id, Viking v) {
        return new VikingEntity(id, v.name(), v.age(), v.heightCm(), v.hairColor(), v.beardStyle(), "");
    }

    public EquipmentItemEntity toEquipmentEntity(Integer vikingId, EquipmentItem item) {
        return new EquipmentItemEntity(null, vikingId, item.name(), item.quality());
    }

    public EquipmentItem toEquipmentItem(EquipmentItemEntity e) {
        return new EquipmentItem(e.name(), e.quality());
    }

    public Viking toViking(VikingEntity entity, List<EquipmentItemEntity> equipment) {
        return new Viking(
                entity.id(), entity.name(), entity.age(), entity.heightCm(),
                entity.hairColor(), entity.beardStyle(),
                equipment.stream().map(this::toEquipmentItem).toList());
    }
}