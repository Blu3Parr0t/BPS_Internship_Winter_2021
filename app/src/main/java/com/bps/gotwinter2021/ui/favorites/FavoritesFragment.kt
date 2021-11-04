package com.bps.gotwinter2021.ui.favorites

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bps.gotwinter2021.databinding.FavoritesFragmentBinding
import jp.wasabeef.blurry.Blurry

class FavoritesFragment : Fragment() {
    private val viewModel: FavoritesViewModel by lazy{
        ViewModelProvider(this).get(FavoritesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FavoritesFragmentBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        Blurry.with(requireContext())
            .radius(10)
            .sampling(8)
            .color(Color.argb(66, 255, 255, 0))
            .async()
            .animate(500)
            .onto(binding.wholeThing);

        return binding.root
    }
}
