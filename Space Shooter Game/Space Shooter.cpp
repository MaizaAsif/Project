#include<graphics.h>
#include<iostream>
#include<stdlib.h>
#include<time.h>
#include<stdio.h>

main()
{
	//Heigh is 900 
	//Width is 1440
	initwindow(1440,900);
	
	int x=720, y=800, flag=0 ;
	int bulletx=0 ,bullety=0;
	int ebx=0, eby=0 , currenteb=0 , eflag=0;  //enemy bullet
	int aliensy[11] = {150,150,150,150,150,250,250,250,250,250,250};
	int aliensx[11] = {240,480,720,960,1200,120,360,600,840,1080,1320};
	int alive[11] = {0,0,0,0,0,0,0,0,0,0,0};  //enemy alive(1) or dead(0)
	int  score = 0 , lives = 3 , level = 1 , speed = 5;
	char scorearr[4];
	char livesarr[1];
	char levelarr[1];
	int precaution;
	int instruction = 1;
	int page = 0;
	int scorecopy=0; // for optimisation purpose and avoiding speed increment.


	while(1)
	{
		setactivepage(page); //To prevent Flickering
		setvisualpage(1 - page);
		
		if (instruction == 1)   // Displays instrctions......................................................................
		{
			setbkcolor(9);
			cleardevice();
			setcolor(BLUE);
			settextstyle(3,0,8);
			outtextxy(370,100,"-- Space Shooter --");
			settextstyle(3,0,6);
			outtextxy(230,300,"Use left and right arrow key for movement");
			outtextxy(370,450,"Use Space Bar for Shooting");
			outtextxy(420,600,"Avoid Enemy Attacks");
			settextstyle(3,0,4);
			outtextxy(420,700,"<< press arrow keys to continue >>");

			setactivepage(1-page); //swapping pages to display drawing/text
			setvisualpage(page);
			getch();
			instruction = 0;		
		}
		
		
		
		//level selection...........................................................................................
		if (scorecopy == 0)
		{
			scorecopy++;
			level = 1;
			speed = 5;
			alive[0]=1;	
			alive[2]=1;
			alive[4]=1;
		}
		else if (scorecopy == 30)
		{
			scorecopy++;
			level = 2;
			speed += 2;
			alive[0]=1;
			alive[1]=1;		
			alive[2]=1;
			alive[3]=1;
			alive[4]=1;
		}
		else if (scorecopy == 80)
		{
			scorecopy++;
			level = 3;
			speed += 2;
			alive[0]=1;
			alive[1]=1;		
			alive[2]=1;
			alive[3]=1;
			alive[4]=1;
			alive[7]=1;
			alive[8]=1;			
		}
		else if (scorecopy == 150)
		{
			scorecopy++;
			level = 4;
			speed += 2;
			alive[0]=1;
			alive[1]=1;		
			alive[2]=1;
			alive[3]=1;
			alive[4]=1;
			alive[7]=1;
			alive[8]=1;	
			alive[6]=1;
			alive[9]=1;						
		}
		else if (scorecopy == 240)
		{
			scorecopy++;
			level = 5;
			speed += 2;
			alive[0]=1;
			alive[1]=1;		
			alive[2]=1;
			alive[3]=1;
			alive[4]=1;
			alive[7]=1;
			alive[8]=1;	
			alive[6]=1;
			alive[9]=1;	
			alive[5]=1;
			alive[10]=1;			
		} else if (scorecopy == 350)
		{
			//Game won
			cleardevice();
			setbkcolor(GREEN);
			cleardevice();
			setcolor(WHITE);
			settextstyle(3,0,9);
			
			outtextxy(400,350,"You Won!");
			settextstyle(3,0,4);
			outtextxy(370,500,"... press ENTER to RESTART or wait to EXIT ...");
			setactivepage(1-page); //flicker prevention issue resolved.
			setvisualpage(page);
			delay(3000);
			if(GetAsyncKeyState(VK_RETURN))
			{
			setbkcolor(9);
			cleardevice();
				score = 0;
				scorecopy = 0;
				x=720;
				y=800;
				eby = 920;
				lives = 3;
				page = 1 - page;
				continue;
			}
			
			break;
		}
		
		
		
		
		else if(lives>0) //-----------------------------------------------------------------------------------------------------//
		{
		
		cleardevice();  	//updating data .......//
		
		settextstyle(2,0,9);
		setcolor(BLUE);
		outtextxy(570,30,"Level :");
		outtextxy(50,30,"Score :");
		outtextxy(1100,30,"Lives :");
		sprintf(scorearr,"%d",score);
		outtextxy(180,30,scorearr);
		sprintf(livesarr,"%d",lives);
		outtextxy(1220,30,livesarr);
		sprintf(levelarr,"%d",level);
		outtextxy(690,30,levelarr);		
			
		//space ship components
		setcolor(YELLOW);
		setfillstyle(SOLID_FILL,YELLOW);
		circle(x,y,15);
		floodfill(x,y,YELLOW);
		setcolor(YELLOW);
		setfillstyle(SOLID_FILL,YELLOW);
		circle(x,y-15,8);
		floodfill(x,y-19,YELLOW);	
		arc(x,y+40,20,160,25);
		arc(x,y+40,20,160,32);
		arc(x,y+40,20,160,40);
		
	
		//printing enemy space ships
		setcolor(RED);
		setfillstyle(SOLID_FILL,RED);
		for (int i=0 ; i<11 ; i++)
		{
			if(alive[i] == 1)
			{
				circle(aliensx[i],aliensy[i],20);
				floodfill(aliensx[i],aliensy[i],RED);
				arc(aliensx[i],aliensy[i],45,135,27);
				arc(aliensx[i],aliensy[i],225,315,27);
				arc(aliensx[i],aliensy[i],60,120,32);
				arc(aliensx[i],aliensy[i],240,300,32);
			}
		}
		
		
		//choosing random enemy for attacking 
		srand(time(0));
		precaution = 0;
		do
		{
			currenteb = rand()%11;
			precaution++;
		} while (alive[currenteb]!=1 && precaution<10);
		
		if (eflag==0 && alive[currenteb]==1)
		{
			ebx = aliensx[currenteb];
			eby = aliensy[currenteb];
			eflag = 1;
		}
				
		//enemy bullet droping down ....................................................................................................
		setcolor(5);
		circle(ebx,eby,10);
		setfillstyle(SOLID_FILL,5);
		floodfill(ebx,eby,5);
		eby+=speed;
		
		if (eby>850)
		{
			eflag=0;
		}
		
	
		//shot bullet traveling upward
		if(bullety>0) //&& me_dead == 0)
		{
			setcolor(0);
			circle(bulletx,bullety,8);
			setfillstyle(SOLID_FILL,0);
			floodfill(bulletx,bullety,0);
			bullety-=35;
		} else
			flag = 0;
		
		
		//continuous inputs / movement .........................................................................................
		if(GetAsyncKeyState(VK_SPACE) && flag == 0) //shoot
		{
			bulletx = x;
			bullety = y;
			flag=1;	
		}
		if(GetAsyncKeyState(VK_RIGHT) && x<1390)
			x += 10;
		else if(GetAsyncKeyState(VK_LEFT) && x>50)
			x -= 10;
		else if(GetAsyncKeyState(VK_ESCAPE))
			break;
	
	
		//destroying enemies.....................................................................................................
		for (int i=0 ; i<11 ; i++)
		{
			if(alive[i] == 1)
			{
				if((bulletx<aliensx[i]+30) && (bulletx>aliensx[i]-30) && (bullety<aliensy[i]+20) && (bullety>aliensy[i]-30))
				{
					bullety = -20; //enemy bullet disappears
					alive[i] = 0;
					//updating score
					score += 10;
					scorecopy = score;  
				}	
			}
		}
		
		//enemies destroying me :(
		if((ebx<x+40) && (ebx>x-40) && (eby>y-20) && (eby<y+20))
		{
			//me_dead = 1;
			eby = 920; //bullet disappears
			lives--;
		}	
		
		
		delay(10);
	}  
	else // Game Over---------------------------------------------------------------------------------------------------------//
	{
			cleardevice();
			setbkcolor(RED);
			cleardevice();
			setcolor(WHITE);
			settextstyle(3,0,9);
			
			outtextxy(400,350,"GAME OVER");
			settextstyle(3,0,4);
			setactivepage(1-page); //flicker prevention issue resolved.
			setvisualpage(page);
			delay(2000);
			break;
	}
				
		
		page = 1 - page; //To prevent Flickering
	}
	
}
