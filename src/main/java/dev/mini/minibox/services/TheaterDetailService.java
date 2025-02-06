package dev.mini.minibox.services;

import dev.mini.minibox.entities.TheaterDetailEntity;
import dev.mini.minibox.entities.TheaterEntity;
import dev.mini.minibox.mappers.TheaterDetailMapper;
import dev.mini.minibox.mappers.TheaterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TheaterDetailService { // 극장의 지점의 상세페이지를 나타내는곳
    private final TheaterMapper theaterBrchMapper;
    private final TheaterDetailMapper theaterDetailMapper;

    @Autowired
    public TheaterDetailService(TheaterMapper theaterMapper, TheaterDetailMapper theaterDetailMapper) {
        this.theaterBrchMapper = theaterMapper;
        this.theaterDetailMapper = theaterDetailMapper;
    }

    public TheaterEntity getDetailByBrchCode(String brchNo) { // 지점코드
        if (brchNo == null || brchNo.isEmpty()) {
            return null;
        }
        return this.theaterBrchMapper.selectTheaters(brchNo);
    }

    public TheaterDetailEntity getDetailByDetailCode(String brchNo) { // 상세코드(지점의 지점코드와 같다)
        if (brchNo == null) {
            return null;
        }
        return this.theaterDetailMapper.selectTheaterDetail(brchNo);
    }

    // 12-17
    public boolean write(TheaterDetailEntity detail) {
        if (detail == null || detail.getBrchNo() == null ||
                detail.getContent() == null || detail.getContent().isEmpty() ||  detail.getContent().length() > 500000) {
            System.out.println("Invalid detail : " + detail);
            return false;
        }

        TheaterEntity theater = this.theaterBrchMapper.selectByBrchNo(detail.getBrchNo());
        if (theater == null) {
            return false;
        }

        detail.setCreatedAt(LocalDateTime.now());
        detail.setUpdatedAt(null);
        detail.setDeletedAt(null);
        int affect = this.theaterDetailMapper.insertDetail(detail);
        System.out.println("Affected : " + affect);
        return affect > 0;
    }

    public boolean updateDetail(TheaterDetailEntity detail) {
        if (detail == null || detail.getBrchNo() == null || detail.getContent() == null || detail.getContent().isEmpty() ||  detail.getContent().length() > 500000) {
            System.out.println("Invalid detail : " + detail);
            return false;
        }
        TheaterDetailEntity theater = this.theaterDetailMapper.selectByDetailCode(detail.getBrchNo());
        if (theater == null) {
            // insert
            if (detail.getTitle() == null || detail.getTitle().isEmpty() || detail.getContent() == null || detail.getContent().isEmpty() || detail.getContent().length() > 500000) {
                return true;
            }
            detail.setCreatedAt(LocalDateTime.now());
            detail.setUpdatedAt(null);
            detail.setDeletedAt(null);
            int affectedRow = this.theaterDetailMapper.insertDetail(detail);
            System.out.println("Affected : " + affectedRow);
            return affectedRow > 0;
        } else {
            // update
            if (detail.getTitle() == null || detail.getTitle().isEmpty() || detail.getContent() == null || detail.getContent().isEmpty() || detail.getContent().length() > 500000) {
                return true;
            }
            detail.setUpdatedAt(LocalDateTime.now());
            int affectedRow = this.theaterDetailMapper.updatedTheaterBrchDetail(detail);
            System.out.println("Affected : " + affectedRow);
            return affectedRow > 0;
        }
//        detail.setUpdatedAt(LocalDateTime.now());
//        int affectedRow = this.theaterDetailMapper.updatedTheaterBrchDetail(detail);
//        System.out.println("Affected : " + affectedRow);
//        return affectedRow > 0;
    }


    public boolean update(TheaterDetailEntity detail) {
        if (detail == null ||
            detail.getTitle() == null || detail.getTitle().isEmpty() ||
            detail.getContent() == null ||detail.getContent().isEmpty()) {
            return false;
        }
        int affects = theaterDetailMapper.update(detail);
        return affects > 0;
    }



    // 모든 영화관 정보 조회 (List로 반환)
    public TheaterDetailEntity[] getTheaterDetails() {
        return theaterDetailMapper.getAllTheaterDetails();
    }
}
