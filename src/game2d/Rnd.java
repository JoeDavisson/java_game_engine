// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

public class Rnd
{
  private static int seed = 12345;

  public static void set(int num)
  {
    seed = Int.abs(num);
  }

  public static int get()
  {
    seed = Int.abs((seed << 17) ^ (seed >> 13) ^ (seed << 5));

    return seed;
  }
}

