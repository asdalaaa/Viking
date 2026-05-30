package org.example.repository;

import org.example.model.EquipmentItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EquipmentItemRepository {

    private final JdbcTemplate jdbc;

    private final RowMapper<EquipmentItemEntity> mapper = (rs, n) -> new EquipmentItemEntity(
            rs.getInt("id"),
            rs.getInt("viking_id"),
            rs.getString("name"),
            rs.getString("quality")
    );

    public EquipmentItemRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<EquipmentItemEntity> findByVikingId(int vikingId) {
        return jdbc.query("select id, viking_id, name, quality from equipment_items where viking_id = ? order by id", mapper, vikingId);
    }

    public List<EquipmentItemEntity> findAll() {
        return jdbc.query("select id, viking_id, name, quality from equipment_items order by viking_id, id", mapper);
    }

    public void save(EquipmentItemEntity e) {
        jdbc.update("insert into equipment_items(viking_id, name, quality) values (?, ?, ?)",
                e.vikingId(), e.name(), e.quality());
    }

    public int deleteByVikingId(int vikingId) {
        return jdbc.update("delete from equipment_items where viking_id = ?", vikingId);
    }
}