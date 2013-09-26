package com.andy.remind.util;

import java.io.IOException;

import com.andy.remind.Declare;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

public class ArriveAlarm {
	private static MediaPlayer musicPlayer = new MediaPlayer();

	public static void startAlarm(Context context) {
		Uri alarmUri;
		if (null != Declare.getUriAlarm()) {
			alarmUri = Declare.getUriAlarm();
		} else {
			alarmUri = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		}
		if (!musicPlayer.isPlaying()) {
			musicPlayer = MediaPlayer.create(context, alarmUri);
			try {
				musicPlayer.prepare();

			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			musicPlayer.start();
		}

	}
}
