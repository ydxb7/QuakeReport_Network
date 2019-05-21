package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    // 当偏好发生更改时，PreferenceFragment 可以实现 OnPreferenceChangeListener 接口 以获取通知。
    // 然后，当用户更改单个偏好 并进行保存时，将使用已更改偏好的关键字来调用 onPreferenceChange() 方法。
    // 请注意，此方法 将返回布尔值，可防止通过返回 false 来 更改建议的偏好设置。
    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            // 获取最小震级偏好对象
            Preference minMagnitude = findPreference(getString(R.string.settings_min_magnitude_key));
            // 把当前最小震级偏好写到屏幕上
            bindPreferenceSummaryToValue(minMagnitude);

            // 获取排序偏好
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            // 把排序偏好写到屏幕上
            bindPreferenceSummaryToValue(orderBy);

        }

        // 在偏好更改后更新已显示的偏好摘要。
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                // 是排序偏好变化
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                // 是最小震级偏好变化
                preference.setSummary(stringValue);
            }
            return true;
        }

        // 现在需要定义 bindPreferenceSummaryToValue() 帮助程序方法来设置当前 EarhtquakePreferenceFragment 实例，作为每个偏好上的侦听程序。
        // 还将读取 设备上 SharedPreferences 中存储的偏好当前值，然后在偏好摘要中进行显示（以便用户能够查看 偏好的当前值）。
        private void bindPreferenceSummaryToValue(Preference preference) {
            // 在当前preference上设置listener
            preference.setOnPreferenceChangeListener(this);
            // 读取 设备上 SharedPreferences 中存储的偏好当前值
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            // 把当前值设置在屏幕上
            onPreferenceChange(preference, preferenceString);
        }
    }

}
