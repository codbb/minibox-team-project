package dev.mini.minibox.dtos;

import java.time.LocalDate;

public class ScheduleRequest {
    private LocalDate playDate;  // 클라이언트에서 전달할 날짜
    private String brchNo;

    // Getter, Setter
    public LocalDate getPlayDate() {
        return playDate;
    }

    public void setPlayDate(LocalDate playDate) {
        this.playDate = playDate;
    }

    public String getBrchNo() {
        return brchNo;
    }

    public void setBrchNo(String brchNo) {
        this.brchNo = brchNo;
    }
}
