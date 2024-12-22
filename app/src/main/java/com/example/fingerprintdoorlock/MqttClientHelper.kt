package com.example.fingerprintdoorlock

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttClientHelper(context: Context) {

    private val serverUri = "https://03fc-116-111-122-206.ngrok-free.app/api/realtime"
    private val clientId = "AndroidClient"
    private val topic = "smartlock/door"
    private val mqttClient = MqttAndroidClient(context, serverUri, clientId)

    fun connect(callback: MqttCallback) {
        mqttClient.setCallback(callback)
        mqttClient.connect(null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                mqttClient.subscribe(topic, 1)
                Log.d("MQTT", "Connected to broker and subscribed to topic $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                Log.e("MQTT", "Failed to connect to broker: ${exception.message}", exception)
            }
        })
    }

    fun publish(message: String) {
        val mqttMessage = MqttMessage(message.toByteArray())
        mqttClient.publish(topic, mqttMessage)
    }
}