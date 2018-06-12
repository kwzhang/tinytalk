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
	public void dropCall(String sender, String receiver) {
		
		long startTime = Long.parseLong(keyValueDB.get("CALL_START", sender, "start"));
		int thisCall = (int)(System.currentTimeMillis() - startTime) / 1000;
		
		// for sender outCallSecond+
		updateCallSecond(sender, "outCallSecond", thisCall);
		// for receiver inCallSecond+
		updateCallSecond(receiver, "inCallSecond", thisCall);
		
	}
	
	public void txtMsg(String sender, String message) {
		String temp = keyValueDB.get("TXTMSG_HISTORY", sender, "bytes");
		int bytesTotal = 0;
		if (temp != null) {
			bytesTotal = Integer.parseInt(temp);
		}
		
		bytesTotal += message.getBytes().length;
		keyValueDB.add("TXTMSG_HISTORY", sender, "bytes", Integer.toString(bytesTotal));
	}
	
	private void updateCallSecond(String phoneNumber, String updateTarget, int thisCall) {
		String currentPeriod = Period.currentPeriod();
		String temp = keyValueDB.get("CALL_HISTORY", phoneNumber + ":" + currentPeriod, updateTarget);
		int callTotal = 0;
		if (temp != null) {
			callTotal = Integer.parseInt(temp);
		}
		
		callTotal += thisCall;
		keyValueDB.add("CALL_HISTORY", phoneNumber + ":" + currentPeriod,  updateTarget, Integer.toString(callTotal + thisCall));
	}
}
