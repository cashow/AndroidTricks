package com.zbar.lib.decode;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cashow.qrcodescan.app.ScanQrcodeActivity;

import java.util.concurrent.CountDownLatch;

/**
 * 作者: 陈涛(1076559197@qq.com)
 * 
 * 时间: 2014年5月9日 下午12:24:34
 *
 * 版本: V_1.0.0
 *
 * 描述: 解码线程
 */
final class DecodeThread extends Thread {

	public static final String TAG = "DecodeThread";

	ScanQrcodeActivity activity;
	private Handler handler;
	private final CountDownLatch handlerInitLatch;

	DecodeThread(ScanQrcodeActivity activity) {
		this.activity = activity;
		handlerInitLatch = new CountDownLatch(1);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	/**
	 * activity退出后，手动释放线程中的引用（线程无法立即结束）
	 */
	public void quit(){
		((DecodeHandler)handler).quit();
		handler = null;
		activity = null;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(activity);
		handlerInitLatch.countDown();
		Log.i(TAG,"run");
		Looper.loop();
	}

}
