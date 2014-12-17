/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdb.ligandanalysis;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class AminoAcidStatisitc {

    public static void AtomnStatisitc(String AminoAcid) {

        try {
            Integer counter = 0;
            //Hashtable to count the atom
            Hashtable<String, Integer> results = new Hashtable<>();
            //Hashtable to save all the distances of each atom.
            Hashtable<String, LinkedList<Float>> distances = new Hashtable<>();
            
            //Open connection to database;
            MysqlDBService.LoadConnection();
            String SQL="select * from liganddata where ResidueName='" + AminoAcid + "'";
            ResultSet rs = MysqlDBService.getResultSet(SQL);
            while (rs.next()) {
                counter++;
                String  key=rs.getString("ResidueBindingAtom");
                Integer count = results.get(key);
                LinkedList<Float> distance=distances.get(key);
                if(count==null){
                    count=0;
                    distance=new LinkedList<>();
                }else{
                count++;    
                }
                String mydis=rs.getString("Distance");
                distance.add(Float.valueOf(mydis));
                results.put(key, count);
                distances.put(key, distance);
            }
            Set<String> keys = results.keySet();
            for (String key : keys) {
                Integer aminocount=results.get(key);
                Float percent=((aminocount.floatValue()*100)/counter.floatValue());
                double averageDistance=DistanceAverage(distances.get(key));
                double stdDistance=DistanceStd(distances.get(key));
                System.out.println("Atom: "+key.toString()+"   Count:"+aminocount.toString()+"     Percent:"+percent+"      Distance:"+averageDistance+"        STD:"+stdDistance);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    public static double DistanceStd(LinkedList<Float> distances){
        SummaryStatistics stats = new SummaryStatistics();
        for (int i=0;i<distances.size();i++){
            stats.addValue(distances.get(i));
        }
        return stats.getStandardDeviation();
        
    }
    public static double DistanceAverage(LinkedList<Float> distances){
        SummaryStatistics stats = new SummaryStatistics();
        for (int i=0;i<distances.size();i++){
            stats.addValue(distances.get(i));
        }
        return stats.getMean();
        
    }
}
