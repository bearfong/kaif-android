package io.kaif.mobile;

import android.app.Application;

public class KaifApplication extends Application {

  private static KaifApplication INSTANCE;

  private Beans beans;

  @Override
  public void onCreate() {
    super.onCreate();
    INSTANCE = this;
    beans = Beans.Initializer.init(this);
    beans.inject(this);
  }

  public static KaifApplication getInstance() {
    return INSTANCE;
  }

  public Beans beans() {
    return beans;
  }

}
