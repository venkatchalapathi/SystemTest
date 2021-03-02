import androidx.room.Entity
import com.google.gson.annotations.SerializedName



data class Json4Kotlin_Base (

	@SerializedName("total_count") val total_count : Int,
	@SerializedName("incomplete_results") val incomplete_results : Boolean,
	@SerializedName("items") val items : List<Items>

)