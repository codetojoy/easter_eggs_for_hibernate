
package net.codetojoy.hibernate.example;

public class Address {
    private String street;
    private Owner owner;

    public String getStreet() { return street; }
    private void setStreet(String street) { this.street = street; }

    public Owner getOwner() { return owner; }
    private void setOwner(Owner owner) { this.owner = owner; }
}