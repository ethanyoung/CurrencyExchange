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

public class NumberFormat extends Activity {
	
	LinearLayout n_LinearLayout;
	ListView n_ListView;
	ArrayAdapter<String> n_Adapter;
	
	AlertDialog FormatDlg;
	
	final String[] item_Decimals = {"0","1","2","3","4","5","6","7","8","9"};
	final String[] item_Grouping = {"取消分组",",（逗号）",".（点）"," （空格）","' （撇号）"}; 
	final String[] item_Separator = {",（逗号）",".（点）"," （空格）","' （撇号）"}; 
	
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(NumberFormat.this, Settings.class);
			startActivity(intent);
			NumberFormat.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public List<String> getData() {
		List<String> data = new ArrayList<String>();
		data.add("小数点位数");
		data.add("分组符号");
		data.add("小数点符号");
		
		return data;
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		n_LinearLayout = new LinearLayout(this);
		n_ListView = new ListView(this);
		n_Adapter = new ArrayAdapter<String>(this,android.R.
				layout.simple_expandable_list_item_1,getData());
		n_ListView.setAdapter(n_Adapter);
		setContentView(n_ListView);
		
		n_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        			long arg3)  {
        		SharedPreferences settings = getSharedPreferences("settings",0);
        		switch (arg2) {
        		case 0:
        			AlertDialog.Builder builder_Decimals = new AlertDialog.Builder(NumberFormat.this);
        			builder_Decimals.setTitle("保留小数点后位数");
        			builder_Decimals.setSingleChoiceItems(item_Decimals, settings.getInt("Decimals", 4), 
        					new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									SharedPreferences settings = getSharedPreferences("settings",0);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putInt("Decimals", which);
								    editor.commit();
								}
								
        			});
        			FormatDlg = builder_Decimals.create();
        			FormatDlg.show();
        			break;
        		case 1:
        			AlertDialog.Builder builder_Grouping = new AlertDialog.Builder(NumberFormat.this);
        			builder_Grouping.setTitle("分组符号");
        			builder_Grouping.setSingleChoiceItems(item_Grouping, settings.getInt("Grouping", 1), 
        					new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									SharedPreferences settings = getSharedPreferences("settings",0);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putInt("Grouping", which);
								    editor.commit();
								}
        			});
        			FormatDlg = builder_Grouping.create();
        			FormatDlg.show();
        			break;
        		case 2:
        			AlertDialog.Builder builder_Separator = new AlertDialog.Builder(NumberFormat.this);
        			builder_Separator.setTitle("小数点符号");
        			builder_Separator.setSingleChoiceItems(item_Separator, settings.getInt("Separator", 1), 
        					new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									SharedPreferences settings = getSharedPreferences("settings",0);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putInt("Separator", which);
								    editor.commit();
								}
        			});
        			FormatDlg = builder_Separator.create();
        			FormatDlg.show();
        			break;
        		}
        	}
        });
	}
}
