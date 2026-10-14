package com.mtchlvr.hotel.service;
import org.springframework.stereotype.Service;
import java.util.Locale;
@Service public class HotelChatService {
 public String reply(String raw){
  if(raw==null||raw.isBlank())return "Please type a question about mtchlvr hotel and I’ll do my best to help.";
  String q=raw.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9\\s?]"," ").replaceAll("\\s+"," ").trim();
  if(q.length()>500)return "Please keep your question under 500 characters so I can help clearly.";
  if(matches(q,"hello","hi","hey","good morning","good afternoon","good evening"))return "Hello! Welcome to mtchlvr hotel 🌿 I can help with accounts, signing in, rooms, reservations, cancellations, upgrades, and using the hotel website. What do you need?";
  if(matches(q,"login","log in","sign in","password","cant sign","can't sign","forgot password"))return "To sign in, open the Sign in page and enter your registered email and password. New accounts must verify their email first. If sign-in fails, check your details and confirm that you entered the correct password.";
  if(matches(q,"register","registration","create account","sign up","verify","otp","verification","email code"))return "To create an account, choose Create an account and enter your name, email, and a password of at least 10 characters. Enter the six-digit email verification code to activate your account. In local development, the OTP may appear in the Codespaces terminal if SMTP email is not configured.";
  if(matches(q,"room","rooms","suite","deluxe","standard","family","price","rate","available","availability"))return "mtchlvr hotel has 12 seeded rooms: 3 Standard rooms (₱1,800/night), 3 Deluxe (₱2,600/night), 3 Suites (₱4,200/night), and 3 Family rooms (₱3,500/night). Browse Rooms to see the room numbers and submit a reservation request for your dates.";
  if(matches(q,"reservation","booking","book","check in","check-in","check out","check-out"))return "Browse Rooms, choose a room, and enter your check-in and check-out dates to submit a reservation request. Sign in first. Check-out must be after check-in, and the room must be available for the full date range. You can view your requests under My reservations.";
  if(matches(q,"cancel","cancellation"))return "Open My reservations and select Request cancellation beside an eligible booking. This sends a cancellation request to the manager; it is not instantly finalized.";
  if(matches(q,"upgrade","room upgrade"))return "Room upgrades are part of the planned hotel workflow, but the current starter version does not yet include a working customer upgrade-request form. Please contact the manager for now.";
  if(matches(q,"manager","admin","report","revenue","export","customer list"))return "The manager dashboard is restricted to manager accounts. The current starter shows the room inventory and reservation requests, with basic approve, reject, and cancel status actions. More advanced reports and editing tools are not implemented yet.";
  if(matches(q,"chatbot","what can you do","help","help me","what do you do"))return "I’m the mtchlvr hotel assistant. I can answer questions about registering, email verification, signing in, room types and sample prices, booking dates, reservation status, cancellations, and which features are currently available.";
  return "I’m the mtchlvr hotel assistant, so I can only help with this website’s features—accounts, email verification, rooms, reservations, cancellations, upgrades, and the manager workflow. I can’t help with unrelated topics, but if you tell me what you’re trying to do on the hotel site, I’ll guide you.";
 }
 private boolean matches(String q,String... terms){for(String t:terms)if(q.contains(t))return true;return false;}
}