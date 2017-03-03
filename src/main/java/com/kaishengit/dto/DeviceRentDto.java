package com.kaishengit.dto;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/17.
 */
public class DeviceRentDto {


    /**
     * companyName : 李四的公司
     * tel : 31231-3234
     * rentDate : 2017-02-17
     * linkMan : 李四
     * address : 河南
     * backDate : 2017-02-25
     * cardNum : 2134254235
     * fax : 212-232
     * totalDay : 8
     * deviceArray : [{"id":"12","deviceName":"保时捷","currNum":"20","unit":"辆21","rentPrice":"20000","rentNum":"2","totalPrice":40000},{"id":"13","deviceName":"法拉第","currNum":"22","unit":"台","rentPrice":"30000","rentNum":"2","totalPrice":60000}]
     * fileArray : ["036782f9-2fee-4621-9e3c-70c89a2179cb.png","7165f955-786f-4ee2-847d-98aea45ab325.txt","aae2b2e8-2947-4a2c-aa3b-3bd7824d3885.txt"]
     */

    private String companyName;
    private String tel;
    private String rentDate;
    private String linkMan;
    private String address;
    private String backDate;
    private String cardNum;
    private String fax;
    private Integer totalDay;
    private List<DeviceArrayBean> deviceArray;
    private List<FileArrayBean> fileArray;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRentDate() {
        return rentDate;
    }

    public void setRentDate(String rentDate) {
        this.rentDate = rentDate;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBackDate() {
        return backDate;
    }

    public void setBackDate(String backDate) {
        this.backDate = backDate;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(Integer totalDay) {
        this.totalDay = totalDay;
    }

    public List<DeviceArrayBean> getDeviceArray() {
        return deviceArray;
    }

    public void setDeviceArray(List<DeviceArrayBean> deviceArray) {
        this.deviceArray = deviceArray;
    }

    public List<FileArrayBean> getFileArray() {
        return fileArray;
    }

    public void setFileArray(List<FileArrayBean> fileArray) {
        this.fileArray = fileArray;
    }

    public static class DeviceArrayBean {
        /**
         * id : 12
         * deviceName : 保时捷
         * currNum : 20
         * unit : 辆21
         * rentPrice : 20000
         * rentNum : 2
         * totalPrice : 40000
         */

        private Integer id;
        private String deviceName;
        private Integer currNum;
        private String unit;
        private Float rentPrice;
        private Integer rentNum;
        private Float totalPrice;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public Integer getCurrNum() {
            return currNum;
        }

        public void setCurrNum(Integer currNum) {
            this.currNum = currNum;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Float getRentPrice() {
            return rentPrice;
        }

        public void setRentPrice(Float rentPrice) {
            this.rentPrice = rentPrice;
        }

        public Integer getRentNum() {
            return rentNum;
        }

        public void setRentNum(Integer rentNum) {
            this.rentNum = rentNum;
        }

        public Float getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(Float totalPrice) {
            this.totalPrice = totalPrice;
        }
    }

    //内部类
    public static class FileArrayBean{
        private String sourceFileName;
        private String newFileName;

        public String getSourceFileName() {
            return sourceFileName;
        }

        public void setSourceFileName(String sourceFileName) {
            this.sourceFileName = sourceFileName;
        }

        public String getNewFileName() {
            return newFileName;
        }

        public void setNewFileName(String newFileName) {
            this.newFileName = newFileName;
        }
    }
}
