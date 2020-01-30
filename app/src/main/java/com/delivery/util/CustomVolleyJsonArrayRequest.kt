package com.delivery.util

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import org.json.JSONArray
import org.json.JSONException
import java.io.UnsupportedEncodingException

class CustomVolleyJsonArrayRequest : Request<JSONArray> {
    private var listener: Response.Listener<JSONArray>
    private var params: Map<String, String?>

    constructor(url: String?, params: MutableMap<String, String?>,
                reponseListener: Response.Listener<JSONArray>, errorListener: Response.ErrorListener?) : super(Method.GET, url, errorListener) {
        listener = reponseListener
        this.params = params
    }

    constructor(method: Int, url: String?, params: Map<String, String?>,
                reponseListener: Response.Listener<JSONArray>, errorListener: Response.ErrorListener?) : super(method, url, errorListener) {
        listener = reponseListener
        this.params = params
    }

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String?> {
        return params
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONArray> {
        return try {
            //[HTTP!!.CONTENT_TYPE]
           // val type = response.headers.get("HTTP","CONTENT_TYPE)
            val jsonString = String(response.data)
            Response.success(JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (je: JSONException) {
            Response.error(ParseError(je))
        }
    }

    override fun deliverResponse(response: JSONArray) { // TODO Auto-generated method stub
        listener.onResponse(response)
    }
}