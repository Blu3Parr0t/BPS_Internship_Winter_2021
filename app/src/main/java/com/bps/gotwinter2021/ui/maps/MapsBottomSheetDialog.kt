package com.bps.gotwinter2021.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bps.gotwinter2021.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MapsBottomSheetDialog : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps_bottom_sheet_dialog, container, false)
    }
}
