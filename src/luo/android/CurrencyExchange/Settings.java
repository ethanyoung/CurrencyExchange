package luo.android.CurrencyExchange;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 设置
 * 提供各个设置的入口，第一级菜单。
 * @author Administrator
 *
 */
public class Settings extends Activity {
	
	LinearLayout s_LinearLayout;
	ListView s_ListView;
	ArrayAdapter<String> s_Adapter;
	
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(Settings.this, CurrencyExchange.class);
			startActivity(intent);
			Settings.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public List<String> getData() {
		List<String> data = new ArrayList<String>();
		data.add("货币设置");
		data.add("数据格式");
		data.add("时间和日期格式");
		data.add("杂项");
		
		return data;
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		s_LinearLayout = new LinearLayout(this);
		s_ListView = new ListView(this);
		s_Adapter = new ArrayAdapter<String>(this,android.R.
				layout.simple_expandable_list_item_1,getData());
		s_ListView.setAdapter(s_Adapter);
		setContentView(s_ListView);
		
		s_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        			long arg3)  {
        		
        		Intent intent;
        		
        		switch (arg2) {
        		
        		//切换到“货币设置”
        		case 0:
        			intent = new Intent();
        			intent.setClass(Settings.this, CurrencySettings.class);
        			startActivity(intent);
        			Settings.this.finish();
        			break;
        			
        		//切换到“数据格式”
        		case 1:
        			intent = new Intent();
        			intent.setClass(Settings.this, NumberFormat.class);
        			startActivity(intent);
        			Settings.this.finish();
        			break;
        			
        		//切换到“时间日期格式”
        		case 2:
        			intent = new Intent();
        			intent.setClass(Settings.this, DateTimeFormat.class);
        			startActivity(intent);
        			Settings.this.finish();
        			break;
        			
        		//切换到“杂项”
        		case 3:
        			intent = new Intent();
        			intent.setClass(Settings.this, Miscellaneous.class);
        			startActivity(intent);
        			Settings.this.finish();
        			break;
        		}
        	}
        });
	}
}
