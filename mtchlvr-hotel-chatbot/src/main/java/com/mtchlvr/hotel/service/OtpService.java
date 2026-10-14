package com.mtchlvr.hotel.service;
import com.mtchlvr.hotel.model.AppUser; import com.mtchlvr.hotel.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value; import org.springframework.mail.SimpleMailMessage; import org.springframework.mail.javamail.JavaMailSender; import org.springframework.stereotype.Service; import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.SecureRandom; import java.time.Instant;
@Service public class OtpService {
 private final UserRepository users; private final PasswordEncoder encoder; private final JavaMailSender mail;
 private final SecureRandom random=new SecureRandom();
 @Value("${app.otp.minutes:10}") private long minutes; @Value("${app.mail.from:no-reply@mtchlvr.local}") private String from;
 public OtpService(UserRepository u,PasswordEncoder e,JavaMailSender m){users=u;encoder=e;mail=m;}
 public void send(AppUser u){String code=String.format("%06d",random.nextInt(1_000_000));u.setOtpHash(encoder.encode(code));u.setOtpExpiresAt(Instant.now().plusSeconds(minutes*60));users.save(u);
  try{SimpleMailMessage msg=new SimpleMailMessage();msg.setFrom(from);msg.setTo(u.getEmail());msg.setSubject("mtchlvr hotel email verification");msg.setText("Your verification code is "+code+". It expires in "+minutes+" minutes.");mail.send(msg);}
  catch(Exception ex){System.out.println("[LOCAL DEVELOPMENT ONLY] OTP for "+u.getEmail()+": "+code);}
 }
 public boolean verify(AppUser u,String code){if(u.getOtpHash()==null||u.getOtpExpiresAt()==null||Instant.now().isAfter(u.getOtpExpiresAt())||code==null||!code.matches("\\d{6}")||!encoder.matches(code,u.getOtpHash()))return false;u.setVerified(true);u.setOtpHash(null);u.setOtpExpiresAt(null);users.save(u);return true;}
}