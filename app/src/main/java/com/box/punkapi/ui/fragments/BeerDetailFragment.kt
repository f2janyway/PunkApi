package com.box.punkapi.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.lifecycle.lifecycleScope
import com.box.punkapi.R
import com.box.punkapi.databinding.FragmentBeerDetailBinding
import com.box.punkapi.model.Hops
import com.box.punkapi.model.Malt
import com.box.punkapi.ui.FoodPairingAdapter
import com.box.punkapi.ui.MainActivity
import com.box.punkapi.utils.setHtmlText
import com.box.punkapi.utils.toBold
import com.box.punkapi.utils.toH
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class BeerDetailFragment : Fragment() {

    private lateinit var binding: FragmentBeerDetailBinding
    private val selectedBeer by lazy {
        (requireActivity() as MainActivity).mainViewModel.selectedBeer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 600
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBeerDetailBinding.inflate(inflater, container, false)
        setBindBeer()

        setHorizontalView()
        setBeerMethodView()
        setIngredientView()
        setFoodPairingView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detailImageView.apply {
            transitionName = selectedBeer.id.toString()

            Glide.with(context)
                .load(selectedBeer.image_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(this)

        }
        startPostponedEnterTransition()
    }



    private fun setBindBeer() {
        binding.beer = selectedBeer
        binding.detailInclude.beer = selectedBeer
        binding.detailInclude.detailVolumeInclude.beer = selectedBeer
    }


    private fun setBeerMethodView() {
        lifecycleScope.launch(Dispatchers.Default) {
            val list = getBeerMethodList()
            methodAddView(list)
        }
    }


    private fun methodAddView(list: List<String>) {
        lifecycleScope.launch(Dispatchers.Main) {
            list.forEach {
                val textView = TextView(requireContext()).apply {
                    setHtmlText(it)
                    val pararm = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                        .apply { setMargins(10) }
                    layoutParams = pararm
                }
                binding.detailInclude.detailMethodLinearLayout.addView(textView)
            }
        }
    }

    private fun getBeerMethodList(): List<String> {
        val list = arrayListOf<String>()
        selectedBeer.method.apply {
            this.mash_temp.forEach {
                val eachItem =
                    "${"Temp".toBold()}:${it.temp.value}${it.temp.unit}  ${"Duration".toBold()}:${it.duration}"
                list.add(eachItem)
            }
            if (this.fermentation != null) {
                val fermentationTemp = this.fermentation.temp
                list.add(
                    "${"Fermentation".toBold()}\n" +
                            "Temp:${fermentationTemp.value}${fermentationTemp.unit}"
                )
            }
            if (this.twist != null) {
                list.add("${"Twist".toBold()}:${this.twist}")
            }
        }
        return list
    }

    @SuppressLint("SetTextI18n")
    private fun setHorizontalView() {
        val textView = binding.detailInclude.detailNumSingleValuesInclude.singlesNumTextView

        val contents = "${getString(R.string.abv).toBold()}:${selectedBeer.abv}\t\t\t\t" +
                "${getString(R.string.ibu).toBold()}:${selectedBeer.ibu}\t\t\t\t" +
                "${getString(R.string.target_fg).toBold()}:${selectedBeer.target_fg}\t\t\t\t" +
                "${getString(R.string.target_og).toBold()}:${selectedBeer.target_og}\t\t\t\t" +
                "${getString(R.string.ebc).toBold()}:${selectedBeer.ebc}\t\t\t\t" +
                "${getString(R.string.srm).toBold()}:${selectedBeer.srm}\t\t\t\t" +
                "${getString(R.string.ph).toBold()}:${selectedBeer.ph}\t\t\t\t" +
                "${getString(R.string.attenuation_level).toBold()}:${selectedBeer.attenuation_level}\t\t\t\t"
        textView.setHtmlText(contents)
    }


    private fun setIngredientView() = lifecycleScope.launch {
        val linearLayout = binding.detailInclude.detailIngredientsLinearLayout
        selectedBeer.ingredients.let {
            addMaltToLinearLayout(linearLayout,it.malt)
            addHopsToLinearLayout(linearLayout,it.hops)
            addYeast(linearLayout,it.yeast)
        }
    }
    private fun addYeast(linearLayout: LinearLayout,yeast:String){
        val textView = TextView(requireContext()).apply {
            setHtmlText("${"Yeast".toH(4)}$yeast")
        }
        linearLayout.addView(textView)
    }
    private fun addMaltToLinearLayout(linearLayout: LinearLayout, list: List<Malt>){
        val sb = StringBuilder()
        sb.append("Malt".toH(4))
        list.forEachIndexed { index, malt ->
            sb.append( "${"Name".toBold()}:${malt.name}<br>" +
                    "${"Amount".toBold()}:${malt.amount.value}${malt.amount.unit}<br>"
            )
            if(index == list.count() - 1)
                sb.append("\n")
            val textView = TextView(requireContext()).apply {
                setHtmlText(sb.toString())
                sb.clear()
            }
            linearLayout.addView(textView)
        }
    }

    private fun addHopsToLinearLayout(linearLayout: LinearLayout, list: List<Hops>){
        val sb = StringBuilder()
        sb.append("<br>Hops".toH(4))
        list.forEach { hopes ->
            sb.append( "${"Name".toBold()}:${hopes.name}<br>" +
                    "${"Amount".toBold()}:${hopes.amount.value}${hopes.amount.unit}<br>" +
                    "${"Add".toBold()}:${hopes.add}<br>" +
                    "${"Middle".toBold()}:${hopes.attribute}<br>"
            )
            val textView = TextView(requireContext()).apply {
                setHtmlText(sb.toString())
                sb.clear()
            }
            linearLayout.addView(textView)
        }
    }

    private fun setFoodPairingView(){
        binding.detailFoodPairingRecyclerView.adapter = FoodPairingAdapter(selectedBeer.food_pairing)
    }


}
