// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Meteor extends Prop
{
  public void add()
  {
    anim = Sprite.meteor1;
    x = Rnd.get() % 2;
    if(x == 1)
      x = -anim.w;
    else
      x = Screen.w + anim.w;
    y = Rnd.get() % (Screen.h - anim.h);
    dir = ((Rnd.get() % 8) * 128 + 64) & 1023;
    angle = Rnd.get() & 1023;
    speed = (float)(Rnd.get() % 8) / 32 + 1.5f;
    status = true;
    type = 2;
    points = 100;
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
      angle -= 4.0f;
      if(angle < 0)
        angle += 1024;
    }
  }

}
