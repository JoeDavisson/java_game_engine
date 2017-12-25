// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

import game2d.*;

public class Powerup extends Prop
{
  private int pulse;
  private int hue;

  public void add(Ship ship, float xpos, float ypos)
  {
    x = xpos;
    y = ypos;

    angle = 256;
    dir = Trig.angle(y - ship.y, x - ship.x);
    status = true;
    scale = 1.0f;
    speed = .5f;
    pulse = 0;
    timer = 0;
    points = 500;
    hue = 0;
    anim = Sprite.powerup1;
  }

  public void setLevel()
  {
    switch(Powers.level)
    {
      case 0:
        anim = Sprite.powerup1;
        break;
      case 1:
        anim = Sprite.powerup2;
        break;
      case 2:
        anim = Sprite.powerup3;
        break;
      case 3:
        anim = Sprite.powerup4;
        break;
      case 4:
        anim = Sprite.powerup5;
        break;
      default:
        anim = Sprite.powerup6;
        break;
    }
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
      pulse += 8;
      pulse &= 1023;
      scale = 1.0f + (float)Math.abs(.5f * Trig.sin[pulse & 1023]);

      /*
      hue += 16;
      if(hue >= 1536)
        hue = 0;

      int val = (int)Math.abs(96 * Trig.sin[pulse & 1023]);

      RgbColor rgb = new RgbColor();
      rgb.hsvToRgb(hue, 255, val);

      light_color = Col.makeRGB(rgb.r, rgb.g, rgb.b);
      */
    }
  }

}
