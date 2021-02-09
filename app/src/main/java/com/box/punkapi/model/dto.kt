package com.box.punkapi.model

//SerializeName은 과제인지라 생략합니다.
data class Beer (
    val id : Float,
    val name : String,
    val tagline : String,
    val first_brewed : String,
    val description : String,
    val image_url : String,
    val abv : Float,
    val ibu : Float,
    val target_fg : Float,
    val target_og : Float,
    val ebc : Float,
    val srm : Float,
    val ph : Float,
    val attenuation_level : Float,
    val volume : Volume,
    val boil_volume : BoilVolume,
    val method : Method,
    val ingredients : Ingredients,
    val food_pairing : List<String>,
    val brewers_tips : String,
    val contributed_by : String
)

data class Amount (override val value: Float, override val unit: String) :ValueUnit

data class BoilVolume (override val value: Float, override val unit: String) :ValueUnit

data class Fermentation (
	val temp : Temp
)

data class Hops (
    val name : String,
    val amount : Amount,
    val add : String,
    val attribute : String
)

data class Ingredients (
    val malt : List<Malt>,
    val hops : List<Hops>,
    val yeast : String
)

data class Malt (
	val name : String,
	val amount : Amount
)

data class MashTemp (
    val temp : Temp,
    val duration : Float
)

data class Method (
    val mash_temp : List<MashTemp>,
    val fermentation : Fermentation,
    val twist : String
)

data class Volume (override val value: Float, override val unit: String) :ValueUnit

data class Temp(override val value: Float, override val unit: String) :ValueUnit
interface ValueUnit{
    val value : Float
    val unit : String
}