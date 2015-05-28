package com.zkc.service;

import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;

import com.zkc.beep.ServiceBeepManager;
import com.zkc.pc700.helper.ScanGpio;
import com.zkc.receiver.RemoteControlReceiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.zkc.util.Constant;

public class CaptureService extends Service {

	static Context context;
	private static final String TAG = "CaptureService";
	public static ServiceBeepManager beepManager;// 扫描成功提示音

	// 屏幕状态广播
	RemoteControlReceiver screenStatusReceiver;

	/*
	 * 一维头恢复出厂设置
	 */
	public static byte[] defaultSetting1D = new byte[] { 0x04, (byte) 0xC8, 0x04, 0x00, (byte) 0xFF, 0x30 };// 出厂设置

	/*
	 * 一维头数据格式
	 */
	public static byte[] dataTypeFor1D = new byte[] { 0x07, (byte) 0xC6, 0x04, 0x08, 0x00, (byte) 0xEB, 0x07, (byte) 0xFE, 0x35 };// 读取数据格式

	/*
	 * 二维头恢复出厂设置
	 */
	public static byte[] defaultSetting2D = new byte[] { 0x16, 0x4D, 0x0D, 0x25, 0x25, 0x25, 0x44, 0x45, 0x46, 0x2E };

	/*
	 * 二维头数据格式
	 */
	public static byte[] dataTypeFor2D = new byte[] { 0x16, 0x4D, 0x0D, 0x38, 0x32, 0x30, 0x32, 0x44, 0x30, 0x31, 0x2E };

	public static SerialPortClass serialPort = null;

	public static String choosed_serial = "/dev/ttyMT0";// 串口号
	public static int choosed_buad = 9600;// 波特率
	public static ScanGpio scanGpio = new ScanGpio();

	public static String barCodeStr = "";
	public static byte[] barCodeHex = new byte[4096];
	public static int barCodeLen = 0;

	@Override
	public IBinder onBind(Intent intent) {
		Log.v(TAG, "CaptureService onBind");
		return null;
	}

	/**
	 * 显示信息Handle
	 */
	public static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			byte[] getdata = (byte[]) msg.obj;
			int size = msg.arg1;
			if (size < 2) {
				return;
			}
			System.arraycopy(getdata, 0, barCodeHex, barCodeLen, size);
			barCodeLen += size;
			try {
				barCodeStr += new String(getdata, 0, size, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			if (getdata[size - 1] == 13 || getdata[size - 1] == 10) {
				try {
					// 播放扫描成功提示音
					if (beepManager != null) {
						beepManager.playBeepSoundAndVibrate();
					}

					byte[] btdata = new byte[barCodeLen];
					System.arraycopy(barCodeHex, 0, btdata, 0, btdata.length);

					barCodeStr = new String(btdata, 0, btdata.length, "UTF-8");
					barCodeStr = barCodeStr.trim();

					if (!RootCommand("input text " + barCodeStr)) {
						// 发送广播消息
						Intent intentBroadcast = new Intent();
						intentBroadcast.setAction(Constant.SCAN_ACTION_NAME);
						intentBroadcast.putExtra(Constant.SCAN_CODE, barCodeStr);
						context.sendBroadcast(intentBroadcast);
					}
					barCodeStr = "";
					barCodeHex = new byte[4096];
					barCodeLen = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	};

	/**
	 * 将byte数组转换为字符串形式表示的十六进制数方便查看
	 */
	public static StringBuffer bytesToString(byte[] bytes, int size) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < size; i++) {
			String s = Integer.toHexString(bytes[i] & 0xff);
			if (s.length() < 2) {
				sBuffer.append('0');
			}
			sBuffer.append(s + " ");
		}
		return sBuffer;
	}

	/**
	 * * Send a single key event.
	 * 
	 * @param event
	 *            is a string representing the keycode of the key event you want
	 *            to execute.
	 */
//	private static void sendKeyEvent(String codeStr) {
//		try {
//			String keyCommand = "input text " + codeStr;
//			Runtime runtime = Runtime.getRuntime();
//			Process proc = runtime.exec(keyCommand);
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * 传入需要的键值即可
	 * 
	 * @param keyCode
	 */
//	private static void sendKeyCode(final int keyCode) {
//		new Thread() {
//			public void run() {
//				try {
//					Instrumentation inst = new Instrumentation();
//					inst.sendKeyDownUpSync(keyCode);
//				} catch (Exception e) {
//					Log.e("Exception when sendPointerSync", e.toString());
//				}
//			}
//		}.start();
//	}

	public static boolean RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			// process.waitFor();
		} catch (Exception e) {
			Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				Thread.sleep(10);
				process.destroy();
			} catch (Exception e) {
			}
		}
		return true;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(TAG, "CaptureService onCreate");

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		context = this;
		Log.v(TAG, "CaptureService onStart");
		// 创建提示音
		beepManager = new ServiceBeepManager(this);
		beepManager.updatePrefs();

		CaptureService.serialPort = new SerialPortClass(CaptureService.choosed_serial, CaptureService.choosed_buad);

		// 打开模块电源
		scanGpio.openPower();
		/*
		 * try { Thread.sleep(500); } catch (InterruptedException e) {
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		/*
		 * // 一维头设置数据格式 CaptureService.serialPort.Write(dataTypeFor1D);
		 * 
		 * try { Thread.sleep(500); } catch (InterruptedException e) {
		 * Auto-generated catch block e.printStackTrace(); } // 二维头设置数据格式
		 * CaptureService.serialPort.Write(dataTypeFor2D);
		 */

		/*
		 * for (int i = 0; i < 15; i++) { CaptureService.scanGpio.openScan(); }
		 */

		// 屏幕状态广播初始化
		screenStatusReceiver = new RemoteControlReceiver();
		IntentFilter screenStatusIF = new IntentFilter();
		screenStatusIF.addAction(Intent.ACTION_SCREEN_ON);
		screenStatusIF.addAction(Intent.ACTION_SCREEN_OFF);
		screenStatusIF.addAction(Intent.ACTION_SHUTDOWN);
		// screenStatusIF.addAction("com.lynk.Receiver.RemoteControlReceiver");
		// 注册
		registerReceiver(screenStatusReceiver, screenStatusIF);
	}

	@Override
	public void onDestroy() {
		Log.v(TAG, "CaptureService onDestroy");
		scanGpio.closePower();
		if (serialPort != null) {
			serialPort.CloseDevice();
			serialPort = null;
		}
		unregisterReceiver(screenStatusReceiver);
		super.onDestroy();
	}

}
