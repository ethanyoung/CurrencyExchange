package luo.android.CurrencyExchange;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CurrencySettings extends Activity {
	
	LinearLayout s_LinearLayout;
	ListView s_ListView;
	ArrayAdapter<String> s_Adapter;
	
	AlertDialog CurrencyDlg;
	
	final String[] item_main = {"默认","根据国家名排序","根据货币简称排序"};
	final String[] item_select = {"根据国家名排序","根据货币简称排序"};
	
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(CurrencySettings.this, Settings.class);
			startActivity(intent);
			CurrencySettings.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public List<String> getData() {
		List<String> data = new ArrayList<String>();
		data.add("更新");
		data.add("主屏幕排序");
		data.add("货币选择排序");
		data.add("用户货币");
		
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
        		SharedPreferences settings = getSharedPreferences("settings",0);
        		Intent intent;
        		switch (arg2) {
        		case 0:
        			//TODO
        			intent = new Intent();
        			intent.setClass(CurrencySettings.this, Updates.class);
        			startActivity(intent);
        			CurrencySettings.this.finish();
        			break;
        			//TODO
        			
        		case 1:
        			/*
        			intent = new Intent();
        			intent.setClass(CurrencySettings.this, SortMainScreen.class);
        			startActivity(intent);
        			CurrencySettings.this.finish();
        			*/
        			
        			settings = getSharedPreferences("settings",0);

        			CurrencyDlg = new AlertDialog.Builder(CurrencySettings.this)
        			.setTitle("主屏幕排序方式")
        			.setSingleChoiceItems(item_main, settings.getInt("SortMain", 0), 
        					new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									SharedPreferences settings = getSharedPreferences("settings",0);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putInt("SortMain", which);
								    editor.commit();
								}

        			})
        			.setPositiveButton("升序", 
        					new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									SharedPreferences settings = getSharedPreferences("settings",0);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putInt("OrderMain", 0);
								    editor.commit();
								}
        			})
        			.setNegativeButton("降序", 
        					new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									SharedPreferences settings = getSharedPreferences("settings",0);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putInt("OrderMain", 1);
								    editor.commit();
								}
        				
        			})
        			.create();
        			CurrencyDlg.show();
        			break;
        		case 2:
        			settings = getSharedPreferences("settings",0);

        			CurrencyDlg = new AlertDialog.Builder(CurrencySettings.this)
        			.setTitle("货币选择时的排序方式")
        			.setSingleChoiceItems(item_select, settings.getInt("SortSelect", 0), 
        					new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									SharedPreferences settings = getSharedPreferences("settings",0);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putInt("SortSelect", which);
								    editor.commit();
								}

        			})
        			.setPositiveButton("升序", 
        					new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									SharedPreferences settings = getSharedPreferences("settings",0);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putInt("OrderSelect", 0);
								    editor.commit();
								}
								
        			})
        			.setNegativeButton("降序", 
        					new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									SharedPreferences settings = getSharedPreferences("settings",0);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putInt("OrderSelect", 1);
								    editor.commit();
								}
								
        			})
        			.create();
        			CurrencyDlg.show();
        			break;
        		case 3:
        			intent = new Intent();
        			intent.setClass(CurrencySettings.this, UserCurrency.class);
        			startActivity(intent);
        			CurrencySettings.this.finish();
        			break;
        		}
        	}
        });
	}

}
