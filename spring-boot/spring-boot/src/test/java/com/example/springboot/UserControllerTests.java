package com.example.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.springboot.Controller.UserController;
import com.example.springboot.Model.User;

@SpringBootTest
class UserControllerTests {

	UserController userController = new UserController();

	@Test
	void userIsLegal() {
		User exampleUser = new User();
        exampleUser.setUsername("username123");
        exampleUser.setPassword("password123");
        exampleUser.setEmail("user@hotmail.com");
        exampleUser.setDob(LocalDate.of(1999, 2, 23)); // Set a date of birth for someone over 18

		boolean result = userController.islegal(exampleUser);

		
		boolean expected = true;
		assertEquals(expected, result);
	}

	@Test
	void userDetailsAreValid() {
		User exampleUser = new User();
        exampleUser.setUsername("Username123");
        exampleUser.setPassword("Password123");
        exampleUser.setEmail("user@hotmail.com");
        exampleUser.setDob(LocalDate.of(1999, 2, 23)); // Set a date of birth for someone over 18

		boolean result = userController.isValid(exampleUser);
		
		boolean expected = true;
		assertEquals(expected, result);
	}

	@Test
	void userDetailsNotValid() { //Removed capital letters
		User exampleUser = new User();
        exampleUser.setUsername("username123");
        exampleUser.setPassword("password123");
        exampleUser.setEmail("user@hotmail.com");
        exampleUser.setDob(LocalDate.of(1999, 2, 23)); // Set a date of birth for someone over 18

		boolean result = userController.isValid(exampleUser);
		
		boolean expected = false;
		assertEquals(expected, result);
	}

	@Test
	void usersObtainedFromFile() {
		
		List<User> result = userController.getUsersFromFile();
		
		assertNotNull(result); //Checks if list of users returned is not empty
	}

	@Test
	void usersSavedToFile() {
		User exampleUser1 = new User();
        exampleUser1.setUsername("Username1");
        exampleUser1.setPassword("Password1");
        exampleUser1.setEmail("user1@hotmail.com");
        exampleUser1.setDob(LocalDate.of(1999, 2, 23)); // Set a date of birth for someone over 18

		User exampleUser2 = new User();
        exampleUser2.setUsername("Username2");
        exampleUser2.setPassword("Password2");
        exampleUser2.setEmail("user2@hotmail.com");
        exampleUser2.setDob(LocalDate.of(2000, 3, 26)); // Set a date of birth for someone over 18

		List<User> userList = Arrays.asList(exampleUser1, exampleUser2);
		List<User> emptyList = new ArrayList<>();

		//Test part
		userController.saveUsersToFile(emptyList); //Ensures that the current JSON file is empty by passing an empty list of users
		List<User> oldUsersInFile = userController.getUsersFromFile();
		userController.saveUsersToFile(userList);
		List<User> newUsersInFile = userController.getUsersFromFile();

		int result = (newUsersInFile.size()) - (oldUsersInFile.size());
		int expected = 2;
		assertEquals(expected, result);
	}

	@Test
	void isUsernameTakenTest() {
		User exampleUser1 = new User();
        exampleUser1.setUsername("Username1");
        exampleUser1.setPassword("Password1");
        exampleUser1.setEmail("user1@hotmail.com");
        exampleUser1.setDob(LocalDate.of(1999, 2, 23)); // Set a date of birth for someone over 18

		User exampleUser2 = new User();
        exampleUser2.setUsername("Username2");
        exampleUser2.setPassword("Password2");
        exampleUser2.setEmail("user2@hotmail.com");
        exampleUser2.setDob(LocalDate.of(2000, 3, 26)); // Set a date of birth for someone over 18

		List<User> userList = Arrays.asList(exampleUser1, exampleUser2);
		
		//Test1
		String newUsername1 = "Username1";
		boolean result1 = userController.isUsernameTaken(userList, newUsername1);
		
		boolean expected1 = true;
		assertEquals(expected1, result1); //Checks if list of users returned is not empty
	
		//Test2
		String newUsername2 = "Username3";
		boolean result2 = userController.isUsernameTaken(userList, newUsername2);
		
		boolean expected2 = false;
		assertEquals(expected2, result2);
	}



}
