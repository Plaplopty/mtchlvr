package com.mtchlvr.hotel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class MtchlvrHotelApplication {
 public static void main(String[] args) {
  try { java.nio.file.Files.createDirectories(java.nio.file.Path.of("data")); }
  catch (java.io.IOException e) { throw new IllegalStateException("Cannot create data directory", e); }
  SpringApplication.run(MtchlvrHotelApplication.class, args);
 }
}