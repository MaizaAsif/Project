//Coded by James Leshko

#include <iostream>
#include <Windows.h>
#include <conio.h>
#include <ctime>
 
using namespace std;
// 35 = walls
// 88 = ghost2
// 89 = ghost3

const int SMALLPILL = 111;
const int POWERPILL = 79;
const int PACMANRIGHT = 074;
const int PACMANLEFT = 076;
const int PACMANUP = 118;
const int PACMANDOWN = 94;

const int GHOST1 = 87;

const int BLANK = 000;

const int WALL = 35;

const int MAXROWS = 32;

const int MAXCOLUMNS = 28;
char keyboardValue = 127;
//Start position is [15][0]

int pacI = 15, pacJ = 0;

//Ghost1 Start postion is [16][14]

int ghostOneI = 16, ghostOneJ = 14;

int node1i = 16, node1j = 5;

int i = 0 , j = 0;

char key = 127;

 

char levelMatrix[MAXROWS][MAXCOLUMNS] =

    {

       { 35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35},

       { 35, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111,  35,  35, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111,  35},

       { 35, 111,  35,  35,  35,  35, 111,  35,  35,  35,  35,  35, 111,  35,  35, 111,  35,  35,  35,  35,  35, 111,  35,  35,  35,  35, 111,  35},

       { 35,  79,  35,  35,  35,  35, 111,  35,  35,  35,  35,  35, 111,  35,  35, 111,  35,  35,  35,  35,  35, 111,  35,  35,  35,  35,  79,  35},

       { 35, 111,  35,  35,  35,  35, 111,  35,  35,  35,  35,  35, 111,  35,  35, 111,  35,  35,  35,  35,  35, 111,  35,  35,  35,  35, 111,  35},

       { 35, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111,  35},

       { 35, 111,  35,  35,  35,  35, 111,  35,  35, 111,  35,  35,  35,  35,  35,  35,  35,  35, 111,  35,  35, 111,  35,  35,  35,  35, 111,  35},

       { 35, 111,  35,  35,  35,  35, 111,  35,  35, 111,  35,  35,  35,  35,  35,  35,  35,  35, 111,  35,  35, 111,  35,  35,  35,  35, 111,  35},

       { 35, 111, 111, 111, 111, 111, 111,  35,  35, 111, 111, 111, 111,  35,  35, 111, 111, 111, 111,  35,  35, 111, 111, 111, 111, 111, 111,  35},

       { 35,  35,  35,  35,  35,  35, 111,  35,  35,  35,  35,  35, 000,  35,  35, 000,  35,  35,  35,  35,  35, 111,  35,  35,  35,  35,  35,  35},

       { 35,  35,  35,  35,  35,  35, 111,  35,  35,  35,  35,  35, 000,  35,  35, 000,  35,  35,  35,  35,  35, 111,  35,  35,  35,  35,  35,  35},

       { 35,  35,  35,  35,  35,  35, 111,  35,  35, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000,  35,  35, 111,  35,  35,  35,  35,  35,  35},

       { 35,  35,  35,  35,  35,  35, 111,  35,  35, 000,  35,  35, 000, 000, 000, 000,  35,  35, 000,  35,  35, 111,  35,  35,  35,  35,  35,  35},

       { 35,  35,  35,  35,  35,  35, 111,  35,  35, 000,  35, 000, 000, 000, 000, 000, 000,  35, 000,  35,  35, 111,  35,  35,  35,  35,  35,  35},

       { 35,  35,  35,  35,  35,  35, 111,  35,  35, 000,  35, 000, 000, 000, 000, 000, 000,  35, 000,  35,  35, 111,  35,  35,  35,  35,  35,  35},

       {074, 000, 000, 000, 000, 000, 111, 000, 000, 000,  35, 000, 000, 000, 000, 000, 000,  35, 000, 000, 000, 111, 000, 000, 000, 000, 000, 000},

       { 35,  35,  35,  35,  35,  35, 111,  35,  35, 000,  35, 000, 000, 000, 000, 000, 000,  35, 000,  35,  35, 111,  35,  35,  35,  35,  35,  35},

       { 35,  35,  35,  35,  35,  35, 111,  35,  35, 000,  35, 000, 000, 000, 000, 000, 000,  35, 000,  35,  35, 111,  35,  35,  35,  35,  35,  35},
       
       { 35,  35,  35,  35,  35,  35, 111,  35,  35, 000,  35,  35, 000, 000, 000, 000,  35,  35, 000,  35,  35, 111,  35,  35,  35,  35,  35,  35},

       { 35,  35,  35,  35,  35,  35, 111,  35,  35, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000,  35,  35, 111,  35,  35,  35,  35,  35,  35},

       { 35,  35,  35,  35,  35,  35, 111,  35,  35, 000,  35,  35,  35,  35,  35,  35,  35,  35, 000,  35,  35, 111,  35,  35,  35,  35,  35,  35},

       { 35, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111,  35,  35, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111,  35},

       { 35, 111,  35,  35,  35,  35, 111,  35,  35,  35,  35,  35, 111,  35,  35, 111,  35,  35,  35,  35,  35, 111,  35,  35,  35,  35, 111,  35},

       { 35, 111,  35,  35,  35,  35, 111,  35,  35,  35,  35,  35, 111,  35,  35, 111,  35,  35,  35,  35,  35, 111,  35,  35,  35,  35, 111,  35},

       { 35,  79, 111, 111,  35,  35, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111,  35,  35, 111, 111,  79,  35},

       { 35,  35,  35, 111,  35,  35, 111,  35,  35, 111,  35,  35,  35,  35,  35,  35,  35,  35, 111,  35,  35, 111,  35,  35, 111,  35,  35,  35},

       { 35,  35,  35, 111,  35,  35, 111,  35,  35, 111,  35,  35,  35,  35,  35,  35,  35,  35, 111,  35,  35, 111,  35,  35, 111,  35,  35,  35},

       { 35, 111, 111, 111, 111, 111, 111,  35,  35, 111, 111, 111, 111,  35,  35, 111, 111, 111, 111,  35,  35, 111, 111, 111, 111, 111, 111,  35},

       { 35, 111,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35, 111,  35,  35, 111,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35, 111,  35},

       { 35, 111,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35, 111,  35,  35, 111,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35, 111,  35},

       { 35, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111,  35},

       { 35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35,  35}

    };

 

char ghostCurrent = levelMatrix[ghostOneI][ghostOneJ];

char ghostTrail = ghostCurrent[&ghostOneI][&ghostOneJ];

 

void getKeyPress();

bool blockValid(int block);

void Ghost();

void GoToXY(int column, int line);

bool ghostMoveUp();

bool ghostMoveDown();

bool ghostMoveLeft();

bool ghostMoveRight();

int findNode();

 

int pillCount = 244;
int lives = 3;
clock_t startTime = 0, endTime= 0;

bool superPellet = false;

bool pill = false;

 

 

 

int main()

{

    HANDLE  hConsole;

    int k =14;

 

    hConsole = GetStdHandle(STD_OUTPUT_HANDLE);

    SetConsoleTextAttribute(hConsole, k);

 
//100
    cout << "       ###############              "<< endl;        
//101
    cout << "      ##################        "<< endl;      
//102
    cout << "    #######################         "<< endl;        
//103
    cout << "  ##############   ###########                  "<< endl;
//104
    cout << "##############################                  "<< endl;
//105
    cout << "#######################                 "<< endl;
//106
    cout << "####################            "<< endl;
//107
    cout << "################            "<< endl;
//108
    cout << "#############               "<< endl;
//109
    cout << "###########                 "<< endl;  
//110
    cout << "#########                       "<< endl;
//111
    cout << "############            "<< "PACMAN" << endl;        

    cout << "##################          "<< endl;      

    cout << "#######################         "<< endl;        
    cout << "###########################                 "<< endl;

    cout << "##############################                  "<< endl;

    cout << "    #######################                 "<< endl;

    cout << "      ####################              "<< endl;

    cout << "        ################            "<< endl;

 

    cout << endl;

    system("pause");

    char key = 0;

    system("mode 80, 50");

    int pills = 30;
//125
    bool stopCounting  = false;
    do

    {
       //Set Start position
      GoToXY(0, 0);

       Ghost();

       getKeyPress();

       //Colum137
       for (i = 0; i <= 31; i++)

       {
	   
          //Row

          for (j = 0; j <= 27; j++)

          {

           //Walls

             if (levelMatrix[i][j] == WALL)

             {

               k = 1;

                SetConsoleTextAttribute(hConsole, k);

             }

             //Pacman

            else if (levelMatrix[i][j] == PACMANRIGHT || levelMatrix[i][j] == PACMANLEFT || levelMatrix[i][j] == PACMANUP ||levelMatrix[i][j] == PACMANDOWN)

           {

                k = 14;

                SetConsoleTextAttribute(hConsole, k);

             }

             //Blank

             else if (levelMatrix[i][j] == BLANK)

             {

                k = 0;

                SetConsoleTextAttribute(hConsole, k);

             }

             //Ghost

             else if (levelMatrix[i][j] == GHOST1)

             {

                if (superPellet)

                {

                   k = 7;

                    SetConsoleTextAttribute(hConsole, k);

                }

                else

                {

                    k = 4;

                    SetConsoleTextAttribute(hConsole, k);

                }

            }

             //pill

            else if (levelMatrix[i][j] == SMALLPILL)

             {

                k = 15;

               SetConsoleTextAttribute(hConsole, k);

             }

            //Power pill

             else if (levelMatrix[i][j] == POWERPILL)

             {

               k = 14;

                SetConsoleTextAttribute(hConsole, k);

             }

 

             cout << levelMatrix[i][j];

             if ( j == 27)

               cout << endl;

 

             if ( j == 27 && i == 31)

              stopCounting = true;

          } 

      }



       //Color

       k = 15;

       SetConsoleTextAttribute(hConsole, k);

        

       cout << "lives remaining " << lives << endl;
      endTime = clock();

       int elapsed = int((endTime - startTime) / CLOCKS_PER_SEC);

       //cout  << elapsed;

 

       if (elapsed > 5)

       {

          superPellet = false;

          elapsed = 0;

     }

    }

   while (lives >= 0 || pillCount != 0);

     

        system("pause");

    return 0;

};

 

bool ghostMoveUp()

{

    char ghostNextUp = levelMatrix[ghostOneI - 1][ghostOneJ];

      //Check the next upward block to be sure it is not a wall

    if ( ghostNextUp != WALL)

    {

       //Check the last block to see if there was a pill

       if ( ghostNextUp == SMALLPILL || ghostNextUp == 117 )

       {

          //put the pill back

         levelMatrix[ghostOneI - 1][ghostOneJ] = GHOST1;

 

         levelMatrix[ghostOneI][ghostOneJ] = ghostTrail;

          ghostOneI--;

       }

       else

       {

          pill = false;

          levelMatrix[ghostOneI][ghostOneJ] = BLANK;

          levelMatrix[ghostOneI - 1][ghostOneJ] = GHOST1;

          ghostOneI--;

       }

       return true;

    }

    else

    {

      return false;

    }

} 

bool ghostMoveDown()

{


    char ghostNextDown = levelMatrix[ghostOneI + 1][ghostOneJ];

    if ( ghostNextDown != WALL)

    {

       if ( ghostNextDown == SMALLPILL || ghostNextDown ==117)

       {

          levelMatrix[ghostOneI + 1][ghostOneJ] = GHOST1;

          levelMatrix[ghostOneI][ghostOneJ] = ghostNextDown;

          ghostOneI++;

       }

       else

       {

          levelMatrix[ghostOneI][ghostOneJ] = BLANK;

          levelMatrix[ghostOneI + 1][ghostOneJ] = GHOST1;

          ghostOneI++;

       }

       return true;

    }

    else
    {

       return false;

    }

}


bool ghostMoveLeft()
{
   char ghostNextLeft = levelMatrix[ghostOneI][ghostOneJ - 1];

  if ( ghostNextLeft != WALL)
      {
         if ( ghostNextLeft == SMALLPILL || ghostNextLeft ==117)

          {

            levelMatrix[ghostOneI][ghostOneJ - 1] = GHOST1;

             levelMatrix[ghostOneI][ghostOneJ] = ghostNextLeft;

           ghostOneJ--;

         }

          else

          {

             levelMatrix[ghostOneI][ghostOneJ] = BLANK;

             levelMatrix[ghostOneI][ghostOneJ - 1] = GHOST1;

             ghostOneJ--;

          }

           return true;

       }

       else

      {

          return false;

       }

 


}
 

bool ghostMoveRight()

{

    char ghostNextRight = levelMatrix[ghostOneI][ghostOneJ + 1];

    if ( ghostNextRight != WALL)

   {

       

       if ( ghostNextRight == SMALLPILL || ghostNextRight ==117)

      {

          levelMatrix[ghostOneI][ghostOneJ + 1] = GHOST1;

          levelMatrix[ghostOneI][ghostOneJ] = ghostNextRight;
          ghostOneJ++;
       }

       else

       {

          levelMatrix[ghostOneI][ghostOneJ] = BLANK;

          levelMatrix[ghostOneI][ghostOneJ + 1] = GHOST1;

          ghostOneJ++;

       }

       return true;

    }

    else

   {

       return false;

    }

 

}

 

void Ghost()

{

    if (superPellet)

    {

       //Ghost is moving up

      if ( pacI > ghostOneI)

       {

          //if wall found

          if (!ghostMoveUp())

          {

             //find node path

          }

       }

     

       //Ghost is moving down

       else if (pacI < ghostOneI)

       {

          if (!ghostMoveDown())

          {

 

          }

       }

 

       //Ghost is moving left
        if (pacJ > ghostOneJ)

       {

          if (!ghostMoveLeft())

          {

 

          }

       }

 

       //Ghost is moving right

       else if (pacJ < ghostOneJ)

       {

          if (!ghostMoveRight())

          {


          }

     }

    }

    else

   {

 

 

       //Ghost is moving up

       if ( pacI < ghostOneI)

       {

 

          //if wall found

          if (!ghostMoveUp())

          {

 

          }

       }

     

      //Ghost is moving down

      else if (pacI > ghostOneI)

       {
          if (!ghostMoveDown())

          {


         }

       }

 

       //Ghost is moving left

     if (pacJ < ghostOneJ)

      {

          if (!ghostMoveLeft())

          {

 
          }

      }


       //Ghost is moving right

       else if (pacJ > ghostOneJ)

       {

          if (!ghostMoveRight())

        {

 

          }

       }

    }

 

    //you caught the ghost

    if (pacJ == ghostOneJ && pacI == ghostOneI && superPellet)

    {

       levelMatrix[ghostOneI][ghostOneJ] = PACMANRIGHT;

       ghostOneI = 16;

       ghostOneJ = 14;

      levelMatrix[16][14] = GHOST1;

    }

    //Reset the postion of pacman you died

    else if (pacJ == ghostOneJ && pacI == ghostOneI)

    {

      lives--;

       levelMatrix[pacI][pacJ] = BLANK;

       levelMatrix[ghostOneI][ghostOneJ] = BLANK;

       pacI = 15;

       pacJ = 0;

      ghostOneI = 16;

       ghostOneJ = 14;

       levelMatrix[15][0] = PACMANRIGHT;

      levelMatrix[16][14] = GHOST1;

    }

}

 

void getKeyPress()
{

    if(GetAsyncKeyState(VK_UP))

    {

       if ( levelMatrix[pacI- 1][pacJ] != WALL)

       {

          if (levelMatrix[pacI- 1][pacJ] == POWERPILL)

          {

             startTime = clock();

             superPellet = true;

          }

          levelMatrix[pacI][pacJ] = BLANK;
         levelMatrix[pacI - 1][pacJ] = PACMANUP;

          pacI--;

       }

    }

    else if (GetAsyncKeyState(VK_LEFT)) //left

    {

       if ( levelMatrix[pacI][pacJ- 1] != WALL)

       {

          if (levelMatrix[pacI][pacJ - 1] == POWERPILL)

          {

            startTime = clock();

             superPellet = true;

          }

          levelMatrix[pacI][pacJ] = BLANK;

          levelMatrix[pacI][pacJ - 1] = PACMANLEFT;

          pacJ--;

       }

    }

    else if (GetAsyncKeyState(VK_RIGHT)) //right

    {

       if ( levelMatrix[pacI][pacJ+ 1] != WALL)
       {

          if (levelMatrix[pacI][pacJ + 1] == POWERPILL)

          {

             startTime = clock();

             superPellet = true;

          }

          levelMatrix[pacI][pacJ] = BLANK;

          levelMatrix[pacI][pacJ + 1] = PACMANRIGHT;

          pacJ++;

       }

    }

    else if (GetAsyncKeyState(VK_DOWN)) //down

    {

       if ( levelMatrix[pacI + 1][pacJ] != WALL)

       {

          if (levelMatrix[pacI + 1][pacJ] == POWERPILL)

          {

            startTime = clock();

           superPellet = true;

          }

          levelMatrix[pacI][pacJ] = BLANK;

          levelMatrix[pacI + 1][pacJ] = PACMANDOWN;

          pacI++;

       }

    }

}

 

void GoToXY(int column, int line)

{

 // Create a COORD structure and fill in its members.

    // This specifies the new position of the cursor that we will set.

    COORD coord;

    coord.X = column;

    coord.Y = line;

 
   // Obtain a handle to the console screen buffer.

    // (You're just using the standard console, so you can use STD_OUTPUT_HANDLE

  // in conjunction with the GetStdHandle() to retrieve the handle.)

    // Note that because it is a standard handle, we don't need to close it.

    HANDLE hConsole = GetStdHandle(STD_OUTPUT_HANDLE);

 

    // Finally, call the SetConsoleCursorPosition function.

    if (!SetConsoleCursorPosition(hConsole, coord))

    {

        // Uh-oh! The function call failed, so you need to handle the error.

        // You can call GetLastError() to get a more specific error code.

        // ...

    }

}

//126
    //create the matrix
//127
    //28 columns
//128
    //32 high

 

