
import MqttBroker.MqttBroker;
import Networks.HSNNetwork;
import Networks.NetworkType;


import java.net.SocketException;
import java.net.UnknownHostException;

import static MqttBroker.MqttBroker.brokerList.MOSQUITTO;

/**
 * Created by darwin on 25.02.17.
 */
public class Main {
    public static void main(String[] args) throws UnknownHostException, SocketException {

        HSNNetwork network = new HSNNetwork();

        //Start The Broker
        MqttBroker broker = MqttBroker.getInstance();
        broker.setBroker(MOSQUITTO);
        broker.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(broker.getStatus()) {
            System.out.println("Mqtt Broker online ");
        }

        System.out.println(network.getIPAddress(NetworkType.LAN));


        broker.stop();
        if(!broker.getStatus()){
            System.out.println("MQTT Broker Offline");
        }


    }


}
