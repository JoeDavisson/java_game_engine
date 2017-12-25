// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Level
{
  public static int current;

  public static int shots;

//  public static int rocks;

//  public static boolean ufo1;

//  public static int maxlevels = 6;

  public static void reset()
  {
    shots = 3;
//    rocks = 0;

 //   ufo1 = false;
  }

  public static void set(int num)
  {
    current = num;
    reset();
  }
}
