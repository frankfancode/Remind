package com.andy.remind.util;
/**
 * 
 */


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.text.Html;
import android.text.Spanned;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.search.core.LatLonPoint;
import com.andy.remind.R;

public class AMapUtil {
	/**
	 * AMap对象判断是否为null
	 */
	public static boolean checkReady(Context context, AMap aMap) {
		if (aMap == null) {
			ToastUtil.show(context, R.string.map_not_ready);
			return false;
		}
		return true;
	}

	public static Spanned stringToSpan(String src) {
		return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
	}

	public static String colorFont(String src, String color) {
		StringBuffer strBuf = new StringBuffer();

		strBuf.append("<font color=").append(color).append(">").append(src)
				.append("</font>");
		return strBuf.toString();
	}

	public static String makeHtmlNewLine() {
		return "<br />";
	}

	public static String makeHtmlSpace(int number) {
		final String space = "&nbsp;";
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < number; i++) {
			result.append(space);
		}
		return result.toString();
	}

	public static String getFriendlyLength(int lenMeter) {
		if (lenMeter > 10000) // 10 km
		{
			int dis = lenMeter / 1000;
			return dis + ChString.Kilometer;
		}

		if (lenMeter > 1000) {
			float dis = (float) lenMeter / 1000;
			DecimalFormat fnum = new DecimalFormat("##0.0");
			String dstr = fnum.format(dis);
			return dstr + ChString.Kilometer;
		}

		if (lenMeter > 100) {
			int dis = lenMeter / 50 * 50;
			return dis + ChString.Meter;
		}

		int dis = lenMeter / 10 * 10;
		if (dis == 0) {
			dis = 10;
		}

		return dis + ChString.Meter;
	}

	public static boolean IsEmptyOrNullString(String s) {
		return (s == null) || (s.trim().length() == 0);
	}

	/**
	 * 把LatLng对象转化为LatLonPoint对象
	 */
	public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
		return new LatLonPoint(latlon.latitude, latlon.longitude);
	}

	/**
	 * long类型时间格式化
	 */
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}

	public static final String HtmlBlack = "#000000";
	public static final String HtmlGray = "#808080";
	
	
	public static void drawLine(AMap aMap,LatLng start, LatLng end) {
		int x1 = aMap.getProjection().toScreenLocation(start).x;
		int y1 = aMap.getProjection().toScreenLocation(start).y;
		int x2 = aMap.getProjection().toScreenLocation(end).x;
		int y2 = aMap.getProjection().toScreenLocation(end).y;
		double arrow_height = 10;// 箭头高度
		double arrow_btomline = 7;// 底边的一半
		double arctangent = Math.atan(arrow_btomline / arrow_height);// 箭头角度
		double arrow_len = Math.sqrt(arrow_btomline * arrow_btomline
				+ arrow_height * arrow_height);// 箭头的长度

		double[] endPoint_1 = rotateVec(x2 - x1, y2 - y1, arctangent, arrow_len);
		double[] endPoint_2 = rotateVec(x2 - x1, y2 - y1, -arctangent,
				arrow_len);
		double x3 = x2 - endPoint_1[0];
		double y3 = y2 - endPoint_1[1];
		double x4 = x2 - endPoint_2[0];
		double y4 = y2 - endPoint_2[1];

		Point pointStart = new Point((int) x3, (int) y3);
		Point pointEnd = new Point((int) x4, (int) y4);
		LatLng start1 = aMap.getProjection().fromScreenLocation(pointStart);
		LatLng end1 = aMap.getProjection().fromScreenLocation(pointEnd);

		aMap.addPolyline(new PolylineOptions().add(start, end).width(3));// 绘制直线
		aMap.addPolyline(new PolylineOptions().add(end, start1)
				.color(Color.RED).width(3));// 绘制左箭头
		aMap.addPolyline(new PolylineOptions().add(end, end1).color(Color.RED)
				.width(3));// 绘制右箭头
	}

	public static double[] rotateVec(int px, int py, double ang, double newLen) {
		double rotateResult[] = new double[2];
		// 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、新长度
		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		double d = Math.sqrt(vx * vx + vy * vy);
		vx = vx / d * newLen;
		vy = vy / d * newLen;
		rotateResult[0] = vx;
		rotateResult[1] = vy;
		return rotateResult;
	}
	// 方法
	public static double getDistance(LatLng latlng1, LatLng latlng2) {
		float[] results = new float[1];
		if (null == latlng1 || null == latlng2) {
			return -1f;
		}

		Location.distanceBetween(latlng1.latitude, latlng1.longitude,
				latlng2.latitude, latlng2.longitude, results);
		return results[0];
	}

}
