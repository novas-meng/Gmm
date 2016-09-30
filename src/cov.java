/**
 * Created by novas on 16/9/30.
 */
public class cov {
    public static double[][] cov(double[][] data)
    {
        double[][] res=new double[data.length][data.length];
        double[] means=new double[data.length];
        for(int i=0;i<means.length;i++)
        {
            double[] temp=data[i];
            double sum=0;
            for(int j=0;j<temp.length;j++)
            {
                sum=sum+temp[j];
            }
            means[i]=sum/temp.length;
        }
        for(int i=0;i<means.length;i++)
        {
            for(int j=i;j<means.length;j++)
            {
                double[] temp1=data[i];
                double[] temp2=data[j];
                double sum=0;
                for(int k=0;k<temp1.length;k++)
                {
                    sum=sum+(temp1[k]-means[i])*(temp2[k]-means[j]);
                }
                sum=sum/(temp1.length-1);
                res[i][j]=sum;
                res[j][i]=sum;
            }
        }
        return res;
    }
    public static void main(String[] args)
    {
        double[][] data=new double[3][10];
        data[0][0]=49;
        data[0][1]=8;
        data[0][2]=12;
        data[0][3]=19;
        data[0][4]=3;
        data[0][5]=34;
        data[0][6]=20;
        data[0][7]=49;
        data[0][8]=20;
        data[0][9]=31;
        data[1][0]=7;
        data[1][1]=19;
        data[1][2]=8;
        data[1][3]=37;
        data[1][4]=43;
        data[1][5]=17;
        data[1][6]=34;
        data[1][7]=14;
        data[1][8]=26;
        data[1][9]=41;
        data[2][0]=29;
        data[2][1]=16;
        data[2][2]=14;
        data[2][3]=22;
        data[2][4]=21;
        data[2][5]=17;
        data[2][6]=27;
        data[2][7]=37;
        data[2][8]=21;
        data[2][9]=21;
        double[][] res=cov(data);
        for(int i=0;i<res.length;i++)
        {
            double[] r=res[i];
            for(int j=0;j<r.length;j++)
            {
                System.out.println(r[j]+" ");
            }
            System.out.println();
        }
    }
}
