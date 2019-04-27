package hu.ait.robinfood.data

data class Organization(var uid: String = "",
                var org_name: String = "",
                var contact_name: String = "",
                var type: String = "",    //either "Restaurant" or "Pantry"
                var address: String = "",
                var description: String = "")