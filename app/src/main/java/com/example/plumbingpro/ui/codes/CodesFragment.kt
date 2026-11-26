package com.example.plumbingpro.ui.codes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plumbingpro.databinding.FragmentCodesBinding

class CodesFragment : Fragment() {

    private var _binding: FragmentCodesBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val URL_IPC = "https://codes.iccsafe.org/content/IPC2021P1"
        private const val URL_UPC = "https://www.iapmo.org/"
        private const val URL_NPC = "https://nrc.canada.ca/en/certifications-evaluations-standards/codes-canada/codes-canada-publications/national-plumbing-code-canada-2020"
        private const val URL_CSA = "https://www.csagroup.org/"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCodesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOpenIpc.setOnClickListener { openUrl(URL_IPC) }
        binding.btnOpenUpc.setOnClickListener { openUrl(URL_UPC) }
        binding.btnOpenNpc.setOnClickListener { openUrl(URL_NPC) }
        binding.btnOpenCsa.setOnClickListener { openUrl(URL_CSA) }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (_: Exception) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
