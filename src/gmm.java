/**
 * Created by novas on 16/9/18.
 */
import Jama.Matrix;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class gmm {
    public static void main(String[] args)throws Exception
    {
            double[][] data=readData("0.txt");
System.out.println(data[0].length);
         gmm(data, 2, data[0].length);
    }
    public static double[][] readData(String name) throws Exception {
        BufferedReader br=new BufferedReader(new FileReader(name));
        String line=br.readLine();
        ArrayList<double[]> arrayList=new ArrayList<>();
        while (line!=null)
        {
            String[] args=line.split(",");
            double[] d=new double[args.length];
            for(int j=0;j<args.length;j++)
            {
                d[j]=Double.valueOf(args[j]);
            }
          //  d[0]=Double.valueOf(args[0]);
           // d[1]=Double.valueOf(args[1]);
           // data[index]=d;
            arrayList.add(d);
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
    public static Matrix[] getParamsRR(int k,int hang,int lie)
    {
        Matrix[] matrixes=new Matrix[k];
        for(int i=0;i<k;i++)
        {
            double[][] rr=new double[hang][lie];
            for(int j=0;j<hang;j++)
            {
                for(int p=0;p<lie;p++)
                {
                    if(p==j)
                    rr[j][p]=1;
                }
            }
            matrixes[i]=new Matrix(rr);
        }
        return matrixes;
    }
//c表示类别数，
    public static void gmm(double[][] data,int leibie,int tezheng)
    {
        Matrix[] sigmaarray=getParamsRR(5, tezheng, tezheng);
        Matrix[] uarray=getParamsRR(5,1,tezheng);

        double[] paiarray=new double[leibie];
        for(int i=0;i<paiarray.length;i++)
        {
            paiarray[i]=1.0/paiarray.length;
        }
       // sigmaarray[0].print(tezheng,tezheng);

        int count=0;
        double pre=Double.MIN_VALUE;
        double now=Double.MIN_VALUE;
        do
        {
            pre=now;
            count++;
            //E步骤
            //存储第n个数据分到第k类的概率
            double[][] r=new double[data.length][leibie];
            double[] Nk=new double[leibie];
            for (int i=0;i<data.length;i++)
            {
                //j表示类别
                for(int j=0;j<leibie;j++)
                {
                    double[][] xx=new double[1][tezheng];
                    xx[0]=data[i];
                    Matrix x=new Matrix(xx);
                    double sum=0;
                    for(int k=0;k<leibie;k++)
                    {
                        sum=sum+paiarray[k]*N(sigmaarray[k],x,uarray[k]);
                    }
                    r[i][j]=paiarray[j]*N(sigmaarray[j],x,uarray[j])/sum;
                   // System.out.println(r[i][j]);
                    if(r[i][j]>1.0/leibie)
                    {
                        Nk[j]++;
                    }
                }
            }
            //M步骤 使用r重新计算参数
            for(int i=0;i<leibie;i++)
            {
                double[][] u=new double[1][tezheng];
                Matrix uk=new Matrix(u);
                double[][] s=new double[tezheng][tezheng];
                Matrix sk=new Matrix(s);
                for(int j=0;j<data.length;j++)
                {
                    double[][] xx=new double[1][tezheng];
                    xx[0]=data[j];
                    Matrix x=new Matrix(xx);
                    uk=uk.plus(x.times(r[j][i]));
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
                for(int j=0;j<leibie;j++)
                {
                    double[][] xx=new double[1][tezheng];
                    xx[0]=data[j];
                    Matrix x=new Matrix(xx);
                    sum1=sum1+paiarray[j]*N(sigmaarray[j],x,uarray[j]);
                }
                sum=sum+Math.log(sum1);
            }
            System.out.println("count="+count+"   "+sum);
            now=sum;
            sigmaarray[0].print(tezheng,tezheng);
            uarray[0].print(tezheng,tezheng);
            System.out.println(paiarray[0]);
            sigmaarray[1].print(tezheng, tezheng);
            uarray[1].print(tezheng, tezheng);
            System.out.println(paiarray[1]);
            System.out.println("now="+now+" pre="+pre+"  "+(now>pre));
        }while (Math.abs(now)!=Math.abs(pre));

    }

}
