package com.designcraft.business.user;

import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.KeyValueDB;
import com.designcraft.infra.db.redis.RedisDBFactory;

import io.swagger.model.CardNumber;
import io.swagger.model.CreditCard;
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
		
		while(userTable.isExist(TABLE_NAME, phoneNumber) ) {
			phoneNumber = (int)(Math.random() * 1000000) + "";
		}
		
		System.out.println("register");
		System.out.println("email: " + user.getEmail());
		System.out.println("address: "  + user.getAddress());
		System.out.println("password: " + user.getPassword());
		System.out.println("creditcard.number: " + user.getCreditCard().getNumber());
		System.out.println("creditcard.expiredate: " + user.getCreditCard().getExpirationDate());
		System.out.println("creditcard.validationcode: " + user.getCreditCard().getValidationCode());
		userTable.add(TABLE_NAME, phoneNumber, "name", user.getName());
		userTable.add(TABLE_NAME, phoneNumber, "email", user.getEmail());
		userTable.add(TABLE_NAME, phoneNumber, "address", user.getAddress());
		userTable.add(TABLE_NAME, phoneNumber, "password", user.getPassword());
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.number", user.getCreditCard().getNumber());
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.expiredate", user.getCreditCard().getExpirationDate());
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.validationcode", user.getCreditCard().getValidationCode());
		userTable.add(TABLE_NAME, phoneNumber, "status", "enable");
		
		return phoneNumber.toString();
	}
	
	public String updateUser(String phoneNumber, User user) {
		if(!userTable.isExist(TABLE_NAME, phoneNumber))
			return "Invaild Phone Number";
		System.out.println("register");
		System.out.println("email: " + user.getEmail());
		System.out.println("address: "  + user.getAddress());
		System.out.println("password: " + user.getPassword());
		System.out.println("creditcard.number: " + user.getCreditCard().getNumber());
		System.out.println("creditcard.expiredate: " + user.getCreditCard().getExpirationDate());
		System.out.println("creditcard.validationcode: " + user.getCreditCard().getValidationCode());
		if(null != user.getName() && !user.getName().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "name", user.getName());
		if(null != user.getEmail() && !user.getEmail().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "email", user.getEmail());
		if(null != user.getAddress() && !user.getAddress().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "address", user.getAddress());
		if(null != user.getPassword() && !user.getPassword().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "password", user.getPassword());
		if(null != user.getCreditCard().getNumber() && !user.getCreditCard().getNumber().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "creditcard.number", user.getCreditCard().getNumber());
		if(null != user.getCreditCard().getExpirationDate() &&!user.getCreditCard().getExpirationDate().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "creditcard.expiredate", user.getCreditCard().getExpirationDate());
		if(null != user.getCreditCard().getValidationCode() && !user.getCreditCard().getValidationCode().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "creditcard.validationcode", user.getCreditCard().getValidationCode());
		
		return phoneNumber.toString();
	}
	
	public String deleteUser(String phoneNumber) {
		if(!userTable.isExist(TABLE_NAME, phoneNumber))
			return "Invaild Phone Number";
		
		System.out.println("delete user ");
		
		userTable.del(TABLE_NAME, phoneNumber);
		System.out.println("removed Phone Number : " + phoneNumber);
		return phoneNumber.toString();
	}

	public String chnagePW(String phoneNumber,String newPW) {
		if(!userTable.isExist(TABLE_NAME, phoneNumber))
			return "Invaild Phone Number";
	
		System.out.println("change password ");	
		userTable.add(TABLE_NAME, phoneNumber, "password", newPW);
	
		return phoneNumber.toString();

	}

	public boolean isPWCorrect(String phoneNumber, String PW) {
		if(!userTable.isExist(TABLE_NAME, phoneNumber))
			return false;

		if(userTable.get(TABLE_NAME, phoneNumber, "password").equals(PW))
			return true;
		else			
			return false;
	}
	
	public boolean isUserCardInfoMatched(String phoneNumber, CardNumber cardNumber) {
		if(userTable.isExist(TABLE_NAME, phoneNumber)) {
			if(userTable.get(TABLE_NAME, phoneNumber, "creditcard.number").equals(cardNumber.getCardNumber())) 
				return true;
		}
		
		return false;		
	}
	
	public boolean isExistUser(String phoneNumber) {
		if(!userTable.isExist(TABLE_NAME, phoneNumber)) {
			return false;
		}
		if(userTable.get(TABLE_NAME, phoneNumber, "status").equals("disable")) {
			return false;
		}
		return true;
	}
		
	
	public boolean isAdminUser(String phoneNumber) {
		if(!phoneNumber.equals("0000000")) {
			return false;
		}
		return true;	
	}
	
	public User getLoginUserinfo(String phoneNumber) {
		User curUser = new User();
		curUser.setName(userTable.get(TABLE_NAME, phoneNumber, "name"));
		curUser.setEmail(userTable.get(TABLE_NAME, phoneNumber, "email"));		
		//curUser.setAddress(userTable.get(TABLE_NAME, phoneNumber, "address"));
		//curUser.setPassword(userTable.get(TABLE_NAME, phoneNumber, "password"));		
		
		return curUser;
		
	}
	
	public User getUserinfo(String phoneNumber) {
		User curUser = new User();
		curUser.setName(userTable.get(TABLE_NAME, phoneNumber, "name"));
		curUser.setEmail(userTable.get(TABLE_NAME, phoneNumber, "email"));		
		curUser.setAddress(userTable.get(TABLE_NAME, phoneNumber, "address"));
		//curUser.setPassword(userTable.get(TABLE_NAME, phoneNumber, "password"));	
		CreditCard creditCard = new CreditCard();
		
		creditCard.setNumber(userTable.get(TABLE_NAME, phoneNumber, "creditcard.number"));
		creditCard.setExpirationDate(userTable.get(TABLE_NAME, phoneNumber, "creditcard.expiredate"));
		creditCard.setValidationCode(userTable.get(TABLE_NAME, phoneNumber, "creditcard.validationcode"));
		curUser.setCreditCard(creditCard);		
		
		return curUser;
		
	}
	

}


