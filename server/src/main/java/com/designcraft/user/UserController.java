package com.designcraft.user;

import io.swagger.model.User;

public class UserController {
	public static void register(User user) {
		System.out.println("register");
		System.out.println("email: " + user.getEmail());
		System.out.println("address: "  + user.getAddress());
		System.out.println("password: " + user.getPassword());
		System.out.println("creditcard.number: " + user.getCreditcard().getNumber());
		System.out.println("creditcard.expiredate: " + user.getCreditcard().getExpirationdate());
		System.out.println("creditcard.validationcode: " + user.getCreditcard().getValidationcode());
	}
}
