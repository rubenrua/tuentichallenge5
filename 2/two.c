/** Challenge 2 - Almost prime
 *
 * Used homemade algorithm inspired on: http://rosettacode.org/wiki/Sieve_of_Eratosthenes 
 *
 * 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20
 *       4   6     9 10          14 15       
 *
 * @author rubenrua@gmail.com
 *
 * TODO: use ptheads...
 * NOTE: I need changed my MacBook Pro (Late 2008) :(
 **/
#include <stdio.h>

#define MAX 100000000

static unsigned list[MAX];

void print_list(){
  long i;
  for ( i = 0; i < MAX; i++ ) {
    printf("%u, ", list[i]);
  }
  printf("\n");    
}

void print_index(unsigned c){
  long i;
  for ( i = 0; i < MAX; i++ ) {
    if (c == list[i]) {
      printf("%li, ", i);
    }
  }
  printf("\n");    
}

long count_index(long min, long max, unsigned c){
  long i, l=0;
  for ( i = min; i <= max; i++ ) {
    if (c == list[i]) {
      l++;
    }
  }
  return l;
}

void init_list(){
  long i,j,p;
  for ( i = 2; i < MAX; i++ ) {
    for ( j = i; j < MAX; j = j + i ) {
      list[j]++;
    }
    p = i*i;
    
    if(p < MAX) {
      list[p]++;
      p = i*i*i;
      if(p < MAX) {
        list[p] += 2;
      }
    }
  }
}

int main() {
  int lines, i;
  long max, min;
  init_list();
  //print_index(3);
  scanf("%d", &lines);
  for(i=0; i<lines; i++) {
    scanf("%li %li\n", &min, &max);
    printf("%li\n", count_index(min, max, 3));
  }  
}


