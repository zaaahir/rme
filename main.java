// Github Repository: https://github.com/zaaahir/rme
// Author: Zaahir Ali

package test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ThreadLocalRandom;

public class Principal {
	//2, 3, 5, 7, 13, 17, 19, 31, 61, 89, 107, 127, 521, 607, 1279, 2203, 2281, 3217
	static int n=521;
	static int[][] matA;
	static int[][] matB;
	
	public static void main(String args[]) throws IOException
	{
		boolean[][] mat = new boolean[n][n];
		boolean[] test = new boolean[n];
		boolean[] compare = new boolean[n];
		
		String hash;

		compare[0]=true;
		test[0]=true;
		int i;

		matA=matidx(readmat("C:\\test\\A.dat"));
		matB=matidx(readmat("C:\\test\\B.dat"));
		
		hash=hashtohex(compHash2("C:\\test\\test.zip"));
		System.out.println("hash2:"+hash);
		hash=hashtohex(compHash("C:\\test\\test.zip"));
		System.out.println("hash1:"+hash);
		
		//finding a primitive matrix
		/*
		for(i=1;i<10000;i++)
		{
			mat=randmat();
				
			if(isPrim(mat))
			{
				writemat(mat,"C:\\test\\test.dat");
				System.out.println("found");
				System.exit(0);
			}
			System.out.println(i);
		}	
		*/
    }
	
	public static boolean[] compHash(String path)  throws IOException
	{
		boolean[] ret = new boolean[n];
		boolean[] byt = new boolean[8];
		InputStream os = new FileInputStream(path);
		int size=(int) new File(path).length();
		int i,j,tmp,tmp2;
		
		
		tmp2=size;
		for(i=0;i<n;i++)
		{
			tmp=tmp2%2;
			if(tmp==1) ret[i]=true;
			else ret[i]=false;
			
			tmp2-=tmp;
			tmp2/=2;
			if(tmp2==0) i=n;
		}
		
		for(i=1;i<=size;i++)
		{
			byt=dectobin(os.read());
			for(j=0;j<8;j++)
			{
				if(byt[j]) ret=matact2(matA,ret);
				else ret=matact2(matB,ret);
			}
		}
		
		return ret;
	}
	
	public static boolean[] compHash2(String path) throws IOException
	{
		boolean[] ret = new boolean[n];
		boolean[] mi = new boolean[n];
		boolean[] byt = new boolean[8];
		InputStream os = new FileInputStream(path);
		int size=(int) new File(path).length();
		int tmp2,i,tmp,j,tmp3;
		tmp2=size;
		
		for(i=0;i<n;i++)
		{
			tmp=tmp2%2;
			if(tmp==1) ret[i]=true;
			else ret[i]=false;
			
			tmp2-=tmp;
			tmp2/=2;
			if(tmp2==0) i=n;
		}
		
		tmp3=0;
		for(i=1;i<=size;i++)
		{
			byt=dectobin(os.read());
			for(j=0;j<8;j++)
			{
				mi[tmp3]=byt[j];
				tmp3++;
				if(tmp3==n)
				{
					tmp3=0;
					ret=addition(addition(matact2(matA,addition(ret,mi)),matact2(matB,addition(not(ret),mi))),not(mi));
				}
			}
		}
		
		if(tmp3!=0)
		{
			for(i=tmp3;i<n;i++) mi[tmp3]=false;
			ret=addition(addition(matact2(matA,addition(ret,mi)),matact2(matB,addition(not(ret),mi))),not(mi));
		}
		
		return ret;
	}
	
	public static String hashtohex(boolean[] hash)
	{
		int i,j;
		boolean[] tmp = new boolean[8];
		String ret="";
		String tmp2;
		
		for(i=0;i<64;i++)
		{
			for(j=0;j<8;j++)
			{
				tmp[j]=hash[8*i+j];
			}
			tmp2=Integer.toHexString(bintodec(tmp));
			if(tmp2.length()==1) tmp2="0"+tmp2;
			ret+=tmp2;
		}
		
		return ret;
	}
	
	public static boolean[][] randmat()
	{
		int i,j,k;
		boolean[][] ret = new boolean[n][n];
		
		for(i=0;i<n;i++) ret[i][i]=true;
		
		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++)
			{
				if(i!=j && ThreadLocalRandom.current().nextInt(0, 2)==0)
				{
					for(k=0;k<n;k++) ret[k][j]^=ret[k][i];
				}
			}
		}
		
		return ret;
	}
	
	public static void writemat(boolean[][] mat, String path) throws IOException
	{
		OutputStream os = new FileOutputStream(path);
		int i,j,tmp=0;
		boolean[] vec=new boolean[8];
		
		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++)
			{
				vec[tmp]=mat[j][i];
				tmp++;
				if(tmp==8)
				{
					tmp=0;
					os.write(bintodec(vec));
				}
			}
		}
		
		if(tmp!=0)
		{
			for(i=tmp;i<=7;i++) vec[i]=false;
			os.write(bintodec(vec));
		}
		os.close();
	}
	
	public static int[][] matidx(boolean[][] mat)
	{
		int[][] ret = new int[n][n];
		int i,j,tmp;
		
		for(i=0;i<n;i++)
		{
			tmp=1;
			for(j=0;j<n;j++)
			{
				if(mat[j][i])
				{
					ret[tmp][i]=j;
					tmp++;
				}
			}
			ret[0][i]=tmp-1;
		}
		
		return ret;
	}
	
	public static boolean[] addition(boolean[] a, boolean[] b)
	{
		boolean[] ret = new boolean[n];
		int i;
		boolean tmp=false;
		
		for(i=n-1;i>=0;i--)
		{
			if(tmp)
			{
				if(a[i] && b[i])
				{
					ret[i]=true;
				}
				else if((a[i] && !b[i]) || (!a[i] && b[i]))
				{
					ret[i]=false;
					tmp=false;
				}
				else if(!a[i] && !b[i])
				{
					ret[i]=true;
					tmp=false;
				}
			}
			else
			{
				if(a[i] && b[i])
				{
					ret[i]=false;
					tmp=true;
				}
				else if((a[i] && !b[i]) || (!a[i] && b[i]))
				{
					ret[i]=true;
				}
				else if(!a[i] && !b[i])
				{
					ret[i]=false;
				}
			}
		}
		
		return ret;
	}
	
	public static boolean[] xor(boolean[] a, boolean b[])
	{
		boolean[] ret = new boolean[n];
		int i;
		
		for(i=0;i<n;i++) ret[i]=a[i]^b[i];
		
		return ret;
	}
	
	public static boolean[] not(boolean[] a)
	{
		boolean[] ret = new boolean[n];
		int i;
		
		for(i=0;i<n;i++) ret[i]=!a[i];
		
		return ret;
	}
	
	public static boolean[][] readmat(String path) throws IOException
	{
		InputStream os = new FileInputStream(path);
		boolean[] vec = new boolean[8];
		int i=0,j=0,k;
		boolean[][] ret = new boolean[n][n];
		
		while(true)
		{
			vec=dectobin(os.read());
			for(k=0;k<8;k++)
			{
				ret[i][j]=vec[k];
				j++;
				if(j==n)
				{
					j=0;
					i++;
					if(i==n)
					{
						os.close();
						return ret;
					}
				}
			}
		}
	}
	
	public static int bintodec(boolean[] vec)
	{
		int ret=0;
		int mult=1;
		int i;
		
		for(i=7;i>=0;i--)
		{
			if(vec[i]) ret+=mult;
			mult*=2;
		}
		return ret;
	}
	
	public static boolean[] dectobin(int byt)
	{
		boolean[] ret = new boolean[8];
		int i;
		int tmp=byt;
		
		for(i=7;i>=0;i--)
		{
			tmp=byt%2;
			if(tmp==1) ret[i]=true;
			byt-=tmp;
			byt/=2;
		}
		
		return ret;
	}
	
	public static boolean[][] matmultbool(boolean[][] mat1, boolean[][] mat2)
	{
		boolean[][] ret = new boolean[n][n];
		int i,j,k;
		
		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++)
			{
				for(k=0;k<n;k++) if(mat1[k][j] && mat2[i][k]) ret[i][j]=!ret[i][j];
			}
		}
		
		return ret;
	}
	
	public static boolean isPrim(boolean[][] mat)
	{
		boolean[][] tmp;
		int i;
		
		tmp=matmultbool(mat,mat);
		
		for(i=1;i<n;i++)
		{
			tmp=matmultbool(tmp,tmp);
		}
		
		return compmat(tmp,mat);
	}
	
	public static boolean compmat(boolean[][] mat1, boolean[][] mat2)
	{
		int i,j;
		
		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++) if(mat1[i][j]!=mat2[i][j]) return false;
		}
		return true;
	}
	
	public static void display(boolean[][] mat)
	{
		int i,j;
		
		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++)
			{
				if(mat[j][i]) System.out.print("1 ");
				else System.out.print("0 ");
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	public static boolean[] matact(boolean[][] mat, boolean[] vec)
	{
		int i,j;
		boolean[] ret = new boolean[n];
		
		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++)
			{
				if(mat[j][i] && vec[j]) ret[i]=!ret[i];
			}
		}
		
		return ret;
	}
	
	public static boolean[] matact2(int[][] mat, boolean[] vec)
	{
		int i,j;
		boolean[] ret = new boolean[n];
		
		for(i=0;i<n;i++)
		{
			for(j=1;j<=mat[0][i];j++)
			{
				if(vec[mat[j][i]]) ret[i]=!ret[i];
			}
		}
		
		return ret;
	}
	
	
	public static void displayvec(boolean[] vec)
	{
		int i;
		
		for(i=0;i<vec.length;i++)
		{
			if(vec[i]) System.out.print("1");
			else System.out.print("0");
		}
		System.out.println("");
	}

	
	//some stuff
	/*
	public static boolean isPrim2(int[] vec)
	{
		int[] x = {2,0,1};
		int[] y = {2,0,1};
		int i;
		int[] tmp;
		
		for(i=1;i<=n/2;i++)
		{
			x=mult(x,x);
			x=mod(x,vec);
		}
		
		tmp=Arrays.copyOf(x, x.length);
		x=add(x,y);
		
		if(gcd(x,vec))
		{
			x=tmp;
			for(i=1;i<=n/2;i++)
			{
				x=mult(x,x);
				x=mod(x,vec);
			}
			x=add(x,y);
			x=mod(x,vec);
			if(x[0]==-1 || (x[0]==1 && x[1]==0)) return true;
			else return false;
		}
		else return false;
	}
	
	public static boolean isInv(boolean[][] mat)
	{
		int i,j,k;
		boolean copy[][]=new boolean[n][n];
		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++) copy[i][j]=mat[i][j];
		}
	
		for(i=0;i<n;i++)
		{
			j=i;
			while(j<n && !copy[i][j]) j++;
			if(j==n) return false;
			
			if(j!=i) for(k=i;k<n;k++) copy[k][i]^=copy[k][j];
			
			for(j=i+1;j<n;j++) if(copy[i][j]) for(k=i;k<n;k++) copy[k][j]^=copy[k][i];
		}
	
		return true;
	}
	
	public static BigInteger[][] matmult(BigInteger[][] mat1, BigInteger[][] mat2)
	{
		BigInteger[][] ret = new BigInteger[n][n];
		int i,j,k;
		
		for(i=0;i<n;i++) for(j=0;j<n;j++) ret[i][j]=BigInteger.valueOf(0);
		
		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++)
			{
				for(k=0;k<n;k++) ret[i][j]=ret[i][j].add(mat1[k][j].multiply(mat2[i][k]));
			}
		}
		
		return ret;
	}
	
	public static int[] polychar(boolean[][] mat)
	{
		int[] ret = new int[n+1];
		BigInteger[][] copy = new BigInteger[n][n];
		BigInteger[][] M = new BigInteger[n][n];
		BigInteger[] coef = new BigInteger[n+1];
		BigInteger tmp;
		coef[n]=BigInteger.valueOf(1);
		ret[n]=1;
		
		int i,j,k;
		
		for(i=0;i<n;i++) 
		{
			for(j=0;j<n;j++)
			{
				if(i==j) M[i][i]=BigInteger.valueOf(1);
				else M[i][j]=BigInteger.valueOf(0);
			}
		}
		
		for(i=0;i<n;i++)
		{
			for(j=0;j<n;j++)
			{
				if(mat[i][j]) copy[i][j]=BigInteger.valueOf(1);
				else copy[i][j]=BigInteger.valueOf(0);
			}
		}
	
		
		for(i=n-1;i>=0;i--)
		{
			M=matmult(copy,M);
			
			
			tmp=BigInteger.valueOf(-1).multiply(tr(M));
			tmp=tmp.divide(BigInteger.valueOf(n-i));

			coef[i]=tmp;
			if(!tmp.mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0))) ret[i]=1;
			for(j=0;j<n;j++) M[j][j]=M[j][j].add(tmp);
		}
		
		return ret;
	}
	
	public static void displayvec(int[] vec)
	{
		int i;
		
		for(i=0;i<vec.length;i++)
		{
			System.out.print(vec[i]+" ");
		}
		System.out.println("");
	}
	
	public static BigInteger tr(BigInteger[][] mat)
	{
		BigInteger ret=BigInteger.valueOf(0);
		int i;
		for(i=0;i<n;i++) ret=ret.add(mat[i][i]);
		return ret;
	}
	
	public static int[] mod(int[] ai, int[] bi)
	{
		int i;
		int temp;
		int mult;
		
		int[] a = Arrays.copyOf(ai, ai[0]+1); 
		int[] b = Arrays.copyOf(bi, bi[0]+1); 
		
		while(true)
		{
			if(a[0]<b[0]) return a;
			
			mult=a[a[0]];

			a[a[0]]=0;
			temp=a[0]-1;

			for(i=b[0]-1;i>=1;i--)
			{
				a[temp]=(a[temp]+(b[i]*mult)%2)%2;				
				temp--;
			}
			
			for(i=a[0]-1;i>=1;i--)
			{
				if(a[i]!=0)
				{
					a[0]=i;
					if(a[i]<0) a[i]+=2;
					i=0;
				}
			}
			if(i==0) {a[0]=-1;return a;}
		}
	}
	
	public static int[] mult(int ai[], int[] bi)
	{
		int[] z= {1,0};
		if((ai[0]==1 && ai[1]==0)||(bi[0]==1 && bi[1]==0)) return z;
		
		int[] ret = new int[ai[0]+bi[0]];
		int i,j;
		
		for(i=1;i<=ai[0];i++)
		{
			for(j=1;j<=bi[0];j++)
			{
				ret[i+j-1]=(ret[i+j-1]+(ai[i]*bi[j]))%2;
			}
		}
		ret[0]=ai[0]+bi[0]-1;
		return ret;
	}
	
	public static boolean gcd(int[] ai, int[] bi)
	{
		int[] tmp;
		while(bi[0]!=-1 && !(bi[0]==1 && bi[1]==0))
		{
			tmp=Arrays.copyOf(bi, bi.length);
			bi=mod(ai,bi);
			ai=tmp;
		}

		if(ai[0]==1 && ai[1]==1) return true;
		else return false;
	}
	
	public static int[] add(int[] ai, int[] bi)
	{
		int ret[];
		int al=ai[0];
		int bl=bi[0];
		int i;
		int op;
		
		if(al==bl)
		{
			op=(ai[al]+bi[al])%2;
			while(op==0 && al>1) {al--;op=(ai[al]+bi[al])%2;}
			ret = new int[al+1];
			for(i=1;i<al;i++) ret[i]=(ai[i]+bi[i])%2;
			ret[al]=op%2;
			ret[0]=al;
		}
		else if(al>bl)
		{
			ret = new int[al+1];
			for(i=1;i<=bl;i++) ret[i]=(ai[i]+bi[i])%2;
			for(i=bl+1;i<=al;i++) ret[i]=ai[i];
			ret[0]=al;
		}
		else
		{
			ret = new int[bl+1];
			for(i=1;i<=al;i++) ret[i]=(ai[i]+bi[i])%2;
			for(i=al+1;i<=bl;i++) ret[i]=bi[i];
			ret[0]=bl;
		}
		
		return ret;
	}
	*/
}

	