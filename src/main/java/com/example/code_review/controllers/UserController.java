package com.example.code_review.controllers;

import com.example.code_review.Repository.UserRepository;
import com.example.code_review.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    // Dålig namngivning på variabel
    @Autowired
    private UserRepository ur;

    // Dålig namngivning på parametrar och variabler
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User u) {
        // Saknar validering
        return ResponseEntity.ok(ur.save(u));
    }

    // Duplicerad kod för felhantering
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> usr = ur.findById(id);
        if (!usr.isPresent()) {
            throw new RuntimeException("User not found");
        }
        return ResponseEntity.ok(usr.get());
    }

    // Samma felhantering duplicerad
    @GetMapping("/users/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> usr = ur.findByEmail(email);
        if (!usr.isPresent()) {
            throw new RuntimeException("User not found");
        }
        return ResponseEntity.ok(usr.get());
    }

    // Dålig felhantering och ingen logging
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> dltUsr(@PathVariable Long id) {
        try {
            ur.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Ingen paginering, kan returnera för mycket data
    @GetMapping("/users")
    public List<User> getAll() {
        return ur.findAll();
    }

    // Duplicerad affärslogik
    @PutMapping("/users/{id}/activate")
    public ResponseEntity<User> activateUser(@PathVariable Long id) {
        Optional<User> usr = ur.findById(id);
        if (!usr.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User u = usr.get();
        u.setActive(true);
        u.setLastModified(new Date());
        u.setModifiedBy("SYSTEM");
        return ResponseEntity.ok(ur.save(u));
    }

    // Samma logik duplicerad
    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable Long id) {
        Optional<User> usr = ur.findById(id);
        if (!usr.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User u = usr.get();
        u.setActive(false);
        u.setLastModified(new Date());
        u.setModifiedBy("SYSTEM");
        return ResponseEntity.ok(ur.save(u));
    }
}