// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Powers
{
  public static int level = 0;
  public static int shot_speed = 15;
  public static int shot_level = 0;
  public static boolean thrust = false;
  public static boolean thrustActive = false;
  public static int shield = 0;
  public static boolean wingman = false;

  public static void reset()
  {
    level = 0;
    shot_speed = 15;
    shot_level = 0;
    thrust = false;
    thrustActive = false;
    shield = 0;
    wingman = false;
  }

  public static void updateLevel()
  {
    if(level > 0)
      thrust = true;
    else
      thrust = false;
 
    shot_level = 0;

    if(level > 1)
      shot_level = 1;
    if(level > 2)
      shot_level = 2;
    if(wingman)
      shot_level = 3;

    if(level < 4)
      shield = 0;
    if(level < 5)
      wingman = false;
  }

  public static void removeLevel()
  {
    if(level > 3)
    {
      level = 3;
    }
    else
    {
      level--;
      if(level < 0)
        level = 0;
    }

    updateLevel(); 
  }
}
