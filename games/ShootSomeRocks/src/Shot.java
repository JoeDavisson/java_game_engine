// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Shot extends Prop
{
  private static int frame = 0;

  public void add(Ship ship)
  {
    status = true;
    timer = 0;
    x = ship.x;
    y = ship.y;
    angle = ship.angle;
    int e;

    switch(Powers.shot_level)
    {
      case 0:
        speed = 8;
        light_color = Col.makeRGB(4, 2, 0);
        anim = Sprite.shot1;
        break;
      case 1:
        speed = 8;
        light_color = Col.makeRGB(4, 4, 0);
        e = frame & 1;
        switch(e)
        {
          case 0:
            anim = Sprite.shot2;
            break;
          case 1:
            anim = Sprite.shot3;
            break;
        }
        break;
      case 2:
        speed = 8;
        light_color = Col.makeRGB(2, 4, 0);
        e = frame % 3;
        switch(e)
        {
          case 0:
            anim = Sprite.shot4;
            break;
          case 1:
            anim = Sprite.shot5;
            break;
          case 2:
            anim = Sprite.shot6;
            break;
        }
        break;
      case 3:
        speed = 8;
        light_color = Col.makeRGB(2, 4, 0);
        e = frame % 4;
        switch(e)
        {
          case 0:
            anim = Sprite.shot4;
            break;
          case 1:
            anim = Sprite.wingshot1;
            break;
          case 2:
            anim = Sprite.shot5;
            break;
          case 3:
            anim = Sprite.wingshot1;
            break;
        }
        break;
    }


    frame++;
    if(frame > 96)
      frame = 0;
  }

  public void remove()
  {
    status = false;
  }

  public void update()
  {
    if(status)
    {
      x -= speed * Trig.cos[(int)angle & 1023];
      y -= speed * Trig.sin[(int)angle & 1023];

      if(x < -anim.w || x > Screen.w + anim.w
         || y < -anim.h || y > Screen.h + anim.h)
      {
        status = false;
      }
    }
  }

}
