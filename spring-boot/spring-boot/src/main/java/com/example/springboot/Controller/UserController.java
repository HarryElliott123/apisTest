package com.example.springboot.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.springboot.Model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")  
public class UserController {

    private final String FILE_PATH = "C:\\Users\\harry\\Desktop\\spring-boot\\spring-boot\\src\\main\\java\\com\\example\\springboot\\Controller\\users.json";

    @GetMapping("users")
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String creditCard) {
        try {
            File file = new File(FILE_PATH);
            ObjectMapper objectMapper = new ObjectMapper();

            List<User> userList = objectMapper.readValue(file, new TypeReference<List<User>>() {}); //Type reference and object mapper helps to convert from object to json

            //Checks if users with or without credit card info will be displayed (optional)
            if (creditCard != null && !creditCard.isEmpty()) {
                List<User> filteredUsersList = new ArrayList<User>();

                for(int i=0; i<userList.size(); ++i) {


                    //If statements were not combined because else statement would run after 1 incorrect if statement (results in an invalid error)
                    if (creditCard.equals("Yes")) {
                        if (userList.get(i).getCreditCardNumber() != null) {
                            filteredUsersList.add(userList.get(i));
                        }
                    }
                    
                    else if (creditCard.equals("No")) {
                        if (userList.get(i).getCreditCardNumber() == null) {
                            filteredUsersList.add(userList.get(i));
                        }
                    }
                    
                    else {
                        System.out.println("!!!!");
                        return ResponseEntity.badRequest().build(); // Invalid filter value provided
                    }
                }

                return ResponseEntity.ok(filteredUsersList);
            }
            return ResponseEntity.ok(userList);
        }
        catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        List<User> userList = getUsersFromFile();

        // The 3 if statements checks the input is valid, usernames isnt taken and is over 18 and returns appropriate http status codes
        if (!isValid(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user data");
        }

        if (!isValid(user) && !islegal(user) || isValid(user) && !islegal(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: User is under 18 years old");
        }

        if (isUsernameTaken(userList, user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
        }
        
        userList.add(user);
        saveUsersToFile(userList);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    
    
    public boolean islegal(User user) {
        // Check DoB and user age is over 18
        try {
            if (Period.between(user.getDob(), LocalDate.now()).getYears() < 18) {
                return false;
            }
        }
        catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public boolean isValid(User user) {
        if (!user.getUsername().matches("^[a-zA-Z0-9]+$")) { // Check username - alphanumeric, no spaces
            return false;
        }

        if (user.getPassword().length() < 8 || !user.getPassword().matches("^.*[A-Z].*$") || !user.getPassword().matches(".*\\d.*")) { // At least one uppercase letter, 1 digit and more than 7 characters    
        return false;
        }

        if (!user.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,4}$")) { //Checks string is in an email fromat
            return false;
        }
    
        if (user.getCreditCardNumber() != null && !user.getCreditCardNumber().matches("^\\d{16}$")) { // Check Credit Card Number has 16 digits
            return false;
        }
        return true; // All validations passed
    }

    public List<User> getUsersFromFile() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                return new ArrayList<>();
            }

            return objectMapper.readValue(file, new TypeReference<List<User>>() {
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void saveUsersToFile(List<User> userList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(FILE_PATH), userList);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isUsernameTaken(List<User> userList, String username) {
        for(int i=0; i<userList.size(); ++i) {
            if (userList.get(i).getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

}
