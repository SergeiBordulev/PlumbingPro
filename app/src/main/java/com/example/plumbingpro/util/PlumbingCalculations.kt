package com.example.plumbingpro.util

import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

object PlumbingCalculations {

    data class VolumeResult(
        val liters: Double,
        val gallons: Double,
        val cubicMeters: Double,
        val weightKg: Double,
        val weightLbs: Double
    )

    fun calculatePipeVolumeMetric(diameterMm: Double, lengthM: Double): VolumeResult {
        val radiusM = (diameterMm / 1000.0) / 2.0
        val cubicMeters = PI * radiusM.pow(2) * lengthM
        val liters = cubicMeters * 1000.0
        val gallons = liters * 0.264172
        val weightKg = liters * 1.0
        val weightLbs = weightKg * 2.20462
        return VolumeResult(liters, gallons, cubicMeters, weightKg, weightLbs)
    }

    fun calculatePipeVolumeImperial(diameterInches: Double, lengthFeet: Double): VolumeResult {
        val radiusFeet = (diameterInches / 12.0) / 2.0
        val cubicFeet = PI * radiusFeet.pow(2) * lengthFeet
        val gallons = cubicFeet * 7.48052
        val liters = gallons / 0.264172
        val cubicMeters = liters / 1000.0
        val weightLbs = gallons * 8.34
        val weightKg = weightLbs / 2.20462
        return VolumeResult(liters, gallons, cubicMeters, weightKg, weightLbs)
    }

    data class FlowResult(
        val flowLpm: Double,
        val flowGpm: Double,
        val velocityMs: Double,
        val velocityFts: Double,
        val statusResId: Int
    )

    fun calculateFlowFromVelocity(diameterMm: Double, velocityMs: Double, statusOptimalResId: Int, statusHighResId: Int, statusLowResId: Int): FlowResult {
        val radiusM = (diameterMm / 1000.0) / 2.0
        val areaM2 = PI * radiusM.pow(2)
        val flowM3s = areaM2 * velocityMs
        val flowLpm = flowM3s * 60000.0
        val flowGpm = flowLpm * 0.264172
        val velocityFts = velocityMs * 3.28084

        val status = when {
            velocityMs > 2.4 -> statusHighResId
            velocityMs < 0.6 -> statusLowResId
            else -> statusOptimalResId
        }

        return FlowResult(flowLpm, flowGpm, velocityMs, velocityFts, status)
    }

    fun calculateVelocityFromFlow(diameterMm: Double, flowLpm: Double, statusOptimalResId: Int, statusHighResId: Int, statusLowResId: Int): FlowResult {
        val radiusM = (diameterMm / 1000.0) / 2.0
        val areaM2 = PI * radiusM.pow(2)
        val flowM3s = flowLpm / 60000.0
        val velocityMs = if (areaM2 > 0) flowM3s / areaM2 else 0.0
        val flowGpm = flowLpm * 0.264172
        val velocityFts = velocityMs * 3.28084

        val status = when {
            velocityMs > 2.4 -> statusHighResId
            velocityMs < 0.6 -> statusLowResId
            else -> statusOptimalResId
        }

        return FlowResult(flowLpm, flowGpm, velocityMs, velocityFts, status)
    }

    data class PipeSizingResult(
        val calculatedDiameterMm: Double,
        val calculatedDiameterInches: Double,
        val recommendedSize: String
    )

    fun calculatePipeDiameterFromFlow(flowLpm: Double, designVelocityMs: Double): PipeSizingResult {
        if (flowLpm <= 0 || designVelocityMs <= 0) {
            return PipeSizingResult(0.0, 0.0, "-")
        }
        val flowM3s = flowLpm / 60000.0
        val areaM2 = flowM3s / designVelocityMs
        val diameterM = sqrt((4.0 * areaM2) / PI)
        val diameterMm = diameterM * 1000.0
        val diameterInches = diameterMm / 25.4

        val recSize = when {
            diameterMm <= 13.0 -> "1/2\" (DN 15 / 16 mm)"
            diameterMm <= 18.0 -> "3/4\" (DN 20 / 20 mm)"
            diameterMm <= 23.0 -> "1\" (DN 25 / 26 mm)"
            diameterMm <= 30.0 -> "1-1/4\" (DN 32 / 32 mm)"
            diameterMm <= 38.0 -> "1-1/2\" (DN 40 / 40 mm)"
            diameterMm <= 50.0 -> "2\" (DN 50 / 50 mm)"
            diameterMm <= 65.0 -> "2-1/2\" (DN 65 / 63 mm)"
            diameterMm <= 80.0 -> "3\" (DN 80 / 75 mm)"
            diameterMm <= 105.0 -> "4\" (DN 100 / 110 mm)"
            else -> "5\"+ (DN 125+)"
        }

        return PipeSizingResult(diameterMm, diameterInches, recSize)
    }

    data class SlopeResult(
        val fallCm: Double,
        val fallMm: Double,
        val fallInches: Double
    )

    fun calculateSlope(runDistanceM: Double, slopePercent: Double): SlopeResult {
        val fallM = runDistanceM * (slopePercent / 100.0)
        val fallCm = fallM * 100.0
        val fallMm = fallM * 1000.0
        val fallInches = fallM * 39.3701
        return SlopeResult(fallCm, fallMm, fallInches)
    }

    data class PressureDropResult(
        val lossBar: Double,
        val lossPsi: Double,
        val headLossMeters: Double
    )

    fun calculatePressureDrop(
        diameterMm: Double,
        flowLpm: Double,
        lengthM: Double,
        cFactor: Double
    ): PressureDropResult {
        if (diameterMm <= 0 || flowLpm <= 0 || lengthM <= 0 || cFactor <= 0) {
            return PressureDropResult(0.0, 0.0, 0.0)
        }
        val pBarPerM = 6.05 * 10.0.pow(5) * flowLpm.pow(1.852) / (cFactor.pow(1.852) * diameterMm.pow(4.87))
        val lossBar = pBarPerM * lengthM
        val lossPsi = lossBar * 14.5038
        val headLossMeters = lossBar * 10.197
        return PressureDropResult(lossBar, lossPsi, headLossMeters)
    }

    data class ThermalExpansionResult(
        val expansionMm: Double,
        val expansionInches: Double,
        val tempDelta: Double,
        val loopArmM: Double
    )

    fun getMaterialAlpha(materialIndex: Int): Double {
        return when (materialIndex) {
            0 -> 150e-6 // PEX
            1 -> 16.5e-6 // Copper
            2 -> 70e-6  // PVC
            3 -> 12e-6  // Steel
            else -> 16.5e-6
        }
    }

    fun calculateThermalExpansion(
        materialAlpha: Double,
        lengthM: Double,
        tempInitialC: Double,
        tempFinalC: Double,
        outerDiameterMm: Double = 25.0
    ): ThermalExpansionResult {
        val tempDelta = tempFinalC - tempInitialC
        val expansionM = lengthM * materialAlpha * tempDelta
        val expansionMm = expansionM * 1000.0
        val expansionInches = expansionMm * 0.0393701

        val loopArmM = if (expansionMm > 0) {
            33.0 * sqrt((outerDiameterMm / 1000.0) * (expansionMm / 1000.0))
        } else {
            0.0
        }

        return ThermalExpansionResult(expansionMm, expansionInches, tempDelta, loopArmM)
    }

    data class TankResult(
        val liters: Double,
        val gallons: Double,
        val cubicMeters: Double,
        val weightKg: Double
    )

    fun calculateVertCylinder(diameterMm: Double, heightM: Double): TankResult {
        val rM = (diameterMm / 1000.0) / 2.0
        val m3 = PI * rM.pow(2) * heightM
        val liters = m3 * 1000.0
        return TankResult(liters, liters * 0.264172, m3, liters)
    }

    fun calculateHorizCylinder(diameterMm: Double, lengthM: Double): TankResult {
        val rM = (diameterMm / 1000.0) / 2.0
        val m3 = PI * rM.pow(2) * lengthM
        val liters = m3 * 1000.0
        return TankResult(liters, liters * 0.264172, m3, liters)
    }

    fun calculateRectangularTank(lengthM: Double, widthM: Double, heightM: Double): TankResult {
        val m3 = lengthM * widthM * heightM
        val liters = m3 * 1000.0
        return TankResult(liters, liters * 0.264172, m3, liters)
    }

    data class FixtureUnitsResult(
        val totalDfu: Int,
        val totalWsfu: Double,
        val minDrainPipeSize: String,
        val peakDemandLpm: Double,
        val peakDemandGpm: Double
    )

    fun calculateFixtureUnits(
        toilets: Int,
        showers: Int,
        bathtubs: Int,
        lavatories: Int,
        kitchenSinks: Int,
        dishwashers: Int,
        washingMachines: Int
    ): FixtureUnitsResult {
        val totalDfu = toilets * 4 + showers * 2 + bathtubs * 2 + lavatories * 1 +
                kitchenSinks * 2 + dishwashers * 2 + washingMachines * 2

        val totalWsfu = toilets * 3.0 + showers * 2.0 + bathtubs * 2.0 + lavatories * 1.0 +
                kitchenSinks * 2.0 + dishwashers * 1.5 + washingMachines * 2.0

        val minDrainSize = when {
            totalDfu <= 3 -> "1.5\" (40 mm)"
            totalDfu <= 6 -> "2\" (50 mm)"
            totalDfu <= 20 -> "3\" (75 mm)"
            else -> "4\" (100 mm)"
        }

        val peakGpm = if (totalWsfu > 0) 4.0 * totalWsfu.pow(0.65) else 0.0
        val peakLpm = peakGpm / 0.264172

        return FixtureUnitsResult(totalDfu, totalWsfu, minDrainSize, peakLpm, peakGpm)
    }
}
