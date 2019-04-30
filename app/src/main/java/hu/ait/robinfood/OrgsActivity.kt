package hu.ait.robinfood

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import hu.ait.robinfood.adapter.OrgsAdapter
import hu.ait.robinfood.data.Organization
import kotlinx.android.synthetic.main.activity_orgs.*

class OrgsActivity : AppCompatActivity() {

    lateinit var orgsAdapter: OrgsAdapter
    lateinit var userOrg: Organization

    var display_type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orgs)

        setSupportActionBar(toolbar)
        MyThread().start()

//        fab.setOnClickListener { view ->
//            startActivity(
//                Intent(this@ForumActivity,
//                    CreatePostActivity::class.java)
//            )
//        }

//        val toggle = ActionBarDrawerToggle(
//            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        )
//        drawer_layout.addDrawerListener(toggle)
//        toggle.syncState()

//        nav_view.setNavigationItemSelectedListener(this)

        orgsAdapter = OrgsAdapter(this,
            FirebaseAuth.getInstance().currentUser!!.uid)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerOrgs.layoutManager = layoutManager

        recyclerOrgs.adapter = orgsAdapter

        initOrgs()
    }

    inner class MyThread : Thread() {
        override fun run() {
            Log.d("hello", "cvb")
            val db = FirebaseFirestore.getInstance()
            Log.d("hello", "1")

            val privateDataRef = db.collection("orgs").document(
                FirebaseAuth.getInstance().currentUser!!.uid)
//            val privateDataRef = db.collection("orgs").document(
//                "zkToEiGwHRW6Iv854Y76")
            Log.d("hello", privateDataRef.toString())

            val document = Tasks.await(privateDataRef.get())
            Log.d("hello", document.toString())

            if (document.exists()) {
                Log.d("hello", "found")
                //Cast the given DocumentSnapshot to our POJO class
                userOrg = document.toObject(Organization::class.java)!!
            } else null

            Log.d("hello", userOrg.toString())
            display_type = if (userOrg!!.type == "Restaurant") "Food pantry" else "Restaurant"
            Log.d("hello", "2")

        }
    }

    private fun initOrgs() {
        Log.d("hello", "ititititit")

        val db = FirebaseFirestore.getInstance()

        val query = db.collection("orgs").whereEqualTo("type", display_type).whereEqualTo(
            "visible", true
        )

        Log.d("hello", "hjhjhjhj")




        var allOrgsListener = query.addSnapshotListener(
            object: EventListener<QuerySnapshot> {
                override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
                    if (e != null) {
                        Toast.makeText(this@OrgsActivity, "listen error: ${e.message}", Toast.LENGTH_LONG).show()
                        return
                    }

                    for (dc in querySnapshot!!.getDocumentChanges()) {
                        when (dc.getType()) {
                            DocumentChange.Type.ADDED -> {
                                val org = dc.document.toObject(Organization::class.java)
                                orgsAdapter.addOrg(org, dc.document.id)
                            }
                            DocumentChange.Type.MODIFIED -> {
                                Toast.makeText(this@OrgsActivity, "update: ${dc.document.id}", Toast.LENGTH_LONG).show()
                            }
                            DocumentChange.Type.REMOVED -> {
                                orgsAdapter.removeOrgByKey(dc.document.id)
                            }
                        }
                    }
                }
            })
    }

//    override fun onBackPressed() {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
//            FirebaseAuth.getInstance().signOut()
//            super.onBackPressed()
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_orgs_menu, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            R.id.nav_logout -> {
//                FirebaseAuth.getInstance().signOut()
//                finish()
//            }
//        }
//        when (item.itemId) {
//            R.id.nav_edit_profile -> {
//                //Maitreyi writes some super cool code here
//            }
//        }
//
////        drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }
}
