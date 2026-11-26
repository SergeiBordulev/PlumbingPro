package com.example.plumbingpro

import com.example.plumbingpro.util.PlumbingCalculations
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlumbingCalculationsTest {

    @Test
    fun testPipeVolumeMetric() {
        // Diameter 50mm (0.05m), Length 10m
        // Radius = 0.025m -> Area = PI * 0.025^2 = 0.001963495 m^2
        // Volume = 0.01963495 m^3 = 19.635 Liters
        val result = PlumbingCalculations.calculatePipeVolumeMetric(50.0, 10.0)
        assertEquals(19.635, result.liters, 0.1)
        assertEquals(5.187, result.gallons, 0.1)
        assertEquals(0.0196, result.cubicMeters, 0.001)
        assertEquals(19.635, result.weightKg, 0.1)
    }

    @Test
    fun testPipeVolumeImperial() {
        // Diameter 2 inches, Length 20 feet
        val result = PlumbingCalculations.calculatePipeVolumeImperial(2.0, 20.0)
        assertTrue(result.gallons > 0)
        assertTrue(result.liters > 0)
        assertTrue(result.weightLbs > 0)
    }

    @Test
    fun testPipeSlope() {
        // Distance 10m, Slope 2% -> Fall = 0.2m = 20cm
        val result = PlumbingCalculations.calculateSlope(10.0, 2.0)
        assertEquals(20.0, result.fallCm, 0.01)
        assertEquals(200.0, result.fallMm, 0.1)
        assertEquals(7.874, result.fallInches, 0.1)
    }

    @Test
    fun testPressureDrop() {
        // Diameter 25mm, Flow 30 L/min, Length 50m, PEX (C=150)
        val result = PlumbingCalculations.calculatePressureDrop(25.0, 30.0, 50.0, 150.0)
        assertTrue(result.lossBar > 0)
        assertTrue(result.lossPsi > 0)
        assertTrue(result.headLossMeters > 0)
    }

    @Test
    fun testThermalExpansion() {
        // Copper alpha = 16.5e-6, Length = 20m, Temp delta = 50°C
        val alpha = PlumbingCalculations.getMaterialAlpha(1)
        val result = PlumbingCalculations.calculateThermalExpansion(alpha, 20.0, 10.0, 60.0)
        // Delta L = 20 * 16.5e-6 * 50 = 0.0165m = 16.5mm
        assertEquals(16.5, result.expansionMm, 0.1)
        assertEquals(50.0, result.tempDelta, 0.01)
    }

    @Test
    fun testTankVolume() {
        // Vertical Cylinder: Diameter 1000mm (1m), Height 2m
        // Volume = PI * 0.5^2 * 2 = 1.57079 m^3 = 1570.8 Liters
        val result = PlumbingCalculations.calculateVertCylinder(1000.0, 2.0)
        assertEquals(1570.8, result.liters, 1.0)
        assertEquals(1.57, result.cubicMeters, 0.01)

        // Rectangular: L=2m, W=1m, H=1.5m -> 3 m^3 = 3000 Liters
        val rectRes = PlumbingCalculations.calculateRectangularTank(2.0, 1.0, 1.5)
        assertEquals(3000.0, rectRes.liters, 0.1)
    }

    @Test
    fun testFixtureUnits() {
        // 2 toilets, 2 showers, 2 sinks
        val result = PlumbingCalculations.calculateFixtureUnits(
            toilets = 2,
            showers = 2,
            bathtubs = 0,
            lavatories = 2,
            kitchenSinks = 1,
            dishwashers = 0,
            washingMachines = 1
        )

        // DFU: 2*4 + 2*2 + 2*1 + 1*2 + 1*2 = 8 + 4 + 2 + 2 + 2 = 18 DFU
        assertEquals(18, result.totalDfu)
        assertTrue(result.totalWsfu > 0)
        assertEquals("3\" (75 mm)", result.minDrainPipeSize)
    }

    @Test
    fun testPipeSizing() {
        // Flow = 30 L/min (0.0005 m^3/s), Velocity = 1.5 m/s
        // Area = 0.0005 / 1.5 = 0.0003333 m^2
        // d = sqrt(4 * 0.0003333 / PI) = 0.0206m = 20.6mm -> ~3/4" (DN 20 / 20mm)
        val result = PlumbingCalculations.calculatePipeDiameterFromFlow(30.0, 1.5)
        assertEquals(20.6, result.calculatedDiameterMm, 0.2)
        assertEquals("1\" (DN 25 / 26 mm)", result.recommendedSize)
    }
}
