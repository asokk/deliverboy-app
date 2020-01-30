package com.delivery.util

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class CustomVolleyJsonRequest : Request<JSONObject> {
    private var listener: Response.Listener<JSONObject>
    private var params: Map<String, String>

    constructor(url: String?, params: Map<String, String>,
                reponseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener?) : super(Method.GET, url, errorListener) {
        listener = reponseListener
        this.params = params
    }

    constructor(method: Int, url: String?, params: Map<String, String>,
                reponseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener?) : super(method, url, errorListener) {
        listener = reponseListener
        this.params = params
    }

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        return params
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
        return try {
            val jsonString = String(response.data)
            Response.success(JSONObject(jsonString),  HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (je: JSONException) {
            Response.error(ParseError(je))
        }
    }

    override fun deliverResponse(response: JSONObject) { // TODO Auto-generated method stub
        listener.onResponse(response)
    }
}