import Jama.Matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by novas on 16/9/23.
 */
public class Gmm2 {
    public static void main(String[] args)throws Exception
    {
        double[][] data=readData();

        gmm(data);
    }
    public static double[][] readData() throws Exception {
        BufferedReader br=new BufferedReader(new FileReader("Test1.csv"));
        String line=br.readLine();
        ArrayList<double[]> arrayList=new ArrayList<>();
        int index=0;
        while (line!=null)
        {
            double[] d=new double[2];
            String[] args=line.split(",");
            d[0]=Double.valueOf(args[0]);
            d[1]=Double.valueOf(args[1]);
            //data[index]=d;
            arrayList.add(d);
            index++;
            line=br.readLine();
        }
        double[][] data=new double[arrayList.size()][2];
        for(int i=0;i<data.length;i++)
        {
            data[i]=arrayList.get(i);
        }
        br.close();
        return data;
    }

    public static double N(Matrix sigma,Matrix x,Matrix u)
    {
        Matrix matrix=x.minus(u).times(sigma.inverse()).times(x.minus(u).transpose());
        double res=1.0/Math.sqrt(2*Math.PI*Math.abs(sigma.det()))*Math.exp(-0.5*matrix.get(0,0));
        return res;
    }
    public static Matrix[] getParamsRR(int k,int d)
    {
        Matrix[] matrixes=new Matrix[k];
        for(int i=0;i<matrixes.length;i++)
        {
            double[][] rr=new double[d][d];
            for(int j=0;j<d;j++)
            {
                for(int p=0;p<d;p++)
                {
                    rr[j][p]=new Random().nextDouble();
                }
            }
            matrixes[i]=new Matrix(rr);
        }
        return matrixes;
    }

    public static void gmm(double[][] data)
    {
        double[][] rr=new double[2][2];
        rr[0][0]=1;
        rr[1][1]=1;
        rr[0][1]=0;
        rr[1][0]=0;
        double[][] rr1=new double[2][2];
        rr1[0][0]=1;
        rr1[0][1]=0;
        rr1[1][0]=0;
        rr1[1][1]=1;

        double[][] uu1=new double[1][2];
        uu1[0][0]=1;
        uu1[0][1]=1;

        double[][] uu2=new double[1][2];
        uu2[0][0]=2;
        uu2[0][1]=2;

        //  Matrix sigma1=Matrix.random(2,2);
        // Matrix sigma2=Matrix.random(2,2);
        Matrix sigma1=new Matrix(rr);
        Matrix sigma2=new Matrix(rr1);
        double pai1=0.5;
        double pai2=0.5;
        Matrix u1=Matrix.random(1,2);
        Matrix u2=Matrix.random(1,2);
        //  Matrix u1=new Matrix(uu2);
        // Matrix u2=new Matrix(uu1);
         Matrix[] sigmaarray=new Matrix[2];
         sigmaarray[0]=sigma1;
         sigmaarray[1]=sigma2;


       // Matrix[] sigmaarray=getParamsRR(5,data[0].length);
        double[] paiarray=new double[2];
        paiarray[0]=pai1;
        paiarray[1]=pai2;

        Matrix[] uarray=new Matrix[2];
        uarray[0]=u1;
        uarray[1]=u2;
        int count=0;
        double pre=Double.MIN_VALUE;
        double now=Double.MIN_VALUE;
        do
        {
            pre=now;
            count++;
            //E步骤
            //存储第n个数据分到第k类的概率
            double[][] r=new double[1000][2];
            double[] Nk=new double[2];
            for (int i=0;i<data.length;i++)
            {
                //j表示类别
                for(int j=0;j<2;j++)
                {
                    double[][] xx=new double[1][2];
                    xx[0]=data[i];
                    Matrix x=new Matrix(xx);
                    double sum=0;
                    for(int k=0;k<2;k++)
                    {
                        sum=sum+paiarray[k]*N(sigmaarray[k],x,uarray[k]);
                    }
                    //  System.out.println("sum="+sum);
                    r[i][j]=paiarray[j]*N(sigmaarray[j],x,uarray[j])/sum;
                    if(r[i][j]>0.5)
                    {
                        Nk[j]++;
                    }
                    //  System.out.print(r[i][j]+"   ");
                }
                //   System.out.println();
            }
            // System.out.println(Nk[0]+"   "+Nk[1]);
            //M步骤 使用r重新计算参数
            for(int i=0;i<2;i++)
            {
                double[][] u=new double[1][2];
                Matrix uk=new Matrix(u);
                double[][] s=new double[2][2];
                Matrix sk=new Matrix(s);
                for(int j=0;j<data.length;j++)
                {
                    double[][] xx=new double[1][2];
                    xx[0]=data[j];
                    Matrix x=new Matrix(xx);
                    uk=uk.plus(x.times(r[j][i]));
                    //   uk.print(2,2);
                    Matrix t=x.minus(uarray[i]).transpose().times(x.minus(uarray[i])).times(r[j][i]);
                    sk=sk.plus(t);
                }
                uk=uk.times(1.0/(Nk[i]+1));
                sk=sk.times(1.0/(Nk[i]+1));
                uarray[i]=uk;
                sigmaarray[i]=sk;
                paiarray[i]=Nk[i]/data.length;
            }
            //计算极大似然函数
            double sum=0;
            for(int i=0;i<data.length;i++)
            {
                double sum1=0;
                for(int j=0;j<2;j++)
                {
                    double[][] xx=new double[1][2];
                    xx[0]=data[j];
                    Matrix x=new Matrix(xx);
                    sum1=sum1+paiarray[j]*N(sigmaarray[j],x,uarray[j]);
                }
                sum=sum+Math.log(sum1);
            }
            System.out.println("count="+count+"   "+sum);
            now=sum;
            sigmaarray[0].print(2,2);
            uarray[0].print(2,2);
            System.out.println(paiarray[0]);
            sigmaarray[1].print(2, 2);
            uarray[1].print(2, 2);
            System.out.println(paiarray[1]);
            System.out.println("now="+now+" pre="+pre+"  "+(now>pre));
        }while (Math.abs(now)!=Math.abs(pre));
    }
}
