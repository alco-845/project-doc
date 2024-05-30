package com.example.storyapp.ui.maps

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.helper.PrefData
import com.example.storyapp.ui.ViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapsViewModel: MapsViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        supportActionBar?.title = "Story Maps"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        addMapMarker()
    }

    private fun addMapMarker() {
        mapsViewModel.getStoryMaps(PrefData.token, 1)
        mapsViewModel.storyMaps.observe(this) { listStory ->
            listStory.forEach{ story ->
                val latLng = LatLng(story.lat, story.lon)
                mMap.addMarker(MarkerOptions().position(latLng).title(story.name))
            }
        }
        mapsViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mapsViewModel.message.observe(this ) {
            Toast.makeText(this@MapsActivity, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbMaps.visibility = View.VISIBLE else binding.pbMaps.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}