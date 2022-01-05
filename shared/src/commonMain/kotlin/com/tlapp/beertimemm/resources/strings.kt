package com.tlapp.beertimemm.resources

object Strings {

    // StartDrinkingModel
    const val ERROR_NO_BLOOD_LEVEL_SET = "You have to set your wanted blood level before starting"
    const val ERROR_TOO_SHORT_DRINK_INTERVAL = "You can\'t possibly drink that in 0 hours?!"
    const val ERROR_NO_PROFILE_FOUND = "An error occurred getting your user profile. Try setting it in the \"Profile\" page."
    const val ERROR_NO_DRINK_SELECTED = "You cannot possibly drink without selecting an alcohol type?"

    const val ALREADY_DRINKING_ALERT_TITLE = "Are you sure?"
    const val ALREADY_DRINKING_ALERT_MESSAGE = "You are already current drinking, aren\'t you? This will reset your current progress. Press \"Yes\" to start a new drinking session."
    const val ALREADY_DRINKING_ALERT_POSITIVE_BUTTON = "Yes"
    const val ALREADY_DRINKING_ALERT_NEGATIVE_BUTTON = "No"

    // DrinkCoordinator
    const val ERROR_NOTHING_TO_DRINK = "It seems like you would have to drink 0 units based on your requirements.. Try upping your game a bit!"
    const val ERROR_ONLY_ONE_DRINK = "Seems like you'll only have one unit to meet your wanted BAC. No need to use this app for that!"
    const val ERROR_TOO_MANY_DRINKS = "Woah, take it easy. This would require over 30 units. I suggest drinking something stronger!"

    // CountDownModel
    const val N_DRINKS_FIRST_DRINK = "Enjoy your first drink! I'll let you know when it's time for more"
    const val N_DRINKS_LAST_DRINK = "Enjoy your last drink!"
    const val COUNTDOWN_LAST_DRINK_DESCRIPTION = "Until finished"
    const val COUNTDOWN_DESCRIPTION = "Until next drink"

    // AddDrinkModel
    const val NO_NAMELESS_DRINK = "You'll need to add a name for your drink"
    const val DRINK_ADDED = "A new day, a new drink!"
    const val NO_ALCOHOL_INPUT_ERROR = "If the drink is without alcohol, I recommend just drinking it normally without this app."
    const val TOO_STRONG_DRINK = "Whao! Over 100%? That's too much my friend."
    const val TOO_WEAK_DRINK = "Are drinks really drinks if they're under 2%?"
    const val NO_VOLUME_DRINK = "If you can't win by reason, go for volume."
    const val BABY_DRINK_LITER = "What's this volume? A baby shot? Please have your drink larger than 0.02L"
    const val BABY_DRINK_OZ = "What's this volume? A baby shot? Please have your drink larger than 1 oz"
    const val TOO_BIG_DRINK_LITER = "Whao, take it easy. Drinks over 1L is too much for this app to handle."
    const val TOO_BIG_DRINK_OZ = "Whao, take it easy. Drinks over 33 oz is too much for this app to handle."
}
