// Copyright (c) 2012 Joe Davisson. All Rights Reserved.


import game2d.*;

public class Ship extends Prop
{
  public float incx, incy;
  public float incr;

  public void add()
  {
    x = Screen.w / 2;
    y = Screen.h / 2;
    incx = 0;
    incy = 0;
    incr = 0;
    angle = 0;
    status = true;
    anim = Sprite.ship1;
    createHitmask(Col.makeRGB(255, 255, 255));
    scale = 1.0f;
  }

  /*
  public void rotateLeft()
  {
    incr = -3.0f;
    update();
    //angle -= 6;
    //if(angle > 1023)
    //  angle -= 1023;
  }

  public void rotateRight()
  {
    incr = 3.0f;
    update();
    //angle += 6;
    //if(angle < 0)
    //  angle += 1023;
  }
  */

  public void forward(boolean thrust)
  {
    if(thrust)
    {
      incx -= .05f * Trig.cos[(int)angle & 1023];
      incy -= .05f * Trig.sin[(int)angle & 1023];
    }
    else
    {
      incx -= .03f * Trig.cos[(int)angle & 1023];
      incy -= .03f * Trig.sin[(int)angle & 1023];
    }
  }

  /*
  public void reverse()
  {
    incx += .04f * Trig.cos[(int)angle & 1023];
    incy += .04f * Trig.sin[(int)angle & 1023];
  }
  */
  /*
  public void brake()
  {
    incx *= .90f;
    incy *= .90f;
  }
  */

  public void update()
  {
    x += incx;
    y += incy;
    if(x < -anim.w)
      x = Screen.w + anim.w;
    if(x > Screen.w + anim.w)
      x = -anim.w;
    if(y < -anim.h)
      y = Screen.h + anim.h;
    if(y > Screen.h + anim.h)
      y = -anim.h;
    incx *= .99f;
    incy *= .99f;
    //angle += incr;
    //if(angle > 1023)
    //  angle -= 1024;
    //if(angle < 0)
    //  angle += 1024;
    //incr *= .95f;
  }

}
