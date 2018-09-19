// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Rock extends Prop
{
  public void add()
  {
    anim = Sprite.rock1;
    x = Rnd.get() % 2;
    if(x == 1)
      x = -anim.w;
    else
      x = Screen.w + anim.w;
    y = Rnd.get() % (Screen.h - anim.h);
    dir = ((Rnd.get() % 8) * 128 + 64) & 1023;
    angle = Rnd.get() & 255;
    speed = (float)(Rnd.get() % 8) / 32 + .5f;
    status = true;
    type = 2;
    points = 50;
  }

  public void update()
  {
    if(status)
    {
      if(x < -anim.w)
        x = Screen.w + anim.w;
      if(x > Screen.w + anim.w)
        x = -anim.w;
      if(y < -anim.h)
        y = Screen.h + anim.h;
      if(y > Screen.h + anim.h)
        y = -anim.h;
      x -= speed * Trig.cos[(int)dir & 1023];
      y -= speed * Trig.sin[(int)dir & 1023];
      angle += .6f;
      if(angle > 1023.99f)
        angle -= 1024;
    }
  }

}
