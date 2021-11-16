package com.example.instacomment

/**
 * Class to hold all general utility methods
 */
class MiscUtils {

    companion object {
        private var miscUtils: MiscUtils? = null

        fun getSharedInstance(): MiscUtils? {
            if (miscUtils == null) {
                miscUtils = MiscUtils()
            }
            return miscUtils
        }
    }

    fun getEpocDifference(createdTime: String?): String? {
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val monthsInMilli = daysInMilli * 30
        val yearsInMilli = monthsInMilli * 12
        try {
            val createTimeLong = createdTime?.toLong()?.times(1000)
            val currentTimeLong = System.currentTimeMillis()
            var different = currentTimeLong - createTimeLong!!
            val elapsedYears = different / yearsInMilli
            different = different % yearsInMilli
            val elapsedMonths = different / monthsInMilli
            different = different % monthsInMilli
            val elapsedWeek = different / (7 * daysInMilli)
            val elapsedDays = different / daysInMilli
            different = different % daysInMilli
            val elapsedHours = different / hoursInMilli
            different = different % hoursInMilli
            val elapsedMinutes = different / minutesInMilli
            different = different % minutesInMilli
            val elapsedSeconds = different / secondsInMilli
            if (elapsedYears > 0) {
                return elapsedYears.toString() + "y"
            }
            if (elapsedMonths > 0) {
                return elapsedMonths.toString() + "m"
            }
            if (elapsedWeek > 0) {
                return elapsedWeek.toString() + "w"
            }
            if (elapsedDays > 0) {
                return elapsedDays.toString() + "d"
            }
            if (elapsedHours > 0) {
                return elapsedHours.toString() + "h"
            }
            return if (elapsedMinutes > 0) {
                elapsedMinutes.toString() + "min"
            } else elapsedSeconds.toString() + "s"
        } catch (e: Exception) {
        }
        return null
    }
}
