package com.box.punkapi.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
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
import com.box.punkapi.utils.appendBoldTag
import com.box.punkapi.utils.appendHTag
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
            duration = 500
            scrimColor = Color.TRANSPARENT
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
                .listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                })
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
                    "${"Temp".appendBoldTag()}:${it.temp.value}${it.temp.unit}  ${"Duration".appendBoldTag()}:${it.duration}"
                list.add(eachItem)
            }
            if (this.fermentation != null) {
                val fermentationTemp = this.fermentation.temp
                list.add(
                    "${"Fermentation".appendBoldTag()}\n" +
                            "Temp:${fermentationTemp.value}${fermentationTemp.unit}"
                )
            }
            if (this.twist != null) {
                list.add("${"Twist".appendBoldTag()}:${this.twist}")
            }
        }
        return list
    }

    @SuppressLint("SetTextI18n")
    private fun setHorizontalView() {
        val textView = binding.detailInclude.detailNumSingleValuesInclude.singlesNumTextView

        val contents = "${getString(R.string.abv).appendBoldTag()}:${selectedBeer.abv}\t\t\t\t" +
                "${getString(R.string.ibu).appendBoldTag()}:${selectedBeer.ibu}\t\t\t\t" +
                "${getString(R.string.target_fg).appendBoldTag()}:${selectedBeer.target_fg}\t\t\t\t" +
                "${getString(R.string.target_og).appendBoldTag()}:${selectedBeer.target_og}\t\t\t\t" +
                "${getString(R.string.ebc).appendBoldTag()}:${selectedBeer.ebc}\t\t\t\t" +
                "${getString(R.string.srm).appendBoldTag()}:${selectedBeer.srm}\t\t\t\t" +
                "${getString(R.string.ph).appendBoldTag()}:${selectedBeer.ph}\t\t\t\t" +
                "${getString(R.string.attenuation_level).appendBoldTag()}:${selectedBeer.attenuation_level}\t\t\t\t"
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
            setHtmlText("${"Yeast".appendHTag(4)}$yeast")
        }
        linearLayout.addView(textView)
    }
    private fun addMaltToLinearLayout(linearLayout: LinearLayout, list: List<Malt>){
        val sb = StringBuilder()
        sb.append("Malt".appendHTag(4))
        list.forEachIndexed { index, malt ->
            sb.append( "${"Name".appendBoldTag()}:${malt.name}<br>" +
                    "${"Amount".appendBoldTag()}:${malt.amount.value}${malt.amount.unit}<br>"
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
        sb.append("<br>Hops".appendHTag(4))
        list.forEach { hopes ->
            sb.append( "${"Name".appendBoldTag()}:${hopes.name}<br>" +
                    "${"Amount".appendBoldTag()}:${hopes.amount.value}${hopes.amount.unit}<br>" +
                    "${"Add".appendBoldTag()}:${hopes.add}<br>" +
                    "${"Middle".appendBoldTag()}:${hopes.attribute}<br>"
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
