package luo.android.CurrencyExchange;

import java.io.InputStreamReader;
import java.net.URL;
import java.text.Collator;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CurrencyExchange extends Activity {

	boolean selected_nation [] = new boolean[22];
	
	LinearLayout m_LinearLayout;
	ListView m_ListView;
	SimpleAdapter m_Adapter;
	DecimalFormat d_format_list;
	TextView title;
	Button Btn_clear;
	Button Btn_inverse;
	Button Btn_edit;
	Button Btn_update;
	String title_amount;
	String title_name;
	String UI_flag;
	int StdMoney_num;
	
	
	public List<Map<String,Object>> getData() {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		double temp;
		if (title_amount.length() == 0) {
			temp = 0;
		}
		else {
			temp = Double.parseDouble(title_amount);
		}
		
		SharedPreferences settings = getSharedPreferences("settings",0);
		float tax = settings.getFloat("Tax", 0);

		String f_money_amount;
		
		Map<String, Object> map;
		int compare_nation = settings.getInt("UserCompare", 1);
		//给内置的所有货币设置内容
		for(int i=0;i<ConstData.symbol.length;i++) {
			
			if (selected_nation[i]) {
				map = new HashMap<String, Object>();
				map.put("nation_name", ConstData.nation[i]);
				map.put("money_name", ConstData.code[i]);
				map.put("flag", ConstData.flag[i]);
				
				if (StdMoney_num !=i && StdMoney_num !=21 && tax!=0){
					f_money_amount = d_format_list.format(temp * ConstData.rates[StdMoney_num][i] * (1+tax/100)); 
				}
				else if (StdMoney_num == 21) {
					f_money_amount = d_format_list.format(
							temp * ConstData.rates[StdMoney_num][compare_nation] * ConstData.rates[compare_nation][i] * (1+tax/100)); 
				}
				else {
					f_money_amount = d_format_list.format(temp * ConstData.rates[StdMoney_num][i]);
				}

				
				map.put("money_amount", f_money_amount);
				map.put("money_symbol", ConstData.symbol[i]);
				map.put("nation_mark", String.valueOf(i));
				list.add(map);
			}
		}
		
		//给用户自定义货币设置内容
		if (selected_nation[21]) {
			map = new HashMap<String, Object>();
			map.put("nation_name", settings.getString("UserName", "用户货币"));
			map.put("money_name", settings.getString("UserCode", "USR"));
			map.put("flag", R.drawable.black);
			
			
			
			if (StdMoney_num != 21 && tax!=0){
				f_money_amount = d_format_list.format(
					temp * ConstData.rates[StdMoney_num][compare_nation] * ConstData.rates[compare_nation][21] * (1+tax/100)); 
			}
			
			else {
				f_money_amount = d_format_list.format(
					temp * ConstData.rates[StdMoney_num][compare_nation] * ConstData.rates[compare_nation][21]); 
			}

			map.put("money_amount", f_money_amount);
			map.put("money_symbol", settings.getString("UserSymbol", "$"));
			map.put("nation_mark", "21");
			list.add(map);
		}
		
		if (!list.isEmpty()) {     

		     Collections.sort(list, new Comparator<Map<String, Object>>() {

		     	@Override
		     	public int compare(Map<String, Object> object1,
		     	Map<String, Object> object2) {
				//根据文本排序
		     		SharedPreferences settings = getSharedPreferences("settings",0);
		     		int result = 0;
		          	switch (settings.getInt("SortMain", 0)) {
		          	case 1:
		          		switch (settings.getInt("OrderMain", 0)) {
	          			case 0:
				     		result = Collator.getInstance(Locale.CHINESE)
				     			.compare((String) object1.get("nation_name"), 
				     					(String) object2.get("nation_name"));
				     		break;
	          			case 1:
	          				result = -Collator.getInstance(Locale.CHINESE)
	          					.compare((String) object1.get("nation_name"), 
	          							(String) object2.get("nation_name"));
	          				break;
		          		}
		          		break;
		          	case 2:
		          		switch (settings.getInt("OrderMain", 0)) {
			          		case 0:
				     		result = ((String) object1.get("money_name"))
				     			.compareTo((String) object2.get("money_name"));
			          			break;
			          		case 1:
	          				result = -((String) object1.get("money_name"))
	          					.compareTo((String) object2.get("money_name"));
			          			break;
		          		}
		          		break;
		          	}
		          	return result;
		     	}     
		     });     
		}
		
		return list;
	}
	
	public List<Map<String,Object>> i_getData() {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		double temp;
		if (title_amount.length() == 0) {
			temp = 0;
		}
		else {
			temp = Double.parseDouble(title_amount);
		}

		SharedPreferences settings = getSharedPreferences("settings",0);
		float tax = settings.getFloat("Tax", 0);
		
		String s_money_amount;
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		int compare_nation = settings.getInt("UserCompare", 1);
		//21种默认货币
		for (int i=0;i<ConstData.symbol.length;i++) {
			if (selected_nation[i]) {
				map = new HashMap<String, Object>();
				map.put("nation_name", ConstData.nation[i]);
				map.put("foreign_amount", title_amount);
				map.put("money_name", ConstData.code[i]);
				map.put("flag", ConstData.flag[i]);
				
				if (StdMoney_num !=i && StdMoney_num!=21 && tax!=0){
					s_money_amount = d_format_list.format(temp / ConstData.rates[StdMoney_num][i] * (1+tax/100)); 
				}
				else if (StdMoney_num ==21) {
					s_money_amount = d_format_list.format(
							temp / ConstData.rates[compare_nation][21] / ConstData.rates[StdMoney_num][compare_nation] * (1+tax/100)); 
				}
				else {
					s_money_amount = d_format_list.format(temp / ConstData.rates[StdMoney_num][i]);
				}

				map.put("money_amount", title_name);
				map.put("money_symbol", s_money_amount);
				map.put("nation_mark", String.valueOf(i));
				list.add(map);
			}
		}
		
		//用户自定义货币
		if (selected_nation[21]) {
			map = new HashMap<String, Object>();
			map.put("nation_name", "用户货币");
			map.put("foreign_amount", title_amount);
			map.put("money_name", "USR");
			map.put("flag", R.drawable.black);
			
			
			
			if (StdMoney_num != 21 && tax!=0){
				s_money_amount = d_format_list.format(
					temp / ConstData.rates[compare_nation][21] / ConstData.rates[StdMoney_num][compare_nation] * (1+tax/100)); 
			}
			else {
				s_money_amount = d_format_list.format(
					temp / ConstData.rates[compare_nation][21] / ConstData.rates[StdMoney_num][compare_nation]); 
			}
			
			map.put("money_amount", title_name);
			map.put("money_symbol", s_money_amount);
			map.put("nation_mark", "21");
			list.add(map);
		}
		
		if (!list.isEmpty()) {     

		     Collections.sort(list, new Comparator<Map<String, Object>>() {

		     	@Override
		     	public int compare(Map<String, Object> object1,
		     	Map<String, Object> object2) {
				//根据文本排序
		     		SharedPreferences settings = getSharedPreferences("settings",0);
		     		int result = 0;
		          	switch (settings.getInt("SortMain", 0)) {
		          	case 1:
		          		switch (settings.getInt("OrderMain", 0)) {
	          			case 0:
				     		result = Collator.getInstance(Locale.CHINESE)
				     			.compare((String) object1.get("nation_name"), 
				     					(String) object2.get("nation_name"));
				     		break;
	          			case 1:
	          				result = -Collator.getInstance(Locale.CHINESE)
	          					.compare((String) object1.get("nation_name"), 
	          							(String) object2.get("nation_name"));
	          				break;
		          		}
		          		break;
		          	case 2:
		          		switch (settings.getInt("OrderMain", 0)) {
			          		case 0:
				     		result = ((String) object1.get("money_name"))
				     			.compareTo((String) object2.get("money_name"));
			          			break;
			          		case 1:
	          				result = -((String) object1.get("money_name"))
	          					.compareTo((String) object2.get("money_name"));
			          			break;
		          		}
		          		break;
		          	}
		          	return result;
		     	}     
		     });     
		}

		return list;
	}

	/**
	 * 响应屏幕数字键、退格键按钮事件。原程序为按钮弹起后方相应事件。
	 */
	public boolean onKeyUp (int keyCode, KeyEvent event) {
		
    	if (KeyEvent.KEYCODE_0<=keyCode && keyCode<=KeyEvent.KEYCODE_9
    			|| (keyCode == KeyEvent.KEYCODE_PERIOD && !title_amount.contains(".")) ) {
    		
    		if (keyCode == KeyEvent.KEYCODE_PERIOD ) {
    			title_amount += ".";
    		}
    		else {
    			title_amount += String.valueOf(keyCode - 7);
    		}
    		
    		if (  13 <= title_amount.length()&& title_amount.length() <= 20
    				&& UI_flag.equals("direct")) {
        		title.setTextSize(title.getTextSize() - 1);
        	}
    		
    	}
    	
    	else if (keyCode == KeyEvent.KEYCODE_DEL) {
    		if (title_amount.length() != 0){
    			title_amount = title_amount.substring(0, title_amount.length()-1);
    			
	    		if (  12 <= title_amount.length()&& title_amount.length() <= 19
	    				&& UI_flag.equals("direct")) {
	        		title.setTextSize(title.getTextSize() + 1);
	        	}
    		}
    		//DisplayToast("title大小"+title.getTextSize());
    	}
    	
    	if (UI_flag.equals("direct")) {
    		title.setText(title_amount +" "+ title_name);
    		UpdateList();
    	}
    	else {
    		i_UpdateList();
    	}
    	
    	return super.onKeyUp(keyCode, event);
    }
	
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
		case KeyEvent.KEYCODE_BACK:
			SharedPreferences settings = getSharedPreferences("settings",0);
			SharedPreferences.Editor editor = settings.edit();
			
			if (settings.getBoolean("UseLastAmount", false)) {
			    editor.putString("LastAmount", title_amount);
			}
			else {
				editor.putString("LastAmount", "");
			}

			String str_rates = new String();
			for (int i=0;i<ConstData.rates.length;i++) {
				for (int j=0;j<ConstData.rates[i].length;j++) {
					str_rates += String.valueOf(ConstData.rates[i][j]) + "#";
					editor.putString("Rates", str_rates);
				}
			}
			
		    editor.commit();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 显示Toast提示
	 * @param str:显示的内容
	 */
	public void DisplayToast (String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 更新ListView的内容
	 */
	public void UpdateList () {
		for (int i=0;i<m_Adapter.getCount();i++) {
			TextView amount = (TextView)m_Adapter.getView(i, null, null).findViewById(R.id.money_amount);
			int length = amount.length();
			//Log.i("Debug", String.valueOf(length));
			if (length<10) {
				UpdateAdapter(R.layout.vlist);
			}
			if (length>17) {
				UpdateAdapter(R.layout.vlist_15);
			}
			switch (length) {
				case 10:
				case 11:
					UpdateAdapter(R.layout.vlist_23);
					break;
				case 12:
				case 13:
					UpdateAdapter(R.layout.vlist_21);
					break;
				case 14:
				case 15:
				case 16:
					UpdateAdapter(R.layout.vlist_19);
					break;
				case 17:
					UpdateAdapter(R.layout.vlist_17);
					break;
			}
		}
	}
	
	/**
	 * inverse后更新ListView内容
	 */
	public void i_UpdateList () {
		for (int i=0;i<m_Adapter.getCount();i++) {
			TextView amount = (TextView)m_Adapter.getView(i, null, null).findViewById(R.id.money_symbol);
			int length = amount.length();
			//Log.i("Debug", String.valueOf(length));
			if (length<10) {
				i_UpdateAdapter(R.layout.vlist);
			}
			if (length>17) {
				i_UpdateAdapter(R.layout.vlist_15);
			}
			switch (length) {
				case 10:
				case 11:
					i_UpdateAdapter(R.layout.vlist_23);
					break;
				case 12:
				case 13:
					i_UpdateAdapter(R.layout.vlist_21);
					break;
				case 14:
				case 15:
				case 16:
					i_UpdateAdapter(R.layout.vlist_19);
					break;
				case 17:
					i_UpdateAdapter(R.layout.vlist_17);
					break;
			}
		}
	}
	
	/**
	 * 刷新SimpleAdapter的内容
	 * @param layout:刷新后的布局
	 */
	public void UpdateAdapter (int layout) {
		
		m_Adapter = new SimpleAdapter(this,getData(),layout,
				new String[]{"nation_name","money_name","flag"
							,"money_symbol","money_amount","nation_mark"},
				new int []{R.id.nation_name,R.id.money_name,R.id.flag,
							R.id.money_symbol,R.id.money_amount,R.id.nation_mark});
		m_ListView.setAdapter(m_Adapter);

	}
	
	/**
	 * Inverse后刷新Adapter的内容，
	 * @param layout:刷新后的布局
	 */
	public void i_UpdateAdapter (int layout) {
		m_Adapter = new SimpleAdapter(this,i_getData(),layout,
				new String[]{"nation_name","money_name","flag","foreign_amount",
							"money_symbol","money_amount","nation_mark"},
				new int []{R.id.nation_name,R.id.money_name,R.id.flag,R.id.foreign_amount,
							R.id.money_symbol,R.id.money_amount,R.id.nation_mark});
		m_ListView.setAdapter(m_Adapter);
	}
	
	/**
	 * 根据设置的格式将时间日期显示在主界面上
	 * @param date:需要显示的时间日期
	 */
	public void setDateTime(Date date) {
		
		SimpleDateFormat DateTimeFormat = null;
		SharedPreferences settings = getSharedPreferences("settings",0);
		
		//设置日期格式
		TextView Update_date = (TextView) findViewById(R.id.main_update);
			
		switch(settings.getInt("DateFormat", 2)) {
			
			case 0:
				DateTimeFormat = new SimpleDateFormat("MM-dd-yyyy");
				break;
			case 1:
				DateTimeFormat = new SimpleDateFormat("dd-MM-yyyy");
				break;
			case 2:
				DateTimeFormat = new SimpleDateFormat("yyyy-MM-dd");
				break;
		}
		
		//显示在主界面上
		Update_date.setText(DateTimeFormat.format(date));
		
		//TODO
		//设置时间格式
		TextView Update_time = (TextView) findViewById(R.id.time_update);
			
		switch(settings.getInt("TimeFormat", 1)) {
		
		case 0:
			DateTimeFormat = new SimpleDateFormat("a hh:mm");
			break;
		case 1:
			DateTimeFormat = new SimpleDateFormat("HH:mm");
			break;
		}
		
		//显示在主界面上
		Update_time.setText(DateTimeFormat.format(date));
		
		
		//Log.i("now", SavedFormat.format(now));	
		//Date before = SavedFormat.parse("2011-4-20 00:00:00");
	}
	
	/**
	 * 从互联网获取并且更新所有的汇率
	 */
	public void UpdatesAllRates() {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();

				for (int i=0;i<ConstData.code.length-2;i++) {
							
					URL url = new URL("http://themoneyconverter.com/"+ConstData.code[i]+"/rss.xml");
					InputStreamReader input = new InputStreamReader(url.openStream(),"UTF-8");
					parser.setInput(input);
					int eventType = parser.getEventType();
					
					do {
						
						for (int j=0;j<ConstData.code.length-2;j++) {
							
							if( eventType == XmlPullParser.TEXT  && i!=j
									&& parser.getText().equals(ConstData.code[j]+"/"+ConstData.code[i])) {
									
								while (eventType != XmlPullParser.START_TAG 
										|| !parser.getName().equals("description")) {
		
									eventType = parser.next();
									
								}
								
								String [] nums = parser.nextText().split("\\D+");
								ConstData.rates [i][j] = Double.parseDouble(nums[1]+"."+nums[2]);
								//Log.i(j+"/"+ConstData.code[i], String.valueOf(ConstData.rates [i][j]));
								
							}
						}
							
						eventType = parser.next();
						
					} while (eventType != XmlPullParser.END_DOCUMENT);
					
					input.close();
					//Log.i(i+"/"+ConstData.code[i], String.valueOf(ConstData.rates [i][0]));	
				}
				
				
				//设置日期格式
				Date now = new Date();
				setDateTime(now);
				//保存更新日期
				SimpleDateFormat SavedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SharedPreferences settings = getSharedPreferences("settings",0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("UpdatesTime", SavedFormat.format(now));
				editor.commit();
				
			} catch (Exception e) {
				
				AlertDialog ErrorDlg = new AlertDialog.Builder(CurrencyExchange.this)
    			.setTitle("无法更新！")
    			.setMessage("无法从网络获取当前汇率\n" +
    						"请检查网络")
    		    .setPositiveButton("确定", 
    		    		new DialogInterface.OnClickListener () {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
    		    	
    		    })
    			.create();
    			ErrorDlg.show();
    			//TODO
				Log.e("ERROR", e.toString());
			}
		
	}
	
	//创建menu
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		//设置menu界面为res/menu/menu.xml
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	/*处理菜单事件*/
	public boolean onOptionsItemSelected(MenuItem item)
	{
		//得到当前选中的MenuItem的ID,
		int item_id = item.getItemId();
        /* 新建一个Intent对象 */
		Intent intent=new Intent();
		switch (item_id)
		{
			case R.id.setting:
				
				SharedPreferences settings = getSharedPreferences("settings",0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("LastAmount", title_amount);
				editor.commit();
				
				/* 指定intent要启动的类 */
				intent.setClass(CurrencyExchange.this, Settings.class);
				/* 启动一个新的Activity */
				startActivity(intent);
				/* 关闭当前的Activity */
				CurrencyExchange.this.finish();
				break;
			case R.id.help:
				intent.setClass(CurrencyExchange.this,Help.class);
				startActivity(intent);
				CurrencyExchange.this.finish();
				break;
			case R.id.back:
				break;
		}
		return true;
	}
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SharedPreferences settings = getSharedPreferences("settings",0);

        //TODO
        try {
            //计算距离最后一次更新的时间
            SimpleDateFormat SavedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date before = SavedFormat.parse(settings.getString("UpdatesTime", "2011-4-20 00:00:00"));
			long time = new Date().getTime() - before.getTime();
			long hour = time/(60*60*1000);
			
			//如果大于等于设置的自动更新时间那么便更新
			if (hour >= settings.getInt("Updates", 24)) {
				UpdatesAllRates();
			}
		//TODO
			else {
				String str_rates = new String();
				for (int i=0;i<ConstData.rates.length;i++) {
					for (int j=0;j<ConstData.rates[i].length;j++) {
						str_rates += String.valueOf(ConstData.rates[i][j]) + "#";
					}
				}        
		
				Log.i("String str_rates", str_rates);
				
		        String [] rates = new String[484];
		        rates = settings.getString("Rates", str_rates).split("#");
		
				Log.i("String []rates", String.valueOf(rates.length));
		        
		        int count = 0;
				for (int i=0;i<ConstData.rates.length;i++) {
					for (int j=0;j<ConstData.rates[i].length;j++) {
						ConstData.rates[i][j] = Double.parseDouble(rates[count]);
						++count;
					}
				}
				//TODO
				//设置一下上次更新的时间
				setDateTime(before);
				//TODO
			}
			
			
		} catch (ParseException e1) {

			e1.printStackTrace();
		}
		
		int compare_nation = settings.getInt("UserCompare", 1);
		
		ConstData.rates[compare_nation][21] = settings.getFloat("UserRate", 1.00000f);
		ConstData.rates[21][compare_nation] = 1/settings.getFloat("UserRate", 1.00000f);
		
        String def = "true#true#true#true#true#true#true#true" +
        			"#false#false#false#false#false#false#false#false" +
        			"#false#false#false#false#false#false#";
        String [] selected = settings.getString("SelectedNation", def).split("#");
        for (int i=0;i<22;i++) {
        	selected_nation[i] = Boolean.parseBoolean(selected[i]);
        }
        int value_Decimals = settings.getInt("Decimals", 4);
        int value_Grouping = settings.getInt("Grouping", 1);
        int value_Separator = settings.getInt("Separator", 1);
		d_format_list = (DecimalFormat) DecimalFormat.getInstance();
		d_format_list.setGroupingSize(3);
		DecimalFormatSymbols symbol = new DecimalFormatSymbols();

		for (int i=0;i<10;i++) {
			if (value_Decimals == i) {
				d_format_list.setMinimumFractionDigits(i);
				d_format_list.setMaximumFractionDigits(i);
				break;
			}
		}
		
		switch (value_Grouping) {
		case 0:
			d_format_list.setGroupingUsed(false);
			break;
		case 1:
			d_format_list.setGroupingUsed(true);
			symbol.setGroupingSeparator(',');
			break;
		case 2:
			d_format_list.setGroupingUsed(true);
			symbol.setGroupingSeparator('.');
			break;
		case 3:
			d_format_list.setGroupingUsed(true);
			symbol.setGroupingSeparator(' ');
			break;
		case 4:
			d_format_list.setGroupingUsed(true);
			symbol.setGroupingSeparator('\'');
			break;
		}
		
		switch (value_Separator) {
		case 0:
			symbol.setDecimalSeparator(',');
			break;
		case 1:
			symbol.setDecimalSeparator('.');	
			break;
		case 2:
			symbol.setDecimalSeparator(' ');
			break;
		case 3:
			symbol.setDecimalSeparator('\'');
			break;
		}
		
		d_format_list.setDecimalFormatSymbols(symbol);
		
		//d_format_title.setGroupingSize(3);
		//Log.i("Tag","hello");
        m_LinearLayout = (LinearLayout) findViewById(R.id.layout);
        m_ListView = new ListView(this);
        StdMoney_num = 0;
        
        UI_flag = "direct";
        
        title_amount = settings.getString("LastAmount", "");
        
        title_name = new String("CNY");
        title = (TextView) findViewById(R.id.title);
        title.setText(title_amount +" "+ title_name);
        
        Btn_clear = (Button) findViewById(R.id.clear);
        Btn_inverse = (Button) findViewById(R.id.inverse);
        Btn_edit = (Button) findViewById(R.id.edit);
        Btn_update = (Button) findViewById(R.id.update);
        
        m_Adapter = new SimpleAdapter(this,getData(),R.layout.vlist,
				new String[]{"nation_name","money_name","flag"
        						,"money_symbol","money_amount"},
				new int []{R.id.nation_name,R.id.money_name,R.id.flag
        						,R.id.money_symbol,R.id.money_amount});
        
		m_ListView.setAdapter(m_Adapter);
        m_LinearLayout.addView(m_ListView);
        
        TextView tax = (TextView) findViewById(R.id.main_tax);
        tax.setText(String.valueOf(settings.getFloat("Tax", 0)));
        
        //为ListView视图添加单击的事件监听
        m_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        			long arg3)  {
        		
        		HashMap<String, Object> map = (HashMap<String, Object>)m_Adapter.getItem(arg2);
        		
        		/*
        		((TextView)arg1.findViewById(R.id.nation_name)).setText("Hello");
        		m_Adapter.notifyDataSetChanged();
        		m_ListView.setAdapter(m_Adapter);
        		Log.i("View", ((TextView)arg1.findViewById(R.id.nation_name)).getText().toString());
        		*/        		 
        		 
        		title_name = map.get("money_name").toString();
        		StdMoney_num = Integer.parseInt(map.get("nation_mark").toString());
        		
        		
        		if (UI_flag.equals("direct")) {
            		title.setText(title_amount +" "+ title_name);
        			UpdateList();
        		}
        		else {
            		title.setText("Inverse" +" "+ title_name);
        			i_UpdateList();
        		}
        		
        	}
        });
        //为clear按钮设置事件监听
        Btn_clear.setOnClickListener(new Button.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		title_amount = "";
        		UpdateList();
        		title.setText(title_amount +" "+ title_name);
        	}
        });
        
        //为inverse按钮设置事件监听
        Btn_inverse.setOnClickListener(new Button.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if (UI_flag.equals("direct")) {
        			
		        	Btn_inverse.setText("Direct");
		        	UI_flag = "inverse";
		        	title.setText("Inverse" +" "+ title_name);
		        	i_UpdateList();
		        	
        		}
        		else {
        			Btn_inverse.setText("Inverse");
        			UI_flag = "direct";
            		title.setText(title_amount +" "+ title_name);
            		UpdateList();
        		}
        	}
        });
        
        //为edit按钮设置事件监听
        Btn_edit.setOnClickListener(new Button.OnClickListener() {
			@Override
        	public void onClick(View v) {
        		
				Intent intent = new Intent();
				intent.setClass(CurrencyExchange.this, SelectCurrencies.class);
				startActivity(intent);
				CurrencyExchange.this.finish();
				
        	}
        	
        });
        
        //为update按钮设置事件监听
        Btn_update.setOnClickListener(new Button.OnClickListener() {
			@Override
        	public void onClick(View v) {
				
					UpdatesAllRates();
					//更新列表ListView
					UpdateList();
				}
        });
    }

	
}
