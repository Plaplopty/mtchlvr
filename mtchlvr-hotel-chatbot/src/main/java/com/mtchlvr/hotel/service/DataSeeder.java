package com.mtchlvr.hotel.service;
import com.mtchlvr.hotel.model.*; import com.mtchlvr.hotel.repo.*; import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.security.crypto.password.PasswordEncoder; import java.math.BigDecimal;
@Configuration public class DataSeeder {
 @Bean CommandLineRunner seed(RoomRepository rooms,UserRepository users,PasswordEncoder encoder){return args->{
  if(rooms.count()==0){add(rooms,"Standard","1",1800,2);add(rooms,"Deluxe","2",2600,2);add(rooms,"Suite","3",4200,2);add(rooms,"Family","4",3500,4);}
  if(!users.existsByEmailIgnoreCase("manager@mtchlvr.local"))users.save(new AppUser("manager@mtchlvr.local",encoder.encode("ChangeMe123!"),"Hotel Manager","MANAGER",true));
 };}
 private void add(RoomRepository repo,String type,String prefix,int rate,int capacity){for(int i=1;i<=3;i++)repo.save(new Room(prefix+"0"+i,type,BigDecimal.valueOf(rate),capacity));}
}