package hu.ait.robinfood.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import hu.ait.robinfood.R
import hu.ait.robinfood.data.Organization
import kotlinx.android.synthetic.main.row_org.view.*

class OrgsAdapter(
    private val context: Context,
    private val uId: String) : RecyclerView.Adapter<OrgsAdapter.ViewHolder>() {

    private var orgsList = mutableListOf<Organization>()
    private var orgKeys = mutableListOf<String>()

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.row_org, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount() = orgsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (uid, orgName, contactName, type, address, shortDescription,
            longDescription, visible) = orgsList[holder.adapterPosition]

        holder.orgNameTv.text = orgName
        holder.orgAddressTv.text = address
        holder.shortDescriptionTv2.text = shortDescription

    }

    fun addOrg(org: Organization, key: String) {
        orgsList.add(org)
        orgKeys.add(key)
        notifyDataSetChanged()
    }

    private fun removeOrg(index: Int) {
        FirebaseFirestore.getInstance().collection("orgs").document(
            orgKeys[index]
        ).delete()

        orgsList.removeAt(index)
        orgKeys.removeAt(index)
        notifyItemRemoved(index)
    }

    fun removeOrgByKey(key: String) {
        val index = orgKeys.indexOf(key)
        if (index != -1) {
            orgsList.removeAt(index)
            orgKeys.removeAt(index)
            notifyItemRemoved(index)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        val imageVw: ImageView = itemView.imageVw
        val orgNameTv: TextView = itemView.orgNameTv
        val orgAddressTv: TextView = itemView.orgAddressTv
        val shortDescriptionTv2: TextView = itemView.shortDescriptionTv2

    }
}
