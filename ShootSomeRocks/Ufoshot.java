// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Ufoshot extends Prop
{
  public void add(Ship ship, Ufo ufo)
  {
    x = ufo.x;
    y = ufo.y;
    angle = Rnd.get() & 1023;
    if(ufo.type == 1)
      angle = ufo.angle;
    timer = 0;
    status = true;

    switch(ufo.type)
    {
      case 0:
        speed = 1.0f;
        break;
      case 1:
        speed = 1.4f;
        break;
      case 2:
        speed = 1.2f;
        break;
      case 3:
        speed = 1.2f;
        break;
      case 4:
        speed = 1.4f;
        angle = Trig.angle(ufo.y - ship.y, ufo.x - ship.x);
        break;
    }

    anim = Sprite.ufoshot1;
  }

  public void update()
  {
    if(status)
    {
      x -= speed * Trig.cos[(int)angle & 1023];
      y -= speed * Trig.sin[(int)angle & 1023];

      if(x < -32 || x > Screen.w + 32 || y < -32 || y > Screen.h + 32)
      {
        status = false;
      }
    }
  }

}
