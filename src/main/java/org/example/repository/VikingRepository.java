package org.example.repository;

import org.example.model.BeardStyle;
import org.example.model.HairColor;
import org.example.model.VikingEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class VikingRepository {

    private final JdbcTemplate jdbc;

    private final RowMapper<VikingEntity> mapper = (rs, n) -> new VikingEntity(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("age"),
            rs.getInt("height_cm"),
            HairColor.valueOf(rs.getString("hair_color")),
            BeardStyle.valueOf(rs.getString("beard_style")),
            rs.getString("description")
    );

    public VikingRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<VikingEntity> findAll() {
        return jdbc.query("select id, name, age, height_cm, hair_color, beard_style, description from vikings order by id", mapper);
    }

    public Optional<VikingEntity> findById(int id) {
        return jdbc.query("select id, name, age, height_cm, hair_color, beard_style, description from vikings where id = ?", mapper, id)
                .stream().findFirst();
    }

    public Integer save(VikingEntity v) {
        KeyHolder key = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into vikings(name, age, height_cm, hair_color, beard_style, description) values (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, v.name());
            ps.setInt(2, v.age());
            ps.setInt(3, v.heightCm());
            ps.setString(4, v.hairColor().name());
            ps.setString(5, v.beardStyle().name());
            ps.setString(6, v.description());
            return ps;
        }, key);
        return key.getKey().intValue();
    }

    public int update(VikingEntity v) {
        return jdbc.update(
                "update vikings set name=?, age=?, height_cm=?, hair_color=?, beard_style=?, description=? where id=?",
                v.name(), v.age(), v.heightCm(), v.hairColor().name(), v.beardStyle().name(), v.description(), v.id());
    }

    public int delete(int id) {
        return jdbc.update("delete from vikings where id = ?", id);
    }
}