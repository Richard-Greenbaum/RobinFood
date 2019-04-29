package hu.ait.robinfood

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    private var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        if (intent.extras.containsKey("TYPE")) {
            type = intent.getStringExtra("TYPE")
            if (type == "Restaurant") {
                headerTv.text = "${headerTv.text} restaurant"
                orgNameEt.hint = "${orgNameEt.hint} restaurant"
                addressEt.hint = "${addressEt.hint} restaurant"
                shortDescriptionTv.text = "Please provide a list of items your restaurant is able to donate"
                longDescriptionTv.text = "Provide any additional information you wish to share about your restaurant here"
            }
            if (type == "Food pantry") {
                headerTv.text = "${headerTv.text} food pantry"
                orgNameEt.hint = "${orgNameEt.hint} food pantry"
                addressEt.hint = "${addressEt.hint} food pantry"
                shortDescriptionTv.text = "Please provide a list of items or types of items your food pantry accepts"
                longDescriptionTv.text = "Provide any additional information you wish to share about your food pantry here"
            }
        }

        finishedBtn.setOnClickListener {
            var ok = true
            if (!orgNameEt.text.isNotEmpty()) {
                orgNameEt.error = "This field cannot be empty"
                ok = false
            }
            if (!contactNameEt.text.isNotEmpty()) {
                contactNameEt.error = "This field cannot be empty"
                ok = false
            }
            if (!addressEt.text.isNotEmpty()) {
                addressEt.error = "This field cannot be empty"
                ok = false
            }
            if (!shortDescriptionEt.text.isNotEmpty()) {
                shortDescriptionEt.error = "This field cannot be empty"
                ok = false
            }

            if (ok) {
                var intentDetails = Intent()
                intentDetails.setClass(this@DetailsActivity, PostsActivity::class.java)
                startActivity(intentDetails)
            }
        }



    }
}