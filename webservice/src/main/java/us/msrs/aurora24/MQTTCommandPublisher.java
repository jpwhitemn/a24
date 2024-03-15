package us.msrs.aurora24;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import us.msrs.aurora24.model.Command;

@Component
public class MQTTCommandPublisher {

	private static final String PUBLISHER_SERVER = "tcp://localhost:1883"; // #TODO - get from config
	private static final String BATCH_JOB_TOPIC = "aurora24/job"; // #TODO - get from config

	public boolean publishMessgae(String id, Command command) {
		String payload = id + "," + command.toString();
		MqttMessage message = new MqttMessage(payload.getBytes());
		message.setQos(0); // #TODO - get from config
		message.setRetained(false); // #TODO - get from config

		try {  // #TODO - we might want to not close the client each time
			IMqttClient mqttClient = new MqttClient(PUBLISHER_SERVER, MqttClient.generateClientId());
			mqttClient.connect();
			mqttClient.publish(BATCH_JOB_TOPIC, message);
			mqttClient.disconnect();
			mqttClient.close();
		} catch (MqttException e) {
			System.out.println(String.format("Problem sending batch job message to queue (%s)", payload));
			// #TODO log the issue
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
