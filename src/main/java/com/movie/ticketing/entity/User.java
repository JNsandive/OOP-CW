package com.movie.ticketing.entity;

import com.movie.ticketing.validator.UserValidator;
import jakarta.persistence.*; // Use this package for all JPA annotations
import lombok.Data;
import lombok.NonNull;

import java.util.regex.Pattern;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "app_user") // Rename to 'app_user' to avoid conflicts
public abstract class User extends UserValidator {
    @Id
    protected String id;
    protected String NIC;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phoneNumber = null;

    public User(String id, String NIC, String firstName, String lastName, String email, @NonNull String phoneNumber) {
        this.id = id;
        this.NIC = NIC;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


    public User() {

    }

    // Common validation for phone number: 10 digits only
    protected boolean validatePhoneNumber() {
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            logger.warning("Invalid phone number: " + phoneNumber);
            return false;
        }
        return true;
    }

    // Common validation for NIC: e.g., alphanumeric and 10 characters
    protected boolean validateNIC() {
        if (NIC == null || !NIC.matches("[A-Za-z0-9]{10}")) {
            logger.warning("Invalid NIC: " + NIC);
            return false;
        }
        return true;
    }

    // Common validation for email: basic email format
    protected boolean validateEmail() {
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        if (email == null || !emailPattern.matcher(email).matches()) {
            logger.warning("Invalid email: " + email);
            return false;
        }
        return true;
    }

    // Optional: Override toString to make logging easier
    @Override
    public String toString() {
        return "User[id=" + id + ", NIC=" + NIC + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
}