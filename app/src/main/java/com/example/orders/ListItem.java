package com.example.orders;

public class ListItem {
    private String id;
    private String name;
    private String address;
    private String address3;
    private String count;
    private String count1;
    private String count2;
    private String price;
    private String price1;
    private String price2;
    private String totalPrice2;
    private String ordering_name;
    private String ordering_name3;
    private String ordering_phone;
    private String ordering_phone3;
    private String datetime;
    private String datetime3;
    private String datetime3_without_second3;
    private String id2;
    private String img;
    private String description;
    private String description1;
    private String id1;
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String name1;

    public ListItem(String id, String name, String address,String count, String price,
                    String ordering_name, String ordering_phone,String datetime){
        this.id = id;
        this.name = name;
        this.address = address;
        this.price = price;
        this.count = count;
        this.ordering_name = ordering_name;
        this.ordering_phone = ordering_phone;
        this.datetime = datetime;
    }

    public ListItem(String id2,String description,String img,String img4){
        this.id2 = id2;
        this.description = description;
        this.img = img;
        this.img4 = img4;
    }

    public ListItem(String id1,String name1,String count1,String img1,String img3,String price1,
                    String description1){
        this.id1 = id1;
        this.name1 = name1;
        this.count1 = count1;
        this.img1 = img1;
        this.img3 = img3;
        this.price1 = price1;
        this.description1 = description1;
    }

    public ListItem(String ordering_name3,String ordering_phone3,String address3,String datetime3,
                    String datetime3_without_second3){
        this.ordering_name3 = ordering_name3;
        this.ordering_phone3 = ordering_phone3;
        this.address3 = address3;
        this.datetime3 = datetime3;
        this.datetime3_without_second3 = datetime3_without_second3;
    }

    public ListItem(String count2,String price2,String totalPrice2,String img2,int a){
        this.count2 = count2;
        this.price2 = price2;
        this.totalPrice2 = totalPrice2;
        this.img2 = img2;
    }

    public String getId(){return id; }

    public String getId1(){return id1; }

    public String getId2(){return id2; }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress3() { return address3; }

    public String getCount() {
        return count;
    }

    public String getCount1() { return count1; }

    public String getCount2() { return count2; }

    public String getPrice() {
        return price;
    }

    public String getPrice1() { return price1; }

    public String getPrice2() { return price2; }

    public String getTotalPrice2() { return totalPrice2; }

    public String getOrderingName() { return ordering_name;}

    public String getOrderingName3() { return ordering_name3;}

    public String getOrderingPhone() { return ordering_phone;}

    public String getOrderingPhone3() { return ordering_phone3;}

    public String getDatetime() { return datetime;}

    public String getDatetime3() { return datetime3;}

    public String getDatetime3_without_second3() { return datetime3_without_second3;}

    public String getDescription() { return description;}

    public String getDescription1() { return description1;}

    public String getImg() { return img;}

    public String getName1() { return name1;}

    public String getImg1() { return img1;}

    public String getImg2() { return img2;}

    public String getImg3() { return img3;}

    public String getImg4() { return img4;}
}
