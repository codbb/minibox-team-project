package dev.mini.minibox.services;

import dev.mini.minibox.entities.AreaEntity;
import dev.mini.minibox.entities.TheaterEntity;
import dev.mini.minibox.mappers.AreaMapper;
import dev.mini.minibox.results.theater.NameCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaService { // 극장의 지역을 나타내는곳
    private final AreaMapper areaMapper;


    @Autowired
    public AreaService(AreaMapper areaMapper) {
        this.areaMapper = areaMapper;
    }

    public AreaEntity[] selectAreas() {
        return this.areaMapper.selectAreas();
    }

    public TheaterEntity[] selectTheaters() {
        return this.areaMapper.selectTheaters();
    }

    public NameCheck name(AreaEntity theater) {
        if (theater.getName() == null || theater.getName().length() < 2 || theater.getName().length() > 10) {
            return NameCheck.FAILURE;
        }
        int affectedRows = this.areaMapper.insertArea(theater);
        return affectedRows > 0 ? NameCheck.SUCCESS : NameCheck.FAILURE;
    }
}
