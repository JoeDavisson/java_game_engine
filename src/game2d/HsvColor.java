// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

public class HsvColor
{
  public int h, s, v;

  public HsvColor()
  {
    h = 0;
    s = 0;
    v = 0;
  }

  public void rgbToHsv(int r, int g, int b)
  {
    int max = Int.max(r, Int.max(g, b));
    int min = Int.min(r, Int.min(g, b));
    int delta = max - min;

    v = max;

    if(max > 0)
      s = (delta * 255) / max;
    else
      s = 0;

    if(s == 0)
    {
      h = 0;
    } else
    {
      if(r == max)
        h = ((g - b) * 255) / delta;
      else if(g == max)
        h = 512 + ((b - r) * 255) / delta;
      else if(b == max)
        h = 1024 + ((r - g) * 255) / delta;
      if(h < 0)
        h += 1536;
    }
  }
}

