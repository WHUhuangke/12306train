package com.example.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_order")
public class OrderEntity {
    @Id
    private String id;
    @Column(nullable = false)
    private String memberId;
    private String trainNo;
    private String travelDate;
    private String seatInfo;
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public String getTrainNo() { return trainNo; }
    public void setTrainNo(String trainNo) { this.trainNo = trainNo; }
    public String getTravelDate() { return travelDate; }
    public void setTravelDate(String travelDate) { this.travelDate = travelDate; }
    public String getSeatInfo() { return seatInfo; }
    public void setSeatInfo(String seatInfo) { this.seatInfo = seatInfo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
