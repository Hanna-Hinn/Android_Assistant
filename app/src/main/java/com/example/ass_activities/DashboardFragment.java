package com.example.ass_activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DownloadManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DashboardFragment extends Fragment {

    private final String request_url = "https://famous-quotes4.p.rapidapi.com/random?category=learning&count=1";
    private TextView quote_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        quote_text = rootView.findViewById(R.id.quote_text);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, request_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonarr = new JSONArray(response);
                    JSONObject jsonobj = (JSONObject) jsonarr.get(0);
                    String quoteObj = jsonobj.getString("text");
                    String author = jsonobj.getString("author");

                    String quote = "\""+quoteObj.toString()+"\"\n" + author.toString();
                    quote_text.setText(quote);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
                quote_text.setText("\"We cannot solve problems with the kind of thinking we employed when we came up with them.\"\n"+"Albert Einstein");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();
                header.put("X-RapidAPI-Key","5af420ab5bmsh6c9309992ee2eefp19e0b4jsn0cfe9f02576f");
                header.put("X-RapidAPI-Host","famous-quotes4.p.rapidapi.com");
                return header;
            }
        };

        queue.add(stringRequest);

        return rootView;
    }
}