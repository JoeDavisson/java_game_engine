// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

import game2d.*;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.image.*;

public class ShootSomeRocks extends Game
{
  // game related
  boolean go_ahead = false;
  boolean restart_game = true;
  boolean paused = false;

  int game_mode = 0;
  int pause_timer = 0;

  float menu_stars_angle = 0;
  float menu_stars_scale = 1.0f;
  int pulse;
  int menu_delay = 0;
  int old_highscore = 0;;

  int MAX_EXPS = 32;
  int MAX_HITS = 32;
  int MAX_POINTS = 32;
  int MAX_SHOTS = 32;
  int MAX_ROCKS = 32;
  int MAX_METEORS = 32;
  int MAX_UFOS = 32;
  int MAX_UFOSHOTS = 32;

  Ship ship = new Ship();
  Exp exp[] = new Exp[MAX_EXPS];
  Hit hit[] = new Hit[MAX_HITS];
  Points point[] = new Points[MAX_POINTS];
  Rock rock[] = new Rock[MAX_ROCKS];
  Meteor meteor[] = new Meteor[MAX_METEORS];
  Shot shot[] = new Shot[MAX_SHOTS];
  Ufo ufo[] = new Ufo[MAX_UFOS];
  Ufoshot ufoshot[] = new Ufoshot[MAX_UFOSHOTS];
  Powerup powerup = new Powerup();
  Extralife extralife = new Extralife();

  FontBmp font16x32 = new FontBmp(Sprite.font16x32.frame(), 16);

  int rock_count;
  int shot_timer;
  //float desired_angle = 0;
  int lives;
  int nextExtralifeScore;

  Bitmap tile = new Bitmap(256, 256);

  int MAX_LIGHTS = 8;
  PropList plist = new PropList(MAX_LIGHTS);
  Light light[] = new Light[MAX_LIGHTS];

  // scroller
  String scroller = new String("                                             This game is easy, just shoot at stuff and collect weapons and money. Extra life if you rescue the broken-down ship! Controls: Click mouse to move, Space to fire.");
  int scrolltick = 0;
  int scrollpos = 0;
  char scrolltemp[] = new char[41];

  public void gameSize()
  {
    Screen.w = 640;
    Screen.h = 480;
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
    for(i = 0; i < MAX_EXPS; i++)
      exp[i] = new Exp();
    for(i = 0; i < MAX_HITS; i++)
      hit[i] = new Hit();
    for(i = 0; i < MAX_POINTS; i++)
      point[i] = new Points();
    for(i = 0; i < MAX_ROCKS; i++)
      rock[i] = new Rock();
    for(i = 0; i < MAX_METEORS; i++)
      meteor[i] = new Meteor();
    for(i = 0; i < MAX_SHOTS; i++)
      shot[i] = new Shot();
    for(i = 0; i < MAX_UFOS; i++)
      ufo[i] = new Ufo();
    for(i = 0; i < MAX_UFOSHOTS; i++)
      ufoshot[i] = new Ufoshot();

    // lights
    for(i = 0; i < MAX_LIGHTS; i++)
      light[i] = new Light();

    // init bitmap light tables
    Bitmap.init(MAX_LIGHTS);
  }

  public void gameLogic()
  {
    // update pulser
    pulse += 4;
    pulse &= 1023;

    // update stars angle
    menu_stars_angle += 0.1f;
    if(menu_stars_angle > 1023)
      menu_stars_angle -= 1024;

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
        if(levelLoop() == true)
        {
          game_mode = 2;
          go_ahead = false;
        }
        break;
      case 2:
        if(actionLoop() == true)
        {
          game_mode = 3;
          go_ahead = false;
        }
        break;
      case 3:
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
        levelFrame();
        break;
      case 2:
        actionFrame();
        break;
      case 3:
        gameOverFrame();
        break;
    }
  }

  public boolean actionLoop()
  {
    int i, j, use;

    // update stars
    Stars.update();

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

    // add ufo
    if((Rnd.get() & 31) == 31)
    {
      for(i = 0; i < Level.ufos; i++)
      {
        if(!ufo[i].status)
          break;
      }
      if(i < Level.ufos)
        ufo[i].add(ship);
    }

    // play ufo sounds
    for(i = 0; i < Level.ufos; i++)
      if(ufo[i].status)
        sound.play(ufo[i].sound, false);

    // add ufoshot
    if(Level.ufos > 0 && Level.ufoshots > 0 && (Rnd.get() & 63) == 63)
    {
      j = Rnd.get() % Level.ufos;
      if(ufo[j].status)
      {
        for(i = 0; i < Level.ufoshots; i++)
        { 
          if(!ufoshot[i].status)
            break;
        }
        if(i < Level.ufoshots)
          ufoshot[i].add(ship, ufo[j]);
      }
    }

    // check keys/buttons

    ship.angle = Trig.angle(ship.y - input.mousey, ship.x - input.mousex);

    if(input.button1)
    {
      ship.forward(Powers.thrust);
      if(Powers.thrust)
        sound.play(Snd.thrust, false);
      Powers.thrustActive = true;
    }
    else
    {
      Powers.thrustActive = false;
    }

    if(input.getKey(KeyEvent.VK_SPACE) && shot_timer > Powers.shot_speed)
    {
      shot_timer = 0;
      addShot();
    }

    // move ship & check collisions
    ship.update();

    // find nearest rock
    use = findNearest(ship, rock, Level.rocks);
    if(use != -1 && Collision.check(ship, rock[use]))
    {
      loseLife(rock[use]);
      rock_count--;
      if(rock_count < 1)
        levelUp();
    }

    // find nearest meteor
    use = findNearest(ship, meteor, Level.meteors);
    if(use != -1 && Collision.check(ship, meteor[use]))
      loseLife(meteor[use]);

    // find nearest ufo
    use = findNearest(ship, ufo, Level.ufos);
    if(use != -1 && Collision.check(ship, ufo[use]))
      loseLife(ufo[use]);

    // find nearest ufoshot
    use = findNearest(ship, ufoshot, Level.ufoshots);
    if(use != -1 && Collision.check(ship, ufoshot[use]))
      loseLife(ufoshot[use]);

    // move shots & check collisions
    for(i = 0; i < Level.shots; i++)
    {
      shot[i].update();

      if(shot[i].status)
      {
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

        // find nearest meteor
        use = findNearest(shot[i], meteor, Level.meteors);
        if(use != -1 && Collision.check(shot[i], meteor[use]))
        {
          sound.play(Snd.rockexp2, true);
          shot[i].remove();
          addExplosion(meteor[use].x, meteor[use].y, meteor[use].speed,
                       meteor[use].dir, meteor[use].angle);
          Score.add(meteor[use].points);
          addPoint(meteor[use].x, meteor[use].y, meteor[use].points);
          meteor[use].remove();
        } 

        use = findNearest(shot[i], ufo, Level.ufos);
        if(use != -1 && Collision.check(shot[i], ufo[use]))
        {
          shot[i].remove();
          ufo[use].hits--;
          addHit(ufo[use].x, ufo[use].y, ufo[use].speed,
                       ufo[use].dir, ufo[use].angle, ufo[use].hitmask);
          sound.play(Snd.hit, false);
          if(ufo[use].hits < 0)
          {
            sound.play(Snd.ufoexp, true);
            addExplosion(ufo[use].x, ufo[use].y, ufo[use].speed,
                         ufo[use].dir, ufo[use].angle);
            Score.add(ufo[use].points);
            addPoint(ufo[use].x, ufo[use].y, ufo[use].points);
            ufo[use].remove();
            //int mask = (Powers.level + 1);
            //if((Rnd.get() % mask) == 0)
            if(((Rnd.get() & 3) == 3) || Powers.level < 2)
            {
              if(!powerup.status)
              {
                powerup.add(ship, ufo[use].x, ufo[use].y);
                powerup.setLevel();
                plist.add(powerup);
              }
            }
          }
        } 

        if(extralife.status)
        {
          if(Collision.check(shot[i], extralife))
          {
            shot[i].remove();
            extralife.hits--;
            addHit(extralife.x, extralife.y, extralife.speed,
                   extralife.dir, extralife.angle, extralife.hitmask);
            sound.play(Snd.hit, false);
            if(extralife.hits < 0)
            {
              sound.play(Snd.ufoexp, true);
              addExplosion(extralife.x, extralife.y, extralife.speed,
                           extralife.dir, extralife.angle);
              extralife.status = false;
            }
          }
        }

      }
    }

// 1 thrust
// 2 shot1
// 3 shot2
// 4 shield
// 5 wingman

    // check powerup
    if(powerup.status && Collision.check(ship, powerup))
    {
      Powers.level++;
      if(Powers.level > 6)
        Powers.level = 6;

      if(Powers.level == 4)
        Powers.shield = 4;
      if(Powers.level == 5)
        Powers.wingman = true;
      if(Powers.level == 6)
      {
        addPoint(powerup.x, powerup.y, 2500);
        Score.add(2500);
      }
      else
      {
        addPoint(powerup.x, powerup.y, powerup.points);
        Score.add(powerup.points);
      }

      Powers.updateLevel();

      sound.play(Snd.powerup1, false);
      sound.play(Snd.powerup2, false);
      powerup.status = false;
    }

    // add extra life every 10000 points
    if(Score.current >= nextExtralifeScore)
    {
      if(!extralife.status)
      {
        extralife.add(ship);
        nextExtralifeScore += 10000;
      }
    }

    // check extra life
    if(extralife.status && Collision.check(ship, extralife))
    {
      sound.play(Snd.powerup1, false);
      sound.play(Snd.powerup2, false);
      lives++;
      if(lives > 99)
        lives = 99;
      extralife.status = false;
    }
 
    // move explosions
    for(i = 0; i < MAX_EXPS; i++)
      exp[i].update();

    // move hits
    for(i = 0; i < MAX_HITS; i++)
      hit[i].update();

    // move points
    for(i = 0; i < MAX_POINTS; i++)
      point[i].update();

    // move rocks
    for(i = 0; i < Level.rocks; i++)
      rock[i].update();

    // move meteors
    for(i = 0; i < Level.meteors; i++)
      meteor[i].update();

    // move ufos
    for(i = 0; i < Level.ufos; i++)
      ufo[i].update(ship);

    // move ufo shots
    for(i = 0; i < Level.ufoshots; i++)
      ufoshot[i].update();

    // move powerup
    powerup.update();

    // move extra life
    extralife.update();

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
    /*
    light[0].x = 320;
    light[0].y = 128;
    light[0].color = Col.makeRGB(64, 192, 64);
    light[1].x = 320;
    light[1].y = 192;
    light[1].color = Col.makeRGB(192, 64, 64);
    lcount = 2;
    */

    // draw stars
    Stars.render(backbuf);

    // draw rocks
    for(i = 0; i < Level.rocks; i++)
    {
      if(rock[i].status)
        rock[i].anim.frame().rotateLitSprite(backbuf,
          (int)rock[i].x, (int)rock[i].y, (int)rock[i].angle, rock[i].scale, light, lcount);
    }

    // draw meteors
    for(i = 0; i < Level.meteors; i++)
    {
      if(meteor[i].status)
        meteor[i].anim.frame().rotateLitSprite(backbuf,
          (int)meteor[i].x, (int)meteor[i].y, (int)meteor[i].angle, meteor[i].scale, light, lcount);
    }

    // draw ufoshots
    for(i = 0; i < Level.ufoshots; i++)
    {
      if(ufoshot[i].status)
        ufoshot[i].anim.frame().rotateSprite(backbuf,
          (int)ufoshot[i].x, (int)ufoshot[i].y, (int)ufoshot[i].angle, ufoshot[i].scale);
    }

    // draw shots
    for(i = 0; i < Level.shots; i++)
    {
      if(shot[i].status)
        shot[i].anim.frame().rotateSprite(backbuf,
          (int)shot[i].x, (int)shot[i].y, (int)shot[i].angle, shot[i].scale);
    }

    // draw ufos
    for(i = 0; i < Level.ufos; i++)
    {
      if(ufo[i].status)
        ufo[i].anim.frame().rotateLitSprite(backbuf,
          (int)ufo[i].x, (int)ufo[i].y, (int)ufo[i].angle, ufo[i].scale, light, lcount);
    }

    // draw explosions
    for(i = 0; i < MAX_EXPS; i++)
    {
      if(exp[i].status)
        exp[i].anim.frame().rotateSprite(backbuf,
          (int)exp[i].x, (int)exp[i].y, (int)exp[i].angle, exp[i].scale);
    }

    // draw powerup
    if(powerup.status)
      powerup.anim.frame().rotateSprite(backbuf,
        (int)powerup.x, (int)powerup.y, (int)powerup.angle, powerup.scale);

    // draw extra life
    if(extralife.status)
    {
      extralife.anim.frame().rotateSprite(backbuf,
        (int)extralife.x, (int)extralife.y, (int)extralife.angle, extralife.scale);
      if(extralife.status)
        Sprite.thrust1.frame().rotateSprite(backbuf,
          (int)extralife.x, (int)extralife.y, (int)extralife.angle, extralife.scale);
      float p = .5f + (float)Math.abs(0.1f * Trig.sin[pulse & 1023]);
      font16x32.drawCenteredString(backbuf, (int)extralife.x, (int)extralife.y + 24, "Rescue", p);
    }

    // draw thrust
    if(Powers.thrust && Powers.thrustActive)
      Sprite.thrust1.frame().rotateSprite(backbuf,
        (int)ship.x, (int)ship.y, (int)ship.angle, ship.scale);

    // draw ship
    if(ship.status)
      ship.anim.frame().rotateLitSprite(backbuf,
        (int)ship.x, (int)ship.y, (int)ship.angle, ship.scale, light, lcount);

    // draw shield
    if(Powers.shield > 1)
      Sprite.shield1.frame().rotateSprite(backbuf,
        (int)ship.x, (int)ship.y, (int)ship.angle, ship.scale);

    // draw wingman
    if(Powers.wingman)
      Sprite.wingman1.frame().rotateLitSprite(backbuf,
        (int)ship.x, (int)ship.y, (int)ship.angle, ship.scale, light, lcount);

    // draw hits
    for(i = 0; i < MAX_HITS; i++)
    {
      if(hit[i].status)
        hit[i].hitmask.rotateSprite(backbuf,
          (int)hit[i].x, (int)hit[i].y, (int)hit[i].angle, hit[i].scale);
    }

    // draw points
    for(i = 0; i < MAX_POINTS; i++)
    {
      if(point[i].status)
        font16x32.drawString(backbuf, (int)point[i].x, (int)point[i].y, String.valueOf(point[i].value), point[i].scale);
    }

    // draw score
    font16x32.drawString(backbuf, 0, 0, String.valueOf(Score.current), 1.0f);

    // draw high score
    //font16x32.drawString(backbuf, 192, 0, String.valueOf(Score.high), 1.0f);

    // draw lives
    int posx = Screen.w - 16;
    for(i = 0; i < lives; i++)
    {
      if(i > 4)
        break;
      ship.anim.frame().rotateSprite(backbuf, posx, 16, 256, .5f);
      posx -= 32;
    }
    if(lives > 5)
      font16x32.drawString(backbuf, posx - 16, 4, String.valueOf(lives), .75f);

    // draw crosshair
    Sprite.cross1.frame().rotateSprite(backbuf,
      input.mousex, input.mousey, (int)256, 1.0f);

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

    // update stars
    Stars.update();

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
    // draw stars
    Stars.render(tile);
    tile.rotozoom(backbuf, Screen.w / 2, Screen.h / 2, (int)menu_stars_angle, menu_stars_scale);
    Sprite.coolguy6.frame().blit(backbuf, 0, 0,
                         (Screen.w - Sprite.coolguy6.w) / 2,
                         (Screen.h - Sprite.coolguy6.h) / 2 - 32,
                         Sprite.coolguy6.w,
                         Sprite.coolguy6.h);

    // draw high score
    font16x32.drawCenteredString(backbuf, Screen.w / 2, 16, String.valueOf(Score.high), 1.0f);

    // draw text
    float p = 1.0f + (float)Math.abs(0.25 * Trig.sin[pulse & 1023]);
    font16x32.drawCenteredString(backbuf, (int)Screen.w / 2, (int)Screen.h / 2 + 64, "Click to Play!", p);

    // draw copyright
    font16x32.drawCenteredString(backbuf, Screen.w / 2, Screen.h - 8, "Copyright (c) 2012 Joe Davisson. All Rights Reserved.", 0.5f);

    // draw scrolltext
    String scroll = new String(scrolltemp);
    font16x32.drawString(backbuf, 0 - scrolltick, 400, scroll, 1.0f);

  }

  public boolean levelLoop()
  {
    // update stars
    Stars.update();

    // delay a bit
    menu_delay++;
    if(menu_delay < 125)
      return false;
    menu_delay = 0;

    return true;
  }

  public void levelFrame()
  {
    // draw stars
    Stars.render(tile);
    tile.rotozoom(backbuf, Screen.w / 2, Screen.h / 2, (int)menu_stars_angle, menu_stars_scale);

    // draw text
    float p = 2.0f + (float)Math.abs(0.5f * Trig.sin[pulse & 1023]);
    font16x32.drawCenteredString(backbuf, (int)Screen.w / 2, (int)Screen.h / 2 - 64, "Level " + String.valueOf(Level.current + 1), p);

    int i;
    int l = lives;
    if(l > 5)
      l = 5;
    int stepx = 64;
    int width = stepx * ((l - 1) >= 0 ? (l - 1) : 0);
    int xpos = Screen.w / 2 - width / 2;

    for(i = 0; i < l; i++)
    {
      ship.anim.frame().rotateSprite(backbuf,
        xpos, Screen.h / 2 + 64, 256, 1.0f);
      xpos += stepx;
    }

    if(lives > 1)
      font16x32.drawCenteredString(backbuf, (int)Screen.w / 2, (int)Screen.h / 2 + 128, String.valueOf(lives) + " Ships Remaining", 1.0f);
    else if(lives > 0)
      font16x32.drawCenteredString(backbuf, (int)Screen.w / 2, (int)Screen.h / 2 + 128, String.valueOf(lives) + " Ship Remaining", 1.0f);
    else
      font16x32.drawCenteredString(backbuf, (int)Screen.w / 2, (int)Screen.h / 2 + 96, "Last Chance!", 1.0f);

  }

  public boolean gameOverLoop()
  {
    // update stars
    Stars.update();

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
    // draw stars
    Stars.render(tile);
    tile.rotozoom(backbuf, Screen.w / 2, Screen.h / 2, (int)menu_stars_angle, menu_stars_scale);

    // draw text
    float p = 2.0f + (float)Math.abs(0.5f * Trig.sin[pulse & 1023]);
    font16x32.drawCenteredString(backbuf, (int)Screen.w / 2, (int)Screen.h / 2, "Game Over", p);

    p = 1.0f + (float)Math.abs(0.25 * Trig.sin[pulse & 1023]);
    font16x32.drawCenteredString(backbuf, (int)Screen.w / 2, (int)Screen.h / 2 + 64, "You got to Level " + String.valueOf(Level.current + 1) + "!", p);

  }

  public void loseLife(Prop p)
  {
    addExplosion(p.x, p.y, p.speed, p.dir, p.angle);
    sound.play(Snd.shipexp, false);
    p.remove();

    Powers.shield--;
    if(Powers.shield > 0)
    {
      addHit(ship.x, ship.y, ship.speed,
             ship.dir, ship.angle, ship.hitmask);
      return;
    }

    lives--;
    if(lives < 0)
    {
      game_mode = 3;
      sound.play(Snd.gameover1, false);
      sound.play(Snd.gameover2, false);
      return;
    }

    addExplosion(ship.x, ship.y, ship.speed,
                 ship.dir, ship.angle);
    sound.play(Snd.shipexp, false);

    Powers.removeLevel();
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
  }

  public void addHit(float x, float y, float speed,
                           float dir, float angle, Bitmap hitmask)
  {
    int i;

    for(i = 0; i < MAX_HITS; i++)
    {
      if(!hit[i].status)
        break;
    }
    if(i < MAX_HITS)
      hit[i].add(x, y, speed, dir, angle, hitmask);
  }

  public void addPoint(float x, float y, int value)
  {
    int i;

    for(i = 0; i < MAX_POINTS; i++)
    {
      if(!point[i].status)
        break;
    }
    if(i < MAX_POINTS)
      point[i].add(x, y, value);
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
    lives = 3;
    nextExtralifeScore = 10000;
    Powers.reset();
    Level.set(0);
    Score.reset();
    restartLevel();
  }

  private void restartLevel()
  {
    int i;

    ship.add();
    Level.set(Level.current);

    for(i = 0; i < Level.rocks; i++)
    {
      rock[i].remove();
      rock[i].add();
    }

    for(i = 0; i < Level.meteors; i++)
    {
      meteor[i].remove();
      meteor[i].add();
    }

    for(i = 0; i < MAX_SHOTS; i++)
      shot[i].remove();
    for(i = 0; i < MAX_UFOS; i++)
      ufo[i].remove();
    for(i = 0; i < MAX_UFOSHOTS; i++)
      ufoshot[i].remove();
    for(i = 0; i < MAX_HITS; i++)
      hit[i].remove();
    for(i = 0; i < MAX_EXPS; i++)
      exp[i].remove();
    for(i = 0; i < MAX_POINTS; i++)
      point[i].remove();
    powerup.remove();
    extralife.remove();

    rock_count = Level.rocks;
    shot_timer = 0;
    ship.x = Screen.w / 2;
    ship.y = Screen.h / 2;

    Powers.updateLevel();

    powerup.setLevel();
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

