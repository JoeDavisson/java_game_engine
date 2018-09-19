// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

public class RgbColor
{
  public int r, g, b;

  public RgbColor()
  {
    r = 0;
    g = 0;
    b = 0;
  }

  public void hsvToRgb(int h, int s, int v)
  {
    if(s == 0)
    {
      r = v;
      g = v;
      b = v;
      return;
    }

    int i = h / 256;
    int f = (h & 255);

    int x = (v * (255 - s)) / 255;
    int y = (v * ((65025 - s * f) / 255)) / 255;
    int z = (v * ((65025 - s * (255 - f)) / 255)) / 255;

    switch(i)
    {
      case 6:
      case 0: r = v; g = z; b = x; break;
      case 1: r = y; g = v; b = x; break;
      case 2: r = x; g = v; b = z; break;
      case 3: r = x; g = y; b = v; break;
      case 4: r = z; g = x; b = v; break;
      case 5: r = v; g = x; b = y; break;
    }
  }
}

