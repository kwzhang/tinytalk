

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Mqtt QOS 1 - Example for delayed message
 * @author bojun.shim
 *
 */
public class MQTTSubscriberQos1Example implements MqttCallback {
	

	public static void main(String[] args) {
	    new MQTTSubscriberQos1Example().doDemo();
	}

	public void doDemo() {
	    try {
	    	MqttClient client;
	    	
	    	// 재접속시에 서버에 쌓여 있는 메시지를 수신하려면 option.setCleanSession(false)을 해야 함 !!!!!
	    	MqttConnectOptions options = new MqttConnectOptions();
	    	options.setCleanSession(false);
	    	
	    	// qos 1 을 사용하려면 persistence가 필요!!!!!
	    	MemoryPersistence persistence = new MemoryPersistence();
	    	
//	        client = new MqttClient("tcp://localhost:1883", "Sending", persistence);
	        client = new MqttClient("tcp://35.168.51.250:1883", "Sending", persistence);
	        client.connect(options);
	        client.setCallback(this);
	        int qos = 1;
	        client.subscribe("01055556666", qos);
	    } catch (MqttException e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public void connectionLost(Throwable cause) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void messageArrived(String topic, MqttMessage message)
	        throws Exception {
	 System.out.println(message);   
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	    // TODO Auto-generated method stub

	}

}
