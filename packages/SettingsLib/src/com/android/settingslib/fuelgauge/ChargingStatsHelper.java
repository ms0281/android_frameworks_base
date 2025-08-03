package com.android.settingslib.fuelgauge;

import android.content.Context;
import android.provider.Settings;
import android.os.UserHandle;
import android.util.Slog;

import java.io.*;

public class ChargingStatsHelper {
    private static final String TAG = "ChargingStatsHelper";

    public static float readFloatFromFile(String path, float divisor) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            reader.close();
            return Float.parseFloat(line.trim()) / divisor;
        } catch (Exception e) {
            Slog.e(TAG, "Failed to read from " + path, e);
            return -1f;
        }
    }

    public static void saveToSettings(Context context, float current, float voltage, float wattage, float temperature) {
        try {
            if (context != null) {
                Settings.Secure.putFloatForUser(
                    context.getContentResolver(), "lockscreen_current", current, UserHandle.USER_CURRENT);
                Settings.Secure.putFloatForUser(
                    context.getContentResolver(), "lockscreen_voltage", voltage, UserHandle.USER_CURRENT);
                Settings.Secure.putFloatForUser(
                    context.getContentResolver(), "lockscreen_wattage", wattage, UserHandle.USER_CURRENT);
                Settings.Secure.putFloatForUser(
                    context.getContentResolver(), "lockscreen_temperature", temperature, UserHandle.USER_CURRENT);
            }
        } catch (Exception e) {
            Slog.e(TAG, "Failed to write to Settings.Secure", e);
        }
    }

    public static boolean isSysuiOrKeyguardContext(Context context) {
        if (context == null) return false;
        String pkg = context.getPackageName();
        return "com.android.systemui".equals(pkg) || "android".equals(pkg); // system_server-Kontext
    }
}

