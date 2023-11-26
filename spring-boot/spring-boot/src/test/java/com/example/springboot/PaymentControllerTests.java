package com.example.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.springboot.Controller.PaymentController;
import com.example.springboot.Controller.UserController;
import com.example.springboot.Model.Payment;
import com.example.springboot.Model.User;


@SpringBootTest
class PaymentControllerTests {

    PaymentController paymentController = new PaymentController();
    UserController userController = new UserController();


	@Test
	void paymentDetailsCheck() {
		Payment examplePayment1 = new Payment();
        examplePayment1.setAmount(123);
        examplePayment1.setCreditCardNumber("1234567890123456");
		
        Payment examplePayment2 = new Payment();
        examplePayment2.setAmount(1234);
        examplePayment2.setCreditCardNumber("1234567890123456");
		
        Payment examplePayment3 = new Payment();
        examplePayment3.setAmount(123);
        examplePayment3.setCreditCardNumber("12345678901234567");
		

        //Test1
        boolean result1 = paymentController.isValidPayment(examplePayment1);
		boolean expected1 = true;
		assertEquals(result1, expected1);

        //Test2
        boolean result2 = paymentController.isValidPayment(examplePayment2);
		boolean expected2 = false;
		assertEquals(result2, expected2);

        //Test3
        boolean result3 = paymentController.isValidPayment(examplePayment3);
		boolean expected3 = false;
		assertEquals(result3, expected3); 
	}

    @Test
	void iscreditCardNumberValid() {
		String cardNumber1 = "1234567890123456";
        String cardNumber2 = "12345678901234567";
        String cardNumber3 = "123456789012345";
		
        
        //Test1
        boolean result1 = paymentController.isValidCreditCardNumber(cardNumber1);
		boolean expected1 = true;
		assertEquals(result1, expected1);

        //Test2
        boolean result2 = paymentController.isValidCreditCardNumber(cardNumber2);
		boolean expected2 = false;
		assertEquals(result2, expected2);

        //Test2
        boolean result3 = paymentController.isValidCreditCardNumber(cardNumber3);
		boolean expected3 = false;
		assertEquals(result3, expected3);
	}

    @Test
	void isAmountValid() {
		int amount1 = 123;
        int amount2 = 12;
        int amount3 = 1234;
		
        
        //Test1
        boolean result1 = paymentController.isValidAmount(amount1);
		boolean expected1 = true;
		assertEquals(result1, expected1);

        //Test2
        boolean result2 = paymentController.isValidAmount(amount2);
		boolean expected2 = false;
		assertEquals(result2, expected2);

        //Test2
        boolean result3 = paymentController.isValidAmount(amount3);
		boolean expected3 = false;
		assertEquals(result3, expected3);
	}

    @Test
    void paymentSavedToFile() {
		Payment examplePayment1 = new Payment();
        examplePayment1.setAmount(123);
        examplePayment1.setCreditCardNumber("1234567890123456");
		
		Payment examplePayment2 = new Payment();
        examplePayment2.setAmount(321);
        examplePayment2.setCreditCardNumber("6543210987654321");
		
		List<Payment> paymentList = Arrays.asList(examplePayment1, examplePayment2);
		List<Payment> emptyList = new ArrayList<>();

		//Test part
		paymentController.savePaymentToFile(emptyList); //Ensures that the current JSON file is empty by passing an empty list of users
		List<Payment> oldPaymentsInFile = paymentController.getPaymentFromFile();
		paymentController.savePaymentToFile(paymentList);
		List<Payment> newPaymentsInFile = paymentController.getPaymentFromFile();

		int result = (newPaymentsInFile.size()) - (oldPaymentsInFile.size());
		int expected = 2;
		assertEquals(expected, result);
	}



    @Test
    void paymentsRecievedFromFile() {
        List<Payment> result = paymentController.getPaymentFromFile();
        assertNotNull(result); //Checks if list of payments returned is not empty
	}

    @Test
    void creditCardExists() {
        User exampleUser1 = new User();
        exampleUser1.setUsername("Username1");
        exampleUser1.setPassword("Password1");
        exampleUser1.setEmail("user1@hotmail.com");
        exampleUser1.setDob(LocalDate.of(1999, 2, 23)); // Set a date of birth for someone over 18
        exampleUser1.setCreditCardNumber("1234567890123456");

		User exampleUser2 = new User();
        exampleUser2.setUsername("Username2");
        exampleUser2.setPassword("Password2");
        exampleUser2.setEmail("user2@hotmail.com");
        exampleUser2.setDob(LocalDate.of(2000, 3, 26)); // Set a date of birth for someone over 18
        exampleUser2.setCreditCardNumber("6543210987654321");

		List<User> userList = Arrays.asList(exampleUser1, exampleUser2);
        userController.saveUsersToFile(userList);
		
		//Test1
		String newCreditCard1 = "1234567890123456";
		boolean result1 = paymentController.doesCreditCardExist(newCreditCard1);
		
		boolean expected1 = true;
		assertEquals(expected1, result1);

		//Test2
		String newCreditCard2 = "1114567890123456";
        boolean result2 = paymentController.doesCreditCardExist(newCreditCard2);
		
		boolean expected2 = false;
		assertEquals(expected2, result2);
    }

    @Test
	void usersObtainedFromFile() {
		List<User> result = paymentController.getUsersFromFile();
		assertNotNull(result); //Checks if list of users returned is not empty
	}




    




}
