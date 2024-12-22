package com.example.fingerprintdoorlock

import android.content.Context
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttClientHelper(context: Context) {

    private val serverUri = "tcp://your_mqtt_broker_address:1883"
    private val clientId = "AndroidClient"
    private val topic = "unlock/events"
    private val mqttClient = MqttAndroidClient(context, serverUri, clientId)

    fun connect(callback: MqttCallback) {
        mqttClient.setCallback(callback)
        mqttClient.connect(null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                mqttClient.subscribe(topic, 1)
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                // Handle connection failure
            }
        })
    }

    fun publish(message: String) {
        val mqttMessage = MqttMessage(message.toByteArray())
        mqttClient.publish(topic, mqttMessage)
    }
}