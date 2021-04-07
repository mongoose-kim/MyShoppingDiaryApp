package ddwu.mobile.finalproject.ma02_20160214;

import java.io.Serializable;

public class ItemDto implements Serializable {

    private int _id;
    private String title;
    private String link;
    private String imageLink;
    private String imageFileName;
    private String lprice;
    private String hprice;
    private String brand;
    private String productId;
    private String category1;
    private String category2;
    private String memo;

    public ItemDto() {
    }

    public ItemDto(int _id, String title, String imageLink, String lprice, String hprice,
                   String brand, String productId, String category1, String category2, String memo) {
        this._id = _id;
        this.title = title;
        this.imageLink = imageLink;
        this.lprice = lprice;
        this.hprice = hprice;
        this.brand = brand;
        this.productId = productId;
        this.category1 = category1;
        this.category2 = category2;
        this.memo = memo;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getImgFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getLprice() {
        return lprice;
    }

    public void setLprice(String lprice) {
        this.lprice = lprice;
    }

    public String getHprice() {
        return hprice;
    }

    public void setHprice(String hprice) {
        this.hprice = hprice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getMemo() { return memo; }

    public void setMemo(String memo) { this.memo = memo; }

    @Override
    public String toString() {
        return "ItemDto{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", imageFileName='" + imageFileName + '\'' +
                ", lprice='" + lprice + '\'' +
                ", hprice='" + hprice + '\'' +
                ", brand='" + brand + '\'' +
                ", productId='" + productId + '\'' +
                ", category1='" + category1 + '\'' +
                ", category2='" + category2 + '\'' +
                '}';
    }
}
