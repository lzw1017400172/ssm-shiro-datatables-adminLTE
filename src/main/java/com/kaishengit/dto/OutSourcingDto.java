package com.kaishengit.dto;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/19.
 */
public class OutSourcingDto {

    /**
     * companyName : 将来的公司
     * address : 田间
     * companyPhone : 42565
     * linkMan : 将来
     * linkManPhone : 424-5656
     * idNum : 725676578367
     * carftArray : [{"name":"电工","price":"300","outSourcingNum":6,"subtotal":1800},{"name":"电焊工","price":"400","outSourcingNum":2,"subtotal":800},{"name":"水泥工","price":"350","outSourcingNum":6,"subtotal":2100}]
     * fileArray : [{"sourceFileName":"ehcache.xml","newFileName":"c20d2d43-949f-4958-a19d-8b7b60565ba5.xml"},{"sourceFileName":"Cmder.exe","newFileName":"6f9c1a25-7721-4333-a4cc-81b7227c9188.exe"}]
     */

    private String companyName;
    private String address;
    private String companyPhone;
    private String linkMan;
    private String linkManPhone;
    private String idNum;
    private List<CarftArrayBean> carftArray;
    private List<FileArrayBean> fileArray;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkManPhone() {
        return linkManPhone;
    }

    public void setLinkManPhone(String linkManPhone) {
        this.linkManPhone = linkManPhone;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public List<CarftArrayBean> getCarftArray() {
        return carftArray;
    }

    public void setCarftArray(List<CarftArrayBean> carftArray) {
        this.carftArray = carftArray;
    }

    public List<FileArrayBean> getFileArray() {
        return fileArray;
    }

    public void setFileArray(List<FileArrayBean> fileArray) {
        this.fileArray = fileArray;
    }

    public static class CarftArrayBean {
        /**
         * name : 电工
         * price : 300
         * outSourcingNum : 6
         * subtotal : 1800
         */

        private Integer id;
        private String name;
        private Float price;
        private int outSourcingNum;
        private Float subtotal;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }

        public int getOutSourcingNum() {
            return outSourcingNum;
        }

        public void setOutSourcingNum(int outSourcingNum) {
            this.outSourcingNum = outSourcingNum;
        }

        public Float getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(Float subtotal) {
            this.subtotal = subtotal;
        }
    }

    public static class FileArrayBean {
        /**
         * sourceFileName : ehcache.xml
         * newFileName : c20d2d43-949f-4958-a19d-8b7b60565ba5.xml
         */

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
