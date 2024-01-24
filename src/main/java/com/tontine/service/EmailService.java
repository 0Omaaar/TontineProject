package com.tontine.service;



public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

}
