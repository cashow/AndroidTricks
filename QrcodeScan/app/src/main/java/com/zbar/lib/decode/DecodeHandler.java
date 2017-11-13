package com.zbar.lib.decode;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cashow.qrcodescan.R;
import com.cashow.qrcodescan.app.ScanQrcodeActivity;
import com.dtr.zbar.build.ZBarDecoder;


/**
 * 作者: 陈涛(1076559197@qq.com)
 * 
 * 时间: 2014年5月9日 下午12:24:13
 * 
 * 版本: V_1.0.0
 * 
 * 描述: 接受消息后解码
 */
final class DecodeHandler extends Handler {

	public static final String TAG = "DecodeHandler";

	ScanQrcodeActivity activity = null;

	DecodeHandler(ScanQrcodeActivity activity) {
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case R.id.decode:
			if(activity != null && activity.getHandler()!=null){
				decode((byte[]) message.obj, message.arg1, message.arg2);
			}
			break;
		case R.id.quit://未被调用
			Log.i(TAG,"quit");
			getLooper().quit();
			break;
		}
	}

	/**
	 * 置空activity引用
	 * 退出当前线程的Looper循环
	 */
	public void quit(){
		Log.i(TAG,"quit");
		activity = null;
		getLooper().quit();
	}

	private void decode(byte[] data, int width, int height) {
		byte[] rotatedData = new byte[data.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++)
				rotatedData[x * height + height - y - 1] = data[x + y * width];
		}
		int tmp = width;// Here we are swapping, that's the difference to #11
		width = height;
		height = tmp;

        ZBarDecoder decoder = new ZBarDecoder();
		String result = null;
		if(null != activity) {
			result = decoder.decodeCrop(rotatedData, width, height, activity.getX(), activity.getY(), activity.getCropWidth(),
					activity.getCropHeight());
		}

		if (result != null) {
			if (null != activity && null != activity.getHandler()) {
				Message msg = new Message();
				msg.obj = result;
				msg.what = R.id.decode_succeeded;
				activity.getHandler().sendMessage(msg);
			}
		} else {
			if (null != activity && null != activity.getHandler()) {
				activity.getHandler().sendEmptyMessage(R.id.decode_failed);
			}
		}
	}

}
