// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

public class Trig
{
  public static float sin[];
  public static float cos[];
  public static int sin2[];
  private static float pi = (float)Math.PI;
  private static boolean initialized = false;

  public static void init()
  {
    if(initialized)
      return;

    sin = new float[1024];
    cos = new float[1024];
    sin2 = new int[1024];

    int i;

    for(i = 0; i < 1024; i++)
    {
      sin[i] = (float)Math.sin((float)i * .0061359f); 
      cos[i] = (float)Math.cos((float)i * .0061359f); 
      sin2[i] = 1024 + (int)(64 * sin[i]); 
    }

    initialized = true;
  }

  public static float angle(float y, float x)
  {
    return (float)Math.atan2(y, x) * 162.97307f;
  }

  /*
  public static float sin(float x)
  {
    return sin_table[(int)x & 1023];
  }

  public static float cos(float x)
  {
    return cos_table[(int)x & 1023];
  }
  */

  /*
  public static float angle(float y, float x)
  {
    float yy = (y > 0 ? y : -y) + .00000001f;
    float c1 = pi / 4;
    float c2 = 3 * c1;
    float result, r;

    if(x >= 0)
    {
      r = (x - yy) / (x + yy);
      result = c1 - c1 * r;
    }
    else
    {
      r = (x + yy) / (yy - x);
      result = c2 - c1 * r;
    }

    if(y < 0)
      return(-result * 162.97307f);
    else
      return(result * 162.97307f);
  }
  */

}
