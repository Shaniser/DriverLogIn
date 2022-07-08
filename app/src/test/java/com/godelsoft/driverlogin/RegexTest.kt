package com.godelsoft.driverlogin

import com.godelsoft.driverlogin.utils.InputUtils
import org.junit.Test

import org.junit.Assert.*

class RegexTest {
    @Test
    fun isCorrectLicencePlateNumberTest() {
        val correctLicencePlateNumbers = listOf(
            // group 1
            "С 227 НА 69",
            "В 555 РХ 39",
            "АО 365 78",
            "АН 7331 47",
            "3733 ММ 55",
            "8776АЕ 64",
            "1133АА77",
            "АА 1133 77",
            "ММ 55 АА 23",
            // group 2
            "0245 OK 43",
            "CТ 1769 76",
            "1895 ТС 25",
            "4400РН50",
            // group 5
            "А 1234 78",
            "012 А 36",
            "5537 А 99",
        )

        val incorrectLicencePlateNumbers = listOf(
            // group 1
            "С 000 НА 69",
            "И 555 РХА 39",
            "АОА 365 78",
            "АН 73331 47",
            "373 ММ 55",
            "8776АЕ 6324",
            "33АА77",
            "АЪ 1133 77",
            "ММ АА 55 23",
            // group 2
            "0245 OK",
            "1769 76",
            "1895 23 25",
            "4400РБ50",
            // group 5
            "А 1234 7998",
            "01 А 36",
            "55337 А 99",
        )

        correctLicencePlateNumbers.forEach {
            assertTrue(
                "\"$it\" must be correct",
                InputUtils.isCorrectLicencePlateNumber(it))
        }

        incorrectLicencePlateNumbers.forEach {
            assertFalse(
                "\"$it\" must be incorrect",
                InputUtils.isCorrectLicencePlateNumber(it))
        }
    }

    @Test
    fun isCorrectVehicleRegistrationCertificateTest() {
        val correct = listOf(
            "12 АЗ 345678",
            "12 99 345678",
            "12ВР345678",
            "1200345678",
        )

        val incorrect = listOf(
            "12 АЧ 345678",
            "12 АА 3456789",
            "12 А3 456789",
            "12 3А 456789",
            "12 АА 45678",
            "123 АА 45678",
            "12 999 345678",
            "12 9 345678",
            "12 АЪ 345678",
        )

        correct.forEach {
            assertTrue(
                "\"$it\" must be correct",
                InputUtils.isCorrectVehicleRegistrationCertificate(it))
        }

        incorrect.forEach {
            assertFalse(
                "\"$it\" must be incorrect",
                InputUtils.isCorrectVehicleRegistrationCertificate(it))
        }
    }

    @Test
    fun isCorrectDriversLicenseNumberTest() {
        val correct = listOf(
            "12 АЧ 345678",
            "12 99 345678",
            "12ВР345678",
            "1200345678",
        )

        val incorrect = listOf(
            "12 АЗ 345678",
            "12 АА 3456789",
            "12 А3 456789",
            "12 3А 456789",
            "12 АА 45678",
            "123 АА 45678",
            "12 999 345678",
            "12 9 345678",
            "12 АЪ 345678",
        )

        correct.forEach {
            assertTrue(
                "\"$it\" must be correct",
                InputUtils.isCorrectDriversLicenseNumber(it))
        }

        incorrect.forEach {
            assertFalse(
                "\"$it\" must be incorrect",
                InputUtils.isCorrectDriversLicenseNumber(it))
        }
    }
}