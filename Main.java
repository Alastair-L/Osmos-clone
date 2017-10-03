ArrayList<Mover> movers;
int movernum = 100; 
float dragConst = 0.01;
float g = 0.02;
Mover player;
float difficulty = 0.5; //between 0 and 1
int mode = 1;
void setup(){
 size(1000,800);
 tsetup();
 movers = new ArrayList<Mover>();
 for(int i = 0; i<movernum;i++){
  movers.add(circles.get(i)); 
 }
 player = movers.get(50);//(int)(Math.floor(difficulty*movernum)));
 if(mode == 2){
    player.r = 60*difficulty;
 }
}

void draw(){
 background(0);
 for(int i = 0; i< movers.size();i++){
   Mover m = movers.get(i);
   /*
  PVector grav = new PVector(0,g*movers[i].mass);
  movers[i].addForce(grav);
  
  movers[i].addForce(getDrag(movers[i]));
  */
  if(m.r<=0){
   movers.remove(m);
   i--;
   continue;
  }
  //colorMode(HSB);
  if(m != player){
    if (m.r> player.r){
       m.c = color(255,20,m.r - player.r);
    }
    else{
       m.c = color(player.r - m.r,20,255);
    }
  }
  else{
    m.c = color(255,255,255);
  }
  
  //colorMode(RGB);
  for (Mover other : movers) {
          if (m != other) {
            float d = dist(m.pos.x, m.pos.y, other.pos.x, other.pos.y);
            if (d  < m.r + other.r) {
              if(m.r > other.r){
                
               float r11 = other.r;
               float r12 = d - m.r;
               float area = (float)(Math.PI*(r11*r11) - (Math.PI*(r12*r12)));
               
               float r21 = m.r;
               float area2= (float)(r21*r21*Math.PI);
               float r22 = (float)Math.sqrt((area+area2)/Math.PI);
              
               m.r = r22;
               other.r = r12;
               
               PVector newVel = PVector.add(PVector.mult(m.vel,area2),PVector.mult(other.vel,area));
               m.vel = newVel.div(area+area2);
              }
              //*/
            }
          }
        }
  m.updatePhysics();
  m.show();
  m.bounce();
 }
  
}

void mousePressed(){
  
  PVector mouse = new PVector(mouseX,mouseY);
  mouse.sub(player.pos);
  mouse.normalize();
  //mouse.mult(-1);
  movers.add(player.poo(mouse));
  mouse.mult(-2);
  player.addForce(mouse);
    
}

PVector getDrag(Mover m){
  PVector drag = m.vel.copy();
  drag.normalize();
  float speed = m.vel.mag();
  drag.mult(-1);
  drag.mult(dragConst*speed*speed);
  return drag;
}





//-------------------setup---------------------------------//














int attempts;
ArrayList<Mover> circles;

void tsetup() {
  circles = new ArrayList<Mover>();
  while(circles.size()<movernum){
    tdraw();
  }
  for (Mover c : circles) {
   c.r -= 0.1; 
   if(mode == 2){
     c.r -= 4;
     c.vel.x = random(-1,1);
     c.vel.y = random(-1,1);
   }
  }
}

void tdraw() {
   Mover newC = newMover();
   if (newC != null) {
     circles.add(newC);
   }

  for (Mover c : circles) {
    if (c.growing) {
      if (c.edges()) {
        c.growing = false;
      } else {
        for (Mover other : circles) {
          if (c != other) {
            float d = dist(c.pos.x, c.pos.y, other.pos.x, other.pos.y);
            if (d - 2 < c.r + other.r) {
              c.growing = false;
              break;
            }
          }
        }
      }
    }
    c.show();
    c.grow();
  }
}

Mover newMover() {

  float x = random(width);
  float y = random(height);

  boolean valid = true;
  
  for (Mover c : circles) {
    float d = dist(x, y, c.pos.x, c.pos.y);
    if (d < c.r) {
      valid = false;
      break;
    }
    
  }

  if (valid) {
    return new Mover(x, y);
  } else {
    return null;
  }
}
