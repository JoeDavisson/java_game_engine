// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Score
{
  public static int current = 0;
  public static int high = 0;
  public static int levelhigh = 0;

  public static void add(int amount)
  {
    current += amount;
    if(current > 99999999)
      current = 99999999;
    if(current > high)
      high = current;
  }

  public static void reset()
  {
    current = 0;
  }
}

