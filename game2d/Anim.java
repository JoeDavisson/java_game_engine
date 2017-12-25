// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

public class Anim
{
  public Bitmap bitmap[];
  public int w, h;
  private int timer = 0;
  private int currentFrame = 0;
  private int speed = 4;

  public Anim(int s, String... fn)
  {
    speed = s;
    currentFrame = 0;
    bitmap = new Bitmap[fn.length];

    int i;
    for(i = 0; i < fn.length; i++)
      bitmap[i] = new Bitmap(fn[i]);

    w = bitmap[0].w;
    h = bitmap[0].h;
    AnimList.add(this);
  }

  public void update()
  {
    timer++;
    if(timer > speed)
    {
      timer = 0;
      currentFrame++;
      if(currentFrame >= bitmap.length)
        currentFrame = 0;
      w = bitmap[currentFrame].w;
      h = bitmap[currentFrame].h;
    }
  }

  public Bitmap frame()
  {
    return bitmap[currentFrame];
  }
}

