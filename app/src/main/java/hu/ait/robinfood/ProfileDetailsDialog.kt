package hu.ait.robinfood

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import hu.ait.robinfood.adapter.OrgsAdapter
import hu.ait.robinfood.data.Organization
import kotlinx.android.synthetic.main.dialog_profile_details.view.*
import android.content.ActivityNotFoundException
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.dialog_profile_details.*


class ProfileDetailsDialog : DialogFragment() {

    private lateinit var profileName: TextView
    private lateinit var profileContact: TextView
    private lateinit var profileAddress: TextView
    private lateinit var profileShortDes: TextView
    private lateinit var profileLongDes: TextView
    private lateinit var btnEmail: ImageView
    private lateinit var btnWebsite: ImageView
    private lateinit var profileImage: ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(resources.getString(R.string.dialog_profile_details_title))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.dialog_profile_details, null
        )

        builder.setView(rootView)

        setTextViewFields(rootView)

        builder.setNeutralButton(resources.getString(R.string.dialog_close)) { dialog, which ->
            dialog.dismiss()
        }

        return builder.create()
    }

    private fun setTextViewFields(rootView: View) {
        profileName = rootView.profileName
        profileAddress = rootView.profileAddress
        profileContact = rootView.profileContact
        profileShortDes = rootView.profileShortDescription
        profileLongDes = rootView.profileLongDescription
        btnEmail = rootView.btnEmail
        btnWebsite = rootView.btnWebsite
        profileImage = rootView.profileImage

        val arguments = this.arguments

        if (arguments != null && arguments.containsKey(
                OrgsAdapter.KEY_ORG_DETAILS)) {

            val organization = arguments.getSerializable(OrgsAdapter.KEY_ORG_DETAILS) as Organization

            profileName.text = organization.orgName
            profileAddress.text = organization.address
            profileContact.text = organization.contactName
            profileShortDes.text = organization.shortDescription
            profileLongDes.text = organization.longDescription

            if (organization.image != "") {
                var imgRef = FirebaseStorage.getInstance().reference.child(organization.image)
                GlideApp.with(this.context!!)
                    .load(imgRef).into(profileImage)
            } else {
                profileImage.visibility = View.GONE
            }

            btnEmail.setOnClickListener {
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                val email = FirebaseAuth.getInstance().currentUser?.email
                emailIntent.data = Uri.parse(resources.getString(R.string.email_uri, email))

                try {
                    startActivity(emailIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        context as OrgsActivity, resources.getString(R.string.email_failed_msg), Toast.LENGTH_LONG
                    ).show()
                }
            }

            if (organization.website != "") {
                btnWebsite.setOnClickListener {
                    val websiteIntent = Intent(Intent.ACTION_VIEW)
                    var website = organization.website
                    if (!website.startsWith("https://") && !website.startsWith("http://")) {
                        website = "http://$website"
                    }
                    websiteIntent.data = Uri.parse(website)
                    startActivity(websiteIntent)
                }
            } else {
                btnWebsite.visibility = View.GONE
            }
        }
    }

}