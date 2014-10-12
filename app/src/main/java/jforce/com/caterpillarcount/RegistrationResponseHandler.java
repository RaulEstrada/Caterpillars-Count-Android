package jforce.com.caterpillarcount;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationResponseHandler extends JsonHttpResponseHandler{

    private String emailAddress;
    private String password;
    private Activity activity;

	public RegistrationResponseHandler(Activity activity, String emailAddress, String password){
        super();
		this.activity = activity;
        this.emailAddress = emailAddress;
        this.password = password;

}
	
	@Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		RegistrationActivity registrationActivity = (RegistrationActivity) activity;

        registrationActivity.getProgressDialog().dismiss();
        Intent data = new Intent();

        data.putExtra("email", emailAddress);
        data.putExtra("password", password);
        registrationActivity.setResult(registrationActivity.RESULT_OK, data);
        registrationActivity.finish();


        return;
			
			
    }
	
	@Override
	public void onFailure(int code, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        RegistrationActivity registrationActivity = (RegistrationActivity) activity;
        registrationActivity.getProgressDialog().dismiss();

        if(code == 409){//HTTP1.1/409 Conflict

            Toast.makeText(registrationActivity, registrationActivity.getResources().getString(R.string.register409), Toast.LENGTH_SHORT).show();
            return;
        }
        else if(code == 500){//HTTP1.1/500 Internal Server Error

            Toast.makeText(registrationActivity, registrationActivity.getResources().getString(R.string.register500), Toast.LENGTH_SHORT).show();
            return;


        }
        else if(code == 400){//HTTP1.1/400 Bad Request

            Toast.makeText(registrationActivity, registrationActivity.getResources().getString(R.string.register400), Toast.LENGTH_SHORT).show();
            return;

        }


        Toast.makeText(registrationActivity, registrationActivity.getResources().getString(R.string.registerException), Toast.LENGTH_SHORT).show();
        return;

    }


}
