package com.designcraft.business.cc;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.designcraft.business.txtmsg.TxtMsgController;
import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.KeyValueDB;
import com.designcraft.infra.db.SetDB;
import com.designcraft.infra.db.redis.RedisDBFactory;

import io.swagger.model.CCRequestInformation;

public class CcController {
	private SetDB mCcSet;
	private KeyValueDB mCcHash;
	
	private String mCcNumber;
	private String mStartTime;
	private String mEndTime;
	private String mOwner;
	private List<String> mListMember;
	
	static class Container {
		public int mExist;
		public String mId;
		
		Container () {
			mExist = 0;
			mId = null;
		}
	}
	
	private final static int MAX_CC = 256;
	private static Container[] mMulticastList = new Container[MAX_CC];
	
	public CcController () {
		AbstractDBFactory dbFactory = new RedisDBFactory();
		mCcSet = dbFactory.createSetDB();
		mCcHash = dbFactory.createKeyValueDB();
	}
	
	/*
	 * Conference Call Request
	 *   create number of conference call
	 *   create Redis set for 'cclist' that is unique CC list
	 *          Redis hash for 'cc123456' that contains CC information such as multicast ip.
	 */
	public void create(CCRequestInformation ccrequest, String sender) {
		mStartTime = ccrequest.getStartDatetime();
		mEndTime = ccrequest.getEndDatetime();
		mOwner = sender;
		mListMember = ccrequest.getMembers();
		
		mCcNumber = String.format("%06d", (int)(Math.random() * 1000000));
		
		// check duplication
		while ( mCcSet.contains("cclist", mCcNumber) == true )
			mCcNumber = String.format("%06d", (int)(Math.random() * 1000000));
		
		mCcSet.add("cclist", mCcNumber);
		
		String ccNumId = "cc" + mCcNumber;

//		mCcHash.add("cc", ccNumId, "membercnt", Integer.toString(mListMember.size()));
//		mCcHash.add("cc", ccNumId, "startdatetime", mStartTime);
//		mCcHash.add("cc", ccNumId, "enddatetime", mEndTime);
		
		mCcHash.add("cc", ccNumId, "ip", CcController.generateMulticastIp(mCcNumber));
	}
	
	private static synchronized String generateMulticastIp(String ccNumber) {
		int idx = 0;
		for (  ;  idx < mMulticastList.length;  idx++ )
			if ( mMulticastList[idx] == null || mMulticastList[idx].mExist == 0 )	break;
		if ( idx >= mMulticastList.length )	return "fail";	// we fixed max size of cc
		
		mMulticastList[idx] = new Container();
		mMulticastList[idx].mExist = 1;
		mMulticastList[idx].mId = ccNumber;
		return "239.0.0." + idx;
	}
	
	private static synchronized void deleteMulticastIp(String ccNumber) {
		int idx = 0;
		for (  ;  idx < mMulticastList.length;  idx++ ) {
			if ( mMulticastList[idx] == null )	continue;
			if ( ccNumber.equals(mMulticastList[idx].mId) )	break;
		}
		if ( idx >= mMulticastList.length )	return;
		
		mMulticastList[idx].mExist = 0;
		mMulticastList[idx].mId = null;
	}

	public String makeInvitationMsg() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[Conference Call] Start : ")
		.append(mStartTime)
		.append(", End : ")
		.append(mEndTime)
		.append(", Number : ")
		.append(mCcNumber);
		
		return sb.toString();
	}
	
	public void setStartTask() {
		new Thread(() -> {
			sendStartMsg();
		}).start();
	}
	
	public void setEndTask() {
		new Thread(() -> {
			deleteCcInfo();
		}).start();
	}
	
	private void sendStartMsg() {
		sleep(mStartTime);
		
		String msg = "Conference Call (" + mCcNumber + ") is started now";

		TxtMsgController controller = new TxtMsgController();
		try {
			controller.sendMsg(mOwner, mListMember, msg, System.currentTimeMillis());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void deleteCcInfo() {
		sleep(mEndTime);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mCcHash.del("cc", "cc" + mCcNumber);
		CcController.deleteMulticastIp(mCcNumber);
	}
	
	private void sleep(String inputTime) {
		try {
			long sleepTime = 0;
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
			Date targetTime = transFormat.parse(inputTime);
			Date curTime = new Date();
			
			System.out.println("targetTime : " + transFormat.format(targetTime.getTime()));
			System.out.println("curTime : " + transFormat.format(curTime));
			
			sleepTime = targetTime.getTime() - curTime.getTime();
			if ( sleepTime < 0 )	sleepTime = 0;
			
			System.out.println("sleep time : " + sleepTime);
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String getMulticastIp(String ccNumber) {
		return mCcHash.get("cc", "cc" + ccNumber, "ip");
	}
	
	public void dropCcDial(String sender, String ccNumber) {
		
	}
}
