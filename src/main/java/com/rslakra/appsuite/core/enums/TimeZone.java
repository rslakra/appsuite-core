package com.rslakra.appsuite.core.enums;

import java.util.Arrays;

/**
 * @author Rohtash Lakra
 * @created 9/20/22 8:22 PM
 */
public enum TimeZone {
    Pacific_Kiritimati("Pacific/Kiritimati", true),
    Pacific_Enderbury("Pacific/Enderbury", true),
    Pacific_Tongatapu("Pacific/Tongatapu", true),
    Pacific_Chatham("Pacific/Chatham", true),
    Asia_Kamchatka("Asia/Kamchatka", true),
    Pacific_Auckland("Pacific/Auckland", true),
    Pacific_Fiji("Pacific/Fiji", true),
    Pacific_Norfolk("Pacific/Norfolk", true),
    Pacific_Guadalcanal("Pacific/Guadalcanal", true),
    Australia_Lord_Howe("Australia/Lord_Howe", true),
    Australia_Brisbane("Australia/Brisbane", true),
    Australia_Sydney("Australia/Sydney", true),
    Australia_Adelaide("Australia/Adelaide", true),
    Australia_Darwin("Australia/Darwin", true),
    Asia_Seoul("Asia/Seoul", true),
    Asia_Tokyo("Asia/Tokyo", true),
    Asia_Hong_Kong("Asia/Hong_Kong", true),
    Asia_Kuala_Lumpur("Asia/Kuala_Lumpur", true),
    Asia_Manila("Asia/Manila", true),
    Asia_Shanghai("Asia/Shanghai", true),
    Asia_Singapore("Asia/Singapore", true),
    Asia_Taipei("Asia/Taipei", true),
    Australia_Perth("Australia/Perth", true),
    Asia_Bangkok("Asia/Bangkok", true),
    Asia_Jakarta("Asia/Jakarta", true),
    Asia_Saigon("Asia/Saigon", true),
    Asia_Rangoon("Asia/Rangoon", true),
    Asia_Dacca("Asia/Dacca", true),
    Asia_Yekaterinburg("Asia/Yekaterinburg", true),
    Asia_Katmandu("Asia/Katmandu", true),
    Asia_Calcutta("Asia/Calcutta", true),
    Asia_Colombo("Asia/Colombo", true),
    Asia_Karachi("Asia/Karachi", true),
    Asia_Tashkent("Asia/Tashkent", true),
    Asia_Kabul("Asia/Kabul", true),
    Asia_Tehran("Asia/Tehran", true),
    Asia_Dubai("Asia/Dubai", true),
    Asia_Tbilisi("Asia/Tbilisi", true),
    Europe_Moscow("Europe/Moscow", true),
    Africa_Nairobi("Africa/Nairobi", true),
    Asia_Baghdad("Asia/Baghdad", true),
    Asia_Jerusalem("Asia/Jerusalem", true),
    Asia_Kuwait("Asia/Kuwait", true),
    Asia_Riyadh("Asia/Riyadh", true),
    Europe_Athens("Europe/Athens", true),
    Europe_Bucharest("Europe/Bucharest", true),
    Europe_Helsinki("Europe/Helsinki", true),
    Europe_Istanbul("Europe/Istanbul", true),
    Europe_Minsk("Europe/Minsk", true),
    Africa_Cairo("Africa/Cairo", true),
    Africa_Johannesburg("Africa/Johannesburg", true),
    Europe_Amsterdam("Europe/Amsterdam", true),
    Europe_Berlin("Europe/Berlin", true),
    Europe_Brussels("Europe/Brussels", true),
    Europe_Paris("Europe/Paris", true),
    Europe_Prague("Europe/Prague", true),
    Europe_Rome("Europe/Rome", true),
    Africa_Algiers("Africa/Algiers", true),
    Europe_Dublin("Europe/Dublin", true),
    Europe_Lisbon("Europe/Lisbon", true),
    Europe_London("Europe/London", true),
    GMT("GMT", true),
    Atlantic_Cape_Verde("Atlantic/Cape_Verde", true),
    Atlantic_South_Georgia("Atlantic/South_Georgia", true),
    America_St_Johns("America/St_Johns", true),
    America_Buenos_Aires("America/Buenos_Aires", true),
    America_Halifax("America/Halifax", true),
    America_Sao_Paulo("America/Sao_Paulo", true),
    Atlantic_Bermuda("Atlantic/Bermuda", true),
    America_Indianapolis("America/Indianapolis", true),
    America_New_York("America/New_York", true),
    America_Puerto_Rico("America/Puerto_Rico", true),
    America_Santiago("America/Santiago", true),
    America_Caracas("America/Caracas", true),
    America_Bogota("America/Bogota", true),
    America_Chicago("America/Chicago", true),
    America_Panama("America/Panama", true),
    America_Denver("America/Denver", true),
    America_El_Salvador("America/El_Salvador", true),
    America_Phoenix("America/Phoenix", true),
    America_Tijuana("America/Tijuana", true),
    America_Anchorage("America/Anchorage", true),
    America_Los_Angeles("America/Los_Angeles", true),
    Pacific_Honolulu("Pacific/Honolulu", true),
    Pacific_Niue("Pacific/Niue", true),
    Pacific_Pago_Pago("Pacific/Pago_Pago", true),
    Atlantic_Azores("Atlantic/Azores", true),
    America_Scoresbysund("America/Scoresbysund", true),
    Africa_Casablanca("Africa/Casablanca", true),
    Asia_Beirut("Asia/Beirut", true),
    Asia_Baku("Asia/Baku", true),
    Asia_Yerevan("Asia/Yerevan", true),
    Pacific_Gambier("Pacific/Gambier", true),
    America_Atka("America/Atka", true),
    Pacific_Pitcairn("Pacific/Pitcairn", true),
    Mexico_BajaSur("Mexico/BajaSur", true),
    America_Mexico_City("America/Mexico_City", true),
    America_Lima("America/Lima", true),
    Pacific_Marquesas("Pacific/Marquesas", true),
    America_Argentina_Buenos_Aires("America/Argentina/Buenos_Aires", true),
    Asia_Dhaka("Asia/Dhaka", true),
    Etc_GMT("Etc/GMT", true);

    private final String timezone;
    private final Boolean supported;

    /**
     * @param timezone
     * @param supported
     */
    TimeZone(String timezone, Boolean supported) {
        this.timezone = timezone;
        this.supported = supported;
    }

    /**
     * @param timezone
     */
    TimeZone(String timezone) {
        this(timezone, false);
    }

    /**
     * @return
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * @return
     */
    public Boolean isSupported() {
        return supported;
    }

    /**
     * Returns the only supported timeZone otherwise null.
     *
     * @param timeZone
     * @return
     */
    public static TimeZone ofString(final String timeZone) {
        return Arrays.stream(values())
            .filter(entry -> entry.getTimezone().equalsIgnoreCase(timeZone) && entry.isSupported())
            .findFirst()
            .orElse(null);
    }
}
