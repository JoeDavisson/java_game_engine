// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

import game2d.*;

public class Snd 
{
  // thrust
  public static Sample thrust = new Sample(4, 1000.0f, 0.3f, 0.0f, 0.0f, 0.1f, 0.0f, 1.0f);

  // shot
  public static Sample shot = new Sample(1, 800.0f, 0.5f, 0.0f, 0.2f, 0.0f, 0.0f, -2.0f);

  // ship explosion
  public static Sample shipexp = new Sample(4, 200.0f, 0.6f, 0.0f, 0.3f, 0.5f, 0.5f, 1.0f);

  // ufo1
  public static Sample ufo1 = new Sample(1, 180.0f, 0.4f, 0.0f, 0.0f, 1.0f, 0.0f, 0.1f);
  // ufo2
  public static Sample ufo2 = new Sample(1, 360.0f, 0.3f, 0.0f, 0.0f, 1.0f, 0.0f, 0.2f);

  // music
  public static Sample music1 = new Sample(4, 2, 0.5f, 0.0f, 0.1f, 0.0f, 0.1f, -0.1f, 6,
                           "q i q i qqiq  iqq i q i qqiq  iqq i q i qqiq  iqq i q i qqiq  iq");
  public static Sample music2 = new Sample(3, 0, 0.6f, 0.05f, 0.05f, 0.1f, 0.0f, 0.0f, 6,
                           "q   33 3r   5 55t   55 5r   3 33q   33 3r   5 55t   55 5r   3 33");
  public static Sample music3 = new Sample(1, 3, 0.6f, 0.05f, 0.2f, 0.0f, 0.2f, 0.0f, 6,
                           "io0[0oi io0[0oi[]         6t6trtio0[0oi io0[0oi[] [ 0 [0iq3r5t7i");
  // hit
  public static Sample hit = new Sample(2, 200.0f, 0.5f, 0.0f, 0.1f, 0.0f, 0.2f, -2.0f);
  // ufo explosion
  public static Sample ufoexp = new Sample(4, 50.0f, 0.6f, 0.0f, 0.3f, 0.0f, 0.5f, 4.0f);

  // rock explosion 1
  public static Sample rockexp1 = new Sample(4, 300.0f, 0.5f, 0.0f, 0.3f, 0.0f, 0.2f, 1.0f);

  // rock explosion 2
  public static Sample rockexp2 = new Sample(4, 500.0f, 0.5f, 0.0f, 0.3f, 0.0f, 0.2f, 1.0f);

  // rock explosion 3
  public static Sample rockexp3 = new Sample(4, 700.0f, 0.5f, 0.0f, 0.3f, 0.0f, 0.2f, 1.0f);

  // game over
  public static Sample gameover1 = new Sample(3, 2, 0.6f, 0.2f, 0.7f, 0.3f, 0.7f, 0.0f, 4,
                           "]pipi6i6r6rw");
  public static Sample gameover2 = new Sample(2, 1, 0.6f, 0.2f, 0.7f, 0.3f, 0.7f, 0.0f, 4,
                           "]pipi6i6r6rw");

  // powerup
  public static Sample powerup1 = new Sample(2, 3, 0.5f, 0.1f, 0.5f, 0.2f, 0.5f, 0.0f, 3,
                           "qert et ");
  public static Sample powerup2 = new Sample(3, 1, 0.5f, 0.2f, 0.7f, 0.3f, 0.7f, 0.0f, 3,
                           "qert et ");
}

