package edu.cnm.deepdive.fizzbuzz;

import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GestureDetectorCompat;
import androidx.preference.PreferenceManager;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

  private Random rng = new Random();
  private int value;
  private TextView valueDisplay;
  private ViewGroup valueContainer;
  private Rect displayRect = new Rect();
  private GestureDetectorCompat detector;
  private Timer timer;
  private boolean running;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    valueDisplay = findViewById(R.id.value_display);
    valueContainer = (ViewGroup) valueDisplay.getParent();
    detector = new GestureDetectorCompat(this, new FlingListener());
    valueContainer.setOnTouchListener(this);
    // TODO Restore any necessary fields, set game state, etc.
  }

  @Override
  protected void onResume() {
    super.onResume();
    // TODO Resume game if running.
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    // TODO Save and necessary fields.
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    MenuItem play = menu.findItem(R.id.play);
    MenuItem pause = menu.findItem(R.id.pause);
    play.setEnabled(!running);
    play.setVisible(!running);
    pause.setEnabled(running);
    pause.setVisible(running);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.play:
        resumeGame();
        break;
      case R.id.pause:
        pauseGame();
        break;
      case R.id.settings:
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        break;
      default:
        handled = super.onOptionsItemSelected(item);
        break;
    }
    return handled;
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    boolean handled = false;
    if (running) {
      handled = detector.onTouchEvent(event);
      if (!handled && event.getActionMasked() == MotionEvent.ACTION_UP) {
        valueDisplay.setTranslationX(0);
        valueDisplay.setTranslationY(0);
        handled = true;
      }
    }
    return handled;
  }

  private void pauseGame() {
    running = false;
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
    // TODO Update any additional necessary fields.
    invalidateOptionsMenu();
  }

  private void resumeGame() {
    running = true;
    updateValue();
    // TODO Update any additional necessary fields.
    invalidateOptionsMenu();
  }

  private void updateValue() {
    int numDigits = PreferenceManager.getDefaultSharedPreferences(this)
        .getInt(getString(R.string.num_digits_key),
            getResources().getInteger(R.integer.num_digits_default));
    int valueLimit = (int) Math.pow(10, numDigits) - 1;
    int timeLimit = PreferenceManager.getDefaultSharedPreferences(this)
        .getInt(getString(R.string.time_limit_key),
            getResources().getInteger(R.integer.time_limit_default));
    if (timer != null) {
      timer.cancel();
    }
    value = 1 + rng.nextInt(valueLimit);
    String valueString = Integer.toString(value);
    valueDisplay.setTranslationX(0);
    valueDisplay.setTranslationY(0);
    valueDisplay.setText(valueString);
    valueDisplay.getPaint().getTextBounds(valueString, 0, valueString.length(), displayRect);
    displayRect.top += valueDisplay.getBaseline();
    displayRect.bottom += valueDisplay.getBaseline();
    if (timeLimit != 0) {
      timer = new Timer();
      timer.schedule(new TimeoutTask(), timeLimit * 1000);
    }
  }

  private class TimeoutTask extends TimerTask {

    @Override
    public void run() {
      runOnUiThread(() -> updateValue());
    }

  }

  private class FlingListener extends GestureDetector.SimpleOnGestureListener {

    private static final int RADIUS_FACTOR = 5;
    private static final double SPEED_THRESHOLD = 300;

    private float originX;
    private float originY;

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
      valueDisplay.setTranslationX(e2.getX() - originX);
      valueDisplay.setTranslationY(e2.getY() - originY);
      return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
      int containerHeight = valueContainer.getHeight();
      int containerWidth = valueContainer.getWidth();
      int radiusX = containerWidth / RADIUS_FACTOR;
      int radiusY = containerHeight / RADIUS_FACTOR;
      double deltaX = e2.getX() - e1.getX();
      double deltaY = e2.getY() - e1.getY();
      double ellipticalDistance =
          deltaX * deltaX / radiusX / radiusX + deltaY * deltaY / radiusY / radiusY;
      double speed = Math.hypot(velocityX, velocityY);
      if (speed >= SPEED_THRESHOLD && ellipticalDistance >= 1) {
        if (Math.abs(deltaY) * containerWidth <= Math.abs(deltaX) * containerHeight) {
          if (deltaX > 0) {
            Log.d("Trace", "Right fling");
          } else {
            Log.d("Trace", "Left fling");
          }
        } else {
          if (deltaY > 0) {
            Log.d("Trace", "Down fling");
          } else {
            Log.d("Trace", "Up fling");
          }
        }
        updateValue();
        return true;
      } else {
        return false;
      }
    }

    @Override
    public boolean onDown(MotionEvent evt) {
      if (displayRect.contains(Math.round(evt.getX()), Math.round(evt.getY()))) {
        originX = evt.getX() - valueDisplay.getTranslationX();
        originY = evt.getY() - valueDisplay.getTranslationY();
        return true;
      }
      return false;
    }

  }

}
