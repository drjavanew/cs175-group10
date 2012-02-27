
public class Matrix {
  public void testing() {
    double a[][]= {{ 1,  1,  1},
                   {-1,  3, -1},
                   {-1, -1,  3},
                   {-1, 1,  3},};
    
    double b[][]= {{ 1,  0,  1,2,1},
            {-1,  3, 0,1,0},
            {-1, 0,  3,4,7},};
    
    double d[][] = multiply(a,b);
    for (int i=0; i<d.length; ++i) 
    {
    	for (int j=0; j<d[0].length; ++j)
           System.out.print(d[i][j] +  "    ");
    	System.out.println();
    }System.out.println();
    
    double[][] c = transpose(a);
    for (int i=0; i<c.length; ++i) 
    {
    	for (int j=0; j<c[0].length; ++j)
           System.out.print(c[i][j] +  "    ");
    	System.out.println();
    }
    System.out.println();
    double e[][] = transpose(b);
    for (int i=0; i<e.length; ++i) 
    {
    	for (int j=0; j<e[0].length; ++j)
           System.out.print(e[i][j] +  "    ");
    	System.out.println();
    }
    System.out.println();
  }

  public int[][] createIdentityInt(int size) {
	  int[][] a = new int[size][size];
	  
	  for (int i =0; i<size; i++) {
		  a[i][i] = 1;
	  }
	  
	  return a;
  }
  
  public double[][] createIdentity(int size) {
	  double[][] a = new double[size][size];
	  
	  for (int i =0; i<size; i++) {
		  a[i][i] = 1;
	  }
	  
	  return a;
  }
  
  public double[][] convertArr2Matrix (double[] A) {
	  int size = A.length;
	  double[][] B = new double[1][size];
	  System.arraycopy(A, 0, B[0], 0, size);
	  return B;
  }
  
  public int[][] convertArr2Matrix (int[] A) {
	  int size = A.length;
	  int[][] B = new int[1][size];
	  System.arraycopy(A, 0, B[0], 0, size);
	  return B;
  }
  
  public int[][] resize(int A[][], int newrow, int newcol ) {
	  	
	  int [][] B = new int[newrow][newcol];
	  if (A.length !=0) {
	  for (int i =0; i< A.length; i++)
		   System.arraycopy(A[i], 0, B[i], 0, A[i].length);
	  }
	  return B;
  }
  
  public double[][] resize(double A[][], int newrow, int newcol ) {
	  	
	  double [][] B = new double[newrow][newcol];
	  if (A.length !=0) {
	  for (int i =0; i< A.length; i++)
		   System.arraycopy(A[i], 0, B[i], 0, A[i].length);
	  }
	  return B;
  }
  
  public double[][] subtract (double A[][], int B[][]){
	  int row = A.length;
	  int col = A[0].length;
	  double[][] C = new double[row][col];
	  
	  for (int i=0; i<row; i++) 
		  for (int j = 0; j<col;j++)
			  C[i][j] = A[i][j] - B[i][j];
	  
	  return C;
	  }
  
  public double[][] subtract (int A[][], int B[][]){
	  int row = A.length;
	  int col = A[0].length;
	  double[][] C = new double[row][col];
	  
	  for (int i=0; i<row; i++) 
		  for (int j = 0; j<col;j++)
			  C[i][j] = A[i][j] - B[i][j];
	  
	  return C;
	  }
  
  public double[][] subtract (double A[][], double B[][]){
	  int row = A.length;
	  int col = A[0].length;
	  double[][] C = new double[row][col];
	  
	  for (int i=0; i<row; i++) 
		  for (int j = 0; j<col;j++)
			  C[i][j] = A[i][j] - B[i][j];
	  
	  return C;
	  }
  
  public int[][] mulConst(int A[][], int constant){
	  int row = A.length;
	  int col = A[0].length;
	  int[][] C = new int[row][col];
	  
	  for (int i=0; i<row; i++) 
		  for (int j = 0; j<col;j++)
			  C[i][j] = constant*A[i][j] ;
	  
	  return C;
  }
  
  public double[][] mulConst(double A[][], int constant){
	  int row = A.length;
	  int col = A[0].length;
	  double[][] C = new double[row][col];
	  
	  for (int i=0; i<row; i++) 
		  for (int j = 0; j<col;j++)
			  C[i][j] = constant*A[i][j] ;
	  
	  return C;
  }
  public void print(double e[][], String name) {
	  System.out.println(name + ":");
	  for (int i=0; i<e.length; ++i) 
	    {
	    	for (int j=0; j<e[0].length; ++j)
	           System.out.print(e[i][j] +  "    ");
	    	System.out.println();
	    }
	    System.out.println();
  }
  
  public int[][] transpose(int A[][]) {
	  int arow = A.length;
	  int acol = A[0].length;
	  
	  int[][] B = new int[acol][arow];
	  
	  for (int i=0; i<acol; ++i) 
	    	for (int j=0; j<arow; ++j)
	    		B[i][j] = A[j][i];
	  return B;
  }
  
  public double[][] transpose(double A[][]) {
	  int arow = A.length;
	  int acol = A[0].length;
	  
	  double[][] B = new double[acol][arow];
	  
	  for (int i=0; i<acol; ++i) 
	    	for (int j=0; j<arow; ++j)
	    		B[i][j] = A[j][i];
	  return B;
  }
  
  public int[][] multiply(int A[][], int B[][]) {
	  int arow = A.length;
	  int acol = A[0].length;
	  int brow = B.length;
	  int bcol = B[0].length;
	  int sum;
	  int[][] C = new int[arow][bcol];
	  
	  
      int[] bcolj = new int[brow];
      for (int j = 0; j < bcol; j++) {
          for (int k = 0; k < brow; k++) bcolj[k] = B[k][j];
          for (int i = 0; i < arow; i++) {
              int[] arowi = A[i];
              sum = 0;
              for (int k = 0; k < acol; k++) {
                  sum += arowi[k] * bcolj[k];
              }
              C[i][j] = sum;
          }
      }
	return C;
  }
  
  public double[][] multiply(double A[][], int B[][]) {
	  int arow = A.length;
	  int acol = A[0].length;
	  int brow = B.length;
	  int bcol = B[0].length;
	  double[][] C = new double[arow][bcol];
	  
	  
      int[] bcolj = new int[brow];
      for (int j = 0; j < bcol; j++) {
          for (int k = 0; k < brow; k++) bcolj[k] = B[k][j];
          for (int i = 0; i < arow; i++) {
              double[] arowi = A[i];
              double sum = 0.0;
              for (int k = 0; k < acol; k++) {
                  sum += arowi[k] * bcolj[k];
              }
              C[i][j] = sum;
          }
      }
	return C;
  }
  public double[][] multiply(double A[][], double B[][]) {
	  int arow = A.length;
	  int acol = A[0].length;
	  int brow = B.length;
	  int bcol = B[0].length;
	  double[][] C = new double[arow][bcol];
	  
	  
      double[] bcolj = new double[brow];
      for (int j = 0; j < bcol; j++) {
          for (int k = 0; k < brow; k++) bcolj[k] = B[k][j];
          for (int i = 0; i < arow; i++) {
              double[] arowi = A[i];
              double sum = 0.0;
              for (int k = 0; k < acol; k++) {
                  sum += arowi[k] * bcolj[k];
              }
              C[i][j] = sum;
          }
      }
	return C;
  }
  
  public double[][] invert(double a[][]) {
    int n = a.length;
    double x[][] = new double[n][n];
    double b[][] = new double[n][n];
    int index[] = new int[n];
    for (int i=0; i<n; ++i) b[i][i] = 1;

 // Transform the matrix into an upper triangle
    gaussian(a, index);

 // Update the matrix b[i][j] with the ratios stored
    for (int i=0; i<n-1; ++i)
      for (int j=i+1; j<n; ++j)
        for (int k=0; k<n; ++k)
          b[index[j]][k]
            -= a[index[j]][i]*b[index[i]][k];

 // Perform backward substitutions
    for (int i=0; i<n; ++i) {
      x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
      for (int j=n-2; j>=0; --j) {
        x[j][i] = b[index[j]][i];
        for (int k=j+1; k<n; ++k) {
          x[j][i] -= a[index[j]][k]*x[k][i];
        }
        x[j][i] /= a[index[j]][j];
      }
    }
  return x;
  }

// Method to carry out the partial-pivoting Gaussian
// elimination.  Here index[] stores pivoting order.

  public void gaussian(double a[][],
    int index[]) {
    int n = index.length;
    double c[] = new double[n];

 // Initialize the index
    for (int i=0; i<n; ++i) index[i] = i;

 // Find the rescaling factors, one from each row
    for (int i=0; i<n; ++i) {
      double c1 = 0;
      for (int j=0; j<n; ++j) {
        double c0 = Math.abs(a[i][j]);
        if (c0 > c1) c1 = c0;
      }
      c[i] = c1;
    }

 // Search the pivoting element from each column
    int k = 0;
    for (int j=0; j<n-1; ++j) {
      double pi1 = 0;
      for (int i=j; i<n; ++i) {
        double pi0 = Math.abs(a[index[i]][j]);
        pi0 /= c[index[i]];
        if (pi0 > pi1) {
          pi1 = pi0;
          k = i;
        }
      }

   // Interchange rows according to the pivoting order
      int itmp = index[j];
      index[j] = index[k];
      index[k] = itmp;
      for (int i=j+1; i<n; ++i) {
        double pj = a[index[i]][j]/a[index[j]][j];

     // Record pivoting ratios below the diagonal
        a[index[i]][j] = pj;

     // Modify other elements accordingly
        for (int l=j+1; l<n; ++l)
          a[index[i]][l] -= pj*a[index[j]][l];
      }
    }
  }


}
