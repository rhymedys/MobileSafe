package com.cc.mobilesafe.Interface;

/**
 * @author Rhymedys 定义一个回调方法，让用户选择是调用progressbar 还是 progreDialog setMax 是进度的总大小
 *         index是当前进度
 */
public interface SmsBackupProgressInterface {
	// 由自己决定是用对话框 还是进度条
	public void setMax(int max);

	public void setProgress(int index);

}
