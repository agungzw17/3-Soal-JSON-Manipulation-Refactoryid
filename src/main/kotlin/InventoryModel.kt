data class InventoryModel (
    var inventory_id:Int? = 0,
    var name:String? = "",
    var type:String? = "",
    var tags:ArrayList<InventoryTagModel>? = ArrayList(),
    var purchased_at: Int? = 0,
    var placement:ArrayList<InventoryPlacementModel>? = ArrayList()
)