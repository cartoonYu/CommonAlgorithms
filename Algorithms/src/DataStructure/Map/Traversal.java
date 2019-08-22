package DataStructure.Map;

public class Traversal {

    private boolean[] visited;

    public void depthTraversal(int[][] matrix){
        if(matrix==null||matrix.length==0){
            return;
        }
        visited=new boolean[matrix.length];
        for(int i=0,length=matrix.length;i<length;i++){
            if(!visited[i]){
                DFS(matrix,i);
            }
        }
    }

    private void DFS(int[][] matrix,int cur){
        visited[cur]=true;
        System.out.print(matrix[cur][0]+" ");
        for(int i=0;i<matrix[i].length;i++){
            if(matrix[cur][i]!=0&&!visited[i+1]){
                DFS(matrix,i);
            }
        }
    }
}
