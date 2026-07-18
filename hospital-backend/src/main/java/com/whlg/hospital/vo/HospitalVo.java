package com.whlg.hospital.vo;

/**
 * 医院VO
 */
public class HospitalVo {

    private Long id;
    private String name;
    private String level;
    private String address;
    private String phone;
    private String intro;
    private String image;
    private String province;
    private String city;
    private Integer departmentCount;
    private Integer doctorCount;
    private Integer realDoctorCount;
    private Integer followCount;
    private Integer status;

    public HospitalVo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Integer getDepartmentCount() { return departmentCount; }
    public void setDepartmentCount(Integer departmentCount) { this.departmentCount = departmentCount; }

    public Integer getDoctorCount() { return doctorCount; }
    public void setDoctorCount(Integer doctorCount) { this.doctorCount = doctorCount; }

    public Integer getRealDoctorCount() { return realDoctorCount; }
    public void setRealDoctorCount(Integer realDoctorCount) { this.realDoctorCount = realDoctorCount; }

    public Integer getFollowCount() { return followCount; }
    public void setFollowCount(Integer followCount) { this.followCount = followCount; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
