/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdb.ligandanalysis;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Set;

public class AminoAcidStatisitc {

    public static void AminoAcidStatisitc(String AminoAcid) {

        try {
            Integer counter = 0;
            Hashtable<String, Integer> results = new Hashtable<>();
            //Open connection to database;
            MysqlDBService.LoadConnection();
            String SQL="select * from liganddata where ResidueName='" + AminoAcid + "'";
            ResultSet rs = MysqlDBService.getResultSet(SQL);
            while (rs.next()) {
                counter++;
                String  key=rs.getString("ResidueBindingAtom");
                Integer count = results.get(key);
                if(count==null){
                    count=0;
                }else{
                count++;    
                }
                results.put(key, count);
            }
            Set<String> keys = results.keySet();
            for (String key : keys) {
                Integer aminocount=results.get(key);
                Float percent=((aminocount.floatValue()*100)/counter.floatValue());
                System.out.println("Atom: "+key.toString()+"   Count:"+aminocount.toString()+"     Percent:"+percent);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
