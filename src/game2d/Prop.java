// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

public class Prop
{
  public float x;
  public float y;
  public float angle;
  public float dir;
  public float speed;
  public float scale;
  public int type;
  public int timer;
  public int hits;
  public int points;
  public Sample sound;
  public boolean status;

  public Anim anim;
  public Bitmap hitmask;

  public int light_color;

  public Prop()
  {
    scale = 1.0f;
    status = false;
    light_color = Col.makeRGB(0, 0, 0);
  }

  public void remove()
  {
    status = false;
  }

  // call after anim set, always uses frame 0
  public void createHitmask(int color)
  {
    int w = anim.w;
    int h = anim.h;
    int x, y;

    hitmask = new Bitmap(w, h);
    for(y = 0; y < h; y++)
    {
      for(x = 0; x < w; x++)
      {
        if(anim.bitmap[0].isEdge(x, y))
          hitmask.setpixel(x, y, color, 0);
        else
          hitmask.setpixel(x, y, Col.makeRGB(255, 0, 255), 0);
      }
    }
  }

}
