package org.example;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationApp {

    private static final String SECRET_KEY = "YourSecretKeyForJWTGeneration";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*[0-9]).{8,}$";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // User input
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Date of Birth (yyyy-mm-dd): ");
        String dobString = scanner.nextLine();

        // Perform validations concurrently
        boolean isValid = validateConcurrently(username, email, password, dobString);

        if (isValid) {
            // Generate JWT token
            String token = generateJWT(username);
            System.out.println("JWT Token: " + token);

            // Verify the token
            boolean isVerified = verifyJWT(token);
            if (isVerified) {
                System.out.println("Verification passed");
            } else {
                System.out.println("Verification failed");
            }
        }
    }

    private static boolean validateConcurrently(String username, String email, String password, String dobString) {
        boolean isValid = true;

        // Validate username
        if (username.isEmpty() || username.length() < 4) {
            System.out.println("Username: invalid (not empty, min 4 characters)");
            isValid = false;
        }

        // Validate email
        if (email.isEmpty() || !isValidEmail(email)) {
            System.out.println("Email: invalid (not empty, valid email address)");
            isValid = false;
        }

        // Validate password
        if (password.isEmpty() || !isValidPassword(password)) {
            System.out.println("Password: invalid (not empty, strong password with at least 1 upper case, " +
                    "1 special character, 1 number, and minimum 8 characters)");
            isValid = false;
        }

        // Validate date of birth
        if (dobString.isEmpty() || !isValidDateOfBirth(dobString)) {
            System.out.println("Date of Birth: invalid (not empty, should be 16 years or greater)");
            isValid = false;
        }

        return isValid;
    }

    private static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private static boolean isValidDateOfBirth(String dobString) {
        LocalDate dob = LocalDate.parse(dobString);
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(dob, currentDate);
        return age.getYears() >= 16;
    }

    private static String generateJWT(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    private static boolean verifyJWT(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


