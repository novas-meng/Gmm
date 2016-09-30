import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by novas on 16/9/20.
 */
public class Main {
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public static void main(String[] args) {
       System.out.println(climbStairs(5));
    }

    public static int climbStairs(int n) {
        int[] res=new int[n+1];
        res[1]=1;
        res[0]=1;
        for(int i=2;i<res.length;i++)
        {
            res[i]=res[i-1]+res[i-2];
        }
        return res[n];
    }
    public static boolean isUglya(int num) {
        int s=num;
        while (s>1)
        {
            if(s%2==0)
            {
                s=s/2;
            }
            else if(s%3==0)
            {
                s=s/3;
            }
            else if(s%5==0)
            {
                s=s/5;
            }
            else
            {
                return false;
            }
        }
        return true;
    }
        public static boolean isUgly(int num) {
        int[] array=new int[num+1];
        array[0]=1;
        for(int i=2;i<array.length;i++)
        {
            if(array[i]!=1)
            {
                for(int j=2;j*i<array.length;j++)
                {
                    array[j*i]=1;
                }
            }
        }
        boolean flag=true;
        for(int i=1;i<=num;i++)
        {
            if(num%i==0)
            {
                if(array[num/i]==0)
                {
                    if(num/i>5)
                    {
                        flag=false;
                    }
                }
            }
        }
        return flag;
    }
    public static int hammingWeight(int n) {
        int count=0;
        int s=n;
        while (s>=1)
        {
            if(s%2==1)
            {
                count++;
            }
            s=s/2;
        }
        return count;
    }
    public static int[] toArray(int n)
    {

        ArrayList<Integer> list=new ArrayList<>();
        int d=n;
        while (d>0)
        {
            int y=d%10;
            d=d/10;
            list.add(y);
        }
        int[] res=new int[list.size()];
        for(int i=0;i<res.length;i++)
        {
            res[i]=list.get(i);
        }
        return res;
    }

    public ListNode deleteDuplicates(ListNode head) {
        ListNode p=head;
        while (p.next!=null)
        {
            if(p.val==p.next.val)
            {
                p.next=p.next.next;

            }
            else
            {
                p=p.next;
            }
        }
        return head;
    }
    public static boolean isHappy(int n) {
        if(n<=0)
            return false;
       int[] array=toArray(n);
        int sum=n;
        ArrayList<Integer> list=new ArrayList<>();
        while (!list.contains(sum))
        {
            list.add(sum);
             sum=0;
            for(int i=0;i<array.length;i++)
            {
                sum=sum+array[i]*array[i];
            }
            array=toArray(sum);
        }
        if(array[0]==1)
        {
            return true;
        }
        return false;
    }
    public static boolean isPowerOfThree(int n) {
        if(n<=0)
            return false;
        double m=Math.log10(n)/Math.log10(3);
        int k=(int)m;
        if(k-m==0)
        {
            return true;
        }
        return false;
    }

}