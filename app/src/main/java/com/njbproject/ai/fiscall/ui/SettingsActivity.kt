package com.njbproject.ai.fiscall.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.njbproject.ai.fiscall.databinding.ActivitySettingsBinding
import com.njbproject.ai.fiscall.utils.Constants
import com.njbproject.ai.fiscall.utils.PreferenceManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setupUI()
    }

    private fun setupUI() {
        val isUserKey = PreferenceManager.isUserApiKey(this)
        binding.rbUser.isChecked = isUserKey
        binding.rbBuiltIn.isChecked = !isUserKey
        binding.etApiKey.isEnabled = isUserKey
        binding.etApiKey.setText(PreferenceManager.getUserApiKey(this))

        binding.rgApiKey.setOnCheckedChangeListener { _, checkedId ->
            binding.etApiKey.isEnabled = (checkedId == binding.rbUser.id)
        }

        // Model Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Constants.AVAILABLE_MODELS)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerModel.adapter = adapter

        val currentModel = PreferenceManager.getModelName(this)
        val spinnerPosition = adapter.getPosition(currentModel)
        if (spinnerPosition >= 0) {
            binding.spinnerModel.setSelection(spinnerPosition)
        }

        // System Prompt
        binding.etSystemPrompt.setText(PreferenceManager.getSystemPrompt(this))

        binding.btnSave.setOnClickListener {
            saveSettings()
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
            Toast.makeText(this, "Riwayat chat dihapus", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveSettings() {
        val isUserKey = binding.rbUser.isChecked
        PreferenceManager.saveApiKeyType(this, isUserKey)

        if (isUserKey) {
            val key = binding.etApiKey.text.toString().trim()
            if (key.isNotEmpty()) {
                PreferenceManager.saveUserApiKey(this, key)
            }
        }

        val selectedModel = binding.spinnerModel.selectedItem.toString()
        PreferenceManager.saveModelName(this, selectedModel)

        val prompt = binding.etSystemPrompt.text.toString().trim()
        if (prompt.isNotEmpty()) {
            PreferenceManager.saveSystemPrompt(this, prompt)
        }

        Toast.makeText(this, "Pengaturan disimpan", Toast.LENGTH_SHORT).show()
        finish()
    }
}
