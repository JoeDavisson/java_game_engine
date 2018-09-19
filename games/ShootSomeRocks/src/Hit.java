// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Hit extends Prop
{
  private int frame;

  public void add(float xpos, float ypos,
                  float s, float d, float a, Bitmap hm)
  {
    x = xpos;
    y = ypos;
    //dir = ((Rnd.get() % 8) * 32 + 16) & 255;
    speed = s;
    dir = d;
    angle = a;
    status = true;
    hitmask = hm;
    frame = 0;
    timer = 0;
    scale = 1.0f;
  }

  public void remove()
  {
    status = false;
  }

  public void update()
  {
    if(status)
    {
      x -= speed * Trig.cos[(int)dir & 1023];
      y -= speed * Trig.sin[(int)dir & 1023];
      scale += .01f;
    }
    timer++;
    if(timer < 4)
      return;
    timer = 0;
    frame++;
    if(frame > 3)
    {
      status = false;
      return;
    }
  }

}
