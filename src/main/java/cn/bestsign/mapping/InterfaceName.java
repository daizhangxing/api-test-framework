package cn.bestsign.mapping;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by dai on 2018/3/9.
 */
public class InterfaceName {



    private Map<String,String> interfaceNameMapping;

    public Map<String, String> getInterfaceNameMapping() {
        return interfaceNameMapping;
    }

    public void setInterfaceNameMapping(Map<String, String> interfaceNameMapping) {
        this.interfaceNameMapping = interfaceNameMapping;
    }



   private InterfaceName(){

       Map<String,String> interfaceNameMapping1 = new TreeMap<String,String>();

       //userService
       interfaceNameMapping1.put("regTest","/user/reg/");
//       interfaceNameMapping1.put("demo","/api/contracts/{contractId}");
       interfaceNameMapping1.put("demo1","/api/contracts/{contractId}");

       this.interfaceNameMapping = interfaceNameMapping1;
   }


    private static class getInterface{

        private static final InterfaceName instance = new InterfaceName();
    }

    public static InterfaceName getInterfaceName(){

        return getInterface.instance;
    }




}
