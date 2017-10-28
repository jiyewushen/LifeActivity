package com.cx.library;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import java.util.List;

/**
 * Created by cx on 2017/10/26.
 */

public final class ActiveAppUtil {
    private  ActiveAppUtil(){
        try {
            throw new Exception("ActiveAppUtil not suppport init");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取前台应用
     * api小于21
     */
    public static String getActiveApp(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert activityManager != null;
        ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;
        return cn.getPackageName();
    }

    /**
     * 判断自身应用是否位于前台
     * 使用ActivityLifecycleCallbacks
     */


    /**
     * 获取前台应用
     * api大于20
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static String getActiveAppNew(Context context) {
        String result=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!hasPermission(context))
                context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            else {
                UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                long time = System.currentTimeMillis();
                assert mUsageStatsManager != null;
                UsageEvents events = mUsageStatsManager.queryEvents(time - 1000 * 10, time);
                 UsageEvents.Event event=new UsageEvents.Event();
                while (events.hasNextEvent()){
                    events.getNextEvent(event);
                    if (event.getEventType()== UsageEvents.Event.MOVE_TO_FOREGROUND){
                        result= event.getPackageName();
                    }
                }
            }

        }
        return result;
    }


    /**
     * 获取前台应用
     * api大于20
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static String getActiveAppNew1(Context context) {
        String result=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!hasPermission(context))
                context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            else {
                UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                long time = System.currentTimeMillis();
                assert mUsageStatsManager != null;
                List<UsageStats>usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,time-1000*10,time);
                    long lastTime=0;
                for (UsageStats usageStat : usageStats) {
                   if (usageStat!=null&&lastTime<usageStat.getLastTimeUsed()){
                       result=usageStat.getPackageName();
                       lastTime=usageStat.getLastTimeUsed();
                   }
                }
            }
        }
        return result;
    }


    /**
     * 获取跳转到当前应用的应用是什么，有可能是home键后跳入近期任务列表，再跳回到本应用，包名可能为com.system.ui
     * api大于20
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static String getPreviousApp(Context context) {
        String result=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!hasPermission(context))
                context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            else {
                UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                long time = System.currentTimeMillis();
                assert mUsageStatsManager != null;
                UsageEvents events = mUsageStatsManager.queryEvents(time - 1000 * 10, time);
                UsageEvents.Event event=new UsageEvents.Event();
                while (events.hasNextEvent()){
                    events.getNextEvent(event);
                    if (event.getEventType()== UsageEvents.Event.MOVE_TO_BACKGROUND){
                        result= event.getPackageName();
                    }
                }
            }

        }
        return result;
    }
















    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static boolean hasPermission(Context context) {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        assert appOpsManager != null;
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());
        return (mode == AppOpsManager.MODE_ALLOWED);
    }


}
