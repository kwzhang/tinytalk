package com.designcraft.business.usage;

import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.KeyValueDB;
import com.designcraft.infra.db.redis.RedisDBFactory;

public class UsageManager {
	private KeyValueDB keyValueDB;
	public UsageManager() {
		AbstractDBFactory factory = new RedisDBFactory();
		keyValueDB = factory.createKeyValueDB();
	}
	/**
	 * sender와 reciver의 통화 시작 시간을 임시 테이블에 저장
	 * @param sender
	 * @param receiver
	 * @param timestamp
	 */
	public void callStart(String sender) {
		keyValueDB.add("CALL_START", sender, "start", Long.toString(System.currentTimeMillis()));
	}
	
	/**
	 * sender의 outcall sec와 receiver의 incall sec를 계산 (현재 시간 - 임시 테일블 저장된 시작 시간)
	 * 계산된 시간을 callhistory 테이블에 저장
	 * 
	 * @param sender
	 * @param receiver
	 */
	public void dropCall(String number) {
		
		long startTime = Long.parseLong(keyValueDB.get("CALL_START", number, "start"));
		int thisCall = (int)(System.currentTimeMillis() - startTime) / 1000;
		
		String type = keyValueDB.get("CALL", number, "type");
		if ( "SENDER".equals(type) ) {
			updateCallSecond(number, "outCallSecond", thisCall);
		} else {
			updateCallSecond(number, "inCallSecond", thisCall);
		}
	}
	
	public void dropReferenceCall(String sender) {
		
		long startTime = Long.parseLong(keyValueDB.get("CALL_START", sender, "start"));
		int thisCall = (int)(System.currentTimeMillis() - startTime) / 1000;
		
		// for sender outCallSecond+
		updateCallSecond(sender, "outCallSecond", thisCall);
	}
	
	public void txtMsg(String member, String message, String flag) {
		String field = null;
		if ( "RECEIVE".equals(flag) )
			field = "receivedBytes";
		else
			field = "sentBytes";
		
		String currentPeriod = Period.currentPeriod();
		String temp = keyValueDB.get("TXTMSG_HISTORY", member + ":" + currentPeriod, field);
		int bytesTotal = 0;
		if (temp != null && !"".equals(temp)) {
			bytesTotal = Integer.parseInt(temp);
		}
		else bytesTotal = 0;
		
		bytesTotal += message.getBytes().length;
		keyValueDB.add("TXTMSG_HISTORY", member + ":" + currentPeriod, field, Integer.toString(bytesTotal));
	}
	
	private void updateCallSecond(String phoneNumber, String updateTarget, int thisCall) {
		String currentPeriod = Period.currentPeriod();
		String temp = keyValueDB.get("CALL_HISTORY", phoneNumber + ":" + currentPeriod, updateTarget);
		int callTotal = 0;
		if (temp != null && !"".equals(temp)) {
			callTotal = Integer.parseInt(temp);
		}
		
		callTotal += thisCall;
		keyValueDB.add("CALL_HISTORY", phoneNumber + ":" + currentPeriod,  updateTarget, Integer.toString(callTotal));
	}
	
	public Integer getIncallHistory(String phoneNumber,String period) {
		String tempInCallSecond = "0";
		tempInCallSecond = keyValueDB.get("CALL_HISTORY", phoneNumber + ":" + period , "inCallSecond");
		if(tempInCallSecond == null || "".equals(tempInCallSecond))
			tempInCallSecond = "0";
		return Integer.parseInt(tempInCallSecond);		
	}
	
	public Integer getOutcallHistory(String phoneNumber,String period) {
		String tempOutCallSecond = "0";
		tempOutCallSecond = keyValueDB.get("CALL_HISTORY", phoneNumber + ":" + period , "outCallSecond");
		if(tempOutCallSecond == null || "".equals(tempOutCallSecond))
			tempOutCallSecond = "0";
		
		return Integer.parseInt(tempOutCallSecond);		
	}
	
	public Integer getReceivedTextHistory(String phoneNumber,String period) {
		String tempText = "0";
		tempText = keyValueDB.get("TXTMSG_HISTORY" , phoneNumber + ":" + period, "receivedBytes");
		if(tempText == null || "".equals(tempText))
			tempText = "0";
		
		return Integer.parseInt(tempText);		
	}
	
	public Integer getSentTextHistory(String phoneNumber,String period) {
		String tempText = "0";
		tempText = keyValueDB.get("TXTMSG_HISTORY" , phoneNumber + ":" + period, "sentBytes");
		if(tempText == null || "".equals(tempText))
			tempText = "0";
		
		return Integer.parseInt(tempText);		
	}
	
}
