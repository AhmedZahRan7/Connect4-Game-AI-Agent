package com.pennypop.project.controller.heurstics;

public class WeightedPlacesHeurstic implements Heurstic{
    int [][] priorities;
    public WeightedPlacesHeurstic (int width,int height){
        priorities =  new int[width][height];
        fillPriorities();
    }
    @Override
    public int evaluate(char[][] board) {
        return 0;
    }
    private void fillPriorities(){
        fillRows();
        fillColumns();
        fillDiagonals();
        printPriorities();
    }
    private void fillColumns(){
        for(int i=0;i<priorities[0].length;i++){
            int l=0,r=priorities.length-1;
            int counter = 1;
            while (l<r) {
                priorities[l++][i]+=counter;
                priorities[r--][i]+=counter;
                counter = Math.min(4,counter+1);
            }
            if(l==r) priorities[l][i]+=counter;
        }
    }
    private void fillRows(){
        for(int[] row : priorities){
            int l=0,r=row.length-1;
            int counter = 1;
            while (l<r) {
                row[l++]+=counter;
                row[r--]+=counter;
                counter = Math.min(4,counter+1);
            }
            if(l==r) row[l]+=counter;
        }
    }
    private void fillDiagonals(){
        fillDirectedDiagonal(1);
        fillDirectedDiagonal(-1);
    }
    private void fillDirectedDiagonal(int slope) {
        for (int i = 0; i < priorities.length; i++) {
            int squareSideLen = Math.min(slope != -1 ? priorities.length - i - 1 : i, priorities[i].length - 1);
            if (squareSideLen < 3) continue;
            int[] startPoint = new int[]{i, 0};
            int[] endPoint = new int[]{i + slope * squareSideLen, squareSideLen};
            boolean updateMirror = priorities[i].length - 1 - endPoint[1] != 0;
            int counter = 1;
            while (startPoint[1] < endPoint[1]) {
                int len = squareSideLen - startPoint[1];
                priorities[startPoint[0]][startPoint[1]] += counter;
                priorities[endPoint[0]][endPoint[1]] += counter;
                if(updateMirror) {
                    priorities[startPoint[0]][priorities[i].length - 1 - startPoint[1]] += counter;
                    priorities[endPoint[0]][priorities[i].length - 1 - endPoint[1]] += counter;
                }
                startPoint[1]++;
                startPoint[0] += slope;
                endPoint[0] -= slope;
                endPoint[1]--;
                if (len >= 4) counter++;
            }
            if (startPoint[0] == endPoint[0]) {
                priorities[startPoint[0]][startPoint[1]] += counter;
                priorities[startPoint[0]][priorities[i].length - 1 - startPoint[1]] += counter;
            }
        }
    }
    public void printPriorities(){
        for(int[] row : priorities){
            for(int i:row) System.out.print(i+" ");
            System.out.println();
        }
    }
}
