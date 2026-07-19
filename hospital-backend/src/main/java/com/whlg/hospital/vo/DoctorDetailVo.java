package com.whlg.hospital.vo;

import java.math.BigDecimal;

/**
 * 医生详情视图对象
 */
public class DoctorDetailVo {

    private Long id;
    private String doctorName;
    private Integer gender;
    private String title;
    private String avatar;
    private String hospitalName;
    private String departmentName;
    private Long departmentId;
    private Long hospitalId;
    private String phone;
    private String intro;
    private String expertise;
    private Integer consultCount;
    private BigDecimal rating;
    private Integer followCount;
    private Integer onlineStatus;
    private BigDecimal price;
    private BigDecimal registrationPrice;

    public DoctorDetailVo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }

    public String getExpertise() { return expertise; }
    public void setExpertise(String expertise) { this.expertise = expertise; }

    public Integer getConsultCount() { return consultCount; }
    public void setConsultCount(Integer consultCount) { this.consultCount = consultCount; }

    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }

    public Integer getFollowCount() { return followCount; }
    public void setFollowCount(Integer followCount) { this.followCount = followCount; }

    public Integer getOnlineStatus() { return onlineStatus; }
    public void setOnlineStatus(Integer onlineStatus) { this.onlineStatus = onlineStatus; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getRegistrationPrice() { return registrationPrice; }
    public void setRegistrationPrice(BigDecimal registrationPrice) { this.registrationPrice = registrationPrice; }
}
