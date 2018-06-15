package com.hz.junxinbaoan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.hz.junxinbaoan.MainActivity;
import com.hz.junxinbaoan.MyApplication;
import com.hz.junxinbaoan.activity.approval.ActivityApprovalList;
import com.hz.junxinbaoan.activity.mine.SchedulingActivity;
import com.hz.junxinbaoan.activity.sign.SignActivity;
import com.hz.junxinbaoan.activity.study.ActivityStudyList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JIGUANG-Example";

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
				//send the Registration Id to your server...

			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
				processCustomMessage(context, bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
				Logger.d(TAG, "[MyReceiver] 接收  到推送下来的通知的ID: " + notifactionId);

				if (MyApplication.mUserInfo.getAccess_token()==null||MyApplication.mUserInfo.getAccess_token().equals("")){
					return;
				}
				JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
				if (json.optString("type").equals("1")){
//					Intent i = new Intent(context,LoginActivity.class);
//					i.putExtra("type",9997);
// 					i.putExtra("name",MyApplication.mUserInfo.getLoginInfo().getUsername());
// 					i.putExtra("password",MyApplication.mUserInfo.getLoginInfo().getPassword());
//					i.putExtras(bundle);
//					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//					MyToast.showToast(context,"账号在别处登录!");
//					MyApplication.mUserInfo.delUserInfo();
//					MyApplication.getInstance().setExitLoginTag();
//					MyApplication.getInstance().removeAllActivity();
//					context.startActivity(i);
				}
			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
//				1.自己账号在别处登录：您的账号已在别处登录
//				2.进入签到区间（这个有点难）：您有签到任务，请及时签到
//				3.排班变更：您的排班有变更，请及时查看
//				4.审批通过：您的审批已通过
//				5.审批驳回：您的审批已驳回
//				6.学习中心增加：学习中心有新的更新，请及时查看！
//				7.发布公告：公告：XXXX（这个时标题）

				if (MyApplication.mUserInfo.getAccess_token()==null||MyApplication.mUserInfo.getAccess_token().equals("")){
					return;
				}
				//打开自定义的Activity
				Intent i = new Intent(context,MainActivity.class);
				i.putExtras(bundle);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
				switch (json.optString("type")){
					case "2":
						i.setClass(context, SignActivity.class);context.startActivity(i);
						break;
					case "3":i.setClass(context, SchedulingActivity.class);context.startActivity(i);
						break;
					case "4":i.setClass(context, ActivityApprovalList.class);context.startActivity(i);
						break;
					case "5":i.setClass(context, ActivityApprovalList.class);context.startActivity(i);
						break;
					case "6":i.setClass(context, ActivityStudyList.class);context.startActivity(i);
						break;
					case "7":
						i.putExtra("type","message");
						i.setClass(context, MainActivity.class);context.startActivity(i);
						break;
				}



			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
				Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
				Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
				Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Logger.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Logger.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//		}
	}
}
