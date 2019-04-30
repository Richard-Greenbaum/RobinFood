package hu.ait.robinfood

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.ait.robinfood.data.Organization
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.row_org.*

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
                uploadOrg()
                var intentDetails = Intent()
                intentDetails.setClass(this@DetailsActivity, OrgsActivity::class.java)
                startActivity(intentDetails)
            }
        }



    }

    fun uploadOrg() {
        val org = Organization(
            FirebaseAuth.getInstance().currentUser!!.uid,
            orgNameEt.text.toString(),
            contactNameEt.text.toString(),
            type,
            addressEt.text.toString(),
            shortDescriptionEt.text.toString(),
            longDescriptionEt.text.toString(),
            visibleCb.isChecked
        )

        var orgsCollection = FirebaseFirestore.getInstance().collection(
            "orgs"
        )

        orgsCollection.document(FirebaseAuth.getInstance().currentUser!!.uid).set(
            org
        ).addOnSuccessListener {
            Toast.makeText(
                this@DetailsActivity,
                "Org saved", Toast.LENGTH_LONG
            ).show()

            finish()
        }.addOnFailureListener {
            Toast.makeText(
                this@DetailsActivity,
                "Error: ${it.message}", Toast.LENGTH_LONG
            ).show()
        }
    }
}