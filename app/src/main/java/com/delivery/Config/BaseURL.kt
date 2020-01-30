package com.delivery.Config

object BaseURL {
    const val APP_NAME = "GroceryDeliver"
    const val PREFS_NAME = "LoginPrefs"
    const val PREFS_NAME2 = "LoginPrefs2"
    const val IS_LOGIN = "isLogin"
    const val KEY_NAME = "user_fullname"
    const val KEY_ID = "user_id"
    const val KEY_ORDER_ID = "ORDER_ID"
    var BASE_URL = "http://192.168.8.1/store/"
    @JvmField
    var GET_ORDER_URL = BASE_URL + "index.php/api/delivery_boy_order"
    @JvmField
    var GET_DELIVERD_ORDER_URL = BASE_URL + "index.php/api/delivered_complete"
    @JvmField
    var LOGIN = BASE_URL + "index.php/api/delivery_boy_login"
    @JvmField
    var OrderDetail = BASE_URL + "index.php/api/order_details"
    @JvmField
    var IMG_PRODUCT_URL = BASE_URL + "uploads/products/"
    @JvmField
    val urlUpload = BASE_URL + "index.php/api/delivered2"
    const val REQCODE = 100
    const val image = "signature"
    const val imageName = "id"
}