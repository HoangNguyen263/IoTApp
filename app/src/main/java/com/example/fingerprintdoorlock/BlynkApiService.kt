package com.example.fingerprintdoorlock
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
interface BlynkApiService {
    @GET("/external/api/update?token={token}&pin={pin}&value={value}")
    fun virtualWrite(
        @Path("token") token: String,
        @Path("pin") pin: Int,
        @Path("value") value: Int
    ): Call<Void>
}