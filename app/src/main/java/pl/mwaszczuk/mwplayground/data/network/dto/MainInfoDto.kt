package pl.mwaszczuk.mwplayground.data.network.dto

import com.google.gson.annotations.SerializedName

class MainInfoDto {

    @SerializedName("temp")
    val temperature: Float = 0f

    @SerializedName("temp_min")
    val tempMin: Float = 0f

    @SerializedName("temp_max")
    val tempMax: Float = 0f

    val pressure: Int = 0

    val humidity: Int = 0
}