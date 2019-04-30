package hu.ait.robinfood.data

data class Organization(
                var uid: String = "",
                var orgName: String = "",
                var contactName: String = "",
                var type: String = "",    //either "Restaurant" or "Pantry"
                var address: String = "",
                var shortDescription: String = "",
                var longDescription: String = "",
                var visible: Boolean = true)