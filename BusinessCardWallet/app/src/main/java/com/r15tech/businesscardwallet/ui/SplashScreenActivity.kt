package com.r15tech.businesscardwallet.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.r15tech.businesscardwallet.databinding.ActivitySplashScreenBinding


class SplashScreenActivity : AppCompatActivity() {

    companion object {
        private const val RED_COLOR = "#FF4444"
    }

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

    override fun onStart() {
        super.onStart()

            showAnimation()
    }

    private fun showAnimation(){

        try{

            var count = 0
            val fadeOut: Animation = AlphaAnimation(1f, 0f)
            fadeOut.duration = 150
            fadeOut.startOffset = 350

            binding.cardviewCard3.startAnimation(fadeOut)

            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    count++
                }
                override fun onAnimationEnd(animation: Animation?) {
                    when (count) {
                        1 -> {
                            binding.cardviewCard3.setCardBackgroundColor(Color.WHITE)
                            binding.cardviewCard2.setCardBackgroundColor(Color.parseColor(RED_COLOR))
                            fadeOut.cancel()
                            binding.cardviewCard2.startAnimation(fadeOut)
                        }
                        2 -> {
                            binding.cardviewCard2.setCardBackgroundColor(Color.WHITE)
                            binding.cardviewCard.setCardBackgroundColor(Color.parseColor(RED_COLOR))
                            fadeOut.cancel()
                            binding.cardviewCard.startAnimation(fadeOut)
                        }
                        else -> {
                            binding.cardviewCard.setCardBackgroundColor(Color.WHITE)
                            fadeOut.cancel()
                            goToMain()
                        }
                    }
                }
                override fun onAnimationRepeat(animation: Animation?) { }
            })

        }catch (e: Exception){
            goToMain()
        }
    }

    private fun goToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}


