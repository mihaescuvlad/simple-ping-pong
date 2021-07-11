import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ping_pong extends PApplet {

  int scorep1=0, scorep2 =0;   // player scores
  float scorep1x = 150, scorep2x = 450, scorey = 60; // coordinates for score drawing
  float mx1 = 300, my1 = 0, mx2 = 300, my2 = 400;   // middle line coordinates

  float cx = 300, cy = 200;      // circle position 
  float pcx, pcy;              // previous circle position
  float r = 5;      // circle radius
  float bspeed = 10;    // ball speed
  float hvel = bspeed;     // circle horisontal velocity
  float vvel = 0;     // circle vertical velocity
  
  float s1x = 5, s1y = 200;    // paddle position
  float s1w = 10, s1h = 100;    // and dimensions
  
  float s2x = 595, s2y = 200;    // paddle position
  float s2w = 10, s2h = 100;    // and dimensions
  
  float speed = 11;    // paddle speed
  float bAng = 0;    // boucne angle
  
  boolean upPressed = false, downPressed = false, wPressed = false, sPressed = false;


  PFont f; // font
public void setup() {
  
  noStroke();
  noCursor();
  rectMode(CENTER);
  f = createFont("Consolas Bold", 24);
  textFont(f);
  textAlign(CENTER, CENTER);
}

public void draw() {
  background(255);
  
  text(scorep1,scorep1x,scorey);
  text(scorep2,scorep2x,scorey);
  fill(255,100,0);
  
  // draw the middle line
  stroke(10,10,10, 150);
  line(mx1,my1,mx2,my2);
  stroke(0, 0);
  
  // update square to keyboard imputs
  if (wPressed) s1y -= speed;
  if (sPressed) s1y += speed;
  if (upPressed) s2y -= speed;
  if (downPressed) s2y += speed;
  
  // save previous ball position
  pcx = cx;
  pcy = cy;
  // update ball position
  cx+=hvel;
  cy+=vvel;
  
  // check for collision
  // if hit, change rectangle color
  boolean hitsp1 = circleRect(cx,cy,r, s1x,s1y,s1w,s1h);
  if (hitsp1) {
    fill(255,150,0);
    // calculate bounce angle
    bAng = bounce(s1y,s1h,cy);
    hvel = bspeed*cos(bAng);
    vvel = bspeed*sin(bAng);
    // move ball to last position
    cx = pcx;
    cy = pcy;
  }
  else {
    fill(255,100,0);
  }
  rect(s1x,s1y, s1w,s1h);
  
  boolean hitsp2 = circleRect(cx,cy,r, s2x,s2y,s2w,s2h);
  if (hitsp2) {
    fill(255,150,0);
    // calculate bounce angle
    bAng = bounce(s2y,s2h,cy);
    hvel = bspeed*-cos(bAng);
    vvel = bspeed*-sin(bAng);
    // move ball to last position
    cx = pcx;
    cy = pcy;
  }
  else {
    fill(0,150,255);
  }
  rect(s2x,s2y, s2w,s2h);
  
  if(cy < 2.5f || cy > 397.5f)
  {
    vvel = vvel*(-1);
  }

  // draw the circle
  fill(0, 150);
  ellipse(cx,cy, r*2,r*2);
  
  
  // if one side wins increase scores and reset the game
  if(cx < 0)
  {
    ++scorep2;
    
    s1x = 5; s1y = 200;
    s2x = 595; s2y = 200;
    
    cx = 580; cy = 200;
    hvel = -bspeed;
    vvel = 0;
    
  }
  else if (cx > 600)
  {
    ++scorep1;
    
    s1x = 5; s1y = 200;
    s2x = 595; s2y = 200;
    
    cx = 20; cy = 200;
    hvel = bspeed;
    vvel = 0;
  }
  
}

// get contact side
public float getSide(float cp,float rp, float rs)
{
  // temporary variable to set edges for testing
  float test = cp;
  
  // which edge is closest?
  if( cp < rp-rs/2)       test = rp - rs/2;
  else if ( cp > rp+rs/2) test = rp + rs/2;
  
  // get distance between the 2 points
  float dist = cp - test;
  return dist;
}

// circle/rectangle collision
public boolean circleRect(float cx, float cy, float radius, float rx, float ry, float rw, float rh) {

  // temporary variables to set edges for testing
  float distX = getSide(cx,rx,rw);
  float distY = getSide(cy,ry,rh);
  
  float distance = sqrt( (distX*distX) + (distY*distY) );

  // if the distance is less than the radius, collision!
  if (distance <= radius) {
    return true;
  }
  return false;
}

// bounce angle
public float bounce(float sy, float sh, float cy)
{
  float pi = 180;
  float mBounceAngle = 5*pi/12;    // 75 degrees
  float hitLocY = sy - cy;        // position compared to paddle position
  
  float norm_hitLocY = hitLocY/(sh/2);  // normalised position [-1,1]
  
  float bounceAngle = norm_hitLocY * mBounceAngle;
  
  return bounceAngle;
}

// player movement controls
public void keyPressed() {
  if (keyCode == UP) upPressed = true;
  else if (keyCode == DOWN) downPressed = true;
  
  if (keyCode == 'w' || keyCode == 'W') wPressed = true;
  else if (keyCode == 's' || keyCode == 'S') sPressed = true;
}

public void keyReleased() {
   if (keyCode == UP) upPressed = false;
   else if (keyCode == DOWN) downPressed = false;
  
   if (keyCode == 'w' || keyCode == 'W') wPressed = false;
   else if (keyCode == 's' || keyCode == 'S') sPressed = false;
 }
  public void settings() {  size(600,400); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ping_pong" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
