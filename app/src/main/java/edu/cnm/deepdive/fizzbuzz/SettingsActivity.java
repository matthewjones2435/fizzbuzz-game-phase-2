package edu.cnm.deepdive.fizzbuzz;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SeekBarPreference;

public class SettingsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.settings_fragment_container, new SettingsFragment())
          .commit();
    }
  }

  public static class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
      setPreferencesFromResource(R.xml.preferences, rootKey);
      PreferenceScreen screen = getPreferenceScreen();
      SeekBarPreference numDigits =
          (SeekBarPreference) screen.findPreference(getString(R.string.num_digits_key));
      numDigits.setMin(1);
      SeekBarPreference gameTime =
          (SeekBarPreference) screen.findPreference(getString(R.string.game_time_key));
      gameTime.setMin(getResources().getInteger(R.integer.game_time_min));
      int gameIncrement = getResources().getInteger(R.integer.game_time_increment);
      gameTime.setOnPreferenceChangeListener((preference, newValue) -> {
        int roundedValue = Math.round((Integer) newValue / (float) gameIncrement) * gameIncrement;
        gameTime.setValue(roundedValue);
        return false;
      });
    }

  }

}
