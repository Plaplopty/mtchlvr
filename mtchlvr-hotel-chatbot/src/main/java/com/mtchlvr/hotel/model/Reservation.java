package com.mtchlvr.hotel.model;
import jakarta.persistence.*;
import java.time.LocalDate; import java.time.Instant;
@Entity @Table(name="reservations")
public class Reservation {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private AppUser customer; @ManyToOne(optional=false) private Room room;
 @Column(nullable=false) private LocalDate checkIn; @Column(nullable=false) private LocalDate checkOut;
 @Column(nullable=false) private String status="PENDING"; @Column(nullable=false) private Instant createdAt=Instant.now();
 protected Reservation(){}
 public Reservation(AppUser c,Room r,LocalDate in,LocalDate out){customer=c;room=r;checkIn=in;checkOut=out;}
 public Long getId(){return id;} public AppUser getCustomer(){return customer;} public Room getRoom(){return room;} public LocalDate getCheckIn(){return checkIn;} public LocalDate getCheckOut(){return checkOut;} public String getStatus(){return status;} public Instant getCreatedAt(){return createdAt;} public void setStatus(String v){status=v;}
}