// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Ufo extends Prop
{
  public void add(Ship ship)
  {
    type = Rnd.get() % 5;
    timer = 0;

    switch(type)
    {
      case 0:
        if(!Level.ufo1)
          return;
        anim = Sprite.ufo1;
        createHitmask(Col.makeHSV(4 * 256, 192, 255));
        speed = .4f;
        dir = ((Rnd.get() % 8) * 128 + 64) & 1023;
        angle = 256;
        hits = 1;
        points = 200;
        sound = Snd.ufo1;
        break;
      case 1:
        if(!Level.ship2)
          return;
        anim = Sprite.ship2;
        createHitmask(Col.makeHSV(0 * 256, 192, 255));
        speed = 1.0f;

        dir = Trig.angle(y - ship.y, x - ship.x);
        angle = dir;
        hits = 3;
        points = 500;
        sound = Snd.ufo1;
        break;
      case 2:
        if(!Level.ball1)
          return;
        anim = Sprite.ball1;
        createHitmask(Col.makeHSV(2 * 256, 192, 255));
        speed = .6f;
        dir = ((Rnd.get() % 8) * 128 + 64) & 1023;
        angle = 256;
        hits = 5;
        points = 1500;
        sound = Snd.ufo1;
        break;
      case 3:
        if(!Level.sat1)
          return;
        anim = Sprite.sat1;
        createHitmask(Col.makeHSV(3 * 256, 192, 255));
        speed = .5f;
        dir = ((Rnd.get() % 8) * 128 + 64) & 1023;
        angle = 256;
        hits = 5;
        points = 750;
        sound = Snd.ufo1;
        break;
      case 4:
        if(!Level.ufo2)
          return;
        anim = Sprite.ufo2;
        createHitmask(Col.makeHSV(1 * 256, 192, 255));
        speed = .6f;
        dir = Trig.angle(y - ship.y, x - ship.x);
        angle = 256;
        hits = 2;
        points = 1000;
        sound = Snd.ufo2;
        break;
    }

    int r = Rnd.get() & 7;
    switch(r)
    {
      case 0:
        x = -anim.w;
        y = -anim.h;
        break;
      case 1:
        x = Screen.w + anim.w;
        y = -anim.h;
        break;
      case 2:
        x = -anim.w;
        y = Screen.h + anim.h;
        break;
      case 3:
        x = Screen.w + anim.w;
        y = Screen.h + anim.h;
        break;
      case 4:
        x = -anim.w;
        y = Screen.h / 2 - anim.h / 2;
        break;
      case 5:
        x = Screen.w + anim.w;
        y = Screen.h / 2 - anim.h / 2;
        break;
      case 6:
        x = Screen.w / 2 - anim.w / 2;
        y = -anim.h;
        break;
      case 7:
        x = Screen.w / 2 - anim.w / 2;
        y = Screen.h + anim.h;
        break;
    }

    status = true;
  }

  public void update(Ship ship)
  {
    //int chase;

    if(status)
    {
      switch(type)
      {
        case 0:
          dir = Trig.angle(y - ship.y, x - ship.x);
          break;
        case 1:
          break;
        case 2:
          break;
        case 3:
          break;
        case 4:
          dir = Trig.angle(y - ship.y, x - ship.x);
          break;
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
