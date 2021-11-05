package com.bps.gotwinter2021.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.bps.gotwinter2021.R
import com.bps.gotwinter2021.common.createViewModel
import com.bps.gotwinter2021.common.secret.API.API_KEY
import com.bps.gotwinter2021.data.network.repo.GOTRepo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import timber.log.Timber

class MapsFragment : Fragment() {

    private val REQUEST_LOCATION_PERMISSION = 1

    private lateinit var googleMap: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var lastLocation: Location

    private lateinit var currentLatLng: LatLng

    private lateinit var placesClient: PlacesClient

    private lateinit var gotIcon: Bitmap

    private lateinit var gotSelectIcon: Bitmap

    private var lastMarker: Marker? = null

    private val viewModel: MapsViewModel by lazy {
        createViewModel {
            MapsViewModel(
                application = this.requireActivity().application,
                GOTRepo.provideGOTRepo()
            )
        }
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            if (isGranted) { // Do something if permission granted
                Timber.d("Permission granted by the user")
                enableMyLocation()
            }
            else { // Do something as the permission is not granted
                Timber.d("Permission denied by the user")
                view?.findNavController()?.navigate(
                    MapsFragmentDirections.actionMapsFragmentToHomeScreenFragment()
                )
            }
        }

    private val callback = OnMapReadyCallback {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        googleMap = it

        googleMap.uiSettings.isZoomControlsEnabled = true

        setMapStyle(googleMap)

        googleMap.setOnMarkerClickListener { marker ->
            onMarkerClick(marker)
        }

        enableMyLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bitmap = BitmapFactory.decodeResource(
            context?.resources, R.drawable.got_theater_icon)

        val bitmap2 = BitmapFactory.decodeResource(
            context?.resources, R.drawable.got_theater_select_icon)

        gotIcon = Bitmap.createScaledBitmap(
            bitmap, 100, 100, true
        )

        gotSelectIcon = Bitmap.createScaledBitmap(
            bitmap2, 120, 120, true
        )

        fusedLocationClient = LocationServices
            .getFusedLocationProviderClient(this.requireActivity())

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        else{
            Timber.d("Permissions already granted.")
        }

        Places.initialize(this.requireContext(), API_KEY)

        placesClient = Places.createClient(this.requireContext())

        viewModel.theaterFeed.observe(viewLifecycleOwner, {
            it?.results?.forEach { theater ->
                val theaterLatLng = LatLng(
                    theater.geometry.location.lat, theater.geometry.location.lng)
                val snippet = getAddress(theaterLatLng)

                val theaterMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(theaterLatLng)
                        .title(theater.name)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.fromBitmap(gotIcon))
                )
                if (theaterMarker != null) {
                    Timber.d("New marker has been made. " +
                            "${theaterMarker.title} is located in ${theaterMarker.position}")
                    theaterMarker.tag = false
                }
            }
        })

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun getAddress(lat: LatLng): String? {
        val geocoder = Geocoder(this.requireContext())
        val list = geocoder.getFromLocation(lat.latitude, lat.longitude,1)
        return list[0].getAddressLine(0)
    }

    private fun onMarkerClick(marker: Marker): Boolean {
        val clickCount = marker.tag as? Boolean

        val distanceLocation = Location("")

        distanceLocation.longitude = marker.position.longitude
        distanceLocation.latitude = marker.position.latitude

        val distance = lastLocation.distanceTo(distanceLocation) / 1000
        Timber.d("Distance is: " + distance)


        if (clickCount == false) {
            Timber.d("This marker coordinates: " + marker.snippet)

            lastMarker?.tag = false
            lastMarker?.setIcon(BitmapDescriptorFactory.fromBitmap(gotIcon))
            lastMarker = marker
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(gotSelectIcon))
            marker.tag = true
        }
        return true
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Timber.d("Case test")
            }
            googleMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) { location ->

                if (location != null) {
                    lastLocation = location
                    currentLatLng = LatLng(location.latitude, location.longitude)
                    val locationString = "${location.latitude},${location.longitude}"
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                    viewModel.getStores(locationString, 100000, "movie_theater")
                    Timber.d("Made the map")

                }
            }
        }
        else {
            Timber.d("Sad, no permissions")
        }
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this.requireContext(),
                    R.raw.map_style
                )
            )
            if (!success) {
                Timber.d("Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Timber.d("Cant find style. Error: ", e)
        }
    }
}
