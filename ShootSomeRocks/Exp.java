// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

import game2d.*;

public class Exp extends Prop
{
  public int frame;

  public void add(float xpos, float ypos, float s, float d, float a)
  {
    x = xpos;
    y = ypos;
    //dir = ((Rnd.get() % 8) * 32 + 16) & 255;
    speed = s;
    dir = d;
    angle = a;
    status = true;
    anim = Sprite.exp1;
    frame = 0;
    timer = 0;
    scale = 1.0f;
    light_color = Col.makeRGB(128, 64, 0);
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
      angle += 4.0f;
      if(angle > 1023.99f)
        angle -= 1024;
      scale += .02f;
    }
    timer++;
    if(timer < 8)
      return;
    timer = 0;
    frame++;
    if(frame > 5)
    {
      status = false;
      return;
    }
    switch(frame)
    {
      case 0:
        anim = Sprite.exp1;
        break;
      case 1:
        anim = Sprite.exp2;
        break;
      case 2:
        anim = Sprite.exp3;
        break;
      case 3:
        anim = Sprite.exp4;
        break;
      case 4:
        anim = Sprite.exp5;
        break;
      case 5:
        anim = Sprite.exp6;
        break;
    }
  }

}
