package com.mtchlvr.hotel.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="rooms")
public class Room {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private String roomNumber;
 @Column(nullable=false) private String type;
 @Column(nullable=false) private BigDecimal nightlyRate;
 @Column(nullable=false) private int capacity;
 @Column(nullable=false) private boolean available=true;
 protected Room(){}
 public Room(String number,String type,BigDecimal rate,int capacity){this.roomNumber=number;this.type=type;this.nightlyRate=rate;this.capacity=capacity;}
 public Long getId(){return id;} public String getRoomNumber(){return roomNumber;} public String getType(){return type;}
 public BigDecimal getNightlyRate(){return nightlyRate;} public int getCapacity(){return capacity;} public boolean isAvailable(){return available;}
 public void setAvailable(boolean v){available=v;}
}