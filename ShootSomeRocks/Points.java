// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Points extends Prop
{
  public int frame;
  public int value;

  public void add(float xpos, float ypos, int v)
  {
    value = v;
    x = xpos;
    y = ypos;
    dir = 96;
    speed = .1f;
    angle = 64;
    status = true;
    frame = 0;
    timer = 0;
    scale = .25f;
  }

  public void remove()
  {
    status = false;
  }

  public void update()
  {
    if(status)
    {
      scale += .007f;
    }
    timer++;
    if(timer < 8)
      return;
    timer = 0;
    frame++;
    if(frame > 8)
    {
      status = false;
      return;
    }
  }

}
