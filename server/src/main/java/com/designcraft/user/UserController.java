package com.designcraft.user;

import com.designcraft.db.AbstractDBFactory;
import com.designcraft.db.KeyValueDB;
import com.designcraft.db.redis.RedisDBFactory;

import io.swagger.model.User;

public class UserController {
	private KeyValueDB userTable;
	private final String TABLE_NAME = "user";
	
	public UserController () {
		AbstractDBFactory dbFactory = new RedisDBFactory();
		userTable = dbFactory.createKeyValueDB();
	}
	
	public String register(User user) {
		String phoneNumber = (int)(Math.random() * 1000000) + "";
		System.out.println("register");
		System.out.println("email: " + user.getEmail());
		System.out.println("address: "  + user.getAddress());
		System.out.println("password: " + user.getPassword());
		System.out.println("creditcard.number: " + user.getCreditcard().getNumber());
		System.out.println("creditcard.expiredate: " + user.getCreditcard().getExpirationdate());
		System.out.println("creditcard.validationcode: " + user.getCreditcard().getValidationcode());
		userTable.add(TABLE_NAME, phoneNumber, "email", user.getEmail());
		userTable.add(TABLE_NAME, phoneNumber, "address", user.getAddress());
		userTable.add(TABLE_NAME, phoneNumber, "password", user.getPassword());
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.number", user.getCreditcard().getNumber());
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.expiredate", user.getCreditcard().getExpirationdate());
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.validationcode", user.getCreditcard().getValidationcode());
		
		return phoneNumber.toString();
	}
}
