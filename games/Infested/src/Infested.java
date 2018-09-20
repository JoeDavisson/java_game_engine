// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

import game2d.*;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.image.*;

public class Infested extends Game
{
  // game related
  boolean go_ahead = false;
  boolean restart_game = true;
  boolean paused = false;

  int game_mode = 0;
  int pause_timer = 0;

  int pulse;
  int menu_delay = 0;
  int old_highscore = 0;;

  int MAX_SHOTS = 32;

  Ship ship = new Ship();
  Shot shot[] = new Shot[MAX_SHOTS];

  FontBmp font8x16 = new FontBmp(Sprite.font8x16.frame(), 8);

  int shot_timer;
  int lives;
  int nextExtralifeScore;

  int MAX_LIGHTS = 8;
  PropList plist = new PropList(MAX_LIGHTS);
  Light light[] = new Light[MAX_LIGHTS];

  // scroller
  String scroller = new String("                                             blah blah blah blah blah blah blah blah blah");
  int scrolltick = 0;
  int scrollpos = 0;
  char scrolltemp[] = new char[41];

  Shrooms shrooms;

  public void gameSize()
  {
    Screen.w = 256;
    Screen.h = 256;
  }

  public void gameInit()
  {
    maxFps(60);
    //show_fps = true;

    int i;

    // go back to main start
    game_mode = 0;
    go_ahead = false;

    // game props
    for(i = 0; i < MAX_SHOTS; i++)
      shot[i] = new Shot();

    // lights
    for(i = 0; i < MAX_LIGHTS; i++)
      light[i] = new Light();

    // shrooms
    shrooms = new Shrooms();

    // init bitmap light tables
    Bitmap.init(MAX_LIGHTS);
  }

  public void gameLogic()
  {
    // update pulser
    pulse += 4;
    pulse &= 1023;

    switch(game_mode)
    {
      case 0:
        if(menuLoop() == true)
        {
          game_mode = 1;
          go_ahead = false;
          old_highscore = Score.high;
          restartGame();
        }
        break;
      case 1:
        if(actionLoop() == true)
        {
          game_mode = 2;
          go_ahead = false;
        }
        break;
      case 2:
        if(gameOverLoop() == true)
        {
          game_mode = 0;
          resetScroll();
          go_ahead = false;
        }
        break;
    }
  }

  public void gameFrame()
  {
    // draw frame
    switch(game_mode)
    {
      case 0:
        menuFrame();
        break;
      case 1:
        actionFrame();
        break;
      case 2:
        gameOverFrame();
        break;
    }
  }

  public boolean actionLoop()
  {
    int i, j, use;

    // check quit
    if(input.getKey(KeyEvent.VK_Q))
    {
      Score.high = old_highscore;
      sound.quiet();
      game_mode = 0;
      return true;
    }

    // check pause
    if(input.getKey(KeyEvent.VK_P) && pause_timer > 20)
    {
      pause_timer = 0;
      if(paused)
        paused = false;
      else
        paused = true;
    }

    pause_timer++;
    if(paused)
      return false;

    //if(input.button1 && shot_timer > 20)
    if(input.getKey(KeyEvent.VK_SPACE) && shot_timer > 20)
    {
      shot_timer = 0;
      addShot();
    }

    if(input.getKey(KeyEvent.VK_UP))
    {
      ship.up();
    }

    if(input.getKey(KeyEvent.VK_DOWN))
    {
      ship.down();
    }

    if(input.getKey(KeyEvent.VK_LEFT))
    {
      ship.left();
    }

    if(input.getKey(KeyEvent.VK_RIGHT))
    {
      ship.right();
    }

    // move ship & check collisions
    ship.update();

    // move shots & check collisions
    for(i = 0; i < Level.shots; i++)
    {
      shot[i].update();

      if(shot[i].status)
      {
/*
        // find nearest rock
        use = findNearest(shot[i], rock, Level.rocks);
        if(use != -1 && Collision.check(shot[i], rock[use]))
        {
          shot[i].remove();
          addExplosion(rock[use].x, rock[use].y, rock[use].speed,
                       rock[use].dir, rock[use].angle);
          Score.add(rock[use].points);
          addPoint(rock[use].x, rock[use].y, rock[use].points);
          switch(rock[use].type)
          {
            case 0:
              sound.play(Snd.rockexp3, true);
              rock[use].anim = Sprite.rock3;
              break;
            case 1:
              sound.play(Snd.rockexp2, true);
              rock[use].anim = Sprite.rock3;
              break;
            case 2:
              sound.play(Snd.rockexp1, true);
              rock[use].anim = Sprite.rock2;
              break;
          }
          rock[use].type--;
          rock[use].dir = ((Rnd.get() % 8) * 128 + 64) & 1023;
          rock[use].speed *= 1.25f;
          if(rock[use].type < 0) {
            rock[use].remove();
            rock_count--;
            if(rock_count < 1)
              levelUp();
          }
        } 
*/
      }
    }

    // add extra life every 10000 points
    if(Score.current >= nextExtralifeScore)
    {
      // update score
    }

    // update timers
    shot_timer++;

    // update animations
    for(i = 0; i < AnimList.count; i++)
      AnimList.anim[i].update();

    return false;
  }

  public void actionFrame()
  {
    int i;
    int x, y;

    // setup light list
    int lcount = 0;
    for(i = 0; i < MAX_LIGHTS; i++)
    {
      if(plist.prop[i].status)
      {
        light[lcount].x = (int)plist.prop[i].x;
        light[lcount].y = (int)plist.prop[i].y;
        light[lcount].color = plist.prop[i].light_color;
        lcount++;
        if(lcount >= MAX_LIGHTS)
          break;
      }
    }

    // clear screen
    backbuf.clear(Col.makeRGB(0, 16, 0));

    // draw shrooms
    int yy = 0;
    for(y = 0; y < 12; y++)
    {
      int xx = 0;
      for(x = 0; x < 16; x++)
      {
        switch(shrooms.get(x, y))
        {
          case 1:
            Sprite.shroom4.frame().blitMasked(backbuf, 0, 0, xx, yy, 16, 16);
            break;
          case 2:
            Sprite.shroom3.frame().blitMasked(backbuf, 0, 0, xx, yy, 16, 16);
            break;
          case 3:
            Sprite.shroom2.frame().blitMasked(backbuf, 0, 0, xx, yy, 16, 16);
            break;
          case 4:
            Sprite.shroom1.frame().blitMasked(backbuf, 0, 0, xx, yy, 16, 16);
            break;
        }
        xx += 16;
      }
      yy += 16;
    }

    // draw shots
    for(i = 0; i < Level.shots; i++)
    {
      if(shot[i].status)
        shot[i].anim.frame().rotateSprite(backbuf,
          (int)shot[i].x, (int)shot[i].y, (int)shot[i].angle, shot[i].scale);
    }

    // draw ship
    if(ship.status)
      ship.anim.frame().rotateLitSprite(backbuf,
        (int)ship.x, (int)ship.y, (int)ship.angle, ship.scale, light, lcount);

    // draw score
    font8x16.drawString(backbuf, 0, Screen.h - 16, String.valueOf(Score.current), 1.0f);

    // draw lives
    int posx = Screen.w - 16;
    for(i = 0; i < lives; i++)
    {
      if(i > 4)
        break;
      ship.anim.frame().rotateSprite(backbuf, posx, Screen.h - 8, 256, .75f);
      posx -= 16;
    }
    if(lives > 5)
      font8x16.drawString(backbuf, posx - 16, Screen.h - 14, String.valueOf(lives), .75f);

    // draw paused
    if(paused)
      backbuf.rectfill(0, 0, backbuf.w - 1, backbuf.h - 1,
                    Col.makeRGB(0, 0, 0), 160);
  }

  public boolean menuLoop()
  {
    // play music
    sound.play(Snd.music1, false);
    sound.play(Snd.music2, false);
    sound.play(Snd.music3, false);

    // update scrolltext
    updateScroll();

    // wait for button to be released & sound to stop playing
    if(!input.button1)
      go_ahead = true;
    if(go_ahead && input.button1)
    {
      // stop music
      sound.quiet();
      return true;
    }

    return false;
  }

  public void menuFrame()
  {
    // menu background
    Sprite.menuback.frame().blit(backbuf, 0, 0, 0, 0,
                                 Sprite.menuback.w, Sprite.menuback.h);

    // draw high score
    font8x16.drawCenteredString(backbuf, Screen.w / 2, 16, String.valueOf(Score.high), 1.0f);

    // draw text
    float p = 1.0f + (float)Math.abs(0.25 * Trig.sin[pulse & 1023]);
    font8x16.drawCenteredString(backbuf, (int)Screen.w / 2, (int)Screen.h / 2 + 64, "Click to Play!", p);

    // draw copyright
    font8x16.drawCenteredString(backbuf, Screen.w / 2, Screen.h - 8, "Copyright (c) 2012 Joe Davisson. All Rights Reserved.", 0.5f);

    // draw scrolltext
    String scroll = new String(scrolltemp);
    font8x16.drawString(backbuf, 0 - scrolltick, 400, scroll, 1.0f);
  }

  public boolean gameOverLoop()
  {
    // delay a bit
    menu_delay++;
    if(menu_delay < 500)
      return false;
    menu_delay = 0;

    sound.quiet();
    return true;
  }

  public void gameOverFrame()
  {
    // draw text
    float p = 2.0f + (float)Math.abs(0.5f * Trig.sin[pulse & 1023]);
    font8x16.drawCenteredString(backbuf, (int)Screen.w / 2, (int)Screen.h / 2, "Game Over", p);

    p = 1.0f + (float)Math.abs(0.25 * Trig.sin[pulse & 1023]);
    font8x16.drawCenteredString(backbuf, (int)Screen.w / 2, (int)Screen.h / 2 + 64, "You got to Level " + String.valueOf(Level.current + 1) + "!", p);

  }

  public void loseLife(Prop p)
  {
    lives--;
    if(lives < 0)
    {
      game_mode = 2;
      sound.play(Snd.gameover1, false);
      sound.play(Snd.gameover2, false);
      return;
    }

    //addExplosion(ship.x, ship.y, ship.speed,
    //             ship.dir, ship.angle);
    sound.play(Snd.shipexp, false);

    game_mode = 1;
    restartLevel();
  }

  public void resetScroll()
  {
    int i, j;
    int l = scroller.length();
    int index = 0;

    scrolltick = 0;
    scrollpos = 0;
    for(i = scrollpos; i < scrollpos + 41; i++)
    {
      j = i;
      if(j >= l)
        j -= l;

      scrolltemp[index++] = scroller.charAt(j);
    }
  }

  public void updateScroll()
  {
    int i, j;
    int l = scroller.length();
    int index = 0;

    scrolltick++;
    if(scrolltick > 15)
    {
      scrolltick = 0;

      for(i = scrollpos; i < scrollpos + 41; i++)
      {
        j = i;
        if(j >= l)
          j -= l;

        scrolltemp[index++] = scroller.charAt(j);
      }

      scrollpos++;
      if(scrollpos >= l)
        scrollpos = 0;
    }
  }
 
  public void addExplosion(float x, float y, float speed,
                           float dir, float angle)
  {
/*
    int i;

    for(i = 0; i < MAX_EXPS; i++)
    {
      if(!exp[i].status)
        break;
    }
    if(i < MAX_EXPS)
    {
      exp[i].add(x, y, speed, dir, angle);
      plist.add(exp[i]);
    }
*/
  }

  private int findNearest(Prop p1, Prop p2[], int num)
  {
    int nearest = 65536;
    int use = -1;
    int i;

    for(i = 0; i < num; i++)
    {
      if(p2[i].status)
      {
        int d = Int.distcmp((int)p1.x, (int)p1.y,
                         (int)p2[i].x, (int)p2[i].y);
        if(d < nearest)
        {
          nearest = d;
          use = i;
        }
      }
    }

    return use;
  }

  private void levelUp()
  {
    Level.current++;
    if(Level.current > 63)
      Level.current = 63;

    if(Level.current > Score.levelhigh)
      Score.levelhigh = Level.current;

    game_mode = 1;
    restartLevel();
  }

  private void restartGame()
  {
    go_ahead = false;
    lives = 16;
    nextExtralifeScore = 10000;
    Level.set(0);
    Score.reset();
    restartLevel();
  }

  private void restartLevel()
  {
    int i;

    ship.add();
    Level.set(Level.current);

    for(i = 0; i < MAX_SHOTS; i++)
      shot[i].remove();

    shot_timer = 0;
    ship.x = Screen.w / 2;
    ship.y = Screen.h - 16;
  }

  private void addShot()
  {
    int i;

    for(i = 0; i < Level.shots; i++)
    { 
      if(!shot[i].status)
        break;
    }
    if(i < Level.shots)
    {
      shot[i].add(ship);
      sound.play(Snd.shot, true);
      plist.add(shot[i]);
    }
  }

}

