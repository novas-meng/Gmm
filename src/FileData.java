import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by novas on 16/9/23.
 */
public class FileData {
    public static ArrayList getFileData(String file)throws Exception
    {
        BufferedReader br=new BufferedReader(new FileReader(file));
        ArrayList<String> list=new ArrayList<>();
        String line=br.readLine();
        while (line!=null)
        {
            list.add(line);
            line=br.readLine();
        }
        br.close();
        return list;
    }

    public static void main(String[] args)throws Exception
    {
        ArrayList<String> samples=getFileData("TrainSamples.csv");
        ArrayList<String> lables=getFileData("TrainLabels.csv");
        HashMap<String,ArrayList<String>> hashMap=new HashMap<>();
        for(int i=0;i<samples.size();i++)
        {
            if(!hashMap.containsKey(lables.get(i)))
            {
                ArrayList<String> arrayList=new ArrayList();
                arrayList.add(samples.get(i));
                hashMap.put(lables.get(i),arrayList);
            }
            else
            {
                ArrayList<String> l=hashMap.get(lables.get(i));
                l.add(samples.get(i));
                hashMap.put(lables.get(i),l);
            }
        }
        for(Map.Entry<String,ArrayList<String>> entry:hashMap.entrySet())
        {
            String key=entry.getKey();
            ArrayList<String> list=entry.getValue();
            FileWriter fw=new FileWriter(key+".txt");
            for(int i=0;i<list.size();i++)
            {
                fw.write(list.get(i)+"\r\n");
            }
            fw.close();
        }
    }
}
