package org.example.repository;

import org.example.model.EquipmentItemEntity;
import org.example.model.Viking;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class VikingStorage {

    private final VikingRepository vikings;
    private final EquipmentItemRepository equipment;
    private final VikingMapper mapper;

    public VikingStorage(VikingRepository vikings, EquipmentItemRepository equipment, VikingMapper mapper) {
        this.vikings = vikings;
        this.equipment = equipment;
        this.mapper = mapper;
    }

    public List<Viking> findAll() {
        Map<Integer, List<EquipmentItemEntity>> eqMap = equipment.findAll()
                .stream().collect(Collectors.groupingBy(EquipmentItemEntity::vikingId));
        return vikings.findAll().stream()
                .map(e -> mapper.toViking(e, eqMap.getOrDefault(e.id(), List.of())))
                .toList();
    }

    public Optional<Viking> findById(int id) {
        return vikings.findById(id)
                .map(e -> mapper.toViking(e, equipment.findByVikingId(id)));
    }

    @Transactional
    public Viking save(Viking v) {
        Integer id = vikings.save(mapper.toEntity(v));
        if (v.equipment() != null) {
            v.equipment().forEach(item -> equipment.save(mapper.toEquipmentEntity(id, item)));
        }
        return findById(id).orElseThrow();
    }

    @Transactional
    public Optional<Viking> update(int id, Viking v) {
        if (vikings.update(mapper.toEntity(id, v)) == 0) return Optional.empty();
        equipment.deleteByVikingId(id);
        if (v.equipment() != null) {
            v.equipment().forEach(item -> equipment.save(mapper.toEquipmentEntity(id, item)));
        }
        return findById(id);
    }

    @Transactional
    public boolean delete(int id) {
        equipment.deleteByVikingId(id);
        return vikings.delete(id) > 0;
    }
}