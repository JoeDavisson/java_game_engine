// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

public class Col
{
  private static RgbColor rgb = new RgbColor();   

  public static int makeRGB(int r, int g, int b)
  {
    return b | (g << 8) | (r << 16) | 0xFF000000;
  }

  public static int makeRGBA(int r, int g, int b, int a)
  {
    return b | (g << 8) | (r << 16) | (r << 24);
  }

  public static int makeHSV(int h, int s, int v)
  {
    rgb.hsvToRgb(h, s, v);

    return Col.makeRGB(rgb.r, rgb.g, rgb.b);
  }

  public static int getr(int c)
  {
    return (c >> 16) & 0xFF;
  }

  public static int getg(int c)
  {
    return (c >> 8) & 0xFF;
  }

  public static int getb(int c)
  {
    return c & 0xFF;
  }
}

