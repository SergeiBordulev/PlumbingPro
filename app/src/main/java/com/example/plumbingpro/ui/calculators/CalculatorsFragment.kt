package com.example.plumbingpro.ui.calculators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.plumbingpro.R
import com.example.plumbingpro.databinding.FragmentCalculatorsBinding
import com.example.plumbingpro.util.PreferencesManager
import com.example.plumbingpro.util.PlumbingCalculations
import java.util.Locale

class CalculatorsFragment : Fragment() {

    private var _binding: FragmentCalculatorsBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PreferencesManager(requireContext())

        setupChipSelector()
        setupSpinners()
        setupCalculators()
        updateUnitHints()
    }

    private fun setupChipSelector() {
        binding.chipGroupCalculators.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
            val id = checkedIds[0]

            binding.containerPipeVolume.visibility = if (id == R.id.chipPipeVolume) View.VISIBLE else View.GONE
            binding.containerFlowRate.visibility = if (id == R.id.chipFlowRate) View.VISIBLE else View.GONE
            binding.containerPipeSizing.visibility = if (id == R.id.chipPipeSizing) View.VISIBLE else View.GONE
            binding.containerPipeSlope.visibility = if (id == R.id.chipPipeSlope) View.VISIBLE else View.GONE
            binding.containerPressureDrop.visibility = if (id == R.id.chipPressureDrop) View.VISIBLE else View.GONE
            binding.containerThermalExpansion.visibility = if (id == R.id.chipThermalExpansion) View.VISIBLE else View.GONE
            binding.containerTankVolume.visibility = if (id == R.id.chipTankVolume) View.VISIBLE else View.GONE
            binding.containerFixtureUnits.visibility = if (id == R.id.chipFixtureUnits) View.VISIBLE else View.GONE
        }
    }

    private fun updateUnitHints() {
        val isImperial = prefs.unitSystem == PreferencesManager.UNIT_IMPERIAL

        if (isImperial) {
            binding.inputLayoutVolumeDiameter.hint = "${getString(R.string.label_pipe_diameter)} (inch)"
            binding.inputLayoutVolumeLength.hint = "${getString(R.string.label_pipe_length)} (ft)"

            binding.inputLayoutFlowDiameter.hint = "${getString(R.string.label_pipe_diameter)} (inch)"
            binding.inputLayoutFlowVelocity.hint = "${getString(R.string.label_velocity)} (ft/s)"

            binding.inputLayoutSizingFlow.hint = "${getString(R.string.label_flow_rate)} (GPM)"
            binding.inputLayoutSizingVelocity.hint = "${getString(R.string.label_design_velocity)} (ft/s)"

            binding.inputLayoutSlopeLength.hint = "${getString(R.string.label_run_distance)} (ft)"

            binding.inputLayoutPressureDiameter.hint = "${getString(R.string.label_pipe_diameter)} (inch)"
            binding.inputLayoutPressureFlow.hint = "${getString(R.string.label_flow_rate)} (GPM)"
            binding.inputLayoutPressureLength.hint = "${getString(R.string.label_pipe_length)} (ft)"

            binding.inputLayoutThermalLength.hint = "${getString(R.string.label_pipe_length)} (ft)"
        } else {
            binding.inputLayoutVolumeDiameter.hint = "${getString(R.string.label_pipe_diameter)} (mm)"
            binding.inputLayoutVolumeLength.hint = "${getString(R.string.label_pipe_length)} (m)"

            binding.inputLayoutFlowDiameter.hint = "${getString(R.string.label_pipe_diameter)} (mm)"
            binding.inputLayoutFlowVelocity.hint = "${getString(R.string.label_velocity)} (m/s)"

            binding.inputLayoutSizingFlow.hint = "${getString(R.string.label_flow_rate)} (L/min)"
            binding.inputLayoutSizingVelocity.hint = "${getString(R.string.label_design_velocity)} (m/s)"

            binding.inputLayoutSlopeLength.hint = "${getString(R.string.label_run_distance)} (m)"

            binding.inputLayoutPressureDiameter.hint = "${getString(R.string.label_pipe_diameter)} (mm)"
            binding.inputLayoutPressureFlow.hint = "${getString(R.string.label_flow_rate)} (L/min)"
            binding.inputLayoutPressureLength.hint = "${getString(R.string.label_pipe_length)} (m)"

            binding.inputLayoutThermalLength.hint = "${getString(R.string.label_pipe_length)} (m)"
        }
    }

    private fun setupSpinners() {
        val materials = arrayOf(
            getString(R.string.mat_pex),
            getString(R.string.mat_copper),
            getString(R.string.mat_pvc),
            getString(R.string.mat_steel)
        )

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, materials)
        binding.spinnerPressureMaterial.adapter = spinnerAdapter
        binding.spinnerThermalMaterial.adapter = spinnerAdapter

        val shapes = arrayOf(
            getString(R.string.shape_vert_cylinder),
            getString(R.string.shape_horiz_cylinder),
            getString(R.string.shape_rectangular)
        )
        binding.spinnerTankShape.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, shapes)

        binding.spinnerTankShape.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val isImperial = prefs.unitSystem == PreferencesManager.UNIT_IMPERIAL
                val unitLen = if (isImperial) "ft" else "m"
                val unitDiam = if (isImperial) "inch" else "mm"

                when (position) {
                    0 -> { // Vertical Cylinder
                        binding.inputLayoutTankDim1.hint = "${getString(R.string.label_diameter)} ($unitDiam)"
                        binding.inputLayoutTankDim2.hint = "${getString(R.string.label_height)} ($unitLen)"
                        binding.inputLayoutTankDim3.visibility = View.GONE
                    }
                    1 -> { // Horizontal Cylinder
                        binding.inputLayoutTankDim1.hint = "${getString(R.string.label_diameter)} ($unitDiam)"
                        binding.inputLayoutTankDim2.hint = "${getString(R.string.label_pipe_length)} ($unitLen)"
                        binding.inputLayoutTankDim3.visibility = View.GONE
                    }
                    2 -> { // Rectangular
                        binding.inputLayoutTankDim1.hint = "${getString(R.string.label_pipe_length)} ($unitLen)"
                        binding.inputLayoutTankDim2.hint = "${getString(R.string.label_width)} ($unitLen)"
                        binding.inputLayoutTankDim3.hint = "${getString(R.string.label_height)} ($unitLen)"
                        binding.inputLayoutTankDim3.visibility = View.VISIBLE
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupCalculators() {
        // 1. Pipe Volume
        binding.btnCalculateVolume.setOnClickListener {
            val d = binding.editVolumeDiameter.text.toString().toDoubleOrNull() ?: 0.0
            val l = binding.editVolumeLength.text.toString().toDoubleOrNull() ?: 0.0

            if (d > 0 && l > 0) {
                val res = if (prefs.unitSystem == PreferencesManager.UNIT_IMPERIAL) {
                    PlumbingCalculations.calculatePipeVolumeImperial(d, l)
                } else {
                    PlumbingCalculations.calculatePipeVolumeMetric(d, l)
                }

                binding.txtVolumeLiters.text = String.format(Locale.getDefault(), "%s: %.2f L", getString(R.string.result_volume_liters), res.liters)
                binding.txtVolumeGallons.text = String.format(Locale.getDefault(), "%s: %.2f gal", getString(R.string.result_volume_gallons), res.gallons)
                binding.txtVolumeM3.text = String.format(Locale.getDefault(), "%s: %.4f m³", getString(R.string.result_volume_m3), res.cubicMeters)
                binding.txtWaterWeight.text = String.format(Locale.getDefault(), "%s: %.2f kg / %.2f lbs", getString(R.string.result_water_weight), res.weightKg, res.weightLbs)
                binding.cardVolumeResult.visibility = View.VISIBLE
            }
        }

        // 2. Flow Rate & Velocity
        binding.btnCalculateFlow.setOnClickListener {
            val d = binding.editFlowDiameter.text.toString().toDoubleOrNull() ?: 0.0
            val vInput = binding.editFlowVelocity.text.toString().toDoubleOrNull() ?: 0.0

            val isImperial = prefs.unitSystem == PreferencesManager.UNIT_IMPERIAL
            val dMm = if (isImperial) d * 25.4 else d
            val vMs = if (isImperial) vInput / 3.28084 else vInput

            if (dMm > 0 && vMs > 0) {
                val res = PlumbingCalculations.calculateFlowFromVelocity(
                    dMm, vMs,
                    R.string.velocity_status_optimal,
                    R.string.velocity_status_high,
                    R.string.velocity_status_low
                )

                binding.txtFlowLpm.text = String.format(Locale.getDefault(), "%s: %.2f L/min", getString(R.string.result_flow_lpm), res.flowLpm)
                binding.txtFlowGpm.text = String.format(Locale.getDefault(), "%s: %.2f GPM", getString(R.string.result_flow_gpm), res.flowGpm)
                binding.txtFlowVelocityResult.text = String.format(Locale.getDefault(), "%s: %.2f m/s (%.2f ft/s)", getString(R.string.result_velocity), res.velocityMs, res.velocityFts)

                binding.txtVelocityStatus.setText(res.statusResId)
                val statusColor = when (res.statusResId) {
                    R.string.velocity_status_optimal -> requireContext().getColor(R.color.status_green)
                    else -> requireContext().getColor(R.color.status_orange)
                }
                binding.txtVelocityStatus.setTextColor(statusColor)

                binding.cardFlowResult.visibility = View.VISIBLE
            }
        }

        // Pipe Sizing
        binding.btnCalculateSizing.setOnClickListener {
            val flowInput = binding.editSizingFlow.text.toString().toDoubleOrNull() ?: 0.0
            val velInput = binding.editSizingVelocity.text.toString().toDoubleOrNull() ?: 1.5

            val isImperial = prefs.unitSystem == PreferencesManager.UNIT_IMPERIAL
            val flowLpm = if (isImperial) flowInput / 0.264172 else flowInput
            val velMs = if (isImperial) velInput / 3.28084 else velInput

            if (flowLpm > 0 && velMs > 0) {
                val res = PlumbingCalculations.calculatePipeDiameterFromFlow(flowLpm, velMs)
                binding.txtSizingCalcDiameter.text = String.format(
                    Locale.getDefault(),
                    "%s: %.1f mm (%.3f in)",
                    getString(R.string.result_calc_diameter),
                    res.calculatedDiameterMm,
                    res.calculatedDiameterInches
                )
                binding.txtSizingRecSize.text = String.format(
                    Locale.getDefault(),
                    "%s: %s",
                    getString(R.string.result_recommended_pipe_size),
                    res.recommendedSize
                )
                binding.cardSizingResult.visibility = View.VISIBLE
            }
        }

        // 3. Pipe Slope
        binding.btnCalculateSlope.setOnClickListener {
            val length = binding.editSlopeLength.text.toString().toDoubleOrNull() ?: 0.0
            val percent = binding.editSlopePercent.text.toString().toDoubleOrNull() ?: 0.0

            if (length > 0 && percent > 0) {
                val isImperial = prefs.unitSystem == PreferencesManager.UNIT_IMPERIAL
                val lengthM = if (isImperial) length / 3.28084 else length

                val res = PlumbingCalculations.calculateSlope(lengthM, percent)

                binding.txtSlopeFallCm.text = String.format(Locale.getDefault(), "%s: %.2f cm (%.1f mm)", getString(R.string.result_total_fall), res.fallCm, res.fallMm)
                binding.txtSlopeFallInches.text = String.format(Locale.getDefault(), "%s: %.2f inches", getString(R.string.result_total_fall), res.fallInches)
                binding.cardSlopeResult.visibility = View.VISIBLE
            }
        }

        // 4. Pressure Drop
        binding.btnCalculatePressure.setOnClickListener {
            val d = binding.editPressureDiameter.text.toString().toDoubleOrNull() ?: 0.0
            val flow = binding.editPressureFlow.text.toString().toDoubleOrNull() ?: 0.0
            val length = binding.editPressureLength.text.toString().toDoubleOrNull() ?: 0.0
            val materialIndex = binding.spinnerPressureMaterial.selectedItemPosition

            val isImperial = prefs.unitSystem == PreferencesManager.UNIT_IMPERIAL
            val dMm = if (isImperial) d * 25.4 else d
            val flowLpm = if (isImperial) flow / 0.264172 else flow
            val lengthM = if (isImperial) length / 3.28084 else length

            val cFactor = when (materialIndex) {
                0 -> 150.0 // PEX
                1 -> 140.0 // Copper
                2 -> 150.0 // PVC
                3 -> 120.0 // Steel
                else -> 140.0
            }

            if (dMm > 0 && flowLpm > 0 && lengthM > 0) {
                val res = PlumbingCalculations.calculatePressureDrop(dMm, flowLpm, lengthM, cFactor)

                binding.txtPressureLossBar.text = String.format(Locale.getDefault(), "%s: %.3f bar", getString(R.string.result_pressure_loss_bar), res.lossBar)
                binding.txtPressureLossPsi.text = String.format(Locale.getDefault(), "%s: %.2f PSI", getString(R.string.result_pressure_loss_psi), res.lossPsi)
                binding.txtHeadLossMeters.text = String.format(Locale.getDefault(), "%s: %.2f m H₂O", getString(R.string.result_head_loss), res.headLossMeters)
                binding.cardPressureResult.visibility = View.VISIBLE
            }
        }

        // 5. Thermal Expansion
        binding.btnCalculateThermal.setOnClickListener {
            val materialIndex = binding.spinnerThermalMaterial.selectedItemPosition
            val length = binding.editThermalLength.text.toString().toDoubleOrNull() ?: 0.0
            val t1 = binding.editTempInitial.text.toString().toDoubleOrNull() ?: 0.0
            val t2 = binding.editTempFinal.text.toString().toDoubleOrNull() ?: 0.0

            val isImperial = prefs.unitSystem == PreferencesManager.UNIT_IMPERIAL
            val lengthM = if (isImperial) length / 3.28084 else length
            val t1C = if (isImperial) (t1 - 32) * 5 / 9 else t1
            val t2C = if (isImperial) (t2 - 32) * 5 / 9 else t2

            if (lengthM > 0) {
                val alpha = PlumbingCalculations.getMaterialAlpha(materialIndex)
                val res = PlumbingCalculations.calculateThermalExpansion(alpha, lengthM, t1C, t2C)

                binding.txtThermalDelta.text = String.format(Locale.getDefault(), "%s: %.1f °C (%.1f °F)", getString(R.string.label_temp_delta), res.tempDelta, res.tempDelta * 9 / 5)
                binding.txtThermalExpansionMm.text = String.format(Locale.getDefault(), "%s: %.2f mm", getString(R.string.result_expansion_length), res.expansionMm)
                binding.txtThermalExpansionInches.text = String.format(Locale.getDefault(), "%s: %.3f inches", getString(R.string.result_expansion_length), res.expansionInches)
                binding.txtThermalLoopArm.text = String.format(Locale.getDefault(), "%s %.2f m (%.2f ft)", getString(R.string.expansion_loop_note), res.loopArmM, res.loopArmM * 3.28084)
                binding.cardThermalResult.visibility = View.VISIBLE
            }
        }

        // 6. Tank Volume
        binding.btnCalculateTank.setOnClickListener {
            val shapePos = binding.spinnerTankShape.selectedItemPosition
            val dim1 = binding.editTankDim1.text.toString().toDoubleOrNull() ?: 0.0
            val dim2 = binding.editTankDim2.text.toString().toDoubleOrNull() ?: 0.0
            val dim3 = binding.editTankDim3.text.toString().toDoubleOrNull() ?: 0.0

            val isImperial = prefs.unitSystem == PreferencesManager.UNIT_IMPERIAL

            val res = when (shapePos) {
                0 -> { // Vertical Cylinder: dim1=diameter, dim2=height
                    val dMm = if (isImperial) dim1 * 25.4 else dim1
                    val hM = if (isImperial) dim2 / 3.28084 else dim2
                    if (dMm > 0 && hM > 0) PlumbingCalculations.calculateVertCylinder(dMm, hM) else null
                }
                1 -> { // Horizontal Cylinder: dim1=diameter, dim2=length
                    val dMm = if (isImperial) dim1 * 25.4 else dim1
                    val lM = if (isImperial) dim2 / 3.28084 else dim2
                    if (dMm > 0 && lM > 0) PlumbingCalculations.calculateHorizCylinder(dMm, lM) else null
                }
                2 -> { // Rectangular: dim1=length, dim2=width, dim3=height
                    val lM = if (isImperial) dim1 / 3.28084 else dim1
                    val wM = if (isImperial) dim2 / 3.28084 else dim2
                    val hM = if (isImperial) dim3 / 3.28084 else dim3
                    if (lM > 0 && wM > 0 && hM > 0) PlumbingCalculations.calculateRectangularTank(lM, wM, hM) else null
                }
                else -> null
            }

            res?.let {
                binding.txtTankLiters.text = String.format(Locale.getDefault(), "%s: %.1f L", getString(R.string.result_volume_liters), it.liters)
                binding.txtTankGallons.text = String.format(Locale.getDefault(), "%s: %.1f gal", getString(R.string.result_volume_gallons), it.gallons)
                binding.txtTankM3.text = String.format(Locale.getDefault(), "%s: %.3f m³", getString(R.string.result_volume_m3), it.cubicMeters)
                binding.txtTankWeight.text = String.format(Locale.getDefault(), "%s: %.1f kg (%.1f lbs)", getString(R.string.result_water_weight), it.weightKg, it.weightKg * 2.20462)
                binding.cardTankResult.visibility = View.VISIBLE
            }
        }

        // 7. Fixture Units
        binding.btnCalculateFixtures.setOnClickListener {
            val toilets = binding.editCountToilet.text.toString().toIntOrNull() ?: 0
            val showers = binding.editCountShower.text.toString().toIntOrNull() ?: 0
            val bathtubs = binding.editCountBathtub.text.toString().toIntOrNull() ?: 0
            val lavatories = binding.editCountLavatory.text.toString().toIntOrNull() ?: 0
            val kitchenSinks = binding.editCountKitchenSink.text.toString().toIntOrNull() ?: 0
            val dishwashers = binding.editCountDishwasher.text.toString().toIntOrNull() ?: 0
            val washingMachines = binding.editCountWashingMachine.text.toString().toIntOrNull() ?: 0

            val res = PlumbingCalculations.calculateFixtureUnits(
                toilets, showers, bathtubs, lavatories, kitchenSinks, dishwashers, washingMachines
            )

            binding.txtTotalDfu.text = String.format(Locale.getDefault(), "%s: %d DFU", getString(R.string.result_total_dfu), res.totalDfu)
            binding.txtTotalWsfu.text = String.format(Locale.getDefault(), "%s: %.1f WSFU", getString(R.string.result_total_wsfu), res.totalWsfu)
            binding.txtMinDrainSize.text = String.format(Locale.getDefault(), "%s: %s", getString(R.string.result_drain_size), res.minDrainPipeSize)
            binding.txtPeakDemand.text = String.format(Locale.getDefault(), "%s: %.1f L/min (%.1f GPM)", getString(R.string.result_peak_demand), res.peakDemandLpm, res.peakDemandGpm)
            binding.cardFixturesResult.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
