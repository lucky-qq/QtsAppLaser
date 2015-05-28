package com.zkc.pc700.helper;

import com.zkc.io.EmGpio;

public class ScanGpio {
	// ���Ӵ���
	SerialPort serialPort = null;

	// �򿪵�Դ
	public void openPower() {
		try {
			if (true == EmGpio.gpioInit()) {
				// ��Դ����
				EmGpio.setGpioOutput(111);
				EmGpio.setGpioDataLow(111);
				Thread.sleep(100);
				// ��Դ����
				EmGpio.setGpioOutput(111);
				EmGpio.setGpioDataHigh(111);
				Thread.sleep(100);
			}
			EmGpio.gpioUnInit();
		} catch (Exception e) {
		}
	}

	public void closePower() {
		try {
			if (true == EmGpio.gpioInit()) {
				// ��Դ����
				EmGpio.setGpioOutput(111);
				EmGpio.setGpioDataLow(111);
				Thread.sleep(100);
				EmGpio.setGpioInput(111);
			}
			EmGpio.gpioUnInit();
		} catch (Exception e) {
		}
	}

	// ��ɨ��
	public void openScan() {
		// ����ɨ��
		try {
			if (true == EmGpio.gpioInit()) {
				EmGpio.setGpioOutput(110);
				EmGpio.setGpioDataHigh(110);
				Thread.sleep(100);
				EmGpio.setGpioDataLow(110);
			}
			EmGpio.gpioUnInit();
		} catch (Exception e) {
		}

	}

	// �ر�ɨ��
	public void closeScan() {
		// ����ɨ��
		try {
			if (true == EmGpio.gpioInit()) {
				EmGpio.setGpioOutput(110);
				EmGpio.setGpioDataHigh(110);
			}
			EmGpio.gpioUnInit();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
