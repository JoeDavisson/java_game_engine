// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Level
{
  public static int current;

  public static int rocks;
  public static int meteors;
  public static int ufos;
  public static int shots;
  public static int ufoshots;

  public static boolean ufo1;
  public static boolean ufo2;
  public static boolean ball1;
  public static boolean ship2;
  public static boolean sat1;

//  public static int maxlevels = 6;

  public static void reset()
  {
    rocks = 0;
    meteors = 0;
    ufos = 0;
    shots = 3;
    ufoshots = 0;

    ufo1 = false;
    ufo2 = false;
    ball1 = false;
    ship2 = false;
    sat1 = false;
  }

  public static void set(int num)
  {
    current = num;
    reset();

    rocks += 6;
    meteors += 2;

    if((num & 1) > 0)
    {
      meteors += 2;
    }

    if((num & 2) > 0)
    {
      ufos += 1;
      ufoshots += 1;
      ufo1 = true;
    }

    if((num & 4) > 0)
    {
      ufos += 1;
      ufoshots += 1;
      ufo2 = true;
    }

    if((num & 8) > 0)
    {
      ufos += 1;
      ufoshots += 1;
      ship2 = true;
    }

    if((num & 16) > 0)
    {
      ufos += 1;
      ufoshots += 1;
      sat1 = true;
    }

    if((num & 32) > 0)
    {
      ufos += 1;
      ufoshots += 1;
      ball1 = true;
    }
  }
}
