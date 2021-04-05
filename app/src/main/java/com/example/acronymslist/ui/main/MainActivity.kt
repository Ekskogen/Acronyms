package com.example.acronymslist.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.acronymslist.R
import com.example.acronymslist.data.network.NoInternetException
import com.example.acronymslist.databinding.ActivityMainBinding
import com.example.acronymslist.ui.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel : MainViewModel by viewModel()

    lateinit var binding: ActivityMainBinding

    private val adapter by lazy {
        AcronymsAdapter(arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
    }

    private fun initializeViews() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            hasFixedSize()
            adapter = this@MainActivity.adapter
        }

        binding.searchBtn.setOnClickListener {
            searchWord()
        }

        binding.searchET.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchWord()
            }
            true
        }

        viewModel.loading.observe(this) {
            when(it) {
                true -> {
                    progressBar.visibility =View.VISIBLE
                    Log.d("TAG", "Loading")
                }
                false -> {
                    progressBar.visibility = View.GONE
                    Log.d("TAG", "No Loading")
                }
            }
        }
        viewModel.state.observe(this) {
            when(it) {
                is State.Done -> {
                    if(it.acronyms.isNullOrEmpty()) {
                        binding.warningTextView.visibility = View.VISIBLE
                        binding.warningTextView.text = getString(R.string.warning_empty_acronyms)
                    } else {
                        binding.warningTextView.visibility = View.GONE
                        adapter.setData(it.acronyms)
                    }
                }
                is State.Error -> {
                    val t = if(it.e is NoInternetException)
                        getString(R.string.error_loading_no_internet)
                    else
                        getString(R.string.error_loading_acronyms)
                    binding.warningTextView.text = t
                    binding.warningTextView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun searchWord() {
        hideKeyboard()
        val text = binding.searchET.text.toString()
        if(text.isBlank())
            Toast.makeText(this, getString(R.string.error_input_acronyms), Toast.LENGTH_LONG).show()
        else {
            viewModel.loadAcronyms(text)
        }
    }

}