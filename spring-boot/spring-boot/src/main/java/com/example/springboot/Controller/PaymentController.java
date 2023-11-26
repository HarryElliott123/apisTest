package com.example.springboot.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.Model.Payment;
import com.example.springboot.Model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/")
public class PaymentController {

    private final String FILE_PATH = "C:\\Users\\harry\\Desktop\\spring-boot\\spring-boot\\src\\main\\java\\com\\example\\springboot\\Controller\\payments.json";
    private final String FILE_PATH2 = "C:\\Users\\harry\\Desktop\\spring-boot\\spring-boot\\src\\main\\java\\com\\example\\springboot\\Controller\\users.json";

    @PostMapping("payments")
    public ResponseEntity<String> makePayment(@RequestBody Payment payment) {
        List<Payment> paymentList = getPaymentFromFile();
        
        if (!isValidPayment(payment)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payment data");
        }

        // Check if credit card number is used by a user
        if (!doesCreditCardExist(payment.getCreditCardNumber())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit card not registered");
        }
        
        paymentList.add(payment);
        savePaymentToFile(paymentList);
        return ResponseEntity.status(HttpStatus.CREATED).body("Payment successful");
    }

    public boolean isValidPayment(Payment payment) {
        return isValidCreditCardNumber(payment.getCreditCardNumber()) && isValidAmount(payment.getAmount());
    }

    public boolean isValidCreditCardNumber(String creditCardNumber) {
        return creditCardNumber != null && creditCardNumber.matches("\\d{16}");
    }

    public boolean isValidAmount(int amount) {
        return amount >= 100 && amount <= 999; // Amount is 3 digits only
    }

    public void savePaymentToFile(List<Payment> paymentList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(FILE_PATH), paymentList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Payment> getPaymentFromFile() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                return new ArrayList<>();
            }

            return objectMapper.readValue(file, new TypeReference<List<Payment>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean doesCreditCardExist(String creditCardNumber) {
        List<User> userList = getUsersFromFile();

        System.out.println("User List: " + userList);

        for (int i = 0; i< userList.size(); ++i) {
            if (creditCardNumber.equals(userList.get(i).getCreditCardNumber())) {
                return true;
            }
        }
        return false;
    }

    public List<User> getUsersFromFile() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(FILE_PATH2);

            if (!file.exists()) {
                return new ArrayList<>();
            }

            return objectMapper.readValue(file, new TypeReference<List<User>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}