package com.why.project.sharewithsystemdemo.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by haiyuKing
 * Used 调用系统自带的分享的工具类
 * 增加适配7.0FileProvider的功能
 */

public class ShareIntentUtil {

	//指定分享到的软件包名
	public static final String PACKAGE_QQ = "com.tencent.mobileqq";//分享到QQ
	public static final String PACKAGE_QZONE = "com.qzone";//分享会到QQ空间
	public static final String PACKAGE_WBLOG = "com.tencent.WBlog";//分享到腾讯微博
	public static final String PACKAGE_WXIN = "com.tencent.mm";//分享到微信
	public static final String PACKAGE_WEIBO = "com.sina.weibo";//分享到新浪微博
	public static final String PACKAGE_BAIDUYUN = "com.baidu.netdisk";//分享到百度云

	//分享文本
	public static void shareText(Context mContext, String shareText, String shareTitle){
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
		//适配7.0FileProvider
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件【很重要】
		}
		mContext.startActivity(Intent.createChooser(shareIntent, shareTitle));//可以设置标题
	}

	//分享文本到指定应用
	public static void shareTextTo(Context mContext, String shareText, String shareTitle, String packageName){
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setPackage(packageName);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
		//适配7.0FileProvider
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件【很重要】
		}

		//通过Intent的resolveActivity方法，并想该方法传入包管理器可以对包管理器进行查询以确定是否有Activity能够启动该Intent
		//https://blog.csdn.net/qq_15796477/article/details/72953514
		PackageManager pm = mContext.getPackageManager();
		ComponentName cn = shareIntent.resolveActivity(pm);
		if(cn == null){
			Toast.makeText(mContext,"未安装该应用",Toast.LENGTH_SHORT).show();
		}else {
			mContext.startActivity(Intent.createChooser(shareIntent, shareTitle));//可以设置标题
		}
	}

	//分享单张图片
	public static void shareOneImg(Context mContext, String imgPath, String shareTitle){
		File file = new File(imgPath);
		if (!file.exists()) {
			Toast.makeText(mContext,"文件不存在",Toast.LENGTH_SHORT).show();
			return;
		}

		//由文件得到uri
		Uri imageUri = getUri(mContext,file);

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
		shareIntent.setType("image/*");
		//适配7.0FileProvider
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件【很重要】
		}
		mContext.startActivity(Intent.createChooser(shareIntent, shareTitle));//可以设置标题
	}

	//分享多张图片
	public static void shareMultImg(Context mContext, ArrayList<String> imgPathList, String shareTitle){

		ArrayList<Uri> uriList = new ArrayList<>();

		for(int i=0;i<imgPathList.size();i++){
			File file = new File(imgPathList.get(i));
			if (!file.exists()) {
				Toast.makeText(mContext,"文件不存在",Toast.LENGTH_SHORT).show();
				return;
			}
			uriList.add(getUri(mContext,file));
		}

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
		shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
		shareIntent.setType("image/*");
		//适配7.0FileProvider
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件【很重要】
		}
		mContext.startActivity(Intent.createChooser(shareIntent, shareTitle));//可以设置标题
	}

	//分享单个文件
	public static void shareOneFile(Context mContext, String filePath, String shareTitle){
		File file = new File(filePath);
		if (!file.exists()) {
			Toast.makeText(mContext,"文件不存在",Toast.LENGTH_SHORT).show();
			return;
		}

		//由文件得到uri
		Uri fileUri = getUri(mContext,file);

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
		shareIntent.setType("*/*");
		//适配7.0FileProvider
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件【很重要】
		}
		mContext.startActivity(Intent.createChooser(shareIntent, shareTitle));//可以设置标题
	}

	//获取到uri--适配7.0FileProvider
	private static Uri getUri(Context mContext, File file){
		Uri uri;
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			String authority = mContext.getApplicationInfo().packageName + ".provider";
			uri = FileProvider.getUriForFile(mContext.getApplicationContext(), authority, file);
			//intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件【很重要】
		} else {
			uri = Uri.fromFile(file);
		}

		return uri;
	}
}
