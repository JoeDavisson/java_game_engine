// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Stars
{
  private static int frame = 0;
  private static int pulse = 0;

  private static int colors[] =
  {
    0xFF222222, 0xFF444444, 0xFF666666, 0xFF888888,
    0xFFAAAAAA, 0xFF888888, 0xFF666666, 0xFF444444
  };

  public static void render(Bitmap bmp)
  {
    int seed = 12345;

    int x, y;

    int p = 0;

    for(y = 0; y < bmp.h; y++)
    {
      for(x = 0; x < bmp.w; x++)
      {
        seed = ((seed << 17) ^ (seed >> 13) ^ (seed << 5));
        if((seed & 255) == 255)
        {
          seed = ((seed << 17) ^ (seed >> 13) ^ (seed << 5));
          int index = ((seed + pulse) & 7); 
          bmp.data[p] = colors[index];
        }
        else
        {
          bmp.data[p] = 0xFF000000;
        }

        p++;
      }
    }
  }  
  
  public static void update()
  {
    frame++;
    if(frame >= 16)
    {
      frame = 0;
      pulse++;
      if(pulse > 7)
        pulse = 0;
    }
  }

}

