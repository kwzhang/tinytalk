package com.designcraft.business.cc;

import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.KeyValueDB;
import com.designcraft.infra.db.SetDB;
import com.designcraft.infra.db.redis.RedisDBFactory;

import io.swagger.model.CCRequestInformation;

public class CcController {
	private SetDB ccSet;
	private KeyValueDB ccHash;
	
	public CcController () {
		AbstractDBFactory dbFactory = new RedisDBFactory();
		ccSet = dbFactory.createSetDB();
		ccHash = dbFactory.createKeyValueDB();
	}
	
	/*
	 * Conference Call Request
	 *   create Redis set for 'cclist' that is unique CC list
	 *          Redis set for 'cc123456' that consists of member list
	 *          Redis hash for 'cc123456' that contains CC information such as member count, start datetime, end datetime.
	 */
	public void create(CCRequestInformation ccrequest) {
		String ccNumber = (int)(Math.random() * 1000000) + "";
		
		// check duplication
		while ( ccSet.contains("cclist", ccNumber) == true )
			ccNumber = (int)(Math.random() * 1000000) + "";
		
		String ccNumStr = "cc" + ccNumber;
		int memberCnt = 0;
		
		ccSet.add("cclist", ccNumber);
		
		for ( String member : ccrequest.getMembers() ) {
			ccSet.add(ccNumStr, member);
			memberCnt++;
		}
		
		ccHash.add("cc", ccNumStr, "memberCnt", Integer.toString(memberCnt));
		ccHash.add("cc", ccNumStr, "startdatetime", ccrequest.getStartDatetime());
		ccHash.add("cc", ccNumStr, "enddatetime", ccrequest.getEndDatetime());
	}

}
