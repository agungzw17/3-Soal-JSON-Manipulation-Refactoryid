import org.apache.commons.lang3.StringUtils
import org.json.JSONArray
import java.time.*

fun main() {
    val classloader = Thread.currentThread().contextClassLoader
    val inputStream = classloader.getResourceAsStream("inventory_list.json")

    val jsonFile = inputStream.bufferedReader().use { it.readText() }

    val list = JSONArray(jsonFile)
    val listItemsInventory = ArrayList<InventoryModel>()
    for (i in 0 until list.length()) {
        val inventory = list.getJSONObject(i)
        val inventoryItem = InventoryModel(
            inventory_id = inventory.getInt("inventory_id"),
            name = inventory.getString("name"),
            type = inventory.getString("type"),
            purchased_at = inventory.getInt("purchased_at")
        )

        for(i in 0 until inventory.getJSONArray("tags").length()) {
            val tagItem = InventoryTagModel(
                name =inventory.getJSONArray("tags").toString()
            )
            inventoryItem.tags?.add(tagItem)
        }

        for(i in 0 until inventory.getJSONObject("placement").length()) {
            val placementItem = InventoryPlacementModel(
                room_id = inventory.getJSONObject("placement").getInt("room_id"),
                name = inventory.getJSONObject("placement").getString("name")
            )
            inventoryItem.placement?.add(placementItem)
        }

        listItemsInventory.add(inventoryItem)
    }

    //1.Find items in Meeting Room.
    println("Find items in Meeting Room:")
    val listItemInMeetingRoom = ArrayList<String>()
    for (inventoryItem in listItemsInventory) {
        for (placementItem in inventoryItem.placement!!) {
            if(placementItem.name!!.toLowerCase().contains("Meeting Room", ignoreCase = true) ) {
                inventoryItem.name?.let { listItemInMeetingRoom.add(it) }
            }
        }
    }
    for (items in listItemInMeetingRoom.distinct()) {
        println("- $items")
    }

    //2.Find all electronic devices
    println("Find all electronic devices:")
    val listElectorinicDevices = ArrayList<String>()
    for (items in listItemsInventory) {
        if(items.type!!.toLowerCase().contains("electronic", ignoreCase = true) ) {
            items.name?.let { listElectorinicDevices.add(it) }
        }
    }
    for (items in listElectorinicDevices.distinct()) {
        println("- $items")
    }

    //3.Find all furnitures
    println("Find all furnitures:")
    val listFurniture = ArrayList<String>()
    for (items in listItemsInventory) {
        if(items.type!!.toLowerCase().contains("furniture", ignoreCase = true) ) {
            items.name?.let { listFurniture.add(it) }
        }
    }
    for (items in listFurniture.distinct()) {
        println("- $items")
    }

    //4.Find all items was purchased at 16 Januari 2020
    println("Find all items was purchased at 16 Januari 2020:")
    val listItemPurchased = ArrayList<String>()
    for (items in listItemsInventory) {
        val dt = items.purchased_at?.let {
            Instant.ofEpochSecond(it.toLong())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }
        if (StringUtils.left(dt.toString(), 10) == "2020-01-16") {
            items.name?.let { listItemPurchased.add(it) }
        }
    }

    for (items in listItemPurchased.distinct()) {
        println("- $items")
    }

    //5.Find all items with brown color
    println("Find all items with brown color:")
    val listItemBrownColor = ArrayList<String>()
    for (InventoryItems in listItemsInventory) {
        for (itemTags in InventoryItems.tags!!) {
            if (itemTags.name.toString().toLowerCase().contains("brown", ignoreCase = true)) {
                InventoryItems.name?.let { listItemBrownColor.add(it) }
            }
        }
    }
    for (items in listItemBrownColor.distinct()) {
        println("- $items")
    }
}