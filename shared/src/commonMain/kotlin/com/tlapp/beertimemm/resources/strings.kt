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

}
