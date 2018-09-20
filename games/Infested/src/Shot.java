// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Shot extends Prop
{
  private static int frame = 0;

  public void add(Ship ship)
  {
    status = true;
    timer = 0;
    speed = 4;
    x = ship.x;
    y = ship.y;
    angle = ship.angle;
    int e;

    anim = Sprite.shot1;
  }

  public void remove()
  {
    status = false;
  }

  public void update()
  {
    if(status)
    {
      y -= speed;

      if(x < -anim.w || x > Screen.w + anim.w
         || y < -anim.h || y > Screen.h + anim.h)
      {
        status = false;
      }
    }
  }

}
