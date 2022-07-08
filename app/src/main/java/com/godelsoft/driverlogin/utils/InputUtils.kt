package com.godelsoft.driverlogin.utils

import java.util.*

class InputUtils {
    companion object {
        private fun mapEnToRuLetters(string: String): String {
            var formatted = string

            val enToRu = mapOf(
                'A' to 'А',
                'B' to 'В',
                'E' to 'Е',
                'K' to 'К',
                'M' to 'М',
                'H' to 'Н',
                'O' to 'О',
                'P' to 'Р',
                'C' to 'С',
                'T' to 'Т',
                'X' to 'Х',
                'Y' to 'У',
            )

            for (pair in enToRu) {
                formatted = formatted.replace(pair.key, pair.value)
            }

            return formatted
        }

        private fun formatString(string: String): String {
            return mapEnToRuLetters(string.uppercase(Locale.ROOT))
        }

        fun isCorrectLicencePlateNumber(licencePlateNumber: String?): Boolean {
            if (licencePlateNumber == null) return false

            val formatted = formatString(licencePlateNumber)

            val regexes = listOf(
                // group 1
                Regex("[АВЕКМНОРСТУХ]\\s*\\d{3}(?<!000)\\s*[АВЕКМНОРСТУХ]{2}\\s*\\d{2,3}"),
                Regex("[АВЕКМНОРСТУХ]{2}\\s*\\d{3}(?<!000)\\s*\\d{2,3}"),
                Regex("[АВЕКМНОРСТУХ]{2}\\s*\\d{4}(?<!0000)\\s*\\d{2,3}"),
                Regex("\\d{4}(?<!0000)\\s*[АВЕКМНОРСТУХ]{2}\\s*\\d{2,3}"),
                Regex("[АВЕКМНОРСТУХ]{2}\\s*\\d{2}(?<!00)\\s*[АВЕКМНОРСТУХ]{2}\\s*\\d{2,3}"),
                // group 2
                Regex("\\d{4}(?<!0000)\\s*[АВЕКМНОРСТУХ]{2}\\s*\\d{2,3}"),
                // group 5
                Regex("[АВЕКМНОРСТУХ]\\s*\\d{4}(?<!0000)\\s*\\d{2,3}"),
                Regex("\\d{3}(?<!000)\\s*[АВЕКМНОРСТУХ]\\s*\\d{2,3}"),
                Regex("\\d{4}(?<!0000)\\s*[АВЕКМНОРСТУХ]\\s*\\d{2,3}"),
            )

            for (regex in regexes) {
                if (regex.matches(formatted)) return true
            }

            return false
        }

        fun isCorrectVehicleRegistrationCertificate(vehicleRegistrationCertificate: String?): Boolean {
            if (vehicleRegistrationCertificate == null) return false

            val formatted = formatString(vehicleRegistrationCertificate)

            val regex = Regex("\\d{2}\\s*([АБВЕЗКМНОРСТХУ]{2}|\\d{2})\\s*\\d{6}")

            return regex.matches(formatted)
        }

        fun isCorrectDriversLicenseNumber(driversLicenseNumber: String?): Boolean {
            if (driversLicenseNumber == null) return false

            val formatted = formatString(driversLicenseNumber)

            val regex = Regex("\\d{2}\\s*([АБВЕКМНОРСТХУЧ]{2}|\\d{2})\\s*\\d{6}")

            return regex.matches(formatted)
        }
    }
}