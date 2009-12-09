
package net.codetojoy.hibernate.example;

public class Owner {
    private long id;

    private String name;

    public long getId() { return id; }
    public void setId(long id ) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    /*
    private Address address;  
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    */
}
