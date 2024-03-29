package com.example.inhamonchallenge.domain.mail.repository;

import com.example.inhamonchallenge.domain.mail.domain.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailRepository extends JpaRepository<Mail, Long> {
    Optional<Mail> findByEmail(String email);
}