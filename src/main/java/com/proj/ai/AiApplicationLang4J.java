package com.proj.ai;

import org.springframework.boot.SpringApplication;
//import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiApplicationLang4J {
  public static void main(String[] args) {
  	 try {
           SpringApplication.run(AiApplicationLang4J.class, args);
       } catch (Throwable t) {
           // This forces Java to print the error even if Spring logging fails
           t.printStackTrace(); 
       }
    
  }
}