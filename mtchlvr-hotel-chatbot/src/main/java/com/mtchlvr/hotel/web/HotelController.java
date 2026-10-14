package com.mtchlvr.hotel.web;
import com.mtchlvr.hotel.model.*; import com.mtchlvr.hotel.repo.*; import com.mtchlvr.hotel.service.*; import jakarta.validation.Valid; import jakarta.validation.constraints.*;
import org.springframework.security.core.Authentication; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Controller; import org.springframework.ui.Model; import org.springframework.validation.BindingResult; import org.springframework.web.bind.annotation.*; import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate; import java.util.Map;
@Controller public class HotelController {
 private final RoomRepository rooms; private final UserRepository users; private final ReservationRepository reservations; private final ReservationService reservationService; private final OtpService otp; private final PasswordEncoder encoder; private final HotelChatService chat;
 public HotelController(RoomRepository r,UserRepository u,ReservationRepository rs,ReservationService svc,OtpService otp,PasswordEncoder enc,HotelChatService chat){rooms=r;users=u;reservations=rs;reservationService=svc;this.otp=otp;encoder=enc;this.chat=chat;}
 @GetMapping("/") public String home(Model m){m.addAttribute("rooms",rooms.findByAvailableTrueOrderByTypeAscRoomNumberAsc());return "index";}
 @GetMapping("/rooms") public String browse(Model m){m.addAttribute("rooms",rooms.findAll());return "rooms";}
 @GetMapping("/login") public String login(){return "login";}
 @GetMapping("/register") public String register(Model m){m.addAttribute("form",new RegisterForm());return "register";}
 @PostMapping("/register") public String registerPost(@Valid @ModelAttribute("form") RegisterForm f,BindingResult e,RedirectAttributes flash){
  if(!f.password.equals(f.confirmPassword))e.rejectValue("confirmPassword","mismatch","Passwords do not match.");
  if(users.existsByEmailIgnoreCase(f.email))e.rejectValue("email","duplicate","An account with this email already exists.");
  if(e.hasErrors())return "register";
  AppUser u=users.save(new AppUser(f.email.trim().toLowerCase(),encoder.encode(f.password),f.fullName.trim(),"CUSTOMER",false));otp.send(u);flash.addFlashAttribute("email",u.getEmail());return "redirect:/verify";
 }
 @GetMapping("/verify") public String verifyPage(Model m,@RequestParam(required=false)String email){if(email!=null)m.addAttribute("email",email);return "verify";}
 @PostMapping("/verify") public String verifyPost(@RequestParam String email,@RequestParam String code,RedirectAttributes flash){AppUser u=users.findByEmailIgnoreCase(email.trim()).orElse(null);if(u==null||!otp.verify(u,code.trim())){flash.addFlashAttribute("error","Code invalid or expired. Check the code in your email or development terminal.");flash.addFlashAttribute("email",email);return "redirect:/verify";}flash.addFlashAttribute("success","Email verified. You can now sign in.");return "redirect:/login";}
 @GetMapping("/customer/reservations") public String myReservations(Authentication a,Model m){m.addAttribute("reservations",reservations.findByCustomerEmailIgnoreCaseOrderByCreatedAtDesc(a.getName()));return "my-reservations";}
 @PostMapping("/customer/reservations") public String reserve(Authentication a,@RequestParam Long roomId,@RequestParam String checkIn,@RequestParam String checkOut,RedirectAttributes f){try{reservationService.reserve(a.getName(),roomId,LocalDate.parse(checkIn),LocalDate.parse(checkOut));f.addFlashAttribute("success","Reservation request submitted.");}catch(Exception ex){f.addFlashAttribute("error",ex.getMessage()==null?"Could not create reservation.":ex.getMessage());}return "redirect:/customer/reservations";}
 @PostMapping("/customer/reservations/{id}/cancel") public String cancel(@PathVariable Long id,Authentication a,RedirectAttributes f){Reservation r=reservations.findById(id).orElse(null);if(r==null||!r.getCustomer().getEmail().equalsIgnoreCase(a.getName()))f.addFlashAttribute("error","Reservation not found.");else if(!r.getStatus().equals("PENDING")&&!r.getStatus().equals("APPROVED"))f.addFlashAttribute("error","This reservation cannot be cancelled in its current status.");else{r.setStatus("CANCELLATION_REQUESTED");reservations.save(r);f.addFlashAttribute("success","Cancellation request sent to the manager.");}return "redirect:/customer/reservations";}
 @GetMapping("/manager") public String manager(Model m){m.addAttribute("rooms",rooms.findAll());m.addAttribute("reservations",reservations.findAllByOrderByCreatedAtDesc());return "manager";}
 @PostMapping("/manager/reservations/{id}/status") public String status(@PathVariable Long id,@RequestParam @Pattern(regexp="APPROVED|REJECTED|CANCELLED")String status,RedirectAttributes f){Reservation r=reservations.findById(id).orElse(null);if(r==null)f.addFlashAttribute("error","Reservation not found.");else{r.setStatus(status);reservations.save(r);f.addFlashAttribute("success","Reservation status updated.");}return "redirect:/manager";}
 @PostMapping("/api/chat") @ResponseBody public Map<String,String> chat(@RequestBody ChatRequest request){String message=request==null?null:request.message();return Map.of("reply",chat.reply(message));}
 
 public record ChatRequest(String message){}

 public static class RegisterForm {
  @NotBlank
  @Size(min=2,max=80)
  private String fullName="";

  @NotBlank
  @Email
  @Size(max=160)
  private String email="";

  @NotBlank
  @Size(min=10,max=72)
  private String password="";

  @NotBlank
  private String confirmPassword="";

  public String getFullName(){return fullName;}
  public void setFullName(String fullName){this.fullName=fullName;}

  public String getEmail(){return email;}
  public void setEmail(String email){this.email=email;}

  public String getPassword(){return password;}
  public void setPassword(String password){this.password=password;}

  public String getConfirmPassword(){return confirmPassword;}
  public void setConfirmPassword(String confirmPassword){this.confirmPassword=confirmPassword;}
 }
}
