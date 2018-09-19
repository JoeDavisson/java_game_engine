// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

import game2d.*;

public class Extralife extends Prop
{
  public void add(Ship ship)
  {
    anim = Sprite.extralife1;
    createHitmask(Col.makeRGB(255, 255, 255));
    hits = 5;
    x = Rnd.get() % 2;
    if(x == 1)
      x = -anim.w;
    else
      x = Screen.w + anim.w;
    y = Rnd.get() % (Screen.h - anim.h);

    dir = Trig.angle(y - ship.y, x - ship.x);
    angle = dir;
    status = true;
    scale = 1.0f;
    speed = .8f;
    timer = 0;
    points = 0;
  }

  public void update()
  {
    if(status)
    {
      timer++;
      if(timer > 800)
      {
        scale *= .97f;
        if(scale < .1f)
          status = false;
        return;
      }
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
    }
  }

}
