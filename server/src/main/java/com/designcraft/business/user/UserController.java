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
	
	
	public String register(User user) throws Exception {
		String phoneNumber = String.format("%07d", (int)(Math.random() * 10000000));
		
		while(userTable.isExist(TABLE_NAME, phoneNumber) ) {
			phoneNumber = String.format("%07d", (int)(Math.random() * 10000000));
		}
		
		userTable.add(TABLE_NAME, phoneNumber, "name", user.getName(), true);
		userTable.add(TABLE_NAME, phoneNumber, "email", user.getEmail(), true);
		userTable.add(TABLE_NAME, phoneNumber, "address", user.getAddress(), true);
		userTable.add(TABLE_NAME, phoneNumber, "password", user.getPassword(), true);
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.number", user.getCreditCard().getNumber(), true);
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.expiredate", user.getCreditCard().getExpirationDate(), true);
		userTable.add(TABLE_NAME, phoneNumber, "creditcard.validationcode", user.getCreditCard().getValidationCode(), true);
		userTable.add(TABLE_NAME, phoneNumber, "status", "enable");
		
		return phoneNumber.toString();
	}
	
	public String updateUser(String phoneNumber, User user) throws Exception {
		if(!userTable.isExist(TABLE_NAME, phoneNumber))
			return "Invaild Phone Number";
		if(null != user.getName() && !user.getName().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "name", user.getName(), true);
		if(null != user.getEmail() && !user.getEmail().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "email", user.getEmail(), true);
		if(null != user.getAddress() && !user.getAddress().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "address", user.getAddress(), true);
		if(null != user.getPassword() && !user.getPassword().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "password", user.getPassword(), true);
		if(null != user.getCreditCard().getNumber() && !user.getCreditCard().getNumber().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "creditcard.number", user.getCreditCard().getNumber(), true);
		if(null != user.getCreditCard().getExpirationDate() &&!user.getCreditCard().getExpirationDate().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "creditcard.expiredate", user.getCreditCard().getExpirationDate(), true);
		if(null != user.getCreditCard().getValidationCode() && !user.getCreditCard().getValidationCode().equals(""))
			userTable.add(TABLE_NAME, phoneNumber, "creditcard.validationcode", user.getCreditCard().getValidationCode(), true);
		
		return phoneNumber.toString();
	}
	
	public String deleteUser(String phoneNumber) {
		if(!userTable.isExist(TABLE_NAME, phoneNumber))
			return "Invaild Phone Number";
		
		userTable.del(TABLE_NAME, phoneNumber);
		return phoneNumber.toString();
	}

	public String chnagePW(String phoneNumber,String newPW) throws Exception {
		if(!userTable.isExist(TABLE_NAME, phoneNumber))
			return "Invaild Phone Number";
	
		userTable.add(TABLE_NAME, phoneNumber, "password", newPW, true);
	
		return phoneNumber.toString();

	}

	public boolean isPWCorrect(String phoneNumber, String PW) throws Exception {
		if(!userTable.isExist(TABLE_NAME, phoneNumber))
			return false;

		if(userTable.get(TABLE_NAME, phoneNumber, "password", true).equals(PW))
			return true;
		else			
			return false;
	}
	
	public boolean isUserCardInfoMatched(String phoneNumber, CreditCard creditCard) throws Exception {
		if(userTable.isExist(TABLE_NAME, phoneNumber)) {
			if(userTable.get(TABLE_NAME, phoneNumber, "creditcard.number", true).equals(creditCard.getNumber())
					&& userTable.get(TABLE_NAME, phoneNumber, "creditcard.expiredate", true).equals(creditCard.getExpirationDate())
					&& userTable.get(TABLE_NAME, phoneNumber, "creditcard.validationcode", true).equals(creditCard.getValidationCode()))
				return true;
		}
		
		return false;		
	}
	
	public boolean isExistUser(String phoneNumber) throws Exception {
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
	
	public User getLoginUserinfo(String phoneNumber) throws Exception {
		User curUser = new User();
		curUser.setName(userTable.get(TABLE_NAME, phoneNumber, "name", true));
		curUser.setEmail(userTable.get(TABLE_NAME, phoneNumber, "email", true));		
		//curUser.setAddress(userTable.get(TABLE_NAME, phoneNumber, "address"));
		//curUser.setPassword(userTable.get(TABLE_NAME, phoneNumber, "password"));		
		
		return curUser;
		
	}
	
	public User getUserinfo(String phoneNumber) throws Exception {
		User curUser = new User();
		curUser.setName(userTable.get(TABLE_NAME, phoneNumber, "name", true));
		curUser.setEmail(userTable.get(TABLE_NAME, phoneNumber, "email", true));		
		curUser.setAddress(userTable.get(TABLE_NAME, phoneNumber, "address", true));
		//curUser.setPassword(userTable.get(TABLE_NAME, phoneNumber, "password"));	
		CreditCard creditCard = new CreditCard();
		
		creditCard.setNumber(userTable.get(TABLE_NAME, phoneNumber, "creditcard.number", true));
		creditCard.setExpirationDate(userTable.get(TABLE_NAME, phoneNumber, "creditcard.expiredate", true));
		creditCard.setValidationCode(userTable.get(TABLE_NAME, phoneNumber, "creditcard.validationcode", true));
		curUser.setCreditCard(creditCard);		
		
		return curUser;
		
	}
	

}


