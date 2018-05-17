package com.designcraft.user;

import io.swagger.model.User;

public class UserDao {
	public static void register(User user) {
		System.out.println("register");
		System.out.println("email: " + user.getEmail());
		System.out.println("number: "  + user.getPhone());
		System.out.println("firstName: " + user.getFirstName());
		System.out.println("lastName: " + user.getLastName());
	}
}
