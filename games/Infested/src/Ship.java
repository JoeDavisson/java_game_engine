// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Ship extends Prop
{
  public float incx, incy;

  public void add()
  {
    x = Screen.w / 2;
    y = Screen.h - 16;
    angle = 256;
    incx = 0;
    incy = 0;
    status = true;
    anim = Sprite.ship1;
    createHitmask(Col.makeRGB(255, 255, 255));
    scale = 1.0f;
  }

  public void up()
  {
    incy -= .1f;
  }

  public void down()
  {
    incy += .1f;
  }

  public void left()
  {
    incx -= .1f;
  }

  public void right()
  {
    incx += .1f;
  }

  public void update()
  {
    x += incx;
    y += incy;
    if(x < anim.w)
      x = anim.w;
    if(x > Screen.w - anim.w)
      x = Screen.w - anim.w;
    if(y < anim.h)
      y = anim.h;
    if(y > Screen.h - anim.h - 16)
      y = Screen.h - anim.h - 16;
    incx *= .95f;
    incy *= .95f;
  }

}
