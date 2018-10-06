package com.why.project.sharewithsystemdemo;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.why.project.sharewithsystemdemo.util.ShareIntentUtil;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		onePermission();

		initEvents();
	}

	private void initEvents() {
		//分享文本
		findViewById(R.id.btn_share_text).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ShareIntentUtil.shareText(MainActivity.this,"这是一段分享的文字","分享文本");
			}
		});

		//分享单张图片
		findViewById(R.id.btn_share_one_img).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Environment.getExternalStorageDirectory()=/storage/emulated/0
				Log.e("why","Environment.getExternalStorageDirectory()="+Environment.getExternalStorageDirectory());
				String imagePath = Environment.getExternalStorageDirectory() + File.separator + "DCIM/Camera/IMG_20160723_103940.jpg";

				ShareIntentUtil.shareOneImg(MainActivity.this,imagePath,"分享单张图片");

			}
		});

		//分享多张图片
		findViewById(R.id.btn_share_mult_img).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Environment.getExternalStorageDirectory()=/storage/emulated/0
				Log.e("why","Environment.getExternalStorageDirectory()="+Environment.getExternalStorageDirectory());

				ArrayList<String> imgPathList = new ArrayList<>();

				String path = Environment.getExternalStorageDirectory() + File.separator;
				imgPathList.add(path+"DCIM/Camera/IMG_20160723_103940.jpg");
				imgPathList.add(path+"DCIM/Camera/IMG_20170820_121408.jpg");
				imgPathList.add(path+"DCIM/Camera/IMG_20171001_080012.jpg");

				ShareIntentUtil.shareMultImg(MainActivity.this,imgPathList,"分享多张图片");
			}
		});

		//分享到qq
		findViewById(R.id.btn_share_to_qq).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				ShareIntentUtil.shareTextTo(MainActivity.this,"这是一段分享的文字","分享到QQ",ShareIntentUtil.PACKAGE_QQ);

			}
		});

		//分享单个文件
		findViewById(R.id.btn_share_one_file).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Environment.getExternalStorageDirectory()=/storage/emulated/0
				Log.e("why","Environment.getExternalStorageDirectory()="+Environment.getExternalStorageDirectory());
				String filePath = Environment.getExternalStorageDirectory() + File.separator + "why/AndroidNotesForProfessionals.pdf";

				ShareIntentUtil.shareOneFile(MainActivity.this,filePath,"分享单个文件");

			}
		});
	}


	/**只有一个运行时权限申请的情况*/
	private void onePermission(){
		RxPermissions rxPermissions = new RxPermissions(MainActivity.this); // where this is an Activity instance
		rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE) //权限名称，多个权限之间逗号分隔开
				.subscribe(new Consumer<Boolean>() {
					@Override
					public void accept(Boolean granted) throws Exception {
						Log.e(TAG, "{accept}granted=" + granted);//执行顺序——1【多个权限的情况，只有所有的权限均允许的情况下granted==true】
						if (granted) { // 在android 6.0之前会默认返回true
							// 已经获取权限
							Toast.makeText(MainActivity.this, "已经获取权限", Toast.LENGTH_SHORT).show();
						} else {
							// 未获取权限
							Toast.makeText(MainActivity.this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
						}
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) throws Exception {
						Log.e(TAG,"{accept}");//可能是授权异常的情况下的处理
					}
				}, new Action() {
					@Override
					public void run() throws Exception {
						Log.e(TAG,"{run}");//执行顺序——2
					}
				});
	}
}
