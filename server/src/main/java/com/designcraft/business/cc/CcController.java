package com.designcraft.business.cc;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.designcraft.business.txtmsg.TxtMsgController;
import com.designcraft.business.usage.UsageManager;
import com.designcraft.infra.db.AbstractDBFactory;
import com.designcraft.infra.db.KeyValueDB;
import com.designcraft.infra.db.SetDB;
import com.designcraft.infra.db.redis.RedisDBFactory;
import com.designcraft.infra.messaging.MessageBody;
import com.designcraft.infra.messaging.MessageSender;
import com.designcraft.infra.messaging.MessageTemplate;
import com.designcraft.infra.messaging.jackson.JacksonMessageBody;
import com.designcraft.infra.messaging.mqtt.MqttSender;

import io.swagger.model.CCJoinedIps;
import io.swagger.model.CCRequestInformation;

public class CcController {
	private SetDB mCcSet;
//	private KeyValueDB mCcHash;
	
	private String mCcNumber;
	private String mStartTime;
	private String mEndTime;
	private String mOwner;
	private List<String> mListMember;
	
	public CcController () {
		AbstractDBFactory dbFactory = new RedisDBFactory();
		mCcSet = dbFactory.createSetDB();
//		mCcHash = dbFactory.createKeyValueDB();
	}
	
	/*
	 * Conference Call Request
	 *   create id of conference call
	 */
	public void create(CCRequestInformation ccrequest, String sender) {
		mStartTime = ccrequest.getStartDatetime();
		mEndTime = ccrequest.getEndDatetime();
		mOwner = sender;
		mListMember = ccrequest.getMembers();
		
		StringBuilder sb = new StringBuilder();
		
		Collections.sort(mListMember);
		for ( String member : mListMember )
			sb.append(member).append("-");
		
		sb.deleteCharAt(sb.length() - 1);
		mCcNumber = sb.toString();	// we cannot allow duplicate cc id that consists of same members.
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
	
	public CCJoinedIps startCall(String ccNumber, String sender, String ipOfSender) throws IOException {
		CCJoinedIps joinedIps = new CCJoinedIps();
		List<String> members = new ArrayList<String>();
		Set<String> participants = mCcSet.get("cccall:" + ccNumber);
		for ( String receiver : participants) {
			String[] arr = receiver.split(":");
			members.add(arr[0]);
			joinedIps.addCcJoinedIpsItem(arr[1]);
		}
		
		NewJoin nj = new NewJoin(ccNumber, ipOfSender);

		MessageBody messageBody = new JacksonMessageBody();
		MessageSender msgSender = new MqttSender();
		
		MessageTemplate template = new MessageTemplate("ccNewJoin", nj);
		String messageJson = messageBody.makeMessageBody(template);
		
		for ( String receiver : members )
			msgSender.sendMessage(receiver, messageJson);
		
		mCcSet.add("cccall:" + ccNumber, sender + ":" + ipOfSender);
		new UsageManager().callStart(sender);
		
		return joinedIps;
	}
	
	public void endCall(String ccNumber, String sender) throws IOException {
		new UsageManager().dropReferenceCall(sender);
		
		List<String> members = new ArrayList<String>();
		Set<String> participants = mCcSet.get("cccall:" + ccNumber);
		for ( String receiver : participants) {
			String[] arr = receiver.split(":");
			if ( sender.equals(arr[0]) )
				mCcSet.del("cccall:" + ccNumber, receiver);
			else
				members.add(arr[0]);
		}
		
		CallDrop cd = new CallDrop(ccNumber, sender);
		
		MessageBody messageBody = new JacksonMessageBody();
		MessageSender msgSender = new MqttSender();
		
		MessageTemplate template = new MessageTemplate("ccCallDrop", cd);
		String messageJson = messageBody.makeMessageBody(template);

		for ( String receiver : members )
			msgSender.sendMessage(receiver, messageJson);
	}
}
