package luo.android.CurrencyExchange;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class Help extends Activity {
	
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(Help.this, CurrencyExchange.class);
			startActivity(intent);
			Help.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		
	}
}
