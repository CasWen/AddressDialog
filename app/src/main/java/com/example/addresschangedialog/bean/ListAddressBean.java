package com.example.addresschangedialog.bean;


import java.io.Serializable;

public class ListAddressBean implements Serializable {

    /**
     * id : 4028f0816a4e35e8016a4e35e8870000
     * userId : 828
     * receiveName : damon
     * receiveMobile : 18221671161
     * provinceId : 310000
     * provinceName : 上海
     * cityId : 310100
     * cityName : 上海市
     * areaId : 310113
     * areaName : 宝山区
     * zipCode : 200000
     * detailAddress : 地铁3号线 长江南路
     * isDefault : true
     * createDate : 1556090317000
     */

    private String id;
    private String userId;
    private String receiveName;
    private String receiveMobile;
    private String provinceId;
    private String provinceName;
    private String cityId;
    private String cityName;
    private String areaId;
    private String areaName;
    private String zipCode;
    private String detailAddress;
    private boolean isDefault;
    private long createDate;

    /**
     *  {
     *         "id": "4028f0816a4e35e8016a4e35e8870000",
     *         "userId": "828",
     *         "receiveName": "damon",
     *         "receiveMobile": "18221671161",
     *         "provinceId": "310000",
     *         "provinceName": "上海",
     *         "cityId": "310100",
     *         "cityName": "上海市",
     *         "areaId": "310113",
     *         "areaName": "宝山区",
     *         "zipCode": "200000",
     *         "detailAddress": "地铁3号线 长江南路",
     *         "isDefault": true,
     *         "createDate": 1556090317000
     *       }
     */


    @Override
    public String toString() {
        return "ListAddressBean{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", receiveName='" + receiveName + '\'' +
                ", receiveMobile='" + receiveMobile + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                ", areaId='" + areaId + '\'' +
                ", areaName='" + areaName + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", isDefault=" + isDefault +
                ", createDate='" + createDate + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveMobile() {
        return receiveMobile;
    }

    public void setReceiveMobile(String receiveMobile) {
        this.receiveMobile = receiveMobile;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
