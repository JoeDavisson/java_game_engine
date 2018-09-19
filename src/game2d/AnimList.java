// Copyright 2012 Joe Davisson. All Rights Reserved.

package game2d;

public class AnimList
{
  public static int count = 0;
  public static Anim anim[] = new Anim[64];

  public static void add(Anim a)
  {
    if(count >= 64)
      return;

    anim[count] = a;
    count++;
  }
}

