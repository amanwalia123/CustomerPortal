package Application;
public class Customer {

    int cid;        //customer id
    String name;    //Name of customer
    String city;    //City for customer

    public Customer(int id, String cusName,String cusCity){
        cid = id;
        name = cusName;
        city = cusCity;
    }

    public int getCid() {
        return cid;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
