package com.example.easyfood.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Database
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.ActivityMealBinding
import com.example.easyfood.db.MealDataBase
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.HomeViewModel
import com.example.easyfood.viewModel.MealViewModel
import com.example.easyfood.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealMVVM: MealViewModel
    private lateinit var youtubeLink: String
    private lateinit var binding: ActivityMealBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDataBase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMVVM = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]
//        mealMVVM = ViewModelProvider(this)[MealViewModel::class.java]
        getMealInfoFromIntent()
        setInformationsViews()

        loadingCase()
        mealMVVM.getMealDetail(mealId)
        observerMealDetailsLiveData()

        onYoutubeImageClick()
        onFavouriteClick()
    }

    private fun onFavouriteClick() {
        binding.btnAddToFav.setOnClickListener{
            mealToSave?.let {
                mealMVVM.insertMeal(it)
                Toast.makeText(this,"Meal saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYt.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave: Meal?=null

    private fun observerMealDetailsLiveData() {
        mealMVVM.observerMealDetailLiveData().observe(this
        ) { t ->
            onResponseCase()
            val meal = t
            mealToSave = meal

            binding.tvCategory2.text = "Category : ${meal!!.strCategory}"
            binding.tvArea.text = "Area : ${meal!!.strArea}"
            binding.tvInstructions.text = meal.strInstructions

            youtubeLink = meal.strYoutube.toString()
        }
    }

    private fun setInformationsViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInfoFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategory2.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYt.visibility = View.INVISIBLE


    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategory2.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYt.visibility = View.VISIBLE
    }
}