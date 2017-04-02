package com.example.hanna.the592beauty3;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "https://phreshout999.000webhostapp.com/Register2.php";
    private Map<String, String> params;

    public RegisterRequest(String ID, String Name, String Password, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("ID", ID);
        params.put("Name", Name);
        params.put("Password", Password);

    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}