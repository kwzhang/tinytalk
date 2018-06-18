package com.designcraft.business.user;

import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.KeyValueDB;
import com.designcraft.infra.db.redis.RedisDBFactory;

import io.swagger.model.User;

public class UserController {
	private KeyValueDB userTable;
	private final String TABLE_NAME = "user";
	
	public UserController () {
		AbstractDBFactory dbFactory = new RedisDBFactory();
		userTable = dbFactory.createKeyValueDB();
	}
	
	
	public String register(User user) {
		//String phoneNumber = (int)(Math.random() * 1000000) + "";
		String phoneNumber = "1112222";
		System.out.println("register");
		System.out.println("email: " + user.getEmail());
		System.out.println("address: "  + user.getAddress());
		System.out.println("password: " + user.getPassword());
		System.out.println("creditcard.number: " + user.getCreditCard().getNumber());
		System.out.println("creditcard.expiredate: " + user.getCreditCard().getExpirationDate());
		System.out.println("creditcard.validationcode: " + user.getCreditCard().getValidationCode());
		userTable.add(TABLE_NAME, phoneNumber, "email", user.getEmail());
		userTable.add(TABLE_NAME, phoneNumber, "address", user.getAddress());
		userTable.add(TABLE_NAME, phoneNumber, "password", user.getPassword());
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.number", user.getCreditCard().getNumber());
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.expiredate", user.getCreditCard().getExpirationDate());
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.validationcode", user.getCreditCard().getValidationCode());
		
		return phoneNumber.toString();
	}
	
	public String updateUser(String phoneNumber, User user) {
		
		System.out.println("register");
		System.out.println("email: " + user.getEmail());
		System.out.println("address: "  + user.getAddress());
		System.out.println("password: " + user.getPassword());
		System.out.println("creditcard.number: " + user.getCreditCard().getNumber());
		System.out.println("creditcard.expiredate: " + user.getCreditCard().getExpirationDate());
		System.out.println("creditcard.validationcode: " + user.getCreditCard().getValidationCode());
		userTable.add(TABLE_NAME, phoneNumber, "email", user.getEmail());
		userTable.add(TABLE_NAME, phoneNumber, "address", user.getAddress());
		userTable.add(TABLE_NAME, phoneNumber, "password", user.getPassword());
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.number", user.getCreditCard().getNumber());
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.expiredate", user.getCreditCard().getExpirationDate());
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.validationcode", user.getCreditCard().getValidationCode());
		
		return phoneNumber.toString();
	}
	
public String deleteUser(String phoneNumber) {
		
		System.out.println("delete user ");
		
		userTable.del(TABLE_NAME, phoneNumber);
		System.out.println("removed Phone Number : " + phoneNumber);
		return phoneNumber.toString();
	}

public String chnagePW(String phoneNumber,String newPW) {
	
	System.out.println("change password ");	
	userTable.add(TABLE_NAME, phoneNumber, "password", newPW);
	
	return phoneNumber.toString();

	}

public boolean isPWCorrect(String phoneNumber, String PW) {

if(userTable.get(TABLE_NAME, phoneNumber, "password").equals(PW))
	return true;
else			
	return false;
}


}


