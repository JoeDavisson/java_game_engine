// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

public class Int
{
  public static int max(int a, int b)
  {
    return a > b ? a : b;
  }

  public static int min(int a, int b)
  {
    return a < b ? a : b;
  }

  public static int abs(int a)
  {
    return a < 0 ? -a : a;
  }

  public static int sign(int a)
  {
    return a < 0 ? -1 : 1;
  }

  // distance
  public static int dist(int x1, int y1, int x2, int y2)
  {
    int dx = x1 - x2;
    int dy = y1 - y2;

    return (int)Math.sqrt(dx * dx + dy * dy);
  }

  // fast distance approximation
  public static int fdist(int x1, int y1, int x2, int y2)
  {
    int dx = x1 - x2;
    int dy = y1 - y2;
    dx = (dx < 0) ? -dx : dx;
    dy = (dy < 0) ? -dy : dy;

    return (dx + (dx << 1) + dy + (dy << 1)) >> 2;
  }

  // skips sqrt, for distance comparisions only
  public static int distcmp(int x1, int y1, int x2, int y2)
  {
    int dx = x1 - x2;
    int dy = y1 - y2;

    return (dx * dx + dy * dy);
  }
}

