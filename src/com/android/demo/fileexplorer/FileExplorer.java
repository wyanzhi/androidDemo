package com.android.demo.fileexplorer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FileExplorer extends Activity {
	ListView listView;
	TextView textView;
	File currentParent;
	File[] currentFiles;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		listView = (ListView) findViewById(R.id.list);
		textView = (TextView) findViewById(R.id.path);
		//File root = new File("/mnt/sdcard/");
		File root = new File("/");
		if(root.exists())
		{
			currentParent = root;
			currentFiles = root.listFiles();
			inflateListView(currentFiles);
		}
		else
			Toast.makeText(this, "Error!", 2000).show();
		
		listView.setOnItemClickListener(new  OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) 
			{
				if (position == 0)
				{	onBackPressed();
					return;
				}
				position--;
				
				if(currentFiles[position].isFile())
					return;
				File[] temp = currentFiles[position].listFiles();
				if(temp == null || temp.length == 0)
					Toast.makeText(FileExplorer.this, "当前路径不可访问或该路径下无文件", 5000).show();
				else
				{
					currentParent = currentFiles[position];
					currentFiles = temp;
					inflateListView(currentFiles);
				}
				
			}
			
		}
		);
		/*
		Button parent = (Button) findViewById(R.id.parent);
		parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View source) 
			{
				try
				{
					if(!currentParent.getCanonicalPath().equals("/mnt/sdcard"))
					{
						currentParent = currentParent.getParentFile();
						currentFiles = currentParent.listFiles();
						inflateListView(currentFiles);
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				
			}
		});
		*/
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		try
		{
			//if(!currentParent.getCanonicalPath().equals("/mnt/sdcard"))
		    if(!currentParent.getCanonicalPath().equals("/"))
			{
				currentParent = currentParent.getParentFile();
				currentFiles = currentParent.listFiles();
				inflateListView(currentFiles);
			}
			else
				finish();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file_explorer, menu);
		//super.onCreateOptionsMenu(menu);
      //  menu.add(Menu.NONE, 1, 0, "关于").setIcon(R.drawable.about);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 super.onOptionsItemSelected(item);
		 switch(item.getItemId())
		 {
		 case R.id.action_settings:
			 Toast.makeText(this, "Designed by Yanzhi, version 0.1", 3500).show();
			 break;
			 default:
				 break;
		 	
		 }
		 return true;
	}

	private void inflateListView(File[] files)
	{
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		Map<String, Object> listItem1 = new HashMap<String, Object>();
		listItem1.put("icon", R.drawable.back);
		listItem1.put("fileName", "..返回");
		listItems.add(listItem1);
		for(int i=0; i<files.length; i++)
		{
			Map<String, Object> listItem = new HashMap<String, Object>();
			if(files[i].isDirectory())
				listItem.put("icon", R.drawable.folder);
			else
				listItem.put("icon",R.drawable.file);
			
			listItem.put("fileName", files[i].getName());
			listItems.add(listItem);
			
		}
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.line, 
				new String[] {"icon","fileName"}, new int[] {R.id.icon, R.id.file_name});
		listView.setAdapter(simpleAdapter);
		try
		{
			textView.setText("当前路径为："+currentParent.getCanonicalPath());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
